package com.example.levan.myapplication.Fragment;


import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.levan.myapplication.R;
import com.example.levan.myapplication.Server.AlarmManageService;
import com.example.levan.myapplication.MyInfoActivity;
import com.example.levan.myapplication.WebActivity;
import com.example.levan.myapplication.tools.saveInfoXml;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * 我的页面
 */
public class MyFragment extends Fragment implements ActivityFragment,View.OnClickListener {

    private Activity that;
    private LinearLayout userInfo;
    private String username;
    private TextView timeView;
    private TextView webView;
    private TextView anli;
    private ImageView head;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_my, container,false);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        that = this.getActivity();
        init();
        System.out.println("更新My over!");
    }

    @Override
    public void init() {
        userInfo = (LinearLayout)findViewById(R.id.userInfo);
        timeView = (TextView)findViewById(R.id.setTimeServer);
        webView = (TextView)findViewById(R.id.webText);
        head = (ImageView) findViewById(R.id.imageView);
        //头像
        Map<String,String> m  = saveInfoXml.readSexInfo(that);
        if(m.get("sex")!=null &&  m.get("sex").equals("女")){
            head.setImageResource(R.mipmap.girl);
        }else  head.setImageResource(R.mipmap.man);

        //设置监听
        anli = (TextView) findViewById(R.id.anli);
        initEvent();
    }

    /**
     * 监听
     */
    private void initEvent(){
        userInfo.setOnClickListener(this);

        anli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) that.getSystemService(Context.CLIPBOARD_SERVICE);
                String url = "https://sub.tbetbe.com";
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", url);
                Toast.makeText(that,"复制成功！去微信分享给其他人把！",Toast.LENGTH_SHORT);
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);


                //分享
                Intent share_intent = new Intent();
                share_intent.setAction(Intent.ACTION_SEND);
                share_intent.setType("text/plain");
                share_intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                share_intent.putExtra(Intent.EXTRA_TEXT, "推荐您使用一款软件:" + "时序："+url);
                share_intent = Intent.createChooser(share_intent, "分享");
                startActivity(share_intent);
            }
        });
        webView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent wi = new Intent(that,WebActivity.class);
                startActivity(wi);
            }
        });
        timeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                Dialog dialog = new TimePickerDialog(that, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // TODO Auto-generated method stub
                        Calendar calendar = Calendar.getInstance();
                        long t = calendar.getTime().getTime();
                        calendar.setTime(new Date());
                        calendar.set(calendar.get(Calendar.YEAR),calendar.get(calendar.MONTH),calendar.get(calendar.DATE),hourOfDay,minute);

                        long len = (calendar.getTime().getTime()-t)/1000;
                        //Log.d("时间",""+t+"  "+calendar.get(calendar.MONTH)+"   "+calendar.getTime().getTime());
                        if( len>0 ){
                            Toast.makeText(that, hourOfDay + "时" + minute + "分"+"提醒您！", Toast.LENGTH_SHORT).show();
                            Bundle bundle = new Bundle();
                            bundle.putString("title","happy");//result.getTxjg()
                            AlarmManageService.addAlarm(that,0,bundle,  len);
                        }else{
                            Toast.makeText(that, "时间好像过去了呢！", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true);   //是否为二十四制
                dialog.show();

            }
        });
    }
    @Override
    public Object findViewById(int id) {
        return that.findViewById(id);
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch( id ){
            case R.id.userInfo:
                    Intent intent=new Intent(that,MyInfoActivity.class);
                    intent.putExtra("username",  that.getIntent().getStringExtra("username"));
                    //启动 传送1
                    startActivityForResult(intent,3);
                break;
        }
    }

}
