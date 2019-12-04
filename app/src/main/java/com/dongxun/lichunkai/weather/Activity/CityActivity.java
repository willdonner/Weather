package com.dongxun.lichunkai.weather.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dongxun.lichunkai.weather.R;
import com.gyf.immersionbar.ImmersionBar;

public class CityActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editText_city;
    private TextView textView_cancel;
    private TextView textView_currentCity;


    private String currentCity = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        initStateBar();
        initView();
        initIntentData();


    }

    /**
     * 接收Intent传过来的数据
     */
    private void initIntentData() {
        Intent intent = getIntent();
        currentCity = intent.getStringExtra("currentCity");
        textView_currentCity.setText("当前城市：" + currentCity);
    }

    /**
     * 初始化组件
     */
    private void initView() {
        editText_city = findViewById(R.id.editText_city);
        textView_cancel= findViewById(R.id.textView_cancel);
        textView_cancel.setOnClickListener(this);
        textView_currentCity = findViewById(R.id.textView_currentCity);
    }

    /**
     * 初始化状态栏
     */
    private void initStateBar() {
        ImmersionBar.with(this).init();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.textView_cancel:
                finish();
                break;
        }
    }
}
