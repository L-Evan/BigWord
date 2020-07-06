package com.example.levan.myapplication.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.levan.myapplication.DataClass.DayInfo;
import com.example.levan.myapplication.DataClass.InfoAdapter;
import com.example.levan.myapplication.MainPageActivity;
import com.example.levan.myapplication.PutActivity;
import com.example.levan.myapplication.R;

import java.util.List;
import java.util.Vector;

/**
 * 我的备忘倒数
 */
public class TodayFragment extends Fragment implements ActivityFragment,View.OnClickListener,FragmentData{

    private Activity that ;
    //测试用的数据
    //private String[] datas = {"张三","李四","王五","麻子","小强"};
    private String username;
    private List<DayInfo> DataList ;
    private FloatingActionButton addButton;
    //布局内元素
    private RecyclerView rcv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.activity_main_today,container, false);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        that = this.getActivity();
        init();
    }
    public Object findViewById(int id){
        return that.findViewById(id);
    }
    public void init(){
        DataList =new Vector<DayInfo>();
        rcv = (RecyclerView)findViewById( R.id.mainRecy);
        //初始化布局
        LinearLayoutManager lm = new LinearLayoutManager(that);
        rcv.setLayoutManager(lm);
        InfoAdapter adapter = new InfoAdapter((MainPageActivity) that,rcv,DataList);
        rcv.setAdapter(adapter);
        addButton  = (FloatingActionButton)findViewById(R.id.addButton);
        addButton.setOnClickListener(this);
        updata();
        System.out.println("更新To over!");
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch( id ){
            case R.id.addButton:
                Intent intent=new Intent(that,PutActivity.class);
                intent.putExtra("username",  that.getIntent().getStringExtra("username"));
                //启动 传送1
                //startActivity(intent);
                getActivity().startActivityForResult(intent,2);
                break;
        }
    }
    public void updata() {
        while(MainPageActivity.getList()==null){
        }
        DataList.clear();
        List <DayInfo> l= MainPageActivity.getList();
        for(int i =0;i<l.size();i++){
            if(l.get(i).isView){
                DataList.add(l.get(i));
            }
        }
        rcv.getAdapter().notifyDataSetChanged();
    }
}
