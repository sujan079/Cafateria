package com.example.cafateriaclientapp.Database.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "orders")
public class DB_Orders {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "item_id")
    private String itemId;

    @ColumnInfo(name = "item_name")
    private String itemName;

    @ColumnInfo(name = "categories")
    private List<String> categories;

    @ColumnInfo(name = "price")
    private Double price;

    @ColumnInfo(name = "quantity")
    private Double quantity;


    public DB_Orders(String itemId, String itemName, List<String> categories, Double price, Double quantity) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.categories = categories;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
