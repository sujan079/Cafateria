package com.example.cafateriaclientapp.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.cafateriaclientapp.Database.Converters.ListTypeConverter;
import com.example.cafateriaclientapp.Database.Dao.OrdersDao;
import com.example.cafateriaclientapp.Database.Dao.UserDao;
import com.example.cafateriaclientapp.Database.Models.DB_Orders;
import com.example.cafateriaclientapp.Database.Models.DB_User;

@Database(entities = {DB_Orders.class, DB_User.class},version = 1,exportSchema = false)
@TypeConverters({ListTypeConverter.class})
public abstract class CafateriaDatabase extends RoomDatabase {


    private static Object LOCK;
    private static String DATABASE_NAME="cafateria_client";
    private static CafateriaDatabase INSTANCE;

    public static CafateriaDatabase getInstance(Context context){
        if(LOCK==null){
            LOCK=new Object();
            INSTANCE= Room.databaseBuilder(context,CafateriaDatabase.class,DATABASE_NAME)
                    .build();
        }
        return INSTANCE;
    }

    public abstract OrdersDao ordersDao();
    public abstract UserDao userDao();
}
