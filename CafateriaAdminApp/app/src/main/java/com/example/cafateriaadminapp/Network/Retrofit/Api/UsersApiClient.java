package com.example.cafateriaadminapp.Network.Retrofit.Api;

import com.example.cafateriaadminapp.Network.Retrofit.Model.Users.User;
import com.example.cafateriaadminapp.Network.Retrofit.Model.Users.Users;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface UsersApiClient{

    @GET("users")
    Call<Users> getAllUsers();

    @PATCH("users/{id}")
    Call<User> updateUser(@Path("id") String id,@Body User updateItem);

    @DELETE("users/{id}")
    Call<Users> deleteUsers(@Path("id") String id);

}
