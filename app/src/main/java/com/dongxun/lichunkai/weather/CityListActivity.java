package com.dongxun.lichunkai.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;

public class CityListActivity extends AppCompatActivity implements View.OnClickListener {


    private String TAG = "CityListActivity";
    private ImageView imageView_back;
    private ImageView imageView_edit;
    private ListView listView_city;
    private ArrayList<RealtimeInfo> realtimeInfos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);

        initBar();
        initView();
        setAdapter();

    }

    /**
     * 设置城市列表适配器
     */
    private void setAdapter() {
        RealtimeInfo realtimeInfo = (RealtimeInfo)getIntent().getSerializableExtra("realtimeInfo");
        realtimeInfos.add(realtimeInfo);
        realtimeInfos.add(realtimeInfo);
        realtimeInfos.add(realtimeInfo);

        CityAdapter cityAdapter = new CityAdapter(CityListActivity.this,R.layout.city,realtimeInfos);
        listView_city.setAdapter(cityAdapter);
    }

    /**
     * 初始化组件
     */
    private void initView() {
        imageView_back = findViewById(R.id.imageView_back);
        imageView_back.setOnClickListener(this);
        imageView_edit = findViewById(R.id.imageView_edit);
        imageView_edit.setOnClickListener(this);
        listView_city = findViewById(R.id.listView_city);
    }

    /**
     * 初始化状态栏
     */
    private void initBar() {
        //沉浸式状态栏
        ImmersionBar.with(this).init();
        //修改状态栏颜色为黑色
        setAndroidNativeLightStatusBar(this,true);
    }


    /**
     * 修改状态栏颜色为黑色
     * @param activity
     * @param dark
     */
    private static void setAndroidNativeLightStatusBar(Activity activity, boolean dark) {
        View decor = activity.getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageView_back:
                finish();
                break;
            case R.id.imageView_edit:
                break;
        }
    }
}
