package com.example.cafateriacounterapp.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {

    public static String[] getDateAndTime(Date date){
        SimpleDateFormat dateFormat=new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
        String dateInStr=dateFormat.format(date);
        return dateInStr.split(" ");
    }
}
