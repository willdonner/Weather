package com.dongxun.lichunkai.weather.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.dongxun.lichunkai.weather.Adapter.HotCityAdapter;
import com.dongxun.lichunkai.weather.Class.HotCity;
import com.dongxun.lichunkai.weather.R;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

public class SelectCityActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "SelectCityActivity";
    private ImageView imageView_back;
    private EditText editText;
    private GridView gridView_hotCity;
    private List<HotCity> cityList = new ArrayList<>();
    private String[] cityArray = {"定位","北京","上海","广州","深圳","珠海","佛山","南京","苏州","厦门","长沙",
            "成都","佛州","杭州","武汉","青岛","西安","太原","石家庄","沈阳","重庆","天津","南宁"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);

        initBar();
        initView();
        setAdapter();
    }

    /**
     * 设置热门城市适配器
     */
    private void setAdapter() {
        for (int i = 0;i < cityArray.length;i++) {
            HotCity hotCity = new HotCity();
            hotCity.setName(cityArray[i]);
            hotCity.setLocation(i==0?true:false);
            cityList.add(hotCity);
        }
        HotCityAdapter hotCityAdapter = new HotCityAdapter(this, cityList);
        gridView_hotCity.setAdapter(hotCityAdapter);

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

    /**
     * 初始化组件
     */
    private void initView() {
        imageView_back = findViewById(R.id.imageView_back);
        imageView_back.setOnClickListener(this);
        editText = findViewById(R.id.editText);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager)editText.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText,0);
        gridView_hotCity = findViewById(R.id.gridView_hotCity);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageView_back:
                finish();
                break;
        }
    }
}
