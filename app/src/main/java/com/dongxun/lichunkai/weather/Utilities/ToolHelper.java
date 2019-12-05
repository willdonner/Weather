package com.dongxun.lichunkai.weather.Utilities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;

import com.dongxun.lichunkai.weather.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 工具类
 */
public final class ToolHelper {

    /**
     * 检查当前网络是否可用
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
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
     * 根据当前日期获得是星期几
     * time=yyyy-MM-dd
     * @return
     */
    public static String getWeekByDate(String time) {
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
        if (a > 20 && a <= 8) {
            return false;
        }
        return true;
    }

    /**
     * 空气质量级别
     * @param aqi
     * @return
     */
    public static String getAqiLevel(int aqi) {
        String aqiLevel = "优";
        if (aqi > 300) aqiLevel = "严重污染";
        if (aqi < 301) aqiLevel = "中度污染";
        if (aqi < 201) aqiLevel = "轻度污染";
        if (aqi < 101) aqiLevel = "良";
        if (aqi < 51) aqiLevel = "优";
        return aqiLevel;
    }

    /**
     * 跳转到应用的详情页面
     */
    public static Intent appSetIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        return localIntent;
    }

    /**
     * 获取天气对应的图标
     * @param wid 天气标识
     * @param isWhite 是否查找白色图标
     * @return
     */
    public static int getWidImg(String wid,Boolean isWhite) {
        switch (wid){
            case "100":
                return isWhite?R.drawable.dayicon_100:R.drawable.icon_100;
            case "100n":
                return isWhite?R.drawable.dayicon_100n:R.drawable.icon_100n;
            case "101":
                return isWhite?R.drawable.dayicon_101:R.drawable.icon_101;
            case "102":
                return isWhite?R.drawable.dayicon_102:R.drawable.icon_102;
            case "103":
                return isWhite?R.drawable.dayicon_103:R.drawable.icon_103;
            case "103n":
                return isWhite?R.drawable.dayicon_103n:R.drawable.icon_103n;
            case "104":
                return isWhite?R.drawable.dayicon_104:R.drawable.icon_104;
            case "104n":
                return isWhite?R.drawable.dayicon_104n:R.drawable.icon_104n;
            case "200":
                return isWhite?R.drawable.dayicon_200:R.drawable.icon_200;
            case "201":
                return isWhite?R.drawable.dayicon_201:R.drawable.icon_201;
            case "202":
                return isWhite?R.drawable.dayicon_202:R.drawable.icon_202;
            case "203":
                return isWhite?R.drawable.dayicon_203:R.drawable.icon_203;
            case "204":
                return isWhite?R.drawable.dayicon_204:R.drawable.icon_204;
            case "205":
                return isWhite?R.drawable.dayicon_205:R.drawable.icon_205;
            case "206":
                return isWhite?R.drawable.dayicon_206:R.drawable.icon_206;
            case "207":
                return isWhite?R.drawable.dayicon_207:R.drawable.icon_207;
            case "208":
                return isWhite?R.drawable.dayicon_208:R.drawable.icon_208;
            case "209":
                return isWhite?R.drawable.dayicon_209:R.drawable.icon_209;
            case "210":
                return isWhite?R.drawable.dayicon_210:R.drawable.icon_210;
            case "211":
                return isWhite?R.drawable.dayicon_211:R.drawable.icon_211;
            case "212":
                return isWhite?R.drawable.dayicon_212:R.drawable.icon_212;
            case "213":
                return isWhite?R.drawable.dayicon_213:R.drawable.icon_213;
            case "300":
                return isWhite?R.drawable.dayicon_300:R.drawable.icon_300;
            case "300n":
                return isWhite?R.drawable.dayicon_300n:R.drawable.icon_300n;
            case "301":
                return isWhite?R.drawable.dayicon_301:R.drawable.icon_301;
            case "301n":
                return isWhite?R.drawable.dayicon_301n:R.drawable.icon_301n;
            case "302":
                return isWhite?R.drawable.dayicon_302:R.drawable.icon_302;
            case "303":
                return isWhite?R.drawable.dayicon_303:R.drawable.icon_303;
            case "304":
                return isWhite?R.drawable.dayicon_304:R.drawable.icon_304;
            case "305":
                return isWhite?R.drawable.dayicon_305:R.drawable.icon_305;
            case "306":
                return isWhite?R.drawable.dayicon_306:R.drawable.icon_306;
            case "307":
                return isWhite?R.drawable.dayicon_307:R.drawable.icon_307;
            case "309":
                return isWhite?R.drawable.dayicon_309:R.drawable.icon_309;
            case "310":
                return isWhite?R.drawable.dayicon_310:R.drawable.icon_310;
            case "311":
                return isWhite?R.drawable.dayicon_311:R.drawable.icon_311;
            case "312":
                return isWhite?R.drawable.dayicon_312:R.drawable.icon_312;
            case "313":
                return isWhite?R.drawable.dayicon_313:R.drawable.icon_313;
            case "400":
                return isWhite?R.drawable.dayicon_400:R.drawable.icon_400;
            case "401":
                return isWhite?R.drawable.dayicon_401:R.drawable.icon_401;
            case "402":
                return  isWhite?R.drawable.dayicon_402:R.drawable.icon_402;
            case "403":
                return  isWhite?R.drawable.dayicon_403:R.drawable.icon_403;
            case "404":
                return  isWhite?R.drawable.dayicon_404:R.drawable.icon_404;
            case "405":
                return  isWhite?R.drawable.dayicon_405:R.drawable.icon_405;
            case "406":
                return  isWhite?R.drawable.dayicon_406:R.drawable.icon_406;
            case "406n":
                return  isWhite?R.drawable.dayicon_406n:R.drawable.icon_406n;
            case "407":
                return  isWhite?R.drawable.dayicon_407:R.drawable.icon_407;
            case "407n":
                return  isWhite?R.drawable.dayicon_407n:R.drawable.icon_407n;
            case "500":
                return  isWhite?R.drawable.dayicon_500:R.drawable.icon_500;
            case "501":
                return  isWhite?R.drawable.dayicon_501:R.drawable.icon_501;
            case "502":
                return  isWhite?R.drawable.dayicon_502:R.drawable.icon_502;
            case "503":
                return  isWhite?R.drawable.dayicon_503:R.drawable.icon_503;
            case "504":
                return  isWhite?R.drawable.dayicon_504:R.drawable.icon_504;
            case "507":
                return  isWhite?R.drawable.dayicon_507:R.drawable.icon_507;
            case "508":
                return  isWhite?R.drawable.dayicon_508:R.drawable.icon_508;
            case "900":
                return  isWhite?R.drawable.dayicon_900:R.drawable.icon_900;
            case "901":
                return  isWhite?R.drawable.dayicon_901:R.drawable.icon_901;
            default:
                return R.drawable.icon_999;
        }
    }


}
