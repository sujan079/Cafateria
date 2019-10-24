package com.example.cafateriaclientapp.Network.Api;

import com.example.cafateriaclientapp.Network.GSON_Models.Orders.Order;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OrdersApi {

    @POST("orders")
    Call<Order> addOrderToHisory(@Body Order order);
}
