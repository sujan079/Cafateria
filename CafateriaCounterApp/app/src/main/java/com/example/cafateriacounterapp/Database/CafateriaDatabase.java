package com.example.cafateriacounterapp.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.cafateriacounterapp.Database.Conveters.ListTypeConverter;
import com.example.cafateriacounterapp.Database.Dao.CategoriesDao;
import com.example.cafateriacounterapp.Database.Dao.MenuItemDao;
import com.example.cafateriacounterapp.Database.Dao.OrderHistoryDao;
import com.example.cafateriacounterapp.Database.Models.DB_Category;
import com.example.cafateriacounterapp.Database.Models.DB_MenuItem;
import com.example.cafateriacounterapp.Database.Models.DB_Orders_History;

@Database(entities = {DB_MenuItem.class, DB_Category.class, DB_Orders_History.class},version = 1,exportSchema = false)
@TypeConverters({ListTypeConverter.class})

public abstract class CafateriaDatabase extends RoomDatabase{

    private static Object LOCK=null;
    private static CafateriaDatabase INSTANCE;
    private static String DATABASE_NAME="cafateria_db";

    public static CafateriaDatabase getInstance(Context context){
        if(LOCK==null){
            LOCK=new Object();
            INSTANCE=Room.databaseBuilder(context,CafateriaDatabase.class,DATABASE_NAME)
                    .build();
        }
        return INSTANCE;
    }

    public abstract MenuItemDao menuItemDao();
    public abstract OrderHistoryDao orderHistoryDao();
    public abstract CategoriesDao categoriesDao();
}
