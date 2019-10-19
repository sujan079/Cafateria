package com.example.cafateriacounterapp.Network.GSON_Models.MenuItems;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class MenuItem {

    @SerializedName("itemName")
    private String itemName;

    @SerializedName("price")
    private Double price;

    @SerializedName("categories")
    private List<String> categories;

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

    @Override
    public String toString() {
        return "MenuItem{" +
                "itemName='" + itemName + '\'' +
                ", price=" + price +
                ", categories=" + categories +
                '}';
    }
}
