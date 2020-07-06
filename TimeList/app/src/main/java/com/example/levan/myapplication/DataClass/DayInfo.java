package com.example.levan.myapplication.DataClass;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

//首页的数据
public class DayInfo {
    String remark;
    String type;
    public long endTime;
    final int id;
    String title;
    public boolean isView;
    public boolean isOver;
    String day;
    String date;
    int imageView;
    private final static  long TIME_OF_DAY = 24*60*60*1000;
    public DayInfo(int id,String title, String remark, String type,   boolean isView , long endTime,boolean isOver ) {
        this.remark = remark;
        this.type = type;
        this.endTime = endTime;
        this.id = id;
        this.title = title;
        this.isView = isView;
        this.isOver = isOver;
        updateData();
    }
    public void updateData(){
        long startTime = new Date().getTime();
        this.date  = new SimpleDateFormat("yyyy/MM/dd").format(Long.valueOf(endTime));
        Log.d(""+endTime," "+startTime);
        long cha = Long.valueOf(endTime) -startTime;
        if( cha<0 ) cha = 0;
        this.day = String.valueOf(  cha/ TIME_OF_DAY );
    }
    public static List<DayInfo> changeNotes(Vector< Map<String,String> > v){
        List<DayInfo>l =  new Vector<DayInfo>();
        for(Map<String,String> m : v ){
            System.out.println(m);
            String id = m.get("id");
            String title = m.get("title");
            String note = m.get("remark");
            String type = m.get("type");
            String isView =  m.get("isView");
            String endTime =  m.get("endTime");
            String isOver =  m.get("isOver");
            l.add(new DayInfo( Integer.valueOf(id),title,  note,  type, "1".equals(isView),   Long.valueOf(endTime),Integer.valueOf(isOver)==1));
        }
        return l;
    }
}
