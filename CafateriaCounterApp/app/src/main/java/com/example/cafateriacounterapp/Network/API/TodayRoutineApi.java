package com.example.cafateriacounterapp.Network.API;

import com.example.cafateriacounterapp.Network.GSON_Models.MenuItems.TodayRoutineData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TodayRoutineApi {

    @GET("today-routine")
    Call<TodayRoutineData> getCounterData(@Query( value = "category") String category);

}
