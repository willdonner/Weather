package com.dongxun.lichunkai.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;

public class CityListActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageView_back;
    private ImageView imageView_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);

        initBar();
        initView();

    }

    /**
     * 初始化组件
     */
    private void initView() {
        imageView_back = findViewById(R.id.imageView_back);
        imageView_back.setOnClickListener(this);
        imageView_edit = findViewById(R.id.imageView_edit);
        imageView_edit.setOnClickListener(this);
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
