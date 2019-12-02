package com.dongxun.lichunkai.weather.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dongxun.lichunkai.weather.Class.FutureInfo;
import com.dongxun.lichunkai.weather.R;

import java.util.List;

public class FutureAdapter extends ArrayAdapter {
    private int resourceId;
    public FutureAdapter(Context context, int resource, List<FutureInfo> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }
    // 子项布局的id


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        FutureInfo futureInfo = (FutureInfo) getItem(position);
        View view;
        ViewHolder viewHolder;

        if (convertView == null){
            // inflate出子项布局，实例化其中的图片控件和文本控件
            convertView = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            // 通过id得到图片控件实例
            viewHolder.imageView_wid = convertView.findViewById(R.id.imageView_wid1);
            // 通过id得到文本空间实例
            viewHolder.textView_week = (TextView) convertView.findViewById(R.id.textView_week);
            viewHolder.textView_date = (TextView) convertView.findViewById(R.id.textView_date);
            viewHolder.textView_weather = (TextView) convertView.findViewById(R.id.textView_weather);
            viewHolder.textView_temperature = (TextView) convertView.findViewById(R.id.textView_temperature);
            // 缓存图片控件和文本控件的实例
            convertView.setTag(viewHolder);
        }
        else {
            view = convertView;
            // 取出缓存
            viewHolder = (ViewHolder)convertView.getTag();
        }
        // 直接使用缓存中的图片控件和文本控件的实例
        // 图片控件设置图片资源
        viewHolder.imageView_wid.setImageResource(futureInfo.getWid_img());
        // 文本控件设置文本内容
        viewHolder.textView_week.setText(futureInfo.getToday()?"今天":futureInfo.getWeek());
        viewHolder.textView_date.setText(futureInfo.getDate().split("-")[1] + "/" +futureInfo.getDate().split("-")[2]);
        viewHolder.textView_weather.setText(futureInfo.getWeather());
//        viewHolder.textView_temperature.setText(futureInfo.getTemperature());

//        View view = LayoutInflater.from(getContext()).inflate(R.layout.future, null);
//
//        TextView textView_week = view.findViewById(R.id.textView_week);
//        TextView textView_date = view.findViewById(R.id.textView_date);
//        ImageView imageView_wid = view.findViewById(R.id.imageView_wid);
//        TextView textView_weather = view.findViewById(R.id.textView_weather);
//        TextView textView_temperature = view.findViewById(R.id.textView_temperature);
//
//        textView_week.setText(futureInfo.getToday()?"今天":futureInfo.getWeek());
//        textView_date.setText(futureInfo.getDate().split("-")[1] + "/" +futureInfo.getDate().split("-")[2]);
//        imageView_wid.setImageResource(futureInfo.getWid_img());
//        textView_weather.setText(futureInfo.getWeather());
//        textView_temperature.setText(futureInfo.getTemperature());

        return convertView;
    }

    // 内部类
    class ViewHolder{
        ImageView imageView_wid;
        TextView textView_week,textView_date,textView_weather,textView_temperature;
    }
}
