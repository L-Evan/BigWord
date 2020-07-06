package com.example.levan.myapplication.DataClass;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.levan.myapplication.MainPageActivity;
import com.example.levan.myapplication.R;

import java.util.List;
import java.util.Objects;
import java.util.Vector;

public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.ViewHolder> {

    private List<String> data;
    private InfoAdapter right;
    public TypeAdapter( List<String> types,InfoAdapter adapter) {
        data = types;
        right = adapter;
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView typeText;
        ViewHolder(View itemView) {
            super(itemView);
            typeText = itemView.findViewById(R.id.textType);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.type_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String s = data.get(position);
        holder.typeText.setText(s);
        //更新数据
        holder.typeText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                List<DayInfo> soucs = MainPageActivity.getList();
                Vector<DayInfo> datas = new Vector<>() ;
                //Log.d("len:",""+datas.size());
                for( DayInfo d : soucs){
                    //Log.d("data:",s+d.type);
                    if( (s.equals(d.type) || "全部".equals(s))  && !d.isOver){
                        datas.add(d);
                    }
                }
                right.getData().clear();
                right.getData().addAll(datas);
                right.notifyDataSetChanged();
            }
        });
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
}
