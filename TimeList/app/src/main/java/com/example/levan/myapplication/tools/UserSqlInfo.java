package com.example.levan.myapplication.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.levan.myapplication.tools.SQLite;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserSqlInfo extends SQLite {
    //查询用户
    public static Map<String,String> queryUser(Context context){
        SqlHelper help = querySql(context,"userinfo",new String[]{"id","username","password"},null,null);
        Cursor cursor = help.cursor;
        Map<String,String> mp = new HashMap<String,String>();
        while(cursor.moveToNext()){
            mp.put(cursor.getString(cursor.getColumnIndex("username") ),cursor.getString(cursor.getColumnIndex("password") ));
        }
        help.db.close();
        return mp;
    }
    //查询用户信息
    public static Map<String,String> queryUserInfo(Context context,String username){
        SqlHelper help = querySql(context,"userinfo",new String[]{"*"},new String[]{"username"},new String[]{username});
        Cursor cursor = help.cursor;
        Map<String,String> mp = new HashMap<String,String>();
        if(cursor.getCount()>0 && cursor.moveToNext()){
            mp.put( "id"   ,cursor.getString(cursor.getColumnIndex("id") ));
            mp.put( "username"   ,cursor.getString(cursor.getColumnIndex("username") ));
            mp.put( "sex"   ,cursor.getString(cursor.getColumnIndex("sex") ));
            mp.put( "name"   ,cursor.getString(cursor.getColumnIndex("name") ));
            mp.put("remark" ,cursor.getString(cursor.getColumnIndex("remark") ));
            Date  d = new Date(cursor.getLong(cursor.getColumnIndex("birthday")));
            mp.put("birthday"  ,String.valueOf(d.getTime()) );
            help.free();
        }
        return mp;
    }
    //插入用户
    public static boolean insertUser(Context context ,String username,String password){
        boolean flag = true;
        SqlBaseHelper sqlHelp = new SqlBaseHelper(context);
        SQLiteDatabase db = sqlHelp.getWritableDatabase();
        ContentValues values = new ContentValues();
        long id;
        values.put("username",username);
        try {
            values.put("password",MD5.getMD5(password));
            id = db.insert("userinfo",null,values);
            if(id<=0) flag =  false;
            else{
                String tpid = notesSqlInfo.getUserID(context,username);
                Log.e("!",""+id);
                db.execSQL("insert into noteInfo(userid,title,isView) values("+Integer.valueOf(tpid)+",'欢迎来到时序！',1)");
            }
            Log.d("myDebug","insert");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Toast.makeText(context,"加密失败！",Toast.LENGTH_SHORT).show();
            flag = false;
        }
        db.close();
        return  flag;
    }

    //更新用户
    public static boolean updateUserInfo(Context context ,String username,String name,String sex,String remark,String birthday){
        boolean flag = true;
        long id;

        SqlBaseHelper sqlHelp = new SqlBaseHelper(context);
        SQLiteDatabase db = sqlHelp.getWritableDatabase();
        ContentValues values = new ContentValues();

        Date d = new Date(Long.valueOf(birthday));//sdf.parse(birthday);
        Log.d("mes",""+d.getTime());
        values.put("birthday",d.getTime());

        values.put("name",name);
//        Log.d("user",""+(Integer.valueOf(username)));
        values.put("sex",Integer.valueOf(sex));
        values.put("remark",remark);
        //
        id =db.update("userinfo",values, "username = ? ",new String[]{username});
        if(id<=0){
            flag =  false;
        }
        Log.d("???",""+id);
        db.close();
        return  flag;
    }


}
