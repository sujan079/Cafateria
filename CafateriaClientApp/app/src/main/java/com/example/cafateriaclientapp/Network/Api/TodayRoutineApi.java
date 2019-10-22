package com.example.cafateriaclientapp.Network.Api;

import com.example.cafateriaclientapp.Network.GSON_Models.MenuItems.TodayRoutineData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TodayRoutineApi {

    @GET("today-routine")
    Call<TodayRoutineData> getTodaysRoutine(@Query("category")String category);

}
