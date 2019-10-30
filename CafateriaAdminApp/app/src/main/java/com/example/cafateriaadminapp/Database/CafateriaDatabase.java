package com.example.cafateriaadminapp.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.cafateriaadminapp.Database.Conveters.ListTypeConverter;
import com.example.cafateriaadminapp.Database.Dao.RoutineItemDao;
import com.example.cafateriaadminapp.Database.Models.DB_RoutineItem;


@Database(entities = {DB_RoutineItem.class},version = 1,exportSchema = false)
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

    public abstract RoutineItemDao routineItemDao();

}
