package com.example.levan.myapplication.Server;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.levan.myapplication.R;

import java.io.IOException;

public class MusicsService extends Service {
    public MusicsService() {
    }
    private static MediaPlayer mediaPlayer;
    public class MyBinder extends Binder {
        public void play(String path){
            if(mediaPlayer==null){
                //定义播放
                //方法1
                //mediaPlayer = new MediaPlayer()
                //方法3
                mediaPlayer = new MediaPlayer().create(MusicsService.this,R.raw.a);
                //转文件  //方法2
                //AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.a);
                //指定音频
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                //指定位置
                /* 方法1 - 2
                try {
                    //file.getFileDescriptor(), file.getStartOffset(), file.getLength()  path
                    //方法1
                    //mediaPlayer.setDataSource(path);
                    //方法2
                    //mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(),
                            //file.getLength());
                    //MediaPlayer m = MediaPlayer.create(this, R.raw.a);
                            //m.start();
                            //m.setLooping(true);
                    mediaPlayer.prepare();
                    //设置prepare监听
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("找不到文件");
                }*/
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.start();
                    }
                });

            }else{
                int position = getCurrentProgress();
                //跳进度
                mediaPlayer.seekTo(position);
                //预览编译过程只能在start开始
                //try {
                //mediaPlayer.prepare();
                //} catch (IOException e) {
                //e.printStackTrace();
                //}
                mediaPlayer.start();
            }
        }
        //暂停 开始按钮
        public void pause(){
            if(mediaPlayer!=null && mediaPlayer.isPlaying()){
                mediaPlayer.pause();
                Log.d("咱","站停");
            }else if(mediaPlayer!=null && (!mediaPlayer.isPlaying())){
                mediaPlayer.start();
            }else{
                Log.d("咱","没找到音乐呢？？？");
            }
        }
        public MusicsService getService() {
            return MusicsService.this;
        }
    }
    //获得进度
    private int getCurrentProgress(){
        if(mediaPlayer!=null && mediaPlayer.isPlaying()){
            //获取进度
            return mediaPlayer.getCurrentPosition();
        }else if(mediaPlayer!=null && (!mediaPlayer.isPlaying()))
            return mediaPlayer.getCurrentPosition();
        return 0;
    }

    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("在绑定了！");
        return new MyBinder();
    }
    //服务销毁
    @Override
    public void onDestroy(){
        if(mediaPlayer!=null){
            //暂停
            mediaPlayer.stop();
            //释放资源
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("服务创建","c成功");
    }
    //服务开启
    @Override
    public ComponentName startService(Intent service) {
        Log.d("服务开始","c成功");
        return super.startService(service);
    }
    //服务绑定
    @Override
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        return super.bindService(service, conn, flags);
    }
}
