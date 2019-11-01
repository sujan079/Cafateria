package com.example.cafateriacounterapp.Network.GSON_Models.OrdersHistory;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Order {

    @SerializedName("itemName")
    private String itemName;

    @SerializedName("price")
    private Double price;

    @SerializedName("categories")
    private List<String> categories;

    @SerializedName("quantity")
    private Integer quantity;

    @SerializedName("order_date")
    private String orderDate;

    @SerializedName("order_time")
    private String orderTime;

    public Order(String itemName, Double price, List<String> categories, Integer quantity, String orderDate, String orderTime) {
        this.itemName = itemName;
        this.price = price;
        this.categories = categories;
        this.quantity = quantity;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }
}
