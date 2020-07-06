package com.example.levan.myapplication.Fragment;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.levan.myapplication.DataClass.DayInfo;
import com.example.levan.myapplication.DataClass.InfoAdapter;
import com.example.levan.myapplication.MainPageActivity;
import com.example.levan.myapplication.R;
import com.example.levan.myapplication.DataClass.TypeAdapter;
import com.example.levan.myapplication.tools.notesSqlInfo;

import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 *  事项页
 */
public class EventFragment extends Fragment implements FragmentData {

    private Activity that ;
    //测试用的数据
    private List<String> types;
    private String username;
    private List<DayInfo> DataList ;
    private SearchView search;
    //布局内元素
    private RecyclerView rcv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate( R.layout.activity_main_event,container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        that = this.getActivity();
        init();
    }
    private Object findViewById(int id){
        return that.findViewById(id);
    }
    private  void init(){
        DataList =new Vector<DayInfo>();
        types = new Vector<String>();
        types.add("全部");
        types.add("日常");
        types.add("办公");

        search = (SearchView) findViewById(R.id.searchView);
        search.setIconified(true);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 0) {
                    Log.e("onQueryTextSubmit","我是点击回车按钮");
                    //添加下面一句,防止数据两次加载  //按下和松开
                    search.setIconified(true);
                }
                //queryString = query;
                //软件盘的搜索按钮点击就是在这里走的逻辑
                //点击搜索
                Log.e("sss!", query);
                //notes like "%query%"  or title like "%query%"
                String sql = "  note like \"%"+query+"%\"  or title like \"%"+query+"%\"  ";
                Vector<Map<String,String>> v = notesSqlInfo.searchNontesInfo(that,that.getIntent().getStringExtra("username"),sql,"endTime DESC");
                DataList.clear();
                List <DayInfo> l= DayInfo.changeNotes(v);
                for(int i =0;i<l.size();i++){
                    if(!l.get(i).isOver){
                        DataList.add(l.get(i));
                    }
                }
                rcv.getAdapter().notifyDataSetChanged();

                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("sss", newText);
                //输得内容改变的方法监听
                return false;
            }
        });

        RecyclerView lcv = (RecyclerView)findViewById( R.id.leftRecy);
        LinearLayoutManager lm = new LinearLayoutManager(that);
        lcv.setLayoutManager(lm);
        //右
        rcv = (RecyclerView)findViewById( R.id.rightRecy);
        //初始化布局
        LinearLayoutManager rm = new LinearLayoutManager(that);
        rcv.setLayoutManager(rm);
        //分隔线 隔空
        rcv.addItemDecoration(new DividerItemDecoration(that,DividerItemDecoration.VERTICAL));
        InfoAdapter adapter = new InfoAdapter((MainPageActivity) that,rcv,DataList);

        TypeAdapter typeAdapter  = new TypeAdapter(types,adapter);
        rcv.setAdapter(adapter);
        lcv.setAdapter(typeAdapter);
        //初始化适配器数据
        updata();
        System.out.println("更新Eve over!");

    }
    public void updata() {
        //不安全
        while(MainPageActivity.getList()==null){
        }
        DataList.clear();
        List <DayInfo> l= MainPageActivity.getList();
        for(int i =0;i<l.size();i++){
            if(!l.get(i).isOver){
                DataList.add(l.get(i));
            }
        }
        rcv.getAdapter().notifyDataSetChanged();
    }

}
