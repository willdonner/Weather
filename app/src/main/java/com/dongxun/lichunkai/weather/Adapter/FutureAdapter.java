package com.dongxun.lichunkai.weather.Adapter;

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

    public FutureAdapter(Context context, int resource, List<FutureInfo> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        FutureInfo futureInfo = (FutureInfo)getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.future, null);

        TextView textView_week = view.findViewById(R.id.textView_week);
        TextView textView_date = view.findViewById(R.id.textView_date);
        ImageView imageView_wid = view.findViewById(R.id.imageView_wid);
        TextView textView_weather = view.findViewById(R.id.textView_weather);
        TextView textView_temperature = view.findViewById(R.id.textView_temperature);

        textView_week.setText(futureInfo.getToday()?"今天":futureInfo.getWeek());
        textView_date.setText(futureInfo.getDate().split("-")[1] + "/" +futureInfo.getDate().split("-")[2]);
        imageView_wid.setImageResource(futureInfo.getWid_img());
        textView_weather.setText(futureInfo.getWeather());
        textView_temperature.setText(futureInfo.getTemperature());

        return view;
    }
}
