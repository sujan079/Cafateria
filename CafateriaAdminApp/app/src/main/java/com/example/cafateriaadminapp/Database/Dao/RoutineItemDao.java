package com.example.cafateriaadminapp.Database.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.cafateriaadminapp.Database.Models.DB_RoutineItem;

import java.util.List;

@Dao
public interface RoutineItemDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addroutineItem(DB_RoutineItem routineItem);

    @Query("SELECT * FROM routineItem_table WHERE day=:day")
    List<DB_RoutineItem> getAllroutineItems(String day);


    @Query("DELETE FROM routineItem_table")
    void deleteroutineItems();

}
