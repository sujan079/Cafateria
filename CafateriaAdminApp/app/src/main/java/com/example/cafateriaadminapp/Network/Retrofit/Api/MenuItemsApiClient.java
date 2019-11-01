package com.example.cafateriaadminapp.Network.Retrofit.Api;

import androidx.lifecycle.LiveData;

import com.example.cafateriaadminapp.Network.Retrofit.Model.MenuItems.MenuItem;
import com.example.cafateriaadminapp.Network.Retrofit.Model.MenuItems.MenuItems;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MenuItemsApiClient {


    @GET("menu-items")
    Call<MenuItems> getAllMenuItems();


    @POST("menu-items")
    Call<MenuItem> insertMenuItem(@Body MenuItem menuItem);

    @GET("menu-items/{id}/")
    Call<MenuItem> getMenuItem(@Path("id") String id);

    @PATCH("menu-items/{id}/")
    Call<MenuItem> updateMenuItem(@Path("id")String id,@Body MenuItem menuItem);


    @DELETE("menu-items/{id}/")
    Call<MenuItem> deleteMenuItem(@Path("id")String id);

}
