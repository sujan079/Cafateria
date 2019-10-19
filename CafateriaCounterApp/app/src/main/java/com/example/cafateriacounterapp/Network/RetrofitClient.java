package com.example.cafateriacounterapp.Network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Object INSTANCE=null;
    private static String BASE_URL="http://192.168.0.6:8000/";
    private static Retrofit retrofit;

    public static Retrofit getInstance(){
        if(INSTANCE==null){
            INSTANCE=new Object();
            retrofit=new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
