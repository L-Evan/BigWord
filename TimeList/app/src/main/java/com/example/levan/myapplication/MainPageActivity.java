package com.example.levan.myapplication;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.levan.myapplication.DataClass.DayInfo;
import com.example.levan.myapplication.Fragment.EventFragment;
import com.example.levan.myapplication.Fragment.MyFragment;
import com.example.levan.myapplication.Fragment.TodayFragment;
import com.example.levan.myapplication.Server.MusicsService;
import com.example.levan.myapplication.User.LoginActivity;
import com.example.levan.myapplication.tools.notesSqlInfo;
import com.example.levan.myapplication.tools.saveInfoXml;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class MainPageActivity extends AppCompatActivity implements OnClickListener {
    //tarbor
    private BottomNavigationView navigation;
    //页面列表
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    //滑动切换容器
    private ViewPager mViewPager;
    //frag 管理设备
    private FragmentPagerAdapter mAdapter;
    //last
    private String username;
    //音乐
    private String path;
    private Intent musicIntend;
    private MusicsService musicService;
    private MusicsService.MyBinder binder;
    private myConn conn;
    //音乐按钮
    private Switch switchmusic;
    //数据
    private static List<DayInfo> DataList ;
    //事件
    private EventFragment tab01;
    //今天
    private TodayFragment tab02;
    //my
    private MyFragment tab03;
    //记录事件
    private String event;
    /**
     * 连接类  更新musicServeice 传递回来
     */
    private class myConn implements ServiceConnection{
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //初始化
            binder = ((MusicsService.MyBinder)iBinder);
            musicService = ((MusicsService.MyBinder)iBinder).getService();

            //闹钟
            if ("time".equals(event)) {
                Log.d("闹钟哦", "好");
                switchmusic.setChecked(true);
                controMusic(true);
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicService = null;
        }
    };
    /**
     * ViewPager
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    mViewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_notifications:
                    mViewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //标题
        setTitle("时序");
        Intent reintend = getIntent();
        //判别是真登陆了么
        Map<String,String> m = saveInfoXml.readStaticInfo(this);
        //记录是什么事件  待完善
        event =reintend.getStringExtra("event");
        if( m.get("staic")==null || m.get("username")==null || !m.get("staic").equals(String.valueOf(true)) ){
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else{
            getIntent().putExtra("username",m.get("username"));
        }
        //设置用户名
        username = reintend.getStringExtra("username");
        Toast.makeText(MainPageActivity.this, username + ",欢迎您！", Toast.LENGTH_SHORT).show();
        //初始化
        initView();
    }
    /**
     * 初始化
     */
    private void initView(){
        //navigation
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //音乐监听
        switchmusic = findViewById(R.id.switchMusic);
        switchmusic.setOnClickListener(this);
        //创建列表
        DataList = new Vector<DayInfo>();

        Log.d("闹钟哦","?"+event);
        initMusic();
        //更新轮播
        initFrag();
        //更新数据
        updataNotes();
    }

    /**
     * 初始化音乐
     */
    private void initMusic(){
        //音乐服务
        /*开启服务  将intend传递  第二种启动方式  对应stopService
          startService(musicIntend);*/
        musicIntend = new Intent(MainPageActivity.this,MusicsService.class);
        //连接对象 获取回调的musiserveice
        conn = new myConn();
        //绑定 函数回调onSerciceConnented函数，通过MusiceService函数下的onBind()方法获得binder 对象并实现绑定
        if( !bindService(musicIntend,conn,BIND_AUTO_CREATE) ){
            Log.d("失败！","999");
        }
    }
    /**
     * 初始化轮播和 frag页面
     */
    private void initFrag(){
        //初始化轮播器   页面
        mViewPager = findViewById(R.id.allView);
        //放入
        tab01 = new EventFragment();
        //更新t
        tab02 = new TodayFragment();
        tab03 = new MyFragment();
        mFragments.add(tab01);
        mFragments.add(tab02);
        mFragments.add(tab03);
        //标签页管理器s
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()){
            @Override
            public int getCount()
            {
                return mFragments.size();
            }
            @Override
            public Fragment getItem(int arg0)
            {
                return mFragments.get(arg0);
            }
        };
        //把fragement 的设配器设置到切换View里面
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            private int currentIndex;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position)
            {
                //选择tarbor后id
                navigation.getMenu().getItem(position).setChecked(true);
                currentIndex = position;
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    //显示屏幕后
    protected void onResume() {
        //处理请求数据
        super.onResume();
        Toast.makeText(this,"时序记录你的点滴",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.switchMusic :
                controMusic(switchmusic.isChecked());
                break;
        }
        Log.d("点了",findViewById(view.getId()).toString());
    }

    /**
     * 方法1 打开音乐s
     */
    void initOpenMusic(){
        //文件相对路径名
        //手机内存路径
        //Environment.getDataDirectory();//
        path = "a.mp3";///Music/a.mp3
        String pathway = path;
        File SDpath = Environment.getExternalStorageDirectory();
        File file = new File(SDpath,pathway);
        String path = file.getAbsolutePath();
        //音乐开启状况
        if(switchmusic.isChecked()){
            Log.d("打开影月？","?"+switchmusic.isChecked());
            if(binder==null){
                switchmusic.setChecked(false);
                Log.d("打开影月？","?"+switchmusic.isChecked());
                return;
            }
             if(file.exists() && file.length()>0){
                binder.play(path);
            }else{
                Toast.makeText(MainPageActivity.this,"找不到音乐！",Toast.LENGTH_SHORT).show();
            }
        }else{
             if(file.exists() && file.length()>0){
                binder.pause();
            }else{
                Toast.makeText(this,"找不到音乐！",Toast.LENGTH_SHORT).show();
            }
        }

    }
    /**
     * 音乐  方法2
     * @param is  音乐打开状态
     */
    private void controMusic(boolean is){
        //音乐开启状况
        if(switchmusic.isChecked()){
            Log.d("打开影月？","?"+switchmusic.isChecked());
            if(binder==null){
                switchmusic.setChecked(false);
                Log.d("打开影月？","?"+switchmusic.isChecked());
                return;
            }
            //方法1
            binder.play(path);
        }else{
            Log.d("打开影月？","?"+switchmusic.isChecked());
            binder.pause();
            //方法1
        }
    }
    /**
     * 重读数据库
     */
    private void updataNotes(){
        Vector< Map<String,String> > v = notesSqlInfo.queryNontesInfo(this,username,"endTime DESC");
        System.out.println(v.toString());
        DataList.clear();
        for(Map<String,String> m : v ){
            System.out.println(m);
            String id = m.get("id");
            String title = m.get("title");
            String note = m.get("remark");
            String type = m.get("type");
            String isView =  m.get("isView");
            String endTime =  m.get("endTime");
            String isOver =  m.get("isOver");
            DataList.add(new DayInfo( Integer.valueOf(id),title,  note,  type, "1".equals(isView),   Long.valueOf(endTime),Integer.valueOf(isOver)==1));
        }
        System.out.println(DataList);
    }
    /**
     * 页面回调
     * @param requestCode 请求
     * @param resultCoide 回复
     * @param data
     */
     protected void onActivityResult(int requestCode, int resultCoide, Intent data){
        //0失败   1成功  请求类型 2 3 4   2： 添加文章
         Log.d("我接收到了",""+requestCode+" "+resultCoide);
        if( requestCode==2 && resultCoide==1){
            Log.d("我接收到了 成功了","我需要去更新下list");
            updataNotes();
            updateFra();
        }else if( requestCode==3 || resultCoide==0){
            Log.d("3","到了");
             //返回回来更新下性别  0 是强制返回
             Map<String,String> m = saveInfoXml.readSexInfo(this);
             ImageView im= (ImageView) tab03.findViewById(R.id.imageView);
             if(m.get("sex")!=null && m.get("sex").equals("女")){
                 im.setImageResource(R.mipmap.girl);
             }else im.setImageResource(R.mipmap.man);
         }
         Log.d("!!!","到了");
    }
    /**
     * 更新所有页面的数据
     */
    public void updateFra(){
        //排序
        Collections.sort(DataList, new Comparator<DayInfo>() {
            @Override
            public int compare(DayInfo dayInfo, DayInfo t1) {
                return Long.compare(t1.endTime, dayInfo.endTime);
            }
        });
        tab01.updata();
        tab02.updata();
    }
    public static List<DayInfo> getList(){
        return  DataList ;
    }
}
