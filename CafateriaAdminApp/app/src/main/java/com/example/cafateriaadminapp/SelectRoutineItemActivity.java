package com.example.cafateriaadminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cafateriaadminapp.Adapter.SelectRoutineItemAdapter;
import com.example.cafateriaadminapp.Database.CafateriaDatabase;
import com.example.cafateriaadminapp.Database.Models.DB_RoutineItem;
import com.example.cafateriaadminapp.Executors.AppExecutor;
import com.example.cafateriaadminapp.Network.Retrofit.Api.MenuItemsApiClient;
import com.example.cafateriaadminapp.Network.Retrofit.Api.RoutineItemApiClient;
import com.example.cafateriaadminapp.Network.Retrofit.Model.MenuItems.MenuItem;
import com.example.cafateriaadminapp.Network.Retrofit.Model.MenuItems.MenuItems;
import com.example.cafateriaadminapp.Network.Retrofit.Model.RoutineItem.RoutineItems;
import com.example.cafateriaadminapp.Network.Retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SelectRoutineItemActivity extends AppCompatActivity {

    private String SELECTED_DAY;

    private Retrofit retrofit;
    private MenuItemsApiClient menuItemsApiClient;
    private RoutineItemApiClient routineItemApiClient;

    private Context context;

    private AlertDialog errorDialog, fetchingData;

    private RecyclerView rvSelectRoutine;
    private SelectRoutineItemAdapter selectRoutineItemAdapter;
    private RecyclerView.LayoutManager selectRoutineLayoutManager;

    private Button btnSelectDone;

    private CafateriaDatabase mDB;


    //handler
    private Handler mUploadRoutineHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            uploadRoutine((List<DB_RoutineItem>) msg.obj);
        }
    };

    private Handler mLoadSelectMenuItems = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            selectRoutineItemAdapter.setMenuItems((List<MenuItem>) msg.obj);
            selectRoutineItemAdapter.notifyDataSetChanged();
        }
    };

    private void uploadRoutine(List<DB_RoutineItem> routineItems) {
        showLoadingDialogBox();
        List<RoutineItems.RoutineItem> routineItemsList = new ArrayList<>();
        for (DB_RoutineItem item : routineItems) {
            routineItemsList.add(new RoutineItems.RoutineItem(
                    item.getItemName(),
                    item.getPrice(),
                    item.getCategory(),
                    item.getDay()
            ));
        }

        Call<RoutineItems> routineItemsCall = routineItemApiClient.addRoutine(new RoutineItems(routineItemsList), SELECTED_DAY);

        routineItemsCall.enqueue(new Callback<RoutineItems>() {
            @Override
            public void onResponse(Call<RoutineItems> call, Response<RoutineItems> response) {
                fetchingData.cancel();

                if (response.code() == 200) {
                    finish();
                } else {
                    errorDialogbox("Something went wrong");
                }

                AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDB.routineItemDao().deleteroutineItems();
                    }
                });
            }

            @Override
            public void onFailure(Call<RoutineItems> call, Throwable t) {
                fetchingData.cancel();
                errorDialogbox(t.getMessage());

                AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDB.routineItemDao().deleteroutineItems();
                    }
                });

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        mDB = CafateriaDatabase.getInstance(context);

        //Deleting the database for previous instance
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDB.routineItemDao().deleteroutineItems();
            }
        });

        Intent data = getIntent();
        SELECTED_DAY = data.getStringExtra(RoutineItemActivity.EXTRA_DAY);

        getSupportActionBar().setTitle("SELECT ITEM " + SELECTED_DAY);
        setContentView(R.layout.activity_select_routine_item);

        btnSelectDone = findViewById(R.id.btn_select_done);

        btnSelectDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        List<MenuItem> selectedMenuItems = selectRoutineItemAdapter.getSelected_menuitems();
                        for (MenuItem menuItem : selectedMenuItems) {
                            saveItemToDatabase(menuItem);
                        }

                        List<DB_RoutineItem> routineItems = mDB.routineItemDao().getAllroutineItems(SELECTED_DAY);
                        Message msg = new Message();
                        msg.obj = routineItems;
                        mUploadRoutineHandler.sendMessage(msg);
                    }
                });
            }
        });

        rvSelectRoutine = findViewById(R.id.rv_selectRoutineItem);
        selectRoutineItemAdapter = new SelectRoutineItemAdapter();
        selectRoutineLayoutManager = new GridLayoutManager(this, 2);

        rvSelectRoutine.setAdapter(selectRoutineItemAdapter);
        rvSelectRoutine.setLayoutManager(selectRoutineLayoutManager);
        rvSelectRoutine.setHasFixedSize(true);


        retrofit = RetrofitClient.getINSTANCE();
        menuItemsApiClient = retrofit.create(MenuItemsApiClient.class);
        routineItemApiClient = retrofit.create(RoutineItemApiClient.class);

        loadMenuData();
    }

    public void saveItemToDatabase(final MenuItem menuItem) {

        DB_RoutineItem routineItem = new DB_RoutineItem(
                menuItem.getItemName(),
                menuItem.getCategories(),
                menuItem.getPrice(),
                SELECTED_DAY,
                menuItem.getId()
        );
        mDB.routineItemDao().addroutineItem(routineItem);
    }

    public void loadMenuData() {
        showLoadingDialogBox();


        Call<MenuItems> menuItemsCall = menuItemsApiClient.getAllMenuItems();

        menuItemsCall.enqueue(new Callback<MenuItems>() {
            @Override
            public void onResponse(Call<MenuItems> call, Response<MenuItems> response) {
                fetchingData.cancel();

                if (response.code() == 200) {
                    final MenuItems menuItems = response.body();


                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message msg = new Message();

                            msg.obj = menuItems.getMenuItems();

                            List<Integer> selectedItem = setUpChecked(RoutineItemActivity.routineItems, menuItems.getMenuItems());
                            selectRoutineItemAdapter.setSelected(selectedItem);

                            mLoadSelectMenuItems.sendMessage(msg);
                        }
                    }).start();

                } else {
                    errorDialogbox("Try Again,Failed To Get MenuItems");

                }
            }

            @Override
            public void onFailure(Call<MenuItems> call, Throwable t) {
                fetchingData.cancel();
                errorDialogbox(t.getMessage());
            }
        });
    }

    public List<Integer> setUpChecked(List<RoutineItems.RoutineItem> routineItems, List<MenuItem> menuItems) {
        List<Integer> selectItem = new ArrayList<>();
        for (int i = 0; i < routineItems.size(); i++) {
            for (int j = 0; j < menuItems.size(); j++) {
                RoutineItems.RoutineItem currentRoutineItem = routineItems.get(i);
                MenuItem menuItem = menuItems.get(j);
                if (currentRoutineItem.getItemName().equals(menuItem.getItemName())) {
                    selectItem.add(j);
                }
            }

        }
        return selectItem;
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
}
