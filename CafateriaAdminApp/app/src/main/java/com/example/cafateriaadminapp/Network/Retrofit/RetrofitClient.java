package com.example.cafateriaadminapp.Network.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit INSTANCE;
    private static String BASE_URL="https://cafateriaapp.herokuapp.com/";

    public static synchronized Retrofit getINSTANCE(){
        if(INSTANCE==null){
            INSTANCE=new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build();
        }
        return INSTANCE;
    }
}
