package com.dongxun.lichunkai.weather.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dongxun.lichunkai.weather.Class.SearchCity;
import com.dongxun.lichunkai.weather.R;

import java.util.List;

public class SearchCityAdapter extends ArrayAdapter<SearchCity> {

    private int resourceId;
    public SearchCityAdapter(Context context, int resource, List<SearchCity> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        SearchCity searchCity = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView textView_city = view.findViewById(R.id.textView_city);
        textView_city.setText(searchCity.getLocation());
        return view;
    }
}
