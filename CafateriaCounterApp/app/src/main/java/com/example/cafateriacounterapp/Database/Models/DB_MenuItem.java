package com.example.cafateriacounterapp.Database.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "menuItem_table")
public class DB_MenuItem {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private int id;



    @ColumnInfo(name = "item-name")
    private String itemName;

    @ColumnInfo(name = "category")
    private List<String> category;

    @ColumnInfo(name = "price")
    private Double price;

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

    public DB_MenuItem(String itemName, List<String> category, Double price) {
        this.itemName = itemName;
        this.category = category;
        this.price = price;
    }
}
