package com.example.cafateriacounterapp.Database.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.cafateriacounterapp.Database.Models.DB_Orders_History;

import java.util.List;

@Dao
public interface OrderHistoryDao {

    @Insert
    void addOrderHistory(DB_Orders_History orders_history);

    @Query("SELECT * FROM order_history")
    List<DB_Orders_History> getAllOrderHistory();


    @Query("DELETE FROM order_history where order_id=:order_id")
    void deleteOrderHistory(int order_id);
}
