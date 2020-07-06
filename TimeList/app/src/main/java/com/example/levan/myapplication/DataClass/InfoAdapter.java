package com.example.levan.myapplication.DataClass;

import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.levan.myapplication.MainPageActivity;
import com.example.levan.myapplication.R;
import com.example.levan.myapplication.tools.dateTools;
import com.example.levan.myapplication.tools.notesSqlInfo;

import java.util.List;
import java.util.Objects;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder>  {

    private List<DayInfo> data;
    private MainPageActivity activity;
    private int rcvID;

    public  InfoAdapter(MainPageActivity activity,RecyclerView rcv , List<DayInfo> data){
        this.data = data;
        rcvID = rcv.getId();
        this.activity  = activity;
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title;
        TextView day;
        TextView date;
        RadioButton radio;
        LinearLayout allView;
        LinearLayout mainLinear;
        ViewHolder(View itemView) {
            super(itemView);
            //找到对应元素
            switch(rcvID){
                case R.id.mainRecy:
                    image = itemView.findViewById(R.id.imageViewIcon);
                    day = itemView.findViewById(R.id.textDay);
                    break;
                case R.id.rightRecy:
                    allView = itemView.findViewById(R.id.allView);
                    radio = itemView.findViewById(R.id.radioButton2);
                    break;
            }
            //对应2个不同的mainline
            mainLinear = itemView.findViewById(R.id.mainline);
            title = itemView.findViewById(R.id.textTitle);
            date = itemView.findViewById(R.id.textDate);

        }
    }
    @Override
    public InfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        parent.getContext();
        View view;
        if( rcvID ==R.id.mainRecy ){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_page_item_right,parent,false);
            //实例化得到Item布局文件的View对象
            //View v = View.inflate(context, R.layout.,null);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_page_item_left,parent,false);
        }

        return new ViewHolder(view);

    }
    //获取当前是第几个
    @Override
    public void onBindViewHolder(InfoAdapter.ViewHolder holder, final int position) {
        DayInfo di = data.get(position);
        //初始
        switch(rcvID){
            case R.id.mainRecy:
                if("办公".equals(di.type)){
                    holder.image.setImageResource(R.mipmap.idea);
                }else holder.image.setImageResource(R.mipmap.day);

                holder.day.setText(di.day);
                holder.date.setText(di.date);
                break;
            case R.id.rightRecy:
                //    holder.allView.setVisibility(View.GONE);
                holder.date.setText(di.date+" "+ dateTools.getWeeken(di.endTime));
                holder.radio.setChecked(false);
                holder.radio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if( notesSqlInfo.updateNontesOverInfo(activity,String.valueOf(data.get(position).id),1)){
                            Log.d("成功","更新");
                            List<DayInfo> v =  MainPageActivity.getList();
                            //InfoAdapter.this.notifyItemChanged(position);
                            for( int i =0;i<v.size() ;i++){
                                if(data.get(position).id==v.get(i).id){
                                    v.get(i).isOver = true;
                                }
                            }
                            activity.updateFra();
                        }
                        Log.d("成功","?");
                    }
                });
                break;
        }
        holder.title.setText(di.title);
         //窗口变化
        holder.mainLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //自定义 窗口
                final Dialog dialog = new ItemDialog(activity,R.style.Theme_AppCompat_Dialog,data.get(position)){
                    @Override
                    public void cancel() {
                        super.cancel();
                        //改变了
                        if( isChange ){
                            List<DayInfo> v =  MainPageActivity.getList();
                            //InfoAdapter.this.notifyItemChanged(position);
                            for( int i =0;i<v.size() ;i++){
                                if(data.get(position).id==v.get(i).id){
                                    v.set(i,data.get(position));
                                }
                            }
                        }
                        activity.updateFra();
                    }
                } ;
                // 弹出dialog
                //开始show才调用  oncreate
                dialog.show();
            }
        });
    }

    public List<DayInfo> getData() {
        return data;
    }
    //数量
    @Override
    public int getItemCount() {
        return data.size();
    }


}
