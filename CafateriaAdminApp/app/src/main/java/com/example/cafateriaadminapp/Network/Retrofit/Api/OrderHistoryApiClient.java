package com.example.cafateriaadminapp.Network.Retrofit.Api;


import com.example.cafateriaadminapp.Network.Retrofit.Model.OrderHistory.OrderHistory;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OrderHistoryApiClient {

    @GET("order-history")
    Call<OrderHistory> getOrderHistory(@Query("user_id")String user_id,@Query("date") String date);



    @DELETE("order-history/{id}")
    Call<OrderHistory> deleteOrderHistory(@Path("id")String id);

}
