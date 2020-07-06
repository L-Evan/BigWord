package com.example.levan.myapplication.DataClass;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.levan.myapplication.DataClass.DayInfo;
import com.example.levan.myapplication.R;
import com.example.levan.myapplication.tools.notesSqlInfo;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * 自定义的dialog
 */
class ItemDialog extends Dialog implements View.OnClickListener{
    //组件
    private Button submitButton;
    private TextView remark;
    private TextView endTime;
    private TextView title;
    private TextView type;
    //数据
    private DayInfo item;
    private long realTime;
    private Context context;
    //是否改变了
    boolean isChange;
    public ItemDialog(Context context, int themeResId,DayInfo item) {
        super(context, themeResId);
        this.item = item;
        this.context = context;
        isChange = false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置布局
        setContentView(LayoutInflater.from(context).inflate(R.layout.slip_dialog, null));
        Log.d("create","quxiao");

        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();
    }
    private void initEvent(){
        submitButton.setOnClickListener(new View.OnClickListener() {//取消
            @Override
            public void onClick(View view) {
                String t = title.getText().toString();
                long time = realTime;
                String ty = type.getText().toString();
                String re = remark.getText().toString();
                //改变了数据
                if(!Objects.equals(t, item.title) ||  time!=item.endTime ||ty!=item.type ||re!=item.remark ){
                    isChange = notesSqlInfo.updateNontesInfo(context ,String.valueOf(item.id),t,re,ty,time);
                    item.title = t;
                    item.endTime = time;
                    item.type = ty;
                    item.remark = re;
                    //更新动态信息
                    item.updateData();
                    Log.d("date",item.date);
                }
                cancel();
            }
        });
        endTime.setOnClickListener(this);
    }
    private void initData(){
        title.setText(item.title);
        endTime.setText(item.date);
        type.setText(item.type);
        remark.setText(item.remark);
        realTime = item.endTime;
        Log.d("出刷","数据"+title.getText());
    }
    private void initView(){
        submitButton = findViewById(R.id.submitButton);
        remark = findViewById(R.id.remark);
        endTime = findViewById(R.id.endTime);
        title = findViewById(R.id.title);
        type = findViewById(R.id.notesType);
        //初始化
        Window dialogWindow = this.getWindow();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);// 边距设为0
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);//背景透明，不然会有个白色的东东
        dialogWindow.setWindowAnimations(R.style.dialogWindowAnim); //设置窗口弹出动画
        //定位处理
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度
        lp.height = 1200; // 高度
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.BOTTOM);
    }
    @Override
    public void onClick(View view) {

        switch (view.getId()){
            //时间
            case R.id.endTime:
                final Calendar calendar=Calendar.getInstance();
                realTime = calendar.getTime().getTime();
                calendar.setTime(new Date(item.endTime));
                DatePickerDialog datePickerDialog = new DatePickerDialog( this.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String text = year + "/" + (month + 1) + "/" + dayOfMonth + "/";
                        endTime.setText(text);
                        calendar.set(year,month,dayOfMonth);
                        realTime = calendar.getTime().getTime();
                    }
                }
                        ,calendar.get(Calendar.YEAR)
                        ,calendar.get(calendar.MONTH)
                        ,calendar.get(calendar.DATE)
                );
                datePickerDialog.show();
                break;
        }
    }
}