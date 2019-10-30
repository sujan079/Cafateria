package com.example.cafateriaadminapp.Database.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "routineItem_table")
public class DB_RoutineItem {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private int id;



    @ColumnInfo(name = "item-name")
    private String itemName;

    @ColumnInfo(name = "category")
    private List<String> category;

    @ColumnInfo(name = "price")
    private Double price;

    @ColumnInfo(name = "day")
    private String day;

    @ColumnInfo(name = "itemId")
    private String itemId;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public DB_RoutineItem(String itemName, List<String> category, Double price, String day, String itemId) {
        this.itemName = itemName;
        this.category = category;
        this.price = price;
        this.day = day;
        this.itemId = itemId;
    }
}
