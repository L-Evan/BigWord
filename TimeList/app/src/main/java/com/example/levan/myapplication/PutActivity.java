package com.example.levan.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.example.levan.myapplication.tools.notesSqlInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PutActivity extends Activity implements View.OnClickListener {
    private Button submit;
    private Switch isView;
    private TextView startDay;
    private TextView endDay;
    private RadioGroup rg;
    private TextView remark;
    private TextView title;
    private long realEndTime;
    private long realStartTime;
    private RadioButton CheckButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put);
        initView();
    }
    @SuppressLint("WrongViewCast")
    private void initView(){
        submit = findViewById(R.id.submitButton);
        isView = findViewById(R.id.is_switch);
        startDay = findViewById(R.id.start);
        endDay = findViewById(R.id.end);
        rg = findViewById(R.id.type);
        Log.d("id",""+R.id.daytype);
        remark = findViewById(R.id.remark);
        title = findViewById(R.id.title);
        submit.setOnClickListener(this);
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy年MM月dd日");
        startDay.setText(sdf.format(new Date()));


        addElemt();
    }
    private void addElemt(){

        endDay.setOnClickListener(this);
        //不可输入
        endDay.setFocusableInTouchMode(false);
    }
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.submitButton :
                if(putNote()){
                    Log.d("发布","成功");
                    setResult(1,this.getIntent());
                    this.finish();
                }else{
                    Log.d("发布","失败");
                }
                break;
            case R.id.end:
                //时间
                final Calendar calendar=Calendar.getInstance();
                realStartTime = calendar.getTime().getTime();
                DatePickerDialog datePickerDialog = new DatePickerDialog( this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            String text = year + "年" + (month + 1) + "月" + dayOfMonth + "日";
                            endDay.setText(text);
                            calendar.set(year,month,dayOfMonth);
                            realEndTime = calendar.getTime().getTime();
                            Log.d("选择",""+realEndTime);
                        }
                    }
                ,calendar.get(Calendar.YEAR)
                ,calendar.get(Calendar.MONTH)
                 ,calendar.get(Calendar.DAY_OF_MONTH));
                DatePicker datePicker = datePickerDialog.getDatePicker();
                //设置最小时间
                datePicker.setMinDate(new Date().getTime());
                datePickerDialog.show();
                break;
        }
    }

    private boolean putNote(){

        String ptitle = title.getText().toString();
        boolean pisView = isView.isChecked();
        String pendDay = endDay.getText().toString();
        CheckButton = findViewById( rg.getCheckedRadioButtonId());
        String ptype = CheckButton.getText().toString();
        String premark = remark.getText().toString();

        int pview = isView.isChecked()?1:0;

        Log.d("type",ptype);
        String username  = getIntent().getStringExtra("username");

        if(ptitle.length()==0 || pendDay.length()==0)
            return false;
        return notesSqlInfo.insertNontesUser(this,username,ptitle,premark,ptype,""+realEndTime,pview);
    }

}
