package com.example.cafateriacounterapp.BackgroundServices;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.example.cafateriacounterapp.Database.CafateriaDatabase;
import com.example.cafateriacounterapp.Database.Models.DB_Orders_History;
import com.example.cafateriacounterapp.Network.API.OrderHistoryApi;
import com.example.cafateriacounterapp.Network.GSON_Models.OrdersHistory.Order;
import com.example.cafateriacounterapp.Network.RetrofitClient;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UploadHistoryJobService extends JobService {

    public static final int UploadHistoryJobServiceID=123;
    private static final String TAG="UploadHistoryJobService";
    private boolean jobCancelled=false;

    private Retrofit retrofitClient;
    private CafateriaDatabase mDB;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "onStartJob: JobStarted");

        mDB=CafateriaDatabase.getInstance(getApplicationContext());

        retrofitClient=RetrofitClient.getInstance();

        new Thread(new Runnable() {
            @Override
            public void run() {
                doInBackground();
            }
        }).start();


        return false;
    }

    public void doInBackground(){
        OrderHistoryApi orderHistoryApi=retrofitClient.create(OrderHistoryApi.class);

        List<DB_Orders_History> histories=mDB.orderHistoryDao().getAllOrderHistory();

        for (DB_Orders_History orders_history:histories){

            if(jobCancelled){
                return;
            }

            Order order=new Order(
                    orders_history.getItemName(),
                    orders_history.getPrice(),
                    orders_history.getCategory(),
                    orders_history.getQuantity(),
                    orders_history.getDate(),
                    orders_history.getTime()
            );

            Call<Order> orderCall=orderHistoryApi.saveOrder(order);

            try {
                Response<Order> orderResponse=orderCall.execute();
                if(orderResponse.code()==200){
                    mDB.orderHistoryDao().deleteOrderHistory(orders_history.getOrder_id());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, "onStopJob: JobStopped");
        jobCancelled=true;
        return true;
    }
}
