package com.example.cafateriaclientapp.Network.GSON_Models.Orders;

import androidx.core.app.NotificationCompat;

import com.example.cafateriaclientapp.Network.GSON_Models.MenuItems.MenuItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Order {

    @SerializedName("user")
    private String user;

    @SerializedName("menu_items")
    private List<MenuItem> menuItems;

    public Order(String user, List<MenuItem> menuItems) {
        this.user = user;
        this.menuItems = menuItems;
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
}
