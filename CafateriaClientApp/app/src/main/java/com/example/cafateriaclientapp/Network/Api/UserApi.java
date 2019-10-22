package com.example.cafateriaclientapp.Network.Api;

import com.example.cafateriaclientapp.Network.GSON_Models.User.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserApi {

    @POST("users")
    Call<User> addUser(@Body User user);

    @GET("users/{user_id}/")
    Call<User> getUserData(@Path(value = "user_id",encoded = true) String user_id);
}
