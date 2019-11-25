package com.dongxun.lichunkai.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CityAdapter extends ArrayAdapter {

    public CityAdapter(Context context, int resource, List<RealtimeInfo> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        RealtimeInfo realtimeInfo = (RealtimeInfo)getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.city, null);

        TextView textView_city = view.findViewById(R.id.textView_city);//城市
        ImageView imageView_currentCity = view.findViewById(R.id.imageView_currentCity);//当前城市图（是：显示，不是：隐藏）
        TextView textView_provice = view.findViewById(R.id.textView_provice);//省份
        ImageView imageView_info = view.findViewById(R.id.imageView_info);//天气图
        TextView textView_details = view.findViewById(R.id.textView_details);//详情
        TextView textView_temperatureRange = view.findViewById(R.id.textView_temperatureRange);//温度范围

        textView_city.setText("昆明");
//        imageView_currentCity.setVisibility(realtimeInfo.getCurrentCity()?View.VISIBLE:View.GONE);
//        textView_provice.setText();
//        imageView_info.setImageResource();
//        textView_details.setText();
//        textView_temperatureRange.setText();



        return view;
    }
}

