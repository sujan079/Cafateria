package com.example.cafateriaadminapp.ui.order_history;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafateriaadminapp.Adapter.CummulativeHistoryAdapter;
import com.example.cafateriaadminapp.Adapter.OrderHistoryAdapter;
import com.example.cafateriaadminapp.Network.Retrofit.Api.OrderHistoryApiClient;
import com.example.cafateriaadminapp.Network.Retrofit.Model.OrderHistory.History;
import com.example.cafateriaadminapp.Network.Retrofit.Model.OrderHistory.OrderHistory;
import com.example.cafateriaadminapp.Network.Retrofit.RetrofitClient;
import com.example.cafateriaadminapp.R;
import com.example.cafateriaadminapp.Utils.DateHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class OrderHistoryFragment extends Fragment {

    public class CummulativeItem {
        public String itemName;
        public Double quantity;


        public CummulativeItem(String itemName, Double quantity) {
            this.itemName = itemName;
            this.quantity = quantity;
        }
    }

    private EditText etDate;
    private final Calendar myCalendar = Calendar.getInstance();

    private List<CummulativeItem> cummulativeItems;
    private Retrofit retrofit;
    private OrderHistoryApiClient orderHistoryApiClient;

    private Context context;

    private AlertDialog errorDialog, fetchingData;

    private RecyclerView rvCummOrders;
    private RecyclerView.LayoutManager layoutManager;
    private CummulativeHistoryAdapter cummulativeHistoryAdapter;

    private String SELECTED_DATE;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_order_history, container, false);
        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();

        retrofit = RetrofitClient.getINSTANCE();
        orderHistoryApiClient = retrofit.create(OrderHistoryApiClient.class);

        rvCummOrders = view.findViewById(R.id.rv_order_history);
        layoutManager = new LinearLayoutManager(context);
        cummulativeHistoryAdapter = new CummulativeHistoryAdapter();

        rvCummOrders.setAdapter(cummulativeHistoryAdapter);
        rvCummOrders.setLayoutManager(layoutManager);


        etDate = view.findViewById(R.id.et_date);

        SELECTED_DATE = DateHelper.getDateAndTime(new Date())[0];

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

        loadOrderHistory(SELECTED_DATE);

    }

    public void loadOrderHistory(String date) {
        showLoadingDialogBox();
        Call<OrderHistory> orderHistoryCall = orderHistoryApiClient.getOrderHistory(null, date);
        orderHistoryCall.enqueue(new Callback<OrderHistory>() {
            @Override
            public void onResponse(Call<OrderHistory> call, Response<OrderHistory> response) {
                fetchingData.cancel();
                if (response.code() == 200) {

                    HashMap<String, Double> nameQuantity = new HashMap<>();
                    List<History> orderHistories = response.body().getOrderHistory();
                    for (History history : orderHistories) {
                        if (nameQuantity.containsKey(history.getItemName())) {
                            Double prev = nameQuantity.get(history.getItemName());
                            nameQuantity.put(history.getItemName(),
                                    prev + history.getQuantity()
                            );
                        } else {
                            nameQuantity.put(history.getItemName(), history.getQuantity());
                        }

                    }

                    List<CummulativeItem> cummulativeItems = new ArrayList<>();

                    for (String key : nameQuantity.keySet()) {
                        cummulativeItems.add(new CummulativeItem(key, nameQuantity.get(key)));
                    }
                    cummulativeHistoryAdapter.setCummulativeItems(cummulativeItems);
                    cummulativeHistoryAdapter.notifyDataSetChanged();

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

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String selectedDate = sdf.format(myCalendar.getTime());
        etDate.setText(selectedDate);
        loadOrderHistory(selectedDate);
        SELECTED_DATE = selectedDate;

    }

}
