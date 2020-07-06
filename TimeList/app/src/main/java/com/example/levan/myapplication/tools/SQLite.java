package com.example.levan.myapplication.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SQLite {

//    public static void  creatSqlBase(Context context){
//        SqlBaseHelper sqlHelp = new SqlBaseHelper(context );
//        SQLiteDatabase db = sqlHelp.getWritableDatabase();
//        Log.d("MyDebug0","createDaba");
//    }
    //辅助内部类
    protected static class SqlHelper{
        Cursor cursor;
        SQLiteDatabase db;
        ContentValues values;
        SqlHelper(SQLiteDatabase db,Cursor cursor){
            this.cursor = cursor;
            this.db = db;
            values = null;
        }
        SqlHelper(SQLiteDatabase db,ContentValues values){
            this.db = db;
            this.values = values;
            cursor = null;
        }
        void free(){
            if(cursor!=null){
                cursor.close();
            }
            if(db!=null){
                db.close();
            }
        }

    }
    //合并预处理
    static String mergeArgs(String calName[]){
        //不能空额
        StringBuilder args= new StringBuilder();
        if(calName!=null)
            for( String cal : calName){
                args.append(cal).append("= ? ");
            }
        return args.toString();
    }
    //服务   合并ContentValues
    private static SqlHelper setDataSql(Context context, String calName[], String values[]) {
        SqlBaseHelper sqlHelp = new SqlBaseHelper(context);
        SQLiteDatabase db = sqlHelp.getWritableDatabase();
        ContentValues ans = new ContentValues();
        if( calName!=null && values!=null)
            for( int i =0;i<calName.length;i++){
                ans.put(calName[i],values[i]);
            }
        return new SqlHelper(db,ans);
    }

    //服务querySql  查询cursor
    private static SqlHelper queryEverySql(Context context, String tableName, String selecName[], String calName[], String values[]) {
        SqlBaseHelper sqlHelp = new SqlBaseHelper(context);
        SQLiteDatabase db = sqlHelp.getWritableDatabase();
        Cursor cursor  = db.query(tableName,selecName,mergeArgs(calName),values,null,null,null);
        SqlHelper help = new SqlHelper(db,cursor);
        return help;
    }
    static SqlHelper querySql(Context context, String tableName, String selecName[], String[] calName, String[] values){
        SqlHelper help = queryEverySql(context,tableName,selecName,calName,values);
        return help;
    }
    //插入用户
    public static long insertSql(Context context ,String tableName,String[] calName,String []values){
        long flag;
        SqlHelper  help= setDataSql(context,calName,values);
        flag = help.db.insert(tableName,null,help.values);
        help.db.close();
        return  flag;
    }

    /**
     * 更新文章（写多余的  不管了
     * @param context
     * @param tableName 表名
     * @param calName 字段数组
     * @param values 值
     * @return static Int
     */
    public static int updatatSql(Context context ,String tableName,String calName[],String values[],String whereArgs[]){
        int flag = 0;
        SqlHelper help = setDataSql(context,calName,values);
        //where args  就是预处理
         flag = help.db.update(tableName,help.values,mergeArgs(calName),whereArgs);
        help.db.close();
        return  flag;
    }
    public static int deleteSql(Context context ,String tableName,String calName[],String values[]){
        SqlBaseHelper sqlHelp = new SqlBaseHelper(context);
        SQLiteDatabase db = sqlHelp.getWritableDatabase();
        String args = mergeArgs(calName);
        return db.delete(tableName,args,values);
    }

}
