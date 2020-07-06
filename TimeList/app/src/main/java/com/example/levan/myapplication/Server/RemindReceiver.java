package com.example.levan.myapplication.Server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.levan.myapplication.MainPageActivity;
//import com.example.levan.myapplication.R;

public class RemindReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        ToastUtils.showMessage(context,"收到消息");
        Toast.makeText(context,"您的定时到了哦！",Toast.LENGTH_SHORT).show();
        //MediaPlayer.create(context, R.raw.remind).start();
        Bundle bundle = intent.getExtras();
        intent = new Intent(context, MainPageActivity.class);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}