package com.dongxun.lichunkai.weather.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dongxun.lichunkai.weather.R;
import com.gyf.immersionbar.ImmersionBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CityActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private EditText editText_city;
    private TextView textView_cancel;
    private TextView textView_currentCity;
    private LinearLayout LinearLayout_currentCity;
    private ListView ListView_hotCity;
    private ImageView imageView_logo;
    private TextView textView_title;


    private String currentCity = "";
    private String[] hotCitys = {"","","","","","","","","",""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        initStateBar();
        initView();
        initIntentData();
        getHotCityRequestWithOkHttp();
    }

    /**
     * 搜索城市
     */
    private String[] searchCity() {
        final String[] searchData = {"","","","","","","","","",""};//默认十条数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();//新建一个OKHttp的对象
                    //和风请求方式
                    Request request = new Request.Builder()
                            .url("https://search.heweather.net/find?location="+editText_city.getText().toString()+"&key=d0922bd7034d4e669fda94cb4254ae19")
                            .build();//创建一个Request对象
                    //第三步构建Call对象
                    Call call = client.newCall(request);
                    //第四步:异步get请求
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            //显示信息

                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string();//处理返回的数据
                            try {
                                JSONObject responses = new JSONObject(responseData);
                                String newresponse = responses.getString("HeWeather6");
                                JSONArray arr = new JSONArray(newresponse);
                                JSONObject temp = (JSONObject) arr.get(0);
                                String status = temp.getString("status");
                                if (status.equals("ok")) {
                                    JSONArray object = temp.getJSONArray("basic");
                                    for (int i =0;i < object.length();i++){
                                        JSONObject object1 = object.getJSONObject(i);
                                        String location = object1.getString("location");
                                        searchData[i] = location;
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            setHotCityAdapter(searchData);
                                        }
                                    });

                                }else {

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        return searchData;
    }


    /**
     * 设置热门城市适配器
     */
    private void setHotCityAdapter(String[] data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        ListView_hotCity.setAdapter(adapter);
        ListView_hotCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(view.getContext(),hotCitys[i],Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 获取热门城市
     */
    private void getHotCityRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();//新建一个OKHttp的对象
                    //和风请求方式
                    Request request = new Request.Builder()
                            .url("https://search.heweather.net/top?group=cn&key=d0922bd7034d4e669fda94cb4254ae19&number=10")
                            .build();//创建一个Request对象
                    //第三步构建Call对象
                    Call call = client.newCall(request);
                    //第四步:异步get请求
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            //显示信息

                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string();//处理返回的数据
                            try {
                                JSONObject responses = new JSONObject(responseData);
                                String newresponse = responses.getString("HeWeather6");
                                JSONArray arr = new JSONArray(newresponse);
                                JSONObject temp = (JSONObject) arr.get(0);
                                String status = temp.getString("status");
                                if (status.equals("ok")) {
                                    JSONArray object = temp.getJSONArray("basic");
                                    for (int i =0;i < object.length();i++){
                                        JSONObject object1 = object.getJSONObject(i);
                                        String location = object1.getString("location");
                                        hotCitys[i] = location;
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            setHotCityAdapter(hotCitys);
                                        }
                                    });

                                }else {

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
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
        editText_city.addTextChangedListener(this);
        textView_cancel= findViewById(R.id.textView_cancel);
        textView_cancel.setOnClickListener(this);
        textView_currentCity = findViewById(R.id.textView_currentCity);
        LinearLayout_currentCity = findViewById(R.id.LinearLayout_currentCity);
        LinearLayout_currentCity.setOnClickListener(this);
        ListView_hotCity = findViewById(R.id.ListView_hotCity);
        imageView_logo = findViewById(R.id.imageView_logo);
        textView_title = findViewById(R.id.textView_title);
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
            case R.id.LinearLayout_currentCity:
                Toast.makeText(this,"当前城市",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //输入框内容改变前
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }
    //输入框内容改变时
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (editText_city.getText().toString().length() != 0){
            searchCity();
            imageView_logo.setImageResource(R.drawable.logo_search);
            textView_title.setText("搜索结果");
        }else {
            setHotCityAdapter(hotCitys);
            imageView_logo.setImageResource(R.drawable.logo_hotcity);
            textView_title.setText("热门城市");
        }
    }
    //输入框内容改变后
    @Override
    public void afterTextChanged(Editable editable) {

    }
}
