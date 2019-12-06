package com.dongxun.lichunkai.weather.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.AlertDialog;
import android.app.StatusBarManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
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
import java.util.Collections;
import java.util.List;

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
    private ImageView imageView_noData_hotCity;
    private String newWeatherApiKey;
    private RecyclerView recyclerView_history;
    private ImageView imageView_noData_historyCity;
    private LinearLayout LinearLayout_history;
    private ImageView imageView_delete;


    private String currentCity = "";
    private List<String> searchHistoryList = new ArrayList<>();
    private List<SearchCity> hotCity = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        newWeatherApiKey = getResources().getString(R.string.newapikey);
        initStateBar();
        initView();
        initIntentData();
        getHotCityRequestWithOkHttp();
        getSearchHistory();
    }

    /**
     * 搜索历史
     */
    private void getSearchHistory() {

        //获取搜索过的城市
        searchHistoryList.removeAll(searchHistoryList);
        searchHistoryList = getHistoryCity();
        //有搜索历史显示，无不显示
        if (searchHistoryList.size() == 0) {
            recyclerView_history.setVisibility(View.GONE);
            imageView_noData_historyCity.setVisibility(View.VISIBLE);
            return;
        }else {
            recyclerView_history.setVisibility(View.VISIBLE);
            imageView_noData_historyCity.setVisibility(View.GONE);

//            LinearLayoutManager layoutManager = new LinearLayoutManager(this);//列表
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
     * @return
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
//        final String[] searchData = {"","","","","","","","","","","","","","","","","","","",""};//默认二十条数据
        final List<SearchCity> searchData = new ArrayList<>();
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
                                Log.w("TAG", "onResponse: " + status);
                                if (status.equals("ok")) {
                                    JSONArray object = temp.getJSONArray("basic");
                                    for (int i =0;i < object.length();i++){
                                        JSONObject object1 = object.getJSONObject(i);
                                        String location = object1.getString("location");
                                        String parent_city = object1.getString("parent_city");

                                        SearchCity searchCity = new SearchCity();
                                        searchCity.setLocation(location);
                                        searchCity.setParent_city(parent_city);
                                        searchData.add(searchCity);
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            imageView_noData_hotCity.setVisibility(View.GONE);
                                            ListView_hotCity.setVisibility(View.VISIBLE);
                                            setHotAdapter(searchData);
                                        }
                                    });

                                }else if(status.equals("unknown location")) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            imageView_noData_hotCity.setVisibility(View.VISIBLE);
                                            ListView_hotCity.setVisibility(View.GONE);
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
     * 设置列表适配器
     */
    private void setHotAdapter(final List<SearchCity> searchData) {
        SearchCityAdapter adapter = new SearchCityAdapter(this,R.layout.searchcity,searchData);
        ListView_hotCity.setAdapter(adapter);
        ListView_hotCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(view.getContext(),data[i],Toast.LENGTH_SHORT).show();

                //存储地点
                addSearchHistory(searchData.get(i).getLocation());

                //返回数据（返回城市）
                backWithData(searchData.get(i).getParent_city());
            }
        });
    }

    /**
     * 添加搜索记录
     */
    public void addSearchHistory(String city) {
        //获取搜索过的城市
        searchHistoryList = getHistoryCity();
        if (searchHistoryList.contains(city)) return;
        String saveData = "";
        for (int j = 0;j<searchHistoryList.size();j++){
            Log.w("TAG", "onItemClick: " +searchHistoryList.get(j));
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
                                        String parent_city = object1.getString("parent_city");

                                        SearchCity searchCity = new SearchCity();
                                        searchCity.setLocation(location);
                                        searchCity.setParent_city(parent_city);
                                        hotCity.add(searchCity);
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            setHotAdapter(hotCity);
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
        imageView_noData_hotCity = findViewById(R.id.imageView_noData_hotCity);
        recyclerView_history = findViewById(R.id.recyclerView_history);
        imageView_noData_historyCity = findViewById(R.id.imageView_noData_historyCity);
        LinearLayout_history = findViewById(R.id.LinearLayout_history);
        imageView_delete = findViewById(R.id.imageView_delete);
        imageView_delete.setOnClickListener(this);
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
            //搜索
            searchCity();
            //更换UI
            LinearLayout_history.setVisibility(View.GONE);
            imageView_logo.setImageResource(R.drawable.logo_search);
            textView_title.setText("搜索结果");
        }else {
            //显示搜索历史和热门城市
            LinearLayout_history.setVisibility(View.VISIBLE);
            imageView_noData_hotCity.setVisibility(View.GONE);
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
