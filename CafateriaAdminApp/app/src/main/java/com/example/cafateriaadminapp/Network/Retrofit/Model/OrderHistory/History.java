package com.example.cafateriaadminapp.Network.Retrofit.Model.OrderHistory;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class History {

    @SerializedName("_id")
    private String _id;

    @SerializedName("itemName")
    private String itemName;

    @SerializedName("price")
    private Double price;

    @SerializedName("categories")
    private List<String> categories;

    @SerializedName("quantity")
    private Double quantity;
    @SerializedName("order_by")
    private String orderBy;
    @SerializedName("order_date")
    private String order_date;
    @SerializedName("order_time")
    private String order_time;

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }
}
