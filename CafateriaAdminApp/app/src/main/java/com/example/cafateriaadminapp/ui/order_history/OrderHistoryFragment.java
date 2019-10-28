package com.example.cafateriaadminapp.ui.order_history;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafateriaadminapp.Adapter.OrderHistoryAdapter;
import com.example.cafateriaadminapp.Network.Retrofit.Api.OrderHistoryApiClient;
import com.example.cafateriaadminapp.Network.Retrofit.Model.OrderHistory.OrderHistory;
import com.example.cafateriaadminapp.Network.Retrofit.RetrofitClient;
import com.example.cafateriaadminapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OrderHistoryFragment extends Fragment {

    private RecyclerView rvOrderHistory;
    private RecyclerView.LayoutManager orderHistoryLayoutManager;
    private OrderHistoryAdapter orderHistoryAdapter;

    private AlertDialog errorDialog, fetchingData;

    private Context context;

    private Retrofit retrofit;
    private OrderHistoryApiClient orderHistoryApiClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_order_history,container,false);
        context=getContext();
        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvOrderHistory=view.findViewById(R.id.rv_order_history);
        orderHistoryLayoutManager=new LinearLayoutManager(context);
        orderHistoryAdapter=new OrderHistoryAdapter();

        rvOrderHistory.setAdapter(orderHistoryAdapter);
        rvOrderHistory.setLayoutManager(orderHistoryLayoutManager);
        rvOrderHistory.setHasFixedSize(true);

        retrofit= RetrofitClient.getINSTANCE();
        orderHistoryApiClient=retrofit.create(OrderHistoryApiClient.class);
        loadOrderHistory();

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

    public void loadOrderHistory(){
        showLoadingDialogBox();
        Call<OrderHistory> orderHistoryCall=orderHistoryApiClient.getOrderHistory(null,null);
        orderHistoryCall.enqueue(new Callback<OrderHistory>() {
            @Override
            public void onResponse(Call<OrderHistory> call, Response<OrderHistory> response) {
                fetchingData.cancel();
                if(response.code()==200){
                    orderHistoryAdapter.setHistories(response.body().getOrderHistory());
                    orderHistoryAdapter.notifyDataSetChanged();

                }else{
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
}
