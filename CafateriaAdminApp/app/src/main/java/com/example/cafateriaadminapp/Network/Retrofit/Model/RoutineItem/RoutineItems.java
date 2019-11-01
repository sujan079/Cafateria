package com.example.cafateriaadminapp.Network.Retrofit.Model.RoutineItem;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RoutineItems {

    @SerializedName("routineItems")
    private List<RoutineItem> routineItems;

    public RoutineItems(List<RoutineItem> routineItems) {
        this.routineItems = routineItems;
    }

    public List<RoutineItem> getRoutineItems() {
        return routineItems;
    }

    public void setRoutineItems(List<RoutineItem> routineItems) {
        this.routineItems = routineItems;
    }

    public static class RoutineItem {

        @SerializedName("_id")
        private String _id;
        @SerializedName("itemName")
        private String itemName;
        @SerializedName("price")
        private Double price;
        @SerializedName("categories")
        private List<String> categories;
        @SerializedName("day")
        private String day;

        public RoutineItem(String itemName, Double price, List<String> categories, String day) {
            this.itemName = itemName;
            this.price = price;
            this.categories = categories;
            this.day = day;
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

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }
    }
}
