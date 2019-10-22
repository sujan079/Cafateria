package com.example.cafateriaclientapp.Database.Converters;

import androidx.room.TypeConverter;

import java.util.Arrays;
import java.util.List;

public class ListTypeConverter {

    @TypeConverter
    public static String toString(List<String> strings){
        String saveString="";
        for (String str:
                strings) {
            saveString+=str+" ";
        }
        return saveString;
    }

    @TypeConverter
    public static List<String> toList(String str){
        List<String> strings= Arrays.asList(str.split(" "));
        return strings;
    }
}
