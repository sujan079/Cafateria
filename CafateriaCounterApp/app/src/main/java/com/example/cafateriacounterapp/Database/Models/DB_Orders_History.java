package com.example.cafateriacounterapp.Database.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "order_history")
public class DB_Orders_History{

    @ColumnInfo(name = "order_id")
    @PrimaryKey(autoGenerate = true)
    private int order_id;

    @ColumnInfo(name = "itemName")
    private String itemName;

    @ColumnInfo(name = "category")
    private List<String> category;

    @ColumnInfo(name = "price")
    private Double price;

    public DB_Orders_History(String itemName, List<String> category, Double price, String date, String time, int quantity) {
        this.itemName = itemName;
        this.category = category;
        this.price = price;
        this.date = date;
        this.time = time;
        this.quantity = quantity;
    }

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "time")
    private String time;

    @ColumnInfo(name = "quantity")
    private int quantity;





    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }
}
