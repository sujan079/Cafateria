package com.example.cafateriaadminapp.Network.Retrofit.Model.OrderHistory;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderHistory {

    @SerializedName("count")
    private Integer count;

    @SerializedName("orderHistory")
    private List<History> orderHistory;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<History> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(List<History> orderHistory) {
        this.orderHistory = orderHistory;
    }
}
