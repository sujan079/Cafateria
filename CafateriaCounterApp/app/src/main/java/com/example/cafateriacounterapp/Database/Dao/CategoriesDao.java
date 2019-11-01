package com.example.cafateriacounterapp.Database.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.cafateriacounterapp.Database.Models.DB_Category;

import java.util.List;

@Dao
public interface CategoriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addCategory(DB_Category category);

    @Query("SELECT * FROM categories_table")
    List<DB_Category> getAllCategories();

    @Query("DELETE FROM categories_table")
    void deleteCategories();
}
