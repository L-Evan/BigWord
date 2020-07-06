package com.example.levan.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.levan.myapplication.R;
import com.example.levan.myapplication.User.LoginActivity;
import com.example.levan.myapplication.User.RegisterActivity;
import com.example.levan.myapplication.tools.UserSqlInfo;
import com.example.levan.myapplication.tools.saveInfoXml;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class MyInfoActivity extends AppCompatActivity  implements View.OnClickListener {
    private RadioGroup sex;
    private TextView remark;
    private TextView name;
    private TextView birthday;
    private ImageView head;
    private String username;
    private Button update;
    private Intent i ;
    private Button over;
    private long realTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
        setTitle("个人信息");
        init();
    }
    private void init(){
        name = findViewById(R.id.name);
        head = findViewById(R.id.headimge);
        remark = findViewById(R.id.remark);
        sex = findViewById(R.id.sex);
        birthday = findViewById(R.id.birthday);
        update = findViewById(R.id.updateButton);
        over = findViewById(R.id.over);
        //数据
        initData();
    }
    private void initData(){

        username = getIntent().getStringExtra("username");
        //查数据库
        Map<String,String> m = UserSqlInfo.queryUserInfo(this,username);
        //查询不到
        if(m==null || m.get("id").length()<=0 ){
            setResult(-1,getIntent());
            this.finish();
        }

        name.setText(m.get("name"));
        //性别 头
        int sexid = Integer.valueOf(m.get("sex"));
        if(m.get("sex").equals("0")){
            head.setImageResource(R.mipmap.man);
        }else head.setImageResource(R.mipmap.girl);
        //性别
        RadioButton b = (RadioButton) sex.getChildAt(sexid);
        b.setChecked(true);
        sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                //i是某一个
                RadioButton b = findViewById(i);
                Log.d("设置",b.getText().toString());
                if(b.getText().toString().equals("男")){
                    head.setImageResource(R.mipmap.man);
                }else{
                    head.setImageResource(R.mipmap.girl);
                }
            }

        });
        //日记内容
        remark.setText(m.get("remark"));
        //生日
        realTime = Long.valueOf(m.get("birthday"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        Date d = new Date(realTime);
        birthday.setText(sdf.format(d));
        //时间控件
        birthday.setOnClickListener(this);


        update.setOnClickListener( this);
        over.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch(view.getId()){

            case R.id.updateButton:
                Log.d("info",view.toString());
                String name =  this.name.getText().toString();
                String remark =  this.remark.getText().toString();
                String tbirthday = String.valueOf(realTime);
                String sex =  ""+this.sex.indexOfChild(findViewById(this.sex.getCheckedRadioButtonId()));
                String tp = username+name+remark+tbirthday+sex;

                //读取
                if( UserSqlInfo.updateUserInfo(this,username,name,sex,remark,tbirthday)){
                    //保存性别
                    RadioButton b = findViewById(this.sex.getCheckedRadioButtonId());
                    saveInfoXml.saveInfo(this,"sex",b.getText().toString());
                    Log.d("sex",b.getText().toString());
                    Toast.makeText(this,"更新成功",Toast.LENGTH_SHORT).show();
                    Log.d("info","suful"+tp);
                }else{
                    Toast.makeText(this,"更新失败",Toast.LENGTH_SHORT).show();
                    Log.d("info2","error"+tp);
                }
                break;
            case R.id.birthday:
                    //时间
                    final Calendar calendar=Calendar.getInstance();
                    calendar.setTime(new Date(realTime));
                    DatePickerDialog datePickerDialog = new DatePickerDialog( this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            String text = year + "年" + (month + 1) + "月" + dayOfMonth + "日";
                            birthday.setText(text);
                            calendar.set(year,month,dayOfMonth);
                            realTime = calendar.getTime().getTime();
                            Log.d("选择",""+realTime);
                        }
                    }
                            ,calendar.get(Calendar.YEAR)
                            ,calendar.get(Calendar.MONTH)
                            ,calendar.get(Calendar.DATE));
                    DatePicker datePicker = datePickerDialog.getDatePicker();
                    //设置最小时间
                    datePicker.setMaxDate(new Date().getTime());
                    datePickerDialog.show();
                break;

            case R.id.over :
                //注销
                saveInfoXml.saveStaticInfo(this,this.username,false);
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                Toast.makeText(this,"注销成功",Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
        }

    }
    protected void onRestart() {
        super.onRestart();
    }
}
