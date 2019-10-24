package com.example.cafateriaclientapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.cafateriaclientapp.Adapter.OrderHistoryAdapter;
import com.example.cafateriaclientapp.Database.CafateriaDatabase;
import com.example.cafateriaclientapp.Database.Models.DB_User;
import com.example.cafateriaclientapp.Executors.AppExecutor;
import com.example.cafateriaclientapp.Network.Api.OrderHistoryApi;
import com.example.cafateriaclientapp.Network.GSON_Models.OrderHistory.History;
import com.example.cafateriaclientapp.Network.GSON_Models.OrderHistory.OrderHistory;
import com.example.cafateriaclientapp.Network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView rvOrderHistory;
    private RecyclerView.LayoutManager orderHistoryLayoutManger;
    private OrderHistoryAdapter orderHistoryAdapter;

    private static AlertDialog errorDialog;


    private Retrofit retrofit;

    private Handler showErrorDialogHandler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            errorDialogbox((String) msg.obj);
        }
    };


    private Handler loadOrdersHistoryHandler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            List<History> info= (List<History>) msg.obj;
            orderHistoryAdapter.setHistories(info);
            orderHistoryAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        rvOrderHistory=findViewById(R.id.rv_order_histories);
        orderHistoryLayoutManger=new LinearLayoutManager(this);
        orderHistoryAdapter=new OrderHistoryAdapter(new ArrayList<History>());
        rvOrderHistory.setAdapter(orderHistoryAdapter);
        rvOrderHistory.setLayoutManager(orderHistoryLayoutManger);

        retrofit= new RetrofitClient().getINSTANCE();
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<DB_User> users= CafateriaDatabase.getInstance(getApplicationContext()).userDao().getUser();
                if(users.size()>0){
                    loadUserHistory(users.get(0));
                }

            }
        });
    }


    public void loadUserHistory(DB_User currentUser){
        OrderHistoryApi orderHistoryApi=retrofit.create(OrderHistoryApi.class);
        Call<OrderHistory> orderHistoryCall=orderHistoryApi.getAllHistory(currentUser.getId(),null,null);
        orderHistoryCall.enqueue(new Callback<OrderHistory>() {
            @Override
            public void onResponse(Call<OrderHistory> call, Response<OrderHistory> response) {
                if(response.code()==200){
                    OrderHistory orderHistory=response.body();
                    Message historyMsg=new Message();
                    historyMsg.obj=orderHistory.getOrderHistory();

                    loadOrdersHistoryHandler.sendMessage(historyMsg);
                }else{
                    Message errMsg=new Message();
                    errMsg.obj="Could Not get History";
                    showErrorDialogHandler.sendMessage(errMsg);

                }
            }

            @Override
            public void onFailure(Call<OrderHistory> call, Throwable t) {
                Message errMsg=new Message();
                errMsg.obj="Could Not get History";
                showErrorDialogHandler.sendMessage(errMsg);

            }
        });
    }

    public void errorDialogbox(String error){


        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View view= LayoutInflater.from(this).inflate(R.layout.error_dialog_box,null,false);

        TextView errorTextView= view.findViewById(R.id.tv_error);
        errorTextView.setText(error);
        builder.setView(view);
        errorDialog=builder.create();
        errorDialog.show();
    }


}
