package com.example.cafateriaclientapp.Database.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.cafateriaclientapp.Database.Models.DB_User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void addUser(DB_User user);

    @Query("SELECT * FROM user")
    List<DB_User> getUser();

    @Query("DELETE FROM user")
    void delUser();
}
