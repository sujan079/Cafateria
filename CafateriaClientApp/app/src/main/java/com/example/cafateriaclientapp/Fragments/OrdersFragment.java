package com.example.cafateriaclientapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafateriaclientapp.ActionListener.OrdersActionListener;
import com.example.cafateriaclientapp.Adapter.OrdersAdapter;
import com.example.cafateriaclientapp.Database.CafateriaDatabase;
import com.example.cafateriaclientapp.Database.Models.DB_Orders;
import com.example.cafateriaclientapp.Database.Models.DB_User;
import com.example.cafateriaclientapp.Executors.AppExecutor;
import com.example.cafateriaclientapp.Network.Api.OrdersApi;
import com.example.cafateriaclientapp.Network.GSON_Models.MenuItems.MenuItem;
import com.example.cafateriaclientapp.Network.GSON_Models.Orders.Order;
import com.example.cafateriaclientapp.Network.RetrofitClient;
import com.example.cafateriaclientapp.R;
import com.example.cafateriaclientapp.Socket.PrintSocket;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OrdersFragment extends Fragment implements OrdersActionListener {

    private CafateriaDatabase mDB;
    private Retrofit retrofit;

    private RecyclerView rvOrders;
    private OrdersAdapter ordersAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private Context context;

    private Button orderBtn;

    private static AlertDialog errorDialog, fetchingData;

    private Socket socket;
    //handlers

    private Handler showErrorDialogHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            fetchingData.cancel();
            errorDialogbox((String) msg.obj);
        }
    };


    private Handler loadOrderItemsHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            List<DB_Orders> orders = (List<DB_Orders>) msg.obj;
            ordersAdapter.setOrdersList(orders);
            ordersAdapter.notifyDataSetChanged();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_orders, container, false);
        context = rootView.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        orderBtn = view.findViewById(R.id.btn_send_order);
        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOrder();
            }
        });


        mDB = CafateriaDatabase.getInstance(context);
        retrofit = new RetrofitClient().getINSTANCE();

        rvOrders = view.findViewById(R.id.rv_orders);
        ordersAdapter = new OrdersAdapter(new ArrayList<DB_Orders>(), this);
        layoutManager = new GridLayoutManager(context, 2);

        rvOrders.setAdapter(ordersAdapter);
        rvOrders.setLayoutManager(layoutManager);

        getAllOrders();

        PrintSocket printSocket = new PrintSocket();

        socket = printSocket.getSocket();
        socket.connect();


    }

    public void sendOrder() {
        showLoadingDialogBox();
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<DB_User> users = mDB.userDao().getUser();
                if (users.size() > 0) {
                    DB_User currentUser = users.get(0);
                    List<DB_Orders> orders = mDB.ordersDao().getOrders();

                    List<MenuItem> ordItems = new ArrayList<>();


                    for (DB_Orders ord : orders) {

                        ordItems.add(new MenuItem(
                                ord.getItemId(),
                                ord.getItemName(),
                                ord.getCategories(),
                                ord.getPrice(),
                                ord.getQuantity()
                        ));
                    }


                    sendOrderToServer(new Order(currentUser.getId(), currentUser.getEmail(), ordItems));

                } else {
                    Message message = new Message();
                    message.obj = "Error No User Found.Please Login";
                    showErrorDialogHandler.sendMessage(message);
                }
            }
        });
    }

    public void sendOrderToServer(final Order orders) {
        OrdersApi ordersApi = retrofit.create(OrdersApi.class);

        Call<Order> ordersHistoryCall = ordersApi.addOrderToHisory(orders);

        ordersHistoryCall.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.code() == 200) {
                    deletAllOrder();


                    orders.setPrintId(new Random().nextInt((int) new Date().getTime()));
                    String jsonObject = new Gson().toJson(orders);

                    socket.emit("print", jsonObject);
                    fetchingData.cancel();
                } else if (response.code() == 302) {
                    Message message = new Message();
                    message.obj = "Could Not Register Order,Your Account IS NOT Active";
                    showErrorDialogHandler.sendMessage(message);
                } else {
                    Message message = new Message();
                    message.obj = "Could Not Register Order";
                    showErrorDialogHandler.sendMessage(message);
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Message message = new Message();
                message.obj = t.getMessage();
                showErrorDialogHandler.sendMessage(message);
            }
        });
    }

    public void getAllOrders() {
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<DB_Orders> orders = mDB.ordersDao().getOrders();
                Message orderMsg = new Message();
                orderMsg.obj = orders;

                loadOrderItemsHandler.sendMessage(orderMsg);

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


    @Override
    public void delOrder(final DB_Orders orders) {
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDB.ordersDao().deleteOrder(orders);
                getAllOrders();
            }
        });
    }

    public void deletAllOrder() {
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDB.ordersDao().deleteOrder();
                getAllOrders();
            }
        });
    }


    @Override
    public void changeOrder(final DB_Orders updatedOrder) {
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDB.ordersDao().updateOrder(updatedOrder);
                getAllOrders();
            }
        });
    }
}
