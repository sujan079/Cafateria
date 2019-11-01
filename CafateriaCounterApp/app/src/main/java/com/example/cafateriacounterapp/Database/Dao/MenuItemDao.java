package com.example.cafateriacounterapp.Database.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.cafateriacounterapp.Database.Models.DB_MenuItem;

import java.util.List;

@Dao
public interface MenuItemDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addMenuItem(DB_MenuItem menuItem);

    @Query("SELECT * FROM menuItem_table")
    List<DB_MenuItem> getAllMenuItems();

    @Query("SELECT * FROM menuItem_table WHERE category LIKE '%'||:category||'%'")
    List<DB_MenuItem> getAllMenuByCategory(String category);

    @Query("DELETE FROM menuItem_table")
    void deleteMenuItems();

}
