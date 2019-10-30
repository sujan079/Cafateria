package com.example.cafateriaclientapp.Network.GSON_Models.User;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("_id")
    private String id;

    @SerializedName("email")
    private String email;

    @SerializedName("active")
    private boolean active;


    public User(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
