package com.example.cafateriaadminapp.Network.Retrofit.Model.Users;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("_id")
    private String _id;

    @SerializedName("email")
    private String email;

    @SerializedName("active")
    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public User(String _id, String email, boolean active) {
        this._id = _id;
        this.email = email;
        this.active = active;
    }



    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
