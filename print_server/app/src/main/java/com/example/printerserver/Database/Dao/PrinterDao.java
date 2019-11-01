package com.example.printerserver.Database.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.printerserver.Database.Models.IpAdders;

import java.util.List;

@Dao
public interface PrinterDao {

    @Insert
    void insert(IpAdders ipAdders);

    @Delete
    void delete(IpAdders ipAdders);

    @Query("SELECT * FROM ip_addr")
    List<IpAdders> getAllIp();
}
