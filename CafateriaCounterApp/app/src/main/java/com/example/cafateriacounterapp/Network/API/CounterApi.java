package com.example.cafateriacounterapp.Network.API;

import com.example.cafateriacounterapp.Network.GSON_Models.MenuItems.CounterData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CounterApi {

    @GET("counter")
    Call<CounterData> getCounterData(@Query( value = "category") String category);

}
