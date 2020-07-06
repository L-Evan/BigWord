package com.example.levan.myapplication.Server;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.Calendar;
//添加一个提醒
public class AlarmManageService {
    private static AlarmManager alarmManager;
    private static String TAG = "AlarmManageService";
    public static void addAlarm(Context context, int requestCode, Bundle bundle, long s){
        Intent intent = new Intent(context,RemindReceiver.class);
        //数据 Bundle 是用来携带信息的
        intent.putExtras(bundle);
        intent.putExtra("event","time");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,requestCode,intent,0);
        Calendar calendar = Calendar.getInstance();
        //定时  minute分钟之后
        calendar.setTimeInMillis(System.currentTimeMillis());
        //播放影月秒数注意
        calendar.add(Calendar.SECOND, (int)s);
        //注册新提醒
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //AlarmManager.RTC_WAKEUP 表示闹钟在睡眠状态下会唤醒系统
        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
    }
}