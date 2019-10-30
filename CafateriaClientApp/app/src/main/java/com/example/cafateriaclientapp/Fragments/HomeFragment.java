package com.example.cafateriaclientapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafateriaclientapp.ActionListener.AddOrderActionListener;
import com.example.cafateriaclientapp.Adapter.MenuItemsAdapter;
import com.example.cafateriaclientapp.Database.CafateriaDatabase;
import com.example.cafateriaclientapp.Database.Models.DB_Orders;
import com.example.cafateriaclientapp.Executors.AppExecutor;
import com.example.cafateriaclientapp.Network.Api.TodayRoutineApi;
import com.example.cafateriaclientapp.Network.GSON_Models.MenuItems.MenuItem;
import com.example.cafateriaclientapp.Network.GSON_Models.MenuItems.TodayRoutineData;
import com.example.cafateriaclientapp.Network.RetrofitClient;
import com.example.cafateriaclientapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeFragment extends Fragment implements AddOrderActionListener {

    private CafateriaDatabase mDB;
    private Retrofit retrofitClient;
    private AlertDialog fetchingData, errorDialog;

    private RecyclerView rvMenuItems;
    private RecyclerView.LayoutManager menuItemsLayoutManger;
    private MenuItemsAdapter menuItemsAdapter;

    private Spinner catogeriesSpinner;
    private String SELECTED_CATOGERY = "ALL";
    private boolean catogeryLoaded = false;


    private Context context;

    //handlers

    private Handler loadMenuItemsHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            List<MenuItem> menuItems = (List<MenuItem>) msg.obj;
            menuItemsAdapter.setMenuItems(menuItems);
            menuItemsAdapter.notifyDataSetChanged();
        }
    };

    private Handler loadCatogeriesHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);


            List<String> catogeries = (List<String>) msg.obj;
            catogeries.add(0, "ALL");

            ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, catogeries);
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
            catogeryLoaded = true;
            catogeriesSpinner.setAdapter(adapter);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        context = rootView.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        retrofitClient = new RetrofitClient().getINSTANCE();

        mDB = CafateriaDatabase.getInstance(context);

        catogeriesSpinner = view.findViewById(R.id.spinner_category);

        catogeriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SELECTED_CATOGERY = (String) adapterView.getSelectedItem();
                loadingFromNetwork();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        rvMenuItems = view.findViewById(R.id.rv_menuItems);
        menuItemsLayoutManger = new GridLayoutManager(context, 2);
        menuItemsAdapter = new MenuItemsAdapter(new ArrayList<MenuItem>(), this);
        rvMenuItems.setAdapter(menuItemsAdapter);
        rvMenuItems.setLayoutManager(menuItemsLayoutManger);

        loadingFromNetwork();
    }

    public void loadingFromNetwork() {

        showLoadingDialogBox();
        TodayRoutineApi todayRoutineApi = retrofitClient.create(TodayRoutineApi.class);
        Call<TodayRoutineData> todayRoutineDataCall;

        if (SELECTED_CATOGERY.equals("ALL")) {
            todayRoutineDataCall = todayRoutineApi.getTodaysRoutine(null);
        } else {
            todayRoutineDataCall = todayRoutineApi.getTodaysRoutine(SELECTED_CATOGERY);

        }


        todayRoutineDataCall.enqueue(new Callback<TodayRoutineData>() {
            @Override
            public void onResponse(Call<TodayRoutineData> call, Response<TodayRoutineData> response) {
                hideDialogBox(fetchingData);
                if (response.code() == 200) {
                    List<MenuItem> menuItems = response.body().getRoutineItems();
                    List<String> categories = response.body().getCategories();


                    Message catogeryMsg = new Message();
                    Message menuItemMsg = new Message();

                    menuItemMsg.obj = menuItems;
                    catogeryMsg.obj = categories;

                    loadMenuItemsHandler.sendMessage(menuItemMsg);

                    if (!catogeryLoaded) {
                        loadCatogeriesHandler.sendMessage(catogeryMsg);

                    }

                } else {
                    errorDialogbox(response.code() + " Retry");
                }
            }

            @Override
            public void onFailure(Call<TodayRoutineData> call, Throwable t) {
                hideDialogBox(fetchingData);
                errorDialogbox(t.getMessage());
            }
        });

    }


    public void showLoadingDialogBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.info_dialog_box, null, false);
        builder.setView(view);
        fetchingData = builder.create();
        fetchingData.show();

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

    public void hideDialogBox(AlertDialog alertDialog) {
        alertDialog.cancel();
    }

    @Override
    public void onOrderClick(MenuItem menuItem) {
        saveOrder(menuItem);
    }

    public void saveOrder(final MenuItem menuItem) {
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {

                List<DB_Orders> orders = mDB.ordersDao().getOrdersById(menuItem.get_id());

                if (orders.size() > 0) {
                    DB_Orders currentOrder = orders.get(0);
                    currentOrder.setQuantity(currentOrder.getQuantity() + 1);
                    mDB.ordersDao().updateOrder(currentOrder);
                } else {
                    mDB.ordersDao().addOrder(new DB_Orders(
                            menuItem.get_id(),
                            menuItem.getItemName(),
                            menuItem.getCategories(),
                            menuItem.getPrice(),
                            1.0
                    ));
                }


            }
        });
    }
}
