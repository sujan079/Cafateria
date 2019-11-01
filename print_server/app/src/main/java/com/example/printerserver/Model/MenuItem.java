package com.example.printerserver.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MenuItem {

    @SerializedName("_id")
    private String _id;


    @SerializedName("itemName")
    private String itemName;

    @SerializedName("categories")
    private List<String> categories;

    @SerializedName("price")
    private Double price;

    @SerializedName("quantity")
    private Double quantity;

    public MenuItem(String _id, String itemName, List<String> categories, Double price, Double quantity) {
        this._id = _id;
        this.itemName = itemName;
        this.categories = categories;
        this.price = price;
        this.quantity = quantity;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
