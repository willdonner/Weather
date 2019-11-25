package com.dongxun.lichunkai.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;

public class SelectCityActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "SelectCityActivity";
    private ImageView imageView_back;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);

        initBar();
        initView();
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
        editText.findFocus();
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager)editText.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText,0);
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
