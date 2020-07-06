package com.example.levan.myapplication.tools;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class saveInfoXml {

    private saveInfoXml(){}
    //待继续完善
    public static boolean saveUserInfo(Context context, String username, String password) {
        SharedPreferences spf = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString("username", username);
        edit.putString("password", password);
        edit.commit();
       return true;
    }
    public static HashMap<String,String> readUserInfo(Context context) {
        SharedPreferences spf = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        String  username =  spf.getString("username",null);
        String  password = spf.getString("password",null);
        HashMap<String,String> h  = new HashMap<String,String>();
        h.put("username", username);
        h.put("password", password);
        return h;
    }
    public static boolean saveStaticInfo(Context context, String username, boolean sta) {
        SharedPreferences spf = context.getSharedPreferences("loginData", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString("username", username);
        edit.putString("staic", String.valueOf(sta));
        edit.commit();
        return true;
    }
    public static HashMap<String,String> readStaticInfo(Context context) {
        SharedPreferences spf = context.getSharedPreferences("loginData", Context.MODE_PRIVATE);
        String  username =  spf.getString("username",null);
        String  password = spf.getString("staic",null);
        HashMap<String,String> h  = new HashMap<String,String>();
        h.put("username", username);
        h.put("staic", password);
        return h;
    }
    //保存性别
    public static boolean saveInfo(Context context, String key, String value) {
        SharedPreferences spf = context.getSharedPreferences("loginData", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString(key, value);
        edit.commit();
        return true;
    }
    public static HashMap<String,String> readSexInfo(Context context) {
        SharedPreferences spf = context.getSharedPreferences("loginData", Context.MODE_PRIVATE);
        String  sex =  spf.getString("sex",null);
        HashMap<String,String> h  = new HashMap<String,String>();
        h.put("sex", sex);
        return h;
    }
}
