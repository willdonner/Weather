package com.dongxun.lichunkai.weather.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dongxun.lichunkai.weather.Activity.CityActivity;
import com.dongxun.lichunkai.weather.R;

import java.util.List;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder> {

    private List<String> mList;
    private OnItemClickListener listener;

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView_city;


        public ViewHolder( View itemView) {
            super(itemView);
            textView_city = (TextView)itemView.findViewById(R.id.textView_historyCity);
        }
    }

    public SearchHistoryAdapter(List<String> searchHistoryList) {
        mList = searchHistoryList;
    }

    //第一步 定义接口（选择城市和删除城市）
    public interface OnItemClickListener {
        void onClick(int position);
    }

    //第二步， 写一个公共的方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.searchhistory,parent,false);
        ViewHolder holder =  new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        String city = mList.get(position);
        holder.textView_city.setText(city);

        holder.textView_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
