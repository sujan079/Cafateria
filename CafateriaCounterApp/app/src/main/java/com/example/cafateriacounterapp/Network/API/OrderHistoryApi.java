package com.example.cafateriacounterapp.Network.API;

import com.example.cafateriacounterapp.Network.GSON_Models.OrdersHistory.Order;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OrderHistoryApi {

    @POST("order-history")
    Call<Order> saveOrder(@Body Order order);
}
