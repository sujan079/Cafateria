package com.example.cafateriaclientapp.Network.GSON_Models.MenuItems;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TodayRoutineData {

    @SerializedName("categories")
    private List<String> categories;

    @SerializedName("routineItems")
    private List<MenuItem> routineItems;

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<MenuItem> getRoutineItems() {
        return routineItems;
    }

    public void setRoutineItems(List<MenuItem> routineItems) {
        this.routineItems = routineItems;
    }
}
