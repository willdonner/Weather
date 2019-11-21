package com.dongxun.lichunkai.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static java.security.AccessController.getContext;

public class FutureAdapter extends ArrayAdapter {

    public FutureAdapter(Context context, int resource, List<FutureInfo> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        FutureInfo futureInfo = (FutureInfo)getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.future, null);

        TextView textView_date = view.findViewById(R.id.textView_date);
        ImageView imageView = view.findViewById(R.id.imageView_wid);
        TextView textView_weather = view.findViewById(R.id.textView_weather);
        TextView textView_temperature = view.findViewById(R.id.textView_temperature);

        textView_date.setText(futureInfo.getWeek() + " " +futureInfo.getDate().split("-")[1] + "/" +futureInfo.getDate().split("-")[2]);
        imageView.setImageResource(futureInfo.getWid_img());
        textView_weather.setText(futureInfo.getWeather());
        textView_temperature.setText(futureInfo.getTemperature());

        return view;
    }
}
