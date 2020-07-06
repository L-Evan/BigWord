package com.example.levan.myapplication.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

class SqlBaseHelper extends SQLiteOpenHelper {

    public SqlBaseHelper(@Nullable Context context  ) {
        //@Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version
        super(context, "editNote.db", null, 1);

    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
          sqLiteDatabase.execSQL("create table userinfo(" +
                "id integer primary key autoincrement," +
                "username varchar(20) NOT NULL," +
                "sex integer NOT NULL DEFAULT 0," +
                "remark TEXT DEFAULT \"时序好棒棒！\" ," +
                "name varchar(30) DEFAULT \"帅哥\" ,"  +
                "password varchar(32) NOT NULL, " +
                "birthday TimeStamp NOT NULL DEFAULT (strftime('%s000', 'now')), " +
                "constraint un_username unique(username)"+
                ") ");
        sqLiteDatabase.execSQL("CREATE TABLE noteInfo(" +
                "id integer primary key autoincrement ," +
                "note   TEXT DEFAULT \"\" ," +
                "title   varchar(20) NOT NULL," +
                "time TimeStamp NOT NULL DEFAULT (strftime('%s','now')*1000)," +
                "endTime TimeStamp DEFAULT  (strftime('%s','now')*1000) ," +
                "type varchar(20) DEFAULT \"日常\" ," +
                "userid integer NOT NULL ," +
                "isView integer DEFAULT 1 ," +
                "isOver integer DEFAULT 0 ," +
                "FOREIGN KEY(userid) REFERENCES userinfo(id)" +
                ");");
                sqLiteDatabase.execSQL("insert into userinfo(id,username,password) values(1,\"admin\",'admin')");
                sqLiteDatabase.execSQL("insert into noteInfo(id,userid,title,isView) values(1,1,'欢迎来到时序！',1)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.d("mydebug","数据库已经更新了");
    }
}
