package com.dongxun.lichunkai.weather.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dongxun.lichunkai.weather.Adapter.SearchCityAdapter;
import com.dongxun.lichunkai.weather.Adapter.SearchHistoryAdapter;
import com.dongxun.lichunkai.weather.Class.SearchCity;
import com.dongxun.lichunkai.weather.R;
import com.gyf.immersionbar.ImmersionBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private LinearLayout LinearLayout_err;
    private String newWeatherApiKey;
    private RecyclerView recyclerView_history;
    private LinearLayout LinearLayout_history;
    private ImageView imageView_delete;
    private TextView textView_errDetails;


    private String currentCity = "";
    private List<String> searchHistoryList = new ArrayList<>();
    private List<SearchCity> hotCity = new ArrayList<>();
    private Map<String,String> apiStatusMap = new HashMap<>();//搜索城市接口状态码和错误吗对应描述

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        initBasicData();
        initStateBar();
        initView();
        initIntentData();
        getHotCityRequestWithOkHttp();
        getSearchHistory();
    }

    /**
     * 初始化基础数据
     */
    private void initBasicData() {
        newWeatherApiKey = getResources().getString(R.string.newapikey);
        apiStatusMap.put("invalid key","无效的key，请检查你的key是否输入以及是否输入有误");
        apiStatusMap.put("invalid key type","你输入的key不适用于当前获取数据的方式，即SDK的KEY不能用于Web API或通过接口直接访问，反之亦然");
        apiStatusMap.put("invalid param","无效的参数，请检查你传递的参数是否正确、完整");
        apiStatusMap.put("invalid param","找不着");
        apiStatusMap.put("bad bind","错误的绑定，例如绑定的package name、bundle id或IP地址不一致的时候");
        apiStatusMap.put("no data for this location","该城市/地区没有你所请求的数据");
        apiStatusMap.put("no more requests","超过访问次数，需要等到当月最后一天24点（免费用户为当天24点）后进行访问次数的重置或升级你的访问量");
        apiStatusMap.put("no balance","没有余额，你的按量计费产品由于余额不足或欠费而不能访问，请尽快充值");
        apiStatusMap.put("too fast","超过限定的QPM");
        apiStatusMap.put("dead","无响应或超时，接口服务异常请联系我们");
        apiStatusMap.put("unknown location","没有你查询的这个地区，或者地区名称错误");
        apiStatusMap.put("permission denied","无访问权限，你没有购买你所访问的这部分服务");
        apiStatusMap.put("sign error","签名错误，请参考签名算法");

    }

    /**
     * 显示搜索历史
     */
    private void getSearchHistory() {

        //获取搜索过的城市
        searchHistoryList.removeAll(searchHistoryList);
        searchHistoryList = getHistoryCity();
        //有搜索历史显示，无不显示
        if (searchHistoryList.size() == 0) {
            LinearLayout_history.setVisibility(View.GONE);
            return;
        }else {
            LinearLayout_history.setVisibility(View.VISIBLE);

            StaggeredGridLayoutManager layoutManager1 = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL);//瀑布流
            recyclerView_history.setLayoutManager(layoutManager1);
            SearchHistoryAdapter searchHistoryAdapter = new SearchHistoryAdapter(searchHistoryList);
            recyclerView_history.setAdapter(searchHistoryAdapter);

            //点击事件(实现自定义的点击事件接口)
            searchHistoryAdapter.setOnItemClickListener(new SearchHistoryAdapter.OnItemClickListener() {
                @Override
                public void onClick(int position) {
                    //选取城市
                    backWithData(searchHistoryList.get(position));
                }
            });
        }

    }


    /**
     * 获取搜索历史城市
     * @return 历史城市List
     */
    public List<String> getHistoryCity() {
        List<String> data = new ArrayList<>();
        SharedPreferences preferences = getSharedPreferences("data",MODE_PRIVATE);
        String searchHistoryCity = preferences.getString("searchHistoryCity","");

        String[] saveData = searchHistoryCity.split(",");
        for (int i=0;i<saveData.length;i++){
            if (!saveData[i].trim().equals("")){
                data.add(saveData[i]);
            }
        }

        return data;
    }

    /**
     * 搜索城市
     */
    private void searchCity() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();//新建一个OKHttp的对象
                    //和风请求方式
                    Request request = new Request.Builder()
                            .url("https://search.heweather.net/find?number=20&location="+editText_city.getText().toString()+"&key="+newWeatherApiKey)
                            .build();//创建一个Request对象
                    //第三步构建Call对象
                    Call call = client.newCall(request);
                    //第四步:异步get请求
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //显示信息
                                    Toast.makeText(CityActivity.this,"搜索请求异常",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string();//处理返回的数据
                            try {
                                JSONObject responses = new JSONObject(responseData);
                                JSONObject temp = (JSONObject) new JSONArray(responses.getString("HeWeather6")).get(0);
                                final String status = temp.getString("status");

                                final List<SearchCity> searchData = new ArrayList<>();  //搜索结果List
                                switch (status){
                                    case "ok":
                                        JSONArray object = temp.getJSONArray("basic");
                                        for (int i =0;i < object.length();i++){
                                            JSONObject object1 = object.getJSONObject(i);
                                            String location = object1.getString("location");//地区／城市名称
                                            String parent_city = object1.getString("parent_city");//该地区／城市的上级城市

                                            SearchCity searchCity = new SearchCity();
                                            searchCity.setLocation(location);
                                            searchCity.setParent_city(parent_city);
                                            searchData.add(searchCity);
                                        }
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                LinearLayout_err.setVisibility(View.GONE);
                                                ListView_hotCity.setVisibility(View.VISIBLE);
                                                setHotAdapter(searchData);
                                            }
                                        });
                                        break;
                                    default:
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //接口数据异常，显示信息异常描述
                                                ListView_hotCity.setVisibility(View.GONE);
                                                LinearLayout_err.setVisibility(View.VISIBLE);
                                                textView_errDetails.setText(apiStatusMap.get(status));
                                            }
                                        });
                                        break;
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
     * 设置列表适配器
     */
    private void setHotAdapter(final List<SearchCity> searchData) {
        SearchCityAdapter adapter = new SearchCityAdapter(this,R.layout.searchcity,searchData);
        ListView_hotCity.setAdapter(adapter);
        ListView_hotCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //存储搜索的地点
                addSearchHistory(searchData.get(i).getLocation());
                //返回数据（返回主页城市数据）
                backWithData(searchData.get(i).getParent_city());
            }
        });
    }

    /**
     * 添加搜索记录（','号分割城市）
     */
    public void addSearchHistory(String city) {
        //获取搜索过的城市
        searchHistoryList = getHistoryCity();
        if (searchHistoryList.contains(city)) return;
        String saveData = "";
        for (int j = 0;j<searchHistoryList.size();j++){
            saveData = saveData.length() == 0?searchHistoryList.get(j):saveData + "," + searchHistoryList.get(j);
        }
        saveData = saveData + "," + city;
        //保存搜索数据
        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putString("searchHistoryCity",saveData);
        editor.apply();
    }

    /**
     * 带数据返回主页
     * @param city
     */
    public void backWithData(String city) {
        Intent intent = new Intent();
        intent.putExtra("resultCity",city);
        setResult(RESULT_OK,intent);
        finish();
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
                            .url("https://search.heweather.net/top?group=cn&key="+newWeatherApiKey+"&number=20")
                            .build();//创建一个Request对象
                    //第三步构建Call对象
                    Call call = client.newCall(request);
                    //第四步:异步get请求
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //显示信息
                                    Toast.makeText(CityActivity.this,"搜索请求异常",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string();//处理返回的数据
                            try {
                                JSONObject responses = new JSONObject(responseData);
                                JSONObject temp = (JSONObject) new JSONArray(responses.getString("HeWeather6")).get(0);
                                final String status = temp.getString("status");

                                switch (status){
                                    case "ok":
                                        JSONArray object = temp.getJSONArray("basic");
                                        for (int i =0;i < object.length();i++){
                                            JSONObject object1 = object.getJSONObject(i);
                                            String location = object1.getString("location");
                                            String parent_city = object1.getString("parent_city");

                                            SearchCity searchCity = new SearchCity();
                                            searchCity.setLocation(location);
                                            searchCity.setParent_city(parent_city);
                                            hotCity.add(searchCity);
                                        }
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                LinearLayout_err.setVisibility(View.GONE);
                                                ListView_hotCity.setVisibility(View.VISIBLE);
                                                setHotAdapter(hotCity);
                                            }
                                        });
                                        break;
                                    default:
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //接口数据异常，显示信息异常描述
                                                ListView_hotCity.setVisibility(View.GONE);
                                                LinearLayout_err.setVisibility(View.VISIBLE);
                                                textView_errDetails.setText(apiStatusMap.get(status));
                                            }
                                        });
                                        break;
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
        LinearLayout_err = findViewById(R.id.LinearLayout_err);
        recyclerView_history = findViewById(R.id.recyclerView_history);
        LinearLayout_history = findViewById(R.id.LinearLayout_history);
        imageView_delete = findViewById(R.id.imageView_delete);
        imageView_delete.setOnClickListener(this);
        textView_errDetails = findViewById(R.id.textView_errDetails);
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
                finish();
                break;
            case R.id.imageView_delete:
                deleteHistoryDialog();
                break;
        }
    }

    /**
     * 删除搜索历史对话框
     */
    private void deleteHistoryDialog() {
        AlertDialog.Builder builder  = new AlertDialog.Builder(this);
        builder.setTitle("确认" ) ;
        builder.setMessage("是否确认？" ) ;
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //保存空数据
                SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                editor.putString("searchHistoryCity","");
                editor.apply();
                //重新加载RecycleView
                getSearchHistory();
            }
        });
        builder.setNegativeButton("否", null);
        builder.show();
    }

    //输入框内容改变前
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }
    //输入框内容改变时
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (editText_city.getText().toString().length() != 0){
            //搜索
            searchCity();
            //更换UI
            LinearLayout_history.setVisibility(View.GONE);
            imageView_logo.setImageResource(R.drawable.logo_search);
            textView_title.setText("搜索结果");
        }else {
            //显示搜索历史和热门城市
            getHistoryCity();
            LinearLayout_err.setVisibility(View.GONE);
            ListView_hotCity.setVisibility(View.VISIBLE);
            setHotAdapter(hotCity);
            imageView_logo.setImageResource(R.drawable.logo_hotcity);
            textView_title.setText("热门城市");
        }
    }

    //输入框内容改变后
    @Override
    public void afterTextChanged(Editable editable) {

    }
}
