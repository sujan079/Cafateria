package com.example.cafateriaadminapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.cafateriaadminapp.Adapter.OrderHistoryAdapter;
import com.example.cafateriaadminapp.Network.Retrofit.Api.OrderHistoryApiClient;
import com.example.cafateriaadminapp.Network.Retrofit.Api.UsersApiClient;
import com.example.cafateriaadminapp.Network.Retrofit.Model.OrderHistory.OrderHistory;
import com.example.cafateriaadminapp.Network.Retrofit.Model.Users.User;
import com.example.cafateriaadminapp.Network.Retrofit.Model.Users.Users;
import com.example.cafateriaadminapp.Network.Retrofit.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OrderHistoryActivity extends AppCompatActivity {

    private EditText etDate;
    private final Calendar myCalendar = Calendar.getInstance();
    private Context context;

    private RecyclerView rvOrderHistory;
    private RecyclerView.LayoutManager orderHistoryLayoutManager;
    private OrderHistoryAdapter orderHistoryAdapter;

    private AlertDialog errorDialog, fetchingData;

    private Retrofit retrofit;
    private OrderHistoryApiClient orderHistoryApiClient;
    private UsersApiClient usersApiClient;

    private String SELECTED_DATE;
    private String SELECTED_USER = "ALL";

    public static String EXTRA_USER_ID = "USER_ID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        context = this;
        etDate = findViewById(R.id.et_date);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        etDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        rvOrderHistory = findViewById(R.id.rv_order_history);
        orderHistoryLayoutManager = new LinearLayoutManager(context);
        orderHistoryAdapter = new OrderHistoryAdapter();

        rvOrderHistory.setAdapter(orderHistoryAdapter);
        rvOrderHistory.setLayoutManager(orderHistoryLayoutManager);
        rvOrderHistory.setHasFixedSize(true);

        retrofit = RetrofitClient.getINSTANCE();
        orderHistoryApiClient = retrofit.create(OrderHistoryApiClient.class);
        usersApiClient = retrofit.create(UsersApiClient.class);

        Intent data = getIntent();
        SELECTED_USER = data.getStringExtra(EXTRA_USER_ID);
        loadUsers(SELECTED_USER);


    }

    public void errorDialogbox(String error) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.error_dialog_box, null, false);

        TextView errorTextView = view.findViewById(R.id.tv_error);
        errorTextView.setText(error);
        builder.setView(view);
        errorDialog = builder.create();
        errorDialog.show();
    }

    public void showLoadingDialogBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.info_dialog_box, null, false);
        builder.setView(view);
        fetchingData = builder.create();
        fetchingData.show();

    }

    public void loadOrderHistory(String user_id, String date) {
        showLoadingDialogBox();
        Call<OrderHistory> orderHistoryCall = orderHistoryApiClient.getOrderHistory(user_id, date);
        orderHistoryCall.enqueue(new Callback<OrderHistory>() {
            @Override
            public void onResponse(Call<OrderHistory> call, Response<OrderHistory> response) {
                fetchingData.cancel();
                if (response.code() == 200) {
                    orderHistoryAdapter.setHistories(response.body().getOrderHistory());
                    orderHistoryAdapter.notifyDataSetChanged();

                } else {
                    errorDialogbox("Could Not Load History");
                }
            }

            @Override
            public void onFailure(Call<OrderHistory> call, Throwable t) {
                fetchingData.cancel();
                errorDialogbox(t.getMessage());
            }
        });

    }

    public void loadUsers(String usr_id) {
        showLoadingDialogBox();
        Call<Users> usersCall = usersApiClient.getAllUsers();

        usersCall.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                fetchingData.cancel();
                if (response.code() == 200) {
                    List<User> users = response.body().getUsers();

                    List<String> strUsers = new ArrayList<>();

                    for (User usr : users) {
                        strUsers.add(usr.get_id());
                    }

                    strUsers.remove(SELECTED_USER);
                    strUsers.add(0,SELECTED_USER);

                    Spinner userSpinner = findViewById(R.id.spinner_user_id);
                    ArrayAdapter<String> usersAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, strUsers);
                    usersAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                    userSpinner.setAdapter(usersAdapter);

                    userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            SELECTED_USER = (String) adapterView.getSelectedItem();
                            loadOrderHistory(SELECTED_USER, SELECTED_DATE);


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } else {
                    errorDialogbox("Could Not Get User Data");
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                fetchingData.cancel();
                errorDialogbox(t.getMessage());
            }
        });
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String selectedDate = sdf.format(myCalendar.getTime());
        etDate.setText(selectedDate);
        loadOrderHistory(SELECTED_USER, selectedDate);
        SELECTED_DATE = selectedDate;

    }
}
