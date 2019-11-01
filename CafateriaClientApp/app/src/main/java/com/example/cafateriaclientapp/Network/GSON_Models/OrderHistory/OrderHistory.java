package com.example.cafateriaclientapp.Network.GSON_Models.OrderHistory;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderHistory {

    @SerializedName("orderHistory")
    private List<History> orderHistory;

    public List<History> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(List<History> orderHistory) {
        this.orderHistory = orderHistory;
    }
}
