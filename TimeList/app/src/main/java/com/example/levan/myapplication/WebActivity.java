package com.example.levan.myapplication;

import android.media.MediaPlayer;
import android.net.http.SslError;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        getSupportActionBar().hide();
        setTitle("作者博客");
        //获得控件
        WebView webView = findViewById(R.id.web);
        //访问网页
         webView.loadUrl("https://sub.tbetbe.com/");
        Toast.makeText(WebActivity.this,"请稍等",Toast.LENGTH_SHORT).show();
        //系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //使用WebView加载显示url
                view.loadUrl(url);
                //返回true
                return true;
            }
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // TODO Auto-generated method stub
                //handler.cancel(); 默认的处理方式,WebView变成空白页
                //super.onReceivedSslError(view, handler, error);  忽略验证
                Log.d("GO","====>handler.proceed();");
                //接收证书
                handler.proceed();
            }
        });
    }
}
