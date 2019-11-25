package com.dongxun.lichunkai.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class HotCityAdapter extends BaseAdapter {

        private List<HotCity> provinceBeanList;
        private LayoutInflater layoutInflater;

        public HotCityAdapter(Context context, List<HotCity> provinceBeanList) {
            this.provinceBeanList = provinceBeanList;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return provinceBeanList.size();
        }

        @Override
        public Object getItem(int position) {
            return provinceBeanList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.hotcity, null);
                holder = new ViewHolder();
                holder.city = (TextView) convertView.findViewById(R.id.textView_city);
                holder.location = (ImageView) convertView.findViewById(R.id.imageView_location);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.city.setText(provinceBeanList.get(position).getName());
            holder.location.setVisibility(provinceBeanList.get(position).getLocation()?View.VISIBLE:View.GONE);
            return convertView;
        }

        class ViewHolder {
            TextView city;
            ImageView location;
        }

    }
