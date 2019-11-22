package com.dongxun.lichunkai.weather;

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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "MainActivity";
    private LocationManager locationManager;
    private LocationListener locationListener;
    private String WeatherKey = "e187097c8e703fce523ff6e8204ef8cc";//查询天气key
    private String WeatherKey2 = "8843798dfd89eeab638b8396174a9c84";
    private String City = "昆明";//查询城市
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
    private Boolean needGetData = true;
    private ImageView imageView_cityList;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1){
                //do something
                if(needGetData && isNetworkConnected(MainActivity.this)){
                    location();
                }
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImmersionBar.with(this).init();
        initView();
        getPermission();
        setBack();
        reGetData();
    }

    /**
     * 每隔一秒请求一次天气数据
     */
    private void reGetData() {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
                //如果获取到了天气数据，停止该线程
                if (!needGetData){
                    cancel();
                }
            }
        };
        timer.schedule(timerTask,1000,1000);//延时1s，每隔1秒执行一次run方法
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
     * @param state 0：正在定位，1：正在更新，2：更新成功，3：更新失败，4：网络不可用
     */
    private void showMessage(final int state) {
        //一秒钟显示信息(数据获取成功则显示2秒，失败则一直显示)
        CountDownTimer countDownTimer = new CountDownTimer(1*1000, 2000) {
            @Override
            public void onTick(long millisUntilFinished) {
                switch (state){
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
                switch (state){
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
        imageView_cityList = findViewById(R.id.imageView_cityList);
        imageView_cityList.setOnClickListener(this);
    }

    /**
     * 定位后根据城市查询天气
     */
    private void getDataByCity() {
        //显示信息
        showMessage(0);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            return;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.removeUpdates(locationListener);

        //显示信息
        showMessage(1);
        // 发送查询天气请求
        sendRequestWithOkHttp(City);
    }

    /**
     * 获取权限
     */
    private void getPermission() {
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){//未开启定位权限
            //开启定位权限,200是标识码
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},200);
        }else{
            location();//开始定位
//           Toast.makeText(MainActivity.this,"已开启定位权限",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 检查当前网络是否可用
     * @return
     */
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 定位
     */
    public void location(){
        if (!isNetworkConnected(this)) {
            showMessage(4);
            return;
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Geocoder gc = new Geocoder(MainActivity.this, Locale.getDefault());
                List<Address> locationList = null;
                try {
                    locationList = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Address address = locationList.get(0);//得到Address实例

                if (address == null) {
                    return;
                }
//                String countryName = address.getCountryName();//得到国家名称
//                String adminArea = address.getAdminArea();//省
                String locality = address.getLocality();//得到城市名称
                City = locality.replace("市","");
                String featureName = address.getFeatureName();//得到周边信息

            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
            @Override
            public void onProviderEnabled(String provider) {
            }
            @Override
            public void onProviderDisabled(String provider) {
            }
        };
        getDataByCity();//定位后根据城市查询天气
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
                    location();//开始定位
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
    private void sendRequestWithOkHttp(final String city){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();//新建一个OKHttp的对象
                    Request request = new Request.Builder()
                            .url("https://apis.juhe.cn/simpleWeather/query?city="+city+"&key="+WeatherKey2+"")
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
                            needGetData = false;
                            String responseData = response.body().string();//处理返回的数据
                            parseJSON(responseData);//解析JSON
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 解析JSON
     * @param responseData
     */
    private void parseJSON(String responseData) {
        try {
            JSONObject response = new JSONObject(responseData);
            String error_code = response.getString("error_code");
            String reason = response.getString("reason");
            if (error_code.equals("0")) {
                //城市
                String city = response.getJSONObject("result").getString("city");

                //当前天气信息
                JSONObject realtime = response.getJSONObject("result").getJSONObject("realtime");

                RealtimeInfo realtimeInfo = new RealtimeInfo();
                realtimeInfo.setAqi(realtime.getString("aqi"));
                realtimeInfo.setDirect(realtime.getString("direct"));
                realtimeInfo.setHumidity(realtime.getString("humidity"));
                realtimeInfo.setInfo(realtime.getString("info"));
                realtimeInfo.setPower(realtime.getString("power"));
                realtimeInfo.setTemperature(realtime.getString("temperature"));
                realtimeInfo.setWid(realtime.getString("wid"));

                //未来天气信息
                JSONArray JSONArray_future = response.getJSONObject("result").getJSONArray("future");

                for (int i = 0;i < JSONArray_future.length();i++) {
                    JSONObject future = JSONArray_future.getJSONObject(i);

                    FutureInfo futureInfo = new FutureInfo();
                    futureInfo.setDate(future.getString("date"));
                    futureInfo.setTemperature(future.getString("temperature"));
                    futureInfo.setWeather(future.getString("weather"));
                    futureInfo.setWid_day(future.getJSONObject("wid").getString("day"));
                    futureInfo.setWid_night(future.getJSONObject("wid").getString("night"));
                    futureInfo.setDirect(future.getString("direct"));
                    futureInfo.setToday(i == 0?true:false);
                    futureInfo.setWeek(getWeek(future.getString("date")));
                    futureInfo.setWid_img(getWidImg(isDay()?future.getJSONObject("wid").getString("day"):future.getJSONObject("wid").getString("night"),false));
                    futureInfos.add(futureInfo);
                }
                searchSuccess(reason,city,realtimeInfo,futureInfos);
            }else {
                searchFail(reason);
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
    private void searchSuccess(final String reason, final String city, final RealtimeInfo realtimeInfo, final ArrayList<FutureInfo> futureInfos){
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
                textView_aqi.setText("空气" + getAqiLevel(Integer.parseInt(realtimeInfo.getAqi())) + " " + realtimeInfo.getAqi());
                textView_time.setText(new SimpleDateFormat("YYYY年MM月dd日 E").format(new Date()));

                FutureAdapter myAdapter = new FutureAdapter(MainActivity.this,R.layout.future,futureInfos);
                ListView_future.setAdapter(myAdapter);
            }
        });
    }

    /**
     * 根据当前日期获得是星期几
     * time=yyyy-MM-dd
     * @return
     */
    public static String getWeek(String time) {
        String Week = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int wek=c.get(Calendar.DAY_OF_WEEK);
        if (wek == 1) {
            Week += "周天";
        }
        if (wek == 2) {
            Week += "周一";
        }
        if (wek == 3) {
            Week += "周二";
        }
        if (wek == 4) {
            Week += "周三";
        }
        if (wek == 5) {
            Week += "周四";
        }
        if (wek == 6) {
            Week += "周五";
        }
        if (wek == 7) {
            Week += "周六";
        }
        return Week;
    }

    /**
     * 判断时间是否为白天
     */
    public static Boolean isDay() {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("HH");
        String str = df.format(date);
        int a = Integer.parseInt(str);
        if (a > 18 && a <= 24) {
            return false;
        }
        return true;
    }

    /**
     * 获取天气对应的图标
     * @param wid 天气标识
     * @param isWhite 是否查找白色图标
     * @return
     */
    public static int getWidImg(String wid,Boolean isWhite) {
        switch (wid){
            case "00": return isWhite?R.drawable.wid_00:R.drawable.info_00;
            case "01": return isWhite?R.drawable.wid_01:R.drawable.info_01;
            case "02": return isWhite?R.drawable.wid_02:R.drawable.info_02;
            case "03": return isWhite?R.drawable.wid_03:R.drawable.info_03;
            case "04": return isWhite?R.drawable.wid_04:R.drawable.info_04;
            case "05": return isWhite?R.drawable.wid_05:R.drawable.info_05;
            case "06": return isWhite?R.drawable.wid_06:R.drawable.info_06;
            case "07": return isWhite?R.drawable.wid_07:R.drawable.info_07;
            case "08": return isWhite?R.drawable.wid_08:R.drawable.info_08;
            case "09": return isWhite?R.drawable.wid_09:R.drawable.info_09;
            case "10": return isWhite?R.drawable.wid_10:R.drawable.info_10;
            case "11": return isWhite?R.drawable.wid_11:R.drawable.info_11;
            case "12": return isWhite?R.drawable.wid_12:R.drawable.info_12;
            case "13": return isWhite?R.drawable.wid_13:R.drawable.info_13;
            case "14": return isWhite?R.drawable.wid_14:R.drawable.info_14;
            case "15": return isWhite?R.drawable.wid_15:R.drawable.info_15;
            case "16": return isWhite?R.drawable.wid_16:R.drawable.info_16;
            case "17": return isWhite?R.drawable.wid_17:R.drawable.info_17;
            case "18": return isWhite?R.drawable.wid_18:R.drawable.info_18;
            case "19": return isWhite?R.drawable.wid_19:R.drawable.info_19;
            case "20": return isWhite?R.drawable.wid_20:R.drawable.info_20;
            case "21": return isWhite?R.drawable.wid_21:R.drawable.info_21;
            case "22": return isWhite?R.drawable.wid_22:R.drawable.info_22;
            case "23": return isWhite?R.drawable.wid_23:R.drawable.info_23;
            case "24": return isWhite?R.drawable.wid_24:R.drawable.info_24;
            case "25": return isWhite?R.drawable.wid_25:R.drawable.info_25;
            case "26": return isWhite?R.drawable.wid_26:R.drawable.info_26;
            case "27": return isWhite?R.drawable.wid_27:R.drawable.info_27;
            case "28": return isWhite?R.drawable.wid_28:R.drawable.info_28;
            case "29": return isWhite?R.drawable.wid_29:R.drawable.info_29;
            case "30": return isWhite?R.drawable.wid_30:R.drawable.info_30;
            case "31": return isWhite?R.drawable.wid_31:R.drawable.info_31;
            case "53": return isWhite?R.drawable.wid_53:R.drawable.info_53;
            default: return isWhite?R.drawable.wid_00:R.drawable.info_00;
        }
    }

    /**
     * 空气质量级别
     * @param aqi
     * @return
     */
    private String getAqiLevel(int aqi) {
        String aqiLevel = "优";
        if (aqi > 300) aqiLevel = "严重污染";
        if (aqi < 301) aqiLevel = "中度污染";
        if (aqi < 201) aqiLevel = "轻度污染";
        if (aqi < 101) aqiLevel = "良";
        if (aqi < 51) aqiLevel = "优";
        return aqiLevel;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageView_cityList:
                Intent intent = new Intent(MainActivity.this,CityListActivity.class);
                startActivity(intent);
                break;
        }
    }
}
