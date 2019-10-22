package com.example.cafateriaclientapp.Network.GSON_Models.OrdersHistory;

import com.google.gson.annotations.SerializedName;

public class OrdersHistory {

    @SerializedName("_id")
    private String _id;

    @SerializedName("itemName")
    private String itemName;

    @SerializedName("price")
    private Double price;

    @SerializedName("order_by")
    private String order_by;

    @SerializedName("quantity")
    private Integer quantity;

    @SerializedName("order_date")
    private String order_date;

    @SerializedName("order_time")
    private String order_time;

    public OrdersHistory(String itemName, Double price, String order_by, Integer quantity, String order_date, String order_time) {
        this.itemName = itemName;
        this.price = price;
        this.order_by = order_by;
        this.quantity = quantity;
        this.order_date = order_date;
        this.order_time = order_time;
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

    public String getOrder_by() {
        return order_by;
    }

    public void setOrder_by(String order_by) {
        this.order_by = order_by;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
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
