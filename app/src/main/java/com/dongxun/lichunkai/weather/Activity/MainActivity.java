package com.dongxun.lichunkai.weather.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dongxun.lichunkai.weather.Adapter.FutureAdapter;
import com.dongxun.lichunkai.weather.Class.FutureInfo;
import com.dongxun.lichunkai.weather.Class.RealtimeInfo;
import com.dongxun.lichunkai.weather.R;
import com.dongxun.lichunkai.weather.Utilities.ToolHelper;
import com.gyf.immersionbar.ImmersionBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.dongxun.lichunkai.weather.Utilities.ToolHelper.*;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";
    private LocationManager locationManager;
    private LocationListener locationListener;
    private String City = "昆明";
    private ImageView imageView_back;
    private TextView textView_city;
    private TextView textView_temperature;
    private TextView textView_humidity;
    private TextView textView_info;
    private ImageView imageView_wid;
    private TextView textView_power;
    private TextView textView_aqi;
    private ArrayList<FutureInfo> futureInfos = new ArrayList<>();
    private ListView ListView_future;
    private TextView textView_time;
    private TextView textView_loading;
    private ImageView imageView_loading;
    private LinearLayout LinearLayout_message;
    private String WeatherApiKey;
    private String WeatherApiKey_backup;
    private String newWeatherApiKey;
    private RealtimeInfo realtimeInfo = new RealtimeInfo();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //聚合天气api
        WeatherApiKey = getResources().getString(R.string.apikey);
        WeatherApiKey_backup = getResources().getString(R.string.apikey_backup);

        //和风天气api
        newWeatherApiKey = getResources().getString(R.string.newapikey);

        ImmersionBar.with(this).init();
        initView();
        setBack();
        getPermission();
    }

    /**
     * 最小化app
     */
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    /**
     * 显示信息
     * @param type 0：正在定位，1：正在更新，2：更新成功，3：更新失败，4：网络不可用
     */
    private void showMessage(final int type) {
        //一秒钟显示信息(数据获取成功则显示2秒，失败则一直显示)
        CountDownTimer countDownTimer = new CountDownTimer(1*1000, 2000) {
            @Override
            public void onTick(long millisUntilFinished) {
                switch (type){
                    case 0:
                        imageView_loading.setImageResource(R.drawable.logo_location);
                        textView_loading.setText("正在定位");
                        break;
                    case 1:
                        imageView_loading.setImageResource(R.drawable.logo_loading);
                        textView_loading.setText("正在更新");
                        break;
                    case 2:
                        imageView_loading.setImageResource(R.drawable.logo_succcess);
                        textView_loading.setText("更新成功");
                        break;
                    case 3:
                        imageView_loading.setImageResource(R.drawable.logo_fail);
                        textView_loading.setText("更新失败");
                        break;
                    case 4:
                        imageView_loading.setImageResource(R.drawable.logo_fail);
                        textView_loading.setText("网络不可用");
                        break;
                }
            }
            @Override
            public void onFinish() {
                switch (type){
                    case 2:
                        LinearLayout_message.setVisibility(View.GONE);
                        break;
                }
            }
        }.start();
    }

    /**
     * 设置背景图片和位置
     */
    private void setBack() {
        imageView_back.setImageResource(getBackImg());
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = -(height - imageView_back.getHeight())/2;
        imageView_back.setLayoutParams(layoutParams);
    }

    /**
     * 根据季节显示背景图
     */
    private int getBackImg() {
        switch (Integer.parseInt(new SimpleDateFormat("MM").format(new Date()))){
            case 3: case 4:case 5:
                return R.drawable.back_spring;
            case 6: case 7:case 8:
                return R.drawable.back_summer;
            case 9: case 10:case 11:
                return R.drawable.back_autumn;
            case 12: case 1:case 2:
                return R.drawable.back_winter;
        }
        return R.drawable.back_spring;
    }


    /**
     * 初始化组件
     */
    private void initView() {
        imageView_back = findViewById(R.id.imageView_back);
        textView_city = findViewById(R.id.textView_city);
        textView_temperature = findViewById(R.id.textView_temperature);
        textView_humidity = findViewById(R.id.textView_humidity);
        textView_info = findViewById(R.id.textView_info);
        imageView_wid = findViewById(R.id.imageView_wid);
        textView_aqi = findViewById(R.id.textView_aqi);
        textView_power = findViewById(R.id.textView_power);
        ListView_future = findViewById(R.id.ListView_future);
        textView_time = findViewById(R.id.textView_time);
        textView_loading = findViewById(R.id.textView_loading);
        imageView_loading = findViewById(R.id.imageView_loading);
        LinearLayout_message = findViewById(R.id.LinearLayout_message);
    }

    /**
     * 定位后根据城市查询天气
     */
    private void getDataByCity() {
        //显示信息
        showMessage(1);
        // 发送查询天气请求
        sendRequestWithOkHttp(City,WeatherApiKey);
        AirsendRequestWithOkHttp(City,newWeatherApiKey);
        forecastsendRequestWithOkHttp(City,newWeatherApiKey);
    }

    /**
     * 获取权限
     */
    private void getPermission() {
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){//未开启定位权限
            //开启定位权限,200是标识码
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},200);
        }else{
            getDataByCity();//定位后根据城市查询天气
        }
    }

    /**
     * 权限请求
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 200://刚才的识别码
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){//用户同意权限,执行我们的操作
                    getDataByCity();
                }else{//用户拒绝之后,当然我们也可以弹出一个窗口,直接跳转到系统设置页面
                    Toast.makeText(MainActivity.this,"未开启定位权限,请手动到设置去开启权限",Toast.LENGTH_LONG).show();
                }
                break;
            default:break;
        }
    }

    /**
     * 请求天气数据
     */
    private void sendRequestWithOkHttp(final String city,final String apikey){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();//新建一个OKHttp的对象
                    //和风请求方式
                    Request request = new Request.Builder()
                            .url("https://free-api.heweather.net/s6/weather/now?location="+city+"&key="+newWeatherApiKey+"")
                            .build();//创建一个Request对象
                    //第三步构建Call对象
                    Call call = client.newCall(request);
                    //第四步:异步get请求
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            //显示信息
                            showMessage(3);
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string();//处理返回的数据
                            try {
                                JSONObject responses = new JSONObject(responseData);
                                String error_code = responses.getString("error_code");
                                if(error_code.equals("10012")){
                                    sendRequestWithOkHttp(city,WeatherApiKey_backup);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                JSONObject responses = new JSONObject(responseData);
                                String newresponse = responses.getString("HeWeather6");
                                parseJSON(newresponse);//解析JSON
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
     * 请求天气空气质量数据
     */
    private void AirsendRequestWithOkHttp(final String city,final String apikey){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();//新建一个OKHttp的对象

                    //和风请求方式
                    Request request = new Request.Builder()
                            .url("https://free-api.heweather.net/s6/air/now?location="+city+"&key="+newWeatherApiKey+"")
                            .build();//创建一个Request对象
                    //第三步构建Call对象
                    Call call = client.newCall(request);
                    //第四步:异步get请求
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            //显示信息
                            showMessage(3);
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string();//处理返回的数据
                            try {
                                JSONObject responses = new JSONObject(responseData);
                                String newresponse = responses.getString("HeWeather6");
                                AirparseJSON(newresponse);//解析JSON
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
     * 请求天气未来7天的天气数据
     */
    private void forecastsendRequestWithOkHttp(final String city,final String apikey){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();//新建一个OKHttp的对象

                    //和风请求方式
                    Request request = new Request.Builder()
                            .url("https://free-api.heweather.net/s6/weather/forecast?location="+city+"&key="+newWeatherApiKey+"")
                            .build();//创建一个Request对象
                    //第三步构建Call对象
                    Call call = client.newCall(request);
                    //第四步:异步get请求
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            //显示信息
                            showMessage(3);
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string();//处理返回的数据
                            try {
                                JSONObject responses = new JSONObject(responseData);
                                String newresponse = responses.getString("HeWeather6");
                                forecastparseJSON(newresponse);//解析JSON
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

    //和风API
    // 解析当前天气未来天气
    /**
     * 解析JSON
     * @param responseData
     */
    private void forecastparseJSON(String responseData) {
        String basic= null,status = null,now= null,daily_forecast = null;
        try {
            JSONArray arr = new JSONArray(responseData);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject temp = (JSONObject) arr.get(i);
                basic = temp.getString("basic");
                status = temp.getString("status");
                daily_forecast = temp.getString("daily_forecast");
            }
            if (status.equals("ok")) {
                //当前空气质量信息
                JSONArray jsonObjectnow = new JSONArray(daily_forecast);
                //未来天气信息
                for (int i = 0;i < jsonObjectnow.length();i++) {
                    JSONObject future = jsonObjectnow.getJSONObject(i);

                    FutureInfo futureInfo = new FutureInfo();
                    futureInfo.setDate(future.getString("date"));
                    futureInfo.setTemperature(future.getString("tmp_max")+"℃"+"/"+future.getString("tmp_min")+"℃");
                    futureInfo.setWeather(future.getString("cond_txt_n"));
                    futureInfo.setWid_day(future.getString("wind_sc"));
//                    futureInfo.setWid_night(future.getString("wind_sc"));
//                    futureInfo.setDirect(future.getString("wind_dir"));
                    futureInfo.setToday(i == 0?true:false);
                    futureInfo.setWeek(ToolHelper.getWeekByDate(future.getString("date")));
                    futureInfo.setWid_img(ToolHelper.getWidImg(ToolHelper.isDay()?future.getString("cond_code_d"):future.getString("cond_code_n"),false));
                    futureInfos.add(futureInfo);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FutureAdapter myAdapter = new FutureAdapter(MainActivity.this,R.layout.future,futureInfos);
                        ListView_future.setAdapter(myAdapter);
                    }});

            }else {
                searchFail("");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //和风API
    // 解析当前天气的空气质量
    /**
     * 解析JSON
     * @param responseData
     */
    private void AirparseJSON(String responseData) {
        String basic= null,status = null,now= null,air_now_city = null;
        try {
            JSONArray arr = new JSONArray(responseData);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject temp = (JSONObject) arr.get(i);
                basic = temp.getString("basic");
                status = temp.getString("status");
                air_now_city = temp.getString("air_now_city");
            }
            if (status.equals("ok")) {
                //当前空气质量信息
                JSONObject jsonObjectnow = new JSONObject(air_now_city);
                   final String air_q = jsonObjectnow.optString("aqi");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView_aqi.setText("空气" + ToolHelper.getAqiLevel(Integer.parseInt(air_q)) + " " + air_q);
                    }});
            }else {
                searchFail("");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //和风API
    // 解析当前天气
    /**
     * 解析JSON
     * @param responseData
     */
    private void parseJSON(String responseData) {
        String basic= null,status = null,now= null,air_now_city = null;
        try {
            JSONArray arr = new JSONArray(responseData);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject temp = (JSONObject) arr.get(i);
                basic = temp.getString("basic");
                status = temp.getString("status");
                now = temp.getString("now");
            }
            JSONObject jsonObjectbasic = new JSONObject(basic);

            if (status.equals("ok")) {
                //城市
                String city = jsonObjectbasic.optString("location");

                //当前天气信息
                JSONObject jsonObjectnow = new JSONObject(now);
                realtimeInfo.setDirect(jsonObjectnow.optString("wind_dir"));
                realtimeInfo.setHumidity(jsonObjectnow.optString("hum"));
                realtimeInfo.setInfo(jsonObjectnow.optString("cond_txt"));
                realtimeInfo.setPower(jsonObjectnow.optString("wind_sc"));
                realtimeInfo.setTemperature(jsonObjectnow.getString("tmp"));
                realtimeInfo.setWid(jsonObjectnow.getString("cond_code"));

//                }
                searchSuccess("",city,realtimeInfo);
            }else {
                searchFail("");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询天气失败，更新UI
     */
    private void searchFail(final String reason) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //显示信息
                showMessage(3);
            }
        });
    }

    /**
     * 查询天气成功，更新UI
     * @param reason
     */
    private void searchSuccess(final String reason, final String city, final RealtimeInfo realtimeInfo){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //显示信息
                showMessage(2);
//                textView_reason.setText(reason);
                textView_city.setText(city);
                textView_temperature.setText(realtimeInfo.getTemperature() + "°");
                textView_humidity.setText("湿度 " + realtimeInfo.getHumidity());
                textView_info.setText(realtimeInfo.getInfo());
                imageView_wid.setImageResource(getWidImg(realtimeInfo.getWid(),true));
                textView_power.setText("风力 " + realtimeInfo.getPower());
                textView_time.setText(new SimpleDateFormat("YYYY年MM月dd日 E").format(new Date()));

            }
        });
    }


}
