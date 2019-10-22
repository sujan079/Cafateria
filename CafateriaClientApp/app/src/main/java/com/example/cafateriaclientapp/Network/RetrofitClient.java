package com.example.cafateriaclientapp.Network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient{

    private static Object LOCK=null;
    private static Retrofit INSTANCE;

    private static String BASE_URL="http://192.168.0.6:8000/";

    public Retrofit getINSTANCE(){
        if(LOCK==null){
            LOCK=new Object();
            INSTANCE= new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return INSTANCE;
    }
}
