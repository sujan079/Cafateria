package com.example.cafateriacounterapp.Network.GSON_Models.MenuItems;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TodayRoutineData {


    @SerializedName("categories")
    private List<String> categories;

    @SerializedName("routineItems")
    private List<MenuItem> menuItems;

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    @Override
    public String toString() {
        return "TodayRoutineData{" +
                "categories=" + categories +
                ", menuItems=" + menuItems +
                '}';
    }
}
