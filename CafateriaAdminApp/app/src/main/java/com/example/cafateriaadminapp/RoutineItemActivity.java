package com.example.cafateriaadminapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.cafateriaadminapp.Adapter.RoutineItemAdapter;
import com.example.cafateriaadminapp.Network.Retrofit.Api.RoutineItemApiClient;
import com.example.cafateriaadminapp.Network.Retrofit.Model.RoutineItem.RoutineItems;
import com.example.cafateriaadminapp.Network.Retrofit.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RoutineItemActivity extends AppCompatActivity {


    public static String EXTRA_DAY = "EXTRA_DAY";
    public static List<RoutineItems.RoutineItem> routineItems = new ArrayList<>();
    public int SELECT_ROUTINE_ITEM_REQUEST_CODE = 154;
    private AlertDialog errorDialog, fetchingData;
    private Context context;
    private TextView mTvDay;
    private FloatingActionButton addRoutineItem;
    private RecyclerView rvRoutine;
    private RecyclerView.LayoutManager routineLayoutManager;
    private RoutineItemAdapter routineItemAdapter;
    private Retrofit retrofit;
    private RoutineItemApiClient routineItemApiClient;
    private String SELECTED_DAY = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine_item);

        context = this;

        addRoutineItem = findViewById(R.id.fab_add_routine_item);

        Intent data = getIntent();

        if (data.hasExtra(EXTRA_DAY)) {
            SELECTED_DAY = data.getStringExtra(EXTRA_DAY);
        }

        getSupportActionBar().setTitle(SELECTED_DAY + " ROUTINE");

        addRoutineItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent selectRoutineIntent = new Intent(getApplicationContext(), SelectRoutineItemActivity.class);
                selectRoutineIntent.putExtra(EXTRA_DAY, SELECTED_DAY);
                startActivityForResult(selectRoutineIntent, SELECT_ROUTINE_ITEM_REQUEST_CODE);
            }
        });

        retrofit = RetrofitClient.getINSTANCE();
        routineItemApiClient = retrofit.create(RoutineItemApiClient.class);

        rvRoutine = findViewById(R.id.rv_routineItems);
        routineLayoutManager = new GridLayoutManager(this, 2);
        routineItemAdapter = new RoutineItemAdapter();

        routineItemAdapter.setOnRoutineItemClickListner(new RoutineItemAdapter.OnRoutineItemClickListner() {
            @Override
            public void removeMenuItem(RoutineItems.RoutineItem routineItem) {
                delRoutineItem(routineItem.get_id());
            }
        });

        rvRoutine.setAdapter(routineItemAdapter);
        rvRoutine.setLayoutManager(routineLayoutManager);
        rvRoutine.setHasFixedSize(true);

        loadRoutineData(SELECTED_DAY);
    }

    public void loadRoutineData(String day) {
        showLoadingDialogBox();
        Call<RoutineItems> routineItemsCall = routineItemApiClient.getRoutineItem(day);

        routineItemsCall.enqueue(new Callback<RoutineItems>() {
            @Override
            public void onResponse(Call<RoutineItems> call, Response<RoutineItems> response) {
                fetchingData.cancel();
                if (response.code() == 200) {
                    routineItems = response.body().getRoutineItems();
                    routineItemAdapter.setRoutineItems(routineItems);
                    routineItemAdapter.notifyDataSetChanged();

                } else {
                    errorDialogbox("Could Not Fetch Item");
                }
            }

            @Override
            public void onFailure(Call<RoutineItems> call, Throwable t) {
                fetchingData.cancel();
                errorDialogbox(t.getMessage());
            }
        });
    }

    public void delRoutineItem(String id) {
        showLoadingDialogBox();
        Call<RoutineItems> routineItemsCall = routineItemApiClient.deleteRoutineItem(id);

        routineItemsCall.enqueue(new Callback<RoutineItems>() {
            @Override
            public void onResponse(Call<RoutineItems> call, Response<RoutineItems> response) {
                fetchingData.cancel();
                if (response.code() == 200) {
                    loadRoutineData(SELECTED_DAY);
                } else {
                    errorDialogbox("Could Not Delete");
                }
            }

            @Override
            public void onFailure(Call<RoutineItems> call, Throwable t) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_ROUTINE_ITEM_REQUEST_CODE) {
            loadRoutineData(SELECTED_DAY);
        }
    }
}
