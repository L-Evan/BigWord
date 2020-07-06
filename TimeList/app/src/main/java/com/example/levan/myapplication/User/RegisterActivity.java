package com.example.levan.myapplication.User;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.levan.myapplication.R;
import com.example.levan.myapplication.tools.*;
public class RegisterActivity extends AppCompatActivity {
    private Intent intent;
    private TextView user;
    private TextView password;
    private TextView testpw ;
    private RadioButton radioButton ;
    private Button register ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("注册");
        init();
        //注册
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                int stc = Register();
                if(stc==0){
                    Toast.makeText(RegisterActivity.this,"注册成功！",Toast.LENGTH_SHORT).show();
                    setResult(1,intent);
                    RegisterActivity.this.finish();
                }else if(stc==-1){
                    Toast.makeText(RegisterActivity.this,"再检查一下哦！",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(RegisterActivity.this,"已存在用户了！",Toast.LENGTH_SHORT).show();
                }
            }
        } );
    }
    private void init(){
        //初始
        intent = this.getIntent();
        user = findViewById(R.id.user);
        password = findViewById(R.id.password);
        testpw = findViewById(R.id.testpw);
        radioButton = findViewById(R.id.radioButton);
        register = findViewById(R.id.button);
        //模拟点击 辅助  实现单按钮
        final RadioButton trg = new RadioButton(this);
        trg.setChecked(false);
        radioButton.setChecked(false);
        radioButton.setOnClickListener(new View.OnClickListener(){
            //先执行自己的改变后
            @Override
            public void onClick(View view) {
                trg.setChecked(!trg.isChecked());
                radioButton.setChecked(trg.isChecked());
            }

        } );
        user.setText( intent.getStringExtra("username"));
    }
    private int Register(){

        String pw = password.getText().toString();
        String us = user.getText().toString();
        String pw2 = testpw.getText().toString();

        if ( !radioButton.isChecked() || pw.length()<6 || pw.length()>16 || !pw.equals(pw2) || us.length()<1  ) {
            //密码错误
            return -1;
        }

        if( UserSqlInfo.insertUser(RegisterActivity.this,us,pw)  ){
            //saveInfoXml.saveUserInfo(RegisterActivity.this,  us,pw);
            intent.putExtra("username",us);
            intent.putExtra("password",pw);
            System.out.println("注册成功");
            return 0;
        }
        //报存失败
        return -2;
    }
    protected void onDestroy() {

//        AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this)
//                .setTitle("真的要离开吗")
//                .setMessage("离开不是为了分别，而是为了以最美的姿态再度重遇。")
//                .setPositiveButton("离别", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        finish();
//                    }
//                }).setNegativeButton("归来", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                         return ;
//                    }
//                }).create();
//        alertDialog.show();

        super.onDestroy();
    }

}
