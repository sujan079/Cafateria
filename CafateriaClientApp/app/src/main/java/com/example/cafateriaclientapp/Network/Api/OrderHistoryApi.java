package com.example.cafateriaclientapp.Network.Api;

import com.example.cafateriaclientapp.Network.GSON_Models.OrdersHistory.OrdersHistory;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OrderHistoryApi {

    @POST("order-history")
    Call<OrdersHistory> addOrderToHisory(@Body OrdersHistory ordersHistory);
}
