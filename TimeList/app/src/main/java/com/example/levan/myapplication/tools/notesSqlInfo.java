package com.example.levan.myapplication.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class notesSqlInfo extends SQLite {

    public static String getUserID(Context context, String username){
        Map<String,String> m = UserSqlInfo.queryUserInfo(context,username);
        return m.get("id");
    }
    //查询文章信息
    public static Vector< Map<String,String> > queryNontesInfo(Context context, String username,String sortName){
        Vector< Map<String,String> > v = new Vector< Map<String,String> >();
        SqlBaseHelper sqlHelp = new SqlBaseHelper(context);
        SQLiteDatabase db = sqlHelp.getWritableDatabase();
        String id = getUserID(context,username);

        //id找不到
        if(id==null || id.length()==0){
            return v;
        }
        //log 不能null
        Log.d("query userid",id);
        Cursor cursor  = db.query("noteInfo",new String[]{"*"},mergeArgs(new String[]{"userid"}),new String[]{id},null,null,sortName);

        while(cursor.getCount()>0 && cursor.moveToNext()){
            Map<String,String> mp = new HashMap<String,String>();
            mp.put( "id"   ,cursor.getString(cursor.getColumnIndex("id") ));
            mp.put( "remark"   ,cursor.getString(cursor.getColumnIndex("note") ));
            mp.put( "title"   ,cursor.getString(cursor.getColumnIndex("title") ));
            mp.put( "startTime"   ,cursor.getString(cursor.getColumnIndex("time") ));
            mp.put( "type"   ,cursor.getString(cursor.getColumnIndex("type") ));
            mp.put( "endTime"   ,cursor.getString(cursor.getColumnIndex("endTime") ));
            mp.put( "isView"   ,cursor.getString(cursor.getColumnIndex("isView") ));
            mp.put( "isOver"   ,cursor.getString(cursor.getColumnIndex("isOver") ));
            v.add(mp);
        }
        cursor.close();
        db.close();
        return v;
    }

    //查询文章搜信息
    public static Vector< Map<String,String> > searchNontesInfo(Context context, String username,String searchText,String sortName){
        SqlBaseHelper sqlHelp = new SqlBaseHelper(context);
        SQLiteDatabase db = sqlHelp.getWritableDatabase();
        String id = getUserID(context,username);
        Log.d("query userid",id);

        Cursor cursor  = db.query("noteInfo",new String[]{"*"},searchText,null,null,null,sortName);
        Vector< Map<String,String> > v = new Vector< Map<String,String> >();

        while(cursor.getCount()>0 && cursor.moveToNext()){
            Map<String,String> mp = new HashMap<String,String>();
            mp.put( "id"   ,cursor.getString(cursor.getColumnIndex("id") ));
            mp.put( "remark"   ,cursor.getString(cursor.getColumnIndex("note") ));
            mp.put( "title"   ,cursor.getString(cursor.getColumnIndex("title") ));
            mp.put( "startTime"   ,cursor.getString(cursor.getColumnIndex("time") ));
            mp.put( "type"   ,cursor.getString(cursor.getColumnIndex("type") ));
            mp.put( "endTime"   ,cursor.getString(cursor.getColumnIndex("endTime") ));
            mp.put( "isView"   ,cursor.getString(cursor.getColumnIndex("isView") ));
            mp.put( "isOver"   ,cursor.getString(cursor.getColumnIndex("isOver") ));
            v.add(mp);
        }
        cursor.close();
        db.close();
        return v;
    }


    /**
     * 插入
     * @param context
     * @param username
     * @param title
     * @param text
     * @param type
     * @param Time
     * @return
     */
    public static boolean insertNontesUser(Context context ,String username,String title,String text,String type,String Time,int isView){
        boolean flag = true;
        String userid = getUserID(context,username);
        SqlBaseHelper sqlHelp = new SqlBaseHelper(context);
        SQLiteDatabase db = sqlHelp.getWritableDatabase();
        ContentValues values = new ContentValues();
        long id;
        values.put("userid",userid);
        values.put("title",title);
        values.put("note",text);
        values.put("type",type);
        values.put("isView",isView);
        values.put("endTime",Long.valueOf(Time) );
        Log.d("time",Time);
        //我来的是好的了
        //long endTime =  df.parse(Time);

        id = db.insert("noteInfo",null,values);
        if(id<=0) flag =  false;
        Log.d("myDebug","insert"+id);
        db.close();
        return  flag;
    }
    /**
     * 更新文章
     */
    public static boolean updateNontesInfo(Context context ,String notesid,String title,String text,String type,long Time){
        boolean flag = true;
        SqlBaseHelper sqlHelp = new SqlBaseHelper(context);
        SQLiteDatabase db = sqlHelp.getWritableDatabase();
        ContentValues values = new ContentValues();

        long id  = 0;
        values.put("title",title);
        values.put("note",text);
        values.put("type",type);
        Log.d("time",""+Time);
        //我来的是好的了
        //long endTime =  df.parse(Time);
        values.put("endTime",Time );
        Log.d("id",notesid);
        Log.d("value",values.toString());
        id = db.update("noteInfo",values,mergeArgs(new String[]{"id"}),new String[]{notesid});
        if(id<=0) flag =  false;
        Log.d("myDebug","update"+id);
        db.close();
        return  flag;
    }
    /**
     * 更新完成状态
     */
    public static boolean updateNontesOverInfo(Context context ,String notesid,int isOver){
        boolean flag = true;
        SqlBaseHelper sqlHelp = new SqlBaseHelper(context);
        SQLiteDatabase db = sqlHelp.getWritableDatabase();
        ContentValues values = new ContentValues();
        long id  = 0;
        //我来的是好的了
        //long endTime =  df.parse(Time);
        values.put("isOver",isOver );
        Log.d("id",notesid);
        Log.d("value",values.toString());
        id = db.update("noteInfo",values,mergeArgs(new String[]{"id"}),new String[]{notesid});
        if(id<=0) flag =  false;
        Log.d("myDebug","update"+id);
        db.close();
        return  flag;
    }

}
