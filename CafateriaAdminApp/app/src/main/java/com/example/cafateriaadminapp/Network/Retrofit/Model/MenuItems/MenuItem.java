package com.example.cafateriaadminapp.Network.Retrofit.Model.MenuItems;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MenuItem {

    @SerializedName("_id")
    private String id;

    @SerializedName("itemName")
    private String itemName;

    @SerializedName("price")
    private Double price;

    @SerializedName("categories")
    private List<String> categories;


    public MenuItem(String itemName, Double price, List<String> categories) {
        this.itemName = itemName;
        this.price = price;
        this.categories = categories;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
