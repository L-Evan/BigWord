package com.example.levan.myapplication.tools;

import java.util.Calendar;
import java.util.Date;

public class dateTools {

    public static String getWeeken(long time){
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(time));
        int weekDay = c.get(Calendar.DAY_OF_WEEK);
        String str;
        if(Calendar.MONDAY == weekDay){
            str = "星期一";
        }else if(Calendar.TUESDAY==weekDay){
            str = "星期二";
        }else if(Calendar.WEDNESDAY==weekDay){
            str ="星期三";
        }else if(Calendar.THURSDAY==weekDay){
            str ="星期四";
        }else if(Calendar.FRIDAY==weekDay){
            str ="星期五";
        }else if(Calendar.SATURDAY==weekDay){
            str ="星期六";
        }else {
            str ="星期日";
        }
        return str;
    }
}
