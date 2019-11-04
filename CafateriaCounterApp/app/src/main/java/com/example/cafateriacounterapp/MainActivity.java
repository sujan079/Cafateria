package com.example.cafateriacounterapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cafateriacounterapp.ActionListeners.CategoriesItemActionListner;
import com.example.cafateriacounterapp.ActionListeners.MenuItemsActionListner;
import com.example.cafateriacounterapp.Adapters.CategoriesAdapter;
import com.example.cafateriacounterapp.Adapters.MenuItemsAdapter;
import com.example.cafateriacounterapp.BackgroundServices.UploadHistoryJobService;
import com.example.cafateriacounterapp.Database.CafateriaDatabase;
import com.example.cafateriacounterapp.Database.Models.DB_Category;
import com.example.cafateriacounterapp.Database.Models.DB_MenuItem;
import com.example.cafateriacounterapp.Database.Models.DB_Orders_History;
import com.example.cafateriacounterapp.Executors.AppExecutor;
import com.example.cafateriacounterapp.Network.API.TodayRoutineApi;
import com.example.cafateriacounterapp.Network.GSON_Models.MenuItems.TodayRoutineData;
import com.example.cafateriacounterapp.Network.GSON_Models.MenuItems.MenuItem;
import com.example.cafateriacounterapp.Network.RetrofitClient;
import com.example.cafateriacounterapp.PrinterControl.BixolonPrinter;
import com.example.cafateriacounterapp.Utils.DateHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements MenuItemsActionListner, CategoriesItemActionListner {

    private static boolean showError = true;
    private static boolean serverConn = true;
    private String TAG = this.getClass().getSimpleName();
    private AlertDialog fetchingData, errorDialog;
    private RecyclerView rvCategories;
    private RecyclerView.LayoutManager categoriesLayoutManager;
    private CategoriesAdapter categoriesAdapter;
    private RecyclerView rvMenuItems;
    private RecyclerView.LayoutManager menuItemsLayoutManager;
    private MenuItemsAdapter menuItemsAdapter;
    private String SELECTED_CATEGORY = "ALL";
    private CafateriaDatabase mDB;

    private static BixolonPrinter bixolonPrinter;

    //Handlers
    private Handler loadMenuItemDataFromDataBaseHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            List<DB_MenuItem> menuItems = (List<DB_MenuItem>) msg.obj;
            loadMenuItems(menuItems);
        }
    };

    //Handlers
    private Handler loadCategoryDataFromDataBaseHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            List<DB_Category> categories = (List<DB_Category>) msg.obj;
            loadCategories(categories);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        rvCategories = findViewById(R.id.rv_categories);
        categoriesAdapter = new CategoriesAdapter(new ArrayList<DB_Category>(), this);
        categoriesLayoutManager = new LinearLayoutManager(this);

        rvCategories.setAdapter(categoriesAdapter);
        rvCategories.setLayoutManager(categoriesLayoutManager);

        rvMenuItems = findViewById(R.id.rv_menu_items);
        menuItemsAdapter = new MenuItemsAdapter(new ArrayList<DB_MenuItem>(), this);
        menuItemsLayoutManager = new GridLayoutManager(this, 4);

        rvMenuItems.setAdapter(menuItemsAdapter);
        rvMenuItems.setLayoutManager(menuItemsLayoutManager);

        mDB = CafateriaDatabase.getInstance(getApplicationContext());


        loadDataFromSource();
        startUpload();

        bixolonPrinter = new BixolonPrinter(this);
    }

    public void startUpload() {
        ComponentName componentName = new ComponentName(this, UploadHistoryJobService.class);

        JobInfo info = new JobInfo.Builder(UploadHistoryJobService.UploadHistoryJobServiceID, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true)
                .setPeriodic(15 * 60 * 1000)
                .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

        scheduler.schedule(info);

    }

    public void loadDataFromSource() {

        if (isNetworkConnected()) {
            loadDataFromNetwork(SELECTED_CATEGORY);
        } else {
            loadFromDataBase(SELECTED_CATEGORY);
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


    public void loadDataFromNetwork(String category) {
        if (category.equals("ALL")) {
            category = null;
        }

        if (serverConn) {
            showLoadingDialogBox();
        }

        Retrofit retrofit = RetrofitClient.getInstance();

        TodayRoutineApi todayRoutineApi = retrofit.create(TodayRoutineApi.class);

        Call<TodayRoutineData> counterDataCall = todayRoutineApi.getCounterData(category);

        counterDataCall.enqueue(new Callback<TodayRoutineData>() {
            @Override
            public void onResponse(Call<TodayRoutineData> call, Response<TodayRoutineData> response) {
                hideDialogBox(fetchingData);
                TodayRoutineData todayRoutineData = response.body();

                saveToDataBase(todayRoutineData.getMenuItems(), todayRoutineData.getCategories());

                showError = true;
                serverConn = true;
            }

            @Override
            public void onFailure(Call<TodayRoutineData> call, Throwable t) {
                if (showError) {
                    errorDialogbox(t.getMessage());
                    hideDialogBox(fetchingData);

                }
                serverConn = false;
                showError = false;
                loadFromDataBase(SELECTED_CATEGORY);
            }
        });


    }

    public void showLoadingDialogBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.info_dialog_box, null, false);
        builder.setView(view);
        fetchingData = builder.create();
        fetchingData.show();

    }

    public void errorDialogbox(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.error_dialog_box, null, false);

        TextView errorTextView = view.findViewById(R.id.tv_error);
        errorTextView.setText(error);
        builder.setView(view);
        errorDialog = builder.create();
        errorDialog.show();
    }

    public void hideDialogBox(AlertDialog alertDialog) {
        alertDialog.cancel();
    }

    public void loadCategories(List<DB_Category> categories) {
        categories.add(0, new DB_Category("ALL"));
        categoriesAdapter.setCategories(categories);
        categoriesAdapter.notifyDataSetChanged();
    }

    public void loadMenuItems(List<DB_MenuItem> menuItems) {
        menuItemsAdapter.setMenuItemList(menuItems);
        menuItemsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCategoryItemClicked(DB_Category category) {
        SELECTED_CATEGORY = category.getCategory();
        loadDataFromSource();
    }

    @Override
    public void onMenuItemClick(DB_MenuItem menuItem) {

        String[] date = DateHelper.getDateAndTime(new Date());

        if (true) {
            print(menuItem);
            saveOrderHistory(new DB_Orders_History(
                    menuItem.getItemName(),
                    menuItem.getCategory(),
                    menuItem.getPrice(),
                    date[0],
                    date[1],
                    1
            ));
        } else {
            Toast.makeText(this, "Print Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveOrderHistory(final DB_Orders_History orders_history) {
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDB.orderHistoryDao().addOrderHistory(orders_history);
            }
        });
    }

    public void print(DB_MenuItem menuItem) {
        String[] date = DateHelper.getDateAndTime(new Date());

        //TODO:Printing Task
        MainActivity.getPrinterInstance().printText("Welcome \n", BixolonPrinter.ALIGNMENT_CENTER, BixolonPrinter.ATTRIBUTE_NORMAL, 1);
        MainActivity.getPrinterInstance().printText("To\n", BixolonPrinter.ALIGNMENT_CENTER, BixolonPrinter.ATTRIBUTE_NORMAL, 1);

        MainActivity.getPrinterInstance().printText("Southwestern Cafeteria\n", BixolonPrinter.ALIGNMENT_CENTER, BixolonPrinter.ATTRIBUTE_NORMAL, 1);


        MainActivity.getPrinterInstance().printText(menuItem.getItemName() + "\n\n", BixolonPrinter.ALIGNMENT_CENTER, BixolonPrinter.ATTRIBUTE_BOLD, 3);

        MainActivity.getPrinterInstance().printText(date[0] + "\t\t\t", BixolonPrinter.ALIGNMENT_LEFT, BixolonPrinter.ATTRIBUTE_NORMAL, 1);

        MainActivity.getPrinterInstance().printText(date[1] + "\n\n", BixolonPrinter.ALIGNMENT_RIGHT, BixolonPrinter.ATTRIBUTE_NORMAL, 1);

    }

    public void saveToDataBase(final List<MenuItem> menuItems, final List<String> categories) {
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDB.menuItemDao().deleteMenuItems();
                mDB.categoriesDao().deleteCategories();
                for (MenuItem item : menuItems) {
                    mDB.menuItemDao().addMenuItem(new DB_MenuItem(item.getItemName(), item.getCategories(), item.getPrice()));
                }
                for (String category : categories) {
                    mDB.categoriesDao().addCategory(new DB_Category(category));
                }
                loadFromDataBase(SELECTED_CATEGORY);

            }
        });
    }

    public void loadFromDataBase(final String category) {
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<DB_MenuItem> menuItems;
                if (category == "ALL") {
                    menuItems = mDB.menuItemDao().getAllMenuItems();

                } else {
                    menuItems = mDB.menuItemDao().getAllMenuByCategory(category);

                }
                List<DB_Category> categories = mDB.categoriesDao().getAllCategories();

                Message categoryMessage = new Message();
                categoryMessage.obj = categories;

                Message menuItemMessage = new Message();
                menuItemMessage.obj = menuItems;

                loadCategoryDataFromDataBaseHandler.sendMessage(categoryMessage);
                loadMenuItemDataFromDataBaseHandler.sendMessage(menuItemMessage);


            }
        });
    }

    public static BixolonPrinter getPrinterInstance() {
        return bixolonPrinter;
    }



}
