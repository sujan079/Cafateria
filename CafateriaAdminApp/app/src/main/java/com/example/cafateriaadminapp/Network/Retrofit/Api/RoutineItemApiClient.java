package com.example.cafateriaadminapp.Network.Retrofit.Api;


import com.example.cafateriaadminapp.Network.Retrofit.Model.RoutineItem.RoutineItems;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RoutineItemApiClient {

    @GET("today-routine")
    Call<RoutineItems> getRoutineItem(@Query("day") String day);

    @POST("today-routine")
    Call<RoutineItems> addRoutine(@Body RoutineItems routineItems,@Query("day") String day);

    @DELETE("today-routine/{id}")
    Call<RoutineItems> deleteRoutineItem(@Path("id") String id);
}
