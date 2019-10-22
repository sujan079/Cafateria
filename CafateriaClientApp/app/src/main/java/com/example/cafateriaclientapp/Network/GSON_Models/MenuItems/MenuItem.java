package com.example.cafateriaclientapp.Network.GSON_Models.MenuItems;

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
