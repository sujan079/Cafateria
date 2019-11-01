package com.example.printerserver.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Order {

    @SerializedName("user")
    private String user;

    @SerializedName("email")
    private String email;

    @SerializedName("print_id")
    private int printId;

    @SerializedName("menu_items")
    private List<MenuItem> menuItems;

    public Order(String user, String email, List<MenuItem> menuItems) {
        this.user = user;
        this.email = email;
        this.menuItems = menuItems;
    }

    public Order(String user, List<MenuItem> menuItems) {
        this.user = user;
        this.menuItems = menuItems;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public int getPrintId() {
        return printId;
    }

    public void setPrintId(int printId) {
        this.printId = printId;
    }
}
