package com.example.levan.myapplication.User;
import com.example.levan.myapplication.MainPageActivity;
import com.example.levan.myapplication.R;
import com.example.levan.myapplication.tools.*;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
//日志
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
//页面跳转
import android.content.Intent; //AppCompatActivity
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    //默认账号密码
    private String df_username = "admin";
    private String df_password = "admin";
    //登陆按钮
    private Button Login ;
    private RadioGroup rg;
    private RadioButton CheckButton ;
    //信息
    private RadioButton setSavePW;
    private TextView username ;
    private TextView password ;
    private TextView register ;
    private Map<String, String> usermp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//创建一个窗口
        setContentView(R.layout.activity_login);//布局文件配置
        setTitle("登录");
        init();
    }
    private void goMainPage(String name){
        Intent intent=new Intent(LoginActivity.this,MainPageActivity.class);
        intent.putExtra("event","login");
        intent.putExtra("username",name);

        Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
        //启动 传送1
        startActivityForResult(intent,1);
        saveInfoXml.saveStaticInfo(this,name,true);
        LoginActivity.this.finish();
    }
    private void init(){

        Map<String,String> m = saveInfoXml.readStaticInfo(this);
        if( m.get("staic")!=null && m.get("username")!=null && m.get("staic").equals(String.valueOf(true)) ){
            goMainPage(m.get("username"));
        }

        //初始  不能必须在onCreate 创建之后
        Login = findViewById(R.id.buttonLogin);
        username = findViewById(R.id.userText);
        password = findViewById(R.id.passwordText);
        register = findViewById(R.id.textViewR);
        rg  = findViewById(R.id.Rgroup);

        setSavePW = findViewById(R.id.setSavePW);
        //模拟点击 辅助
        final RadioButton trg = new RadioButton(this);
        trg.setChecked(false);
        setSavePW.setOnClickListener(new View.OnClickListener(){
            //先执行自己的改变后
            @Override
            public void onClick(View view) {
                trg.setChecked(!trg.isChecked());
                setSavePW.setChecked(trg.isChecked());
            }
        } );
        HashMap<String,String> mp = saveInfoXml.readUserInfo(this);
        username.setText(mp.get("username"));
        password.setText(mp.get("password"));
        //设置可以点击
        register.setClickable(true);
        //监听
        initEvent();
    }

    /**
     * 监听
     */
    void initEvent(){
        //快速注册按钮
        register.setOnClickListener( new OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intentRegister = new Intent( LoginActivity.this,RegisterActivity.class );
                //启动
                startActivityForResult(intentRegister,0);
            }
        });
        //登录按钮
        Login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //验证
                if ( login(LoginActivity.this)) {
                    goMainPage(username.getText().toString());
                } else {
                    //调试
                    System.out.println(username.getText().toString()+"-"+password.getText().toString()+"-" );
                    //消息
                    Toast.makeText(LoginActivity.this,"账号或密码错误",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 登录
     * @param context
     * @return
     */
    private boolean login(Context context){

            //读取文件 获取map对象
            usermp = UserSqlInfo.queryUser(context);
            //调试
            System.out.println("读取"+usermp.toString());
            //读取不到
            if(usermp==null){
                Toast.makeText(LoginActivity.this,"文件读取错误！",Toast.LENGTH_SHORT).show();
                return  false;
            }
            //输入的账户
            String nusername = username.getText().toString();
            String npassword = password.getText().toString();
            //查询的密码
            String itPassword  = usermp.get(nusername);


            System.out.println("读取1"+itPassword);
            System.out.println("读取2"+npassword);
            CheckButton = findViewById(rg.getCheckedRadioButtonId());
            String sta = CheckButton.getText().toString();

            ////默认的账号密码  正确登录
            if( sta.equals("离线") && nusername.equals(df_username) && npassword.equals(df_password))
                return true;
            try {
                if( npassword!=null && MD5.getMD5(npassword).equals(itPassword)){
                    if(setSavePW.isChecked())
                        saveInfoXml.saveUserInfo(this,nusername,npassword);
                    return true;
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        return false;
    }
    protected void onActivityResult(int requestCode ,int resultCoide,Intent data){

        if( requestCode==0 && resultCoide==1){
            username.setText(data.getStringExtra("username"));
            password.setText(data.getStringExtra("password"));
        }
    }
}
