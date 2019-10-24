package com.example.cafateriaclientapp.Network.Api;

import com.example.cafateriaclientapp.Network.GSON_Models.OrderHistory.OrderHistory;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OrderHistoryApi {

    @GET("order-history")
    Call<OrderHistory> getAllHistory(@Query("user_id") String user_id, @Query("date") String date, @Query("limit") Integer limit);
}
