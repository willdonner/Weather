package com.dongxun.lichunkai.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private LocationManager locationManager;
    private LocationListener locationListener;
    private  String WeatherKey = "e187097c8e703fce523ff6e8204ef8cc";//查询天气key
    private String City = "昆明";//查询城市
    private String data = "{\n" +
            "\t\"reason\":\"查询成功\",\n" +
            "\t\"result\":{\n" +
            "\t\t\"city\":\"昆明\",\n" +
            "\t\t\"realtime\":{\n" +
            "\t\t\t\"temperature\":\"18\",\n" +
            "\t\t\t\"humidity\":\"41\",\n" +
            "\t\t\t\"info\":\"阴\",\n" +
            "\t\t\t\"wid\":\"02\",\n" +
            "\t\t\t\"direct\":\"西南风\",\n" +
            "\t\t\t\"power\":\"4级\",\n" +
            "\t\t\t\"aqi\":\"25\"\n" +
            "\t\t},\n" +
            "\t\t\"future\":[\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"date\":\"2019-11-20\",\n" +
            "\t\t\t\t\"temperature\":\"9\\/21℃\",\n" +
            "\t\t\t\t\"weather\":\"晴\",\n" +
            "\t\t\t\t\"wid\":{\n" +
            "\t\t\t\t\t\"day\":\"00\",\n" +
            "\t\t\t\t\t\"night\":\"00\"\n" +
            "\t\t\t\t},\n" +
            "\t\t\t\t\"direct\":\"西南风转持续无风向\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"date\":\"2019-11-21\",\n" +
            "\t\t\t\t\"temperature\":\"9\\/21℃\",\n" +
            "\t\t\t\t\"weather\":\"晴\",\n" +
            "\t\t\t\t\"wid\":{\n" +
            "\t\t\t\t\t\"day\":\"00\",\n" +
            "\t\t\t\t\t\"night\":\"00\"\n" +
            "\t\t\t\t},\n" +
            "\t\t\t\t\"direct\":\"持续无风向\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"date\":\"2019-11-22\",\n" +
            "\t\t\t\t\"temperature\":\"9\\/21℃\",\n" +
            "\t\t\t\t\"weather\":\"晴\",\n" +
            "\t\t\t\t\"wid\":{\n" +
            "\t\t\t\t\t\"day\":\"00\",\n" +
            "\t\t\t\t\t\"night\":\"00\"\n" +
            "\t\t\t\t},\n" +
            "\t\t\t\t\"direct\":\"持续无风向\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"date\":\"2019-11-23\",\n" +
            "\t\t\t\t\"temperature\":\"8\\/22℃\",\n" +
            "\t\t\t\t\"weather\":\"晴\",\n" +
            "\t\t\t\t\"wid\":{\n" +
            "\t\t\t\t\t\"day\":\"00\",\n" +
            "\t\t\t\t\t\"night\":\"00\"\n" +
            "\t\t\t\t},\n" +
            "\t\t\t\t\"direct\":\"持续无风向\"\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"date\":\"2019-11-24\",\n" +
            "\t\t\t\t\"temperature\":\"8\\/20℃\",\n" +
            "\t\t\t\t\"weather\":\"多云\",\n" +
            "\t\t\t\t\"wid\":{\n" +
            "\t\t\t\t\t\"day\":\"01\",\n" +
            "\t\t\t\t\t\"night\":\"01\"\n" +
            "\t\t\t\t},\n" +
            "\t\t\t\t\"direct\":\"西南风转持续无风向\"\n" +
            "\t\t\t}\n" +
            "\t\t]\n" +
            "\t},\n" +
            "\t\"error_code\":0\n" +
            "}";
    private TextView textView_reason;
    private TextView textView_city;
    private TextView textView_temperature;
    private TextView textView_humidity;
    private TextView textView_info;
    private TextView textView_wid;
    private TextView textView_direct;
    private TextView textView_power;
    private TextView textView_aqi;
    private TextView textView_future;
    private ArrayList<FutureInfo> futureInfos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initBar();
        initView();
        getPermission();



    }

    /**
     * 初始化状态栏
     */
    private void initBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    /**
     * 初始化组件
     */
    private void initView() {
//        textView_reason = findViewById(R.id.textView_reason);
//        textView_city = findViewById(R.id.textView_city);
//        textView_temperature = findViewById(R.id.textView_temperature);
//        textView_humidity = findViewById(R.id.textView_humidity);
//        textView_info = findViewById(R.id.textView_info);
//        textView_wid = findViewById(R.id.textView_wid);
//        textView_direct = findViewById(R.id.textView_direct);
//        textView_aqi = findViewById(R.id.textView_aqi);
//        textView_power = findViewById(R.id.textView_power);
//        textView_future = findViewById(R.id.textView_future);
    }

    /**
     * 显示信息
     */
    private void showMessage() {

    }

    /**
     * 定位后根据城市查询天气
     */
    private void getDataByCity() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            return;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.removeUpdates(locationListener);
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
     * 定位
     */
    public void location(){
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
        getDataByCity();
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
//                    OkHttpClient client = new OkHttpClient();//新建一个OKHttp的对象
//                    Request request = new Request.Builder()
//                            .url("https://apis.juhe.cn/simpleWeather/query?city="+city+"&key="+WeatherKey+"")
//                            .build();//创建一个Request对象
//                    Response response = client.newCall(request).execute();//发送请求获取返回数据
//                    String responseData = response.body().string();//处理返回的数据
                    parseJSON(data);//解析JSON
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
//                textView_reason.setText(reason);
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
//                textView_reason.setText(reason);
//                textView_city.setText(city);
//                textView_temperature.setText(realtimeInfo.getTemperature());
//                textView_humidity.setText(realtimeInfo.getHumidity());
//                textView_info.setText(realtimeInfo.getInfo());
//                textView_wid.setText(realtimeInfo.getWid());
//                textView_direct.setText(realtimeInfo.getDirect());
//                textView_power.setText(realtimeInfo.getPower());
//                textView_aqi.setText(realtimeInfo.getAqi());
//
//                for (int i = 0;i < futureInfos.size();i++) {
//                    FutureInfo futureInfo = futureInfos.get(i);
//                    textView_future.append(futureInfo.getDate()+futureInfo.getDirect()+futureInfo.getTemperature()+futureInfo.getWeather()+futureInfo.getWid_day()+futureInfo.getWid_night());
//                    textView_future.append("\n");
//                }
            }
        });
    }
}
