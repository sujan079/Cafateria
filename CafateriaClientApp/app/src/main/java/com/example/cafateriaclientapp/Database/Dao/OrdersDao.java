package com.example.cafateriaclientapp.Database.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cafateriaclientapp.Database.Models.DB_Orders;

import java.util.List;

@Dao
public interface OrdersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addOrder(DB_Orders order);


    @Update
    void  updateOrder(DB_Orders order);

    @Delete
    void  deleteOrder(DB_Orders order);

    @Query("DELETE FROM orders")
    void  deleteOrder();

    @Query("SELECT * FROM orders")
    List<DB_Orders> getOrders();

    @Query("SELECT * FROM orders WHERE item_id=:menu_id")
    List<DB_Orders> getOrdersById(String menu_id);

}
