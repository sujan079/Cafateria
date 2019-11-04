package com.example.printerserver.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;

import com.example.printerserver.Database.Dao.PrinterDao;
import com.example.printerserver.Database.Models.IpAdders;

@Database(entities = {IpAdders.class}, version = 1,exportSchema = false)
public abstract class RoomDatabase extends androidx.room.RoomDatabase {

    private static RoomDatabase INSTANCE;
    private static String DATABASE_NAME = "printers";

    public static RoomDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room
                    .databaseBuilder(context, RoomDatabase.class, DATABASE_NAME)
                    .build();
        }
        return INSTANCE;
    }

    public abstract PrinterDao printerDao();
}
