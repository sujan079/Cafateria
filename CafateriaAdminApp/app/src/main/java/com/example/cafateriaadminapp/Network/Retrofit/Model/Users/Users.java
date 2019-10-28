package com.example.cafateriaadminapp.Network.Retrofit.Model.Users;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Users {

    @SerializedName("count")
    private Integer count;

    @SerializedName("users")
    private List<User> users;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
