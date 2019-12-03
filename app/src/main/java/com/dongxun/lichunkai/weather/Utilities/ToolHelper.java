package com.dongxun.lichunkai.weather.Utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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
     * 获取天气对应的图标
     * @param wid 天气标识
     * @param isWhite 是否查找白色图标
     * @return
     */
    public static int getWidImg(String wid,Boolean isWhite) {
        switch (wid){
            case "100":
                return R.drawable.icon_100;
            case "100n":
                return R.drawable.icon_100n;
            case "101":
                return R.drawable.icon_101;
            case "102":
                return R.drawable.icon_102;
            case "103":
                return R.drawable.icon_103;
            case "103n":
                return R.drawable.icon_103n;
            case "104":
                return R.drawable.icon_104;
            case "104n":
                return R.drawable.icon_104n;
            case "200":
                return R.drawable.icon_200;
            case "201":
                return R.drawable.icon_201;
            case "202":
                return R.drawable.icon_202;
            case "203":
                return R.drawable.icon_203;
            case "204":
                return R.drawable.icon_204;
            case "205":
                return R.drawable.icon_205;
            case "206":
                return R.drawable.icon_206;
            case "207":
                return R.drawable.icon_207;
            case "208":
                return R.drawable.icon_208;
            case "209":
                return R.drawable.icon_209;
            case "210":
                return R.drawable.icon_210;
            case "211":
                return R.drawable.icon_211;
            case "212":
                return R.drawable.icon_212;
            case "213":
                return R.drawable.icon_213;
            case "300":
                return R.drawable.icon_300;
            case "300n":
                return R.drawable.icon_300n;
            case "301":
                return R.drawable.icon_301;
            case "301n":
                return R.drawable.icon_301n;
            case "302":
                return R.drawable.icon_302;
            case "303":
                return R.drawable.icon_303;
            case "304":
                return R.drawable.icon_304;
            case "305":
                return R.drawable.icon_305;
            case "306":
                return R.drawable.icon_306;
            case "307":
                return R.drawable.icon_307;
            case "309":
                return R.drawable.icon_309;
            case "310":
                return R.drawable.icon_310;
            case "311":
                return R.drawable.icon_311;
            case "312":
                return R.drawable.icon_312;
            case "313":
                return R.drawable.icon_313;
            case "400":
                return R.drawable.icon_400;
            case "401":
                return R.drawable.icon_401;
            case "402":
                return  R.drawable.icon_402;
            case "403":
                return  R.drawable.icon_403;
            case "404":
                return  R.drawable.icon_404;
            case "405":
                return  R.drawable.icon_405;
            case "406":
                return  R.drawable.icon_406;
            case "406n":
                return  R.drawable.icon_406n;
            case "407":
                return  R.drawable.icon_407;
            case "407n":
                return  R.drawable.icon_407n;
            case "500":
                return  R.drawable.icon_500;
            case "501":
                return  R.drawable.icon_501;
            case "502":
                return  R.drawable.icon_502;
            case "503":
                return  R.drawable.icon_503;
            case "504":
                return  R.drawable.icon_504;
            case "507":
                return  R.drawable.icon_507;
            case "508":
                return  R.drawable.icon_508;
            case "900":
                return  R.drawable.icon_900;
            case "901":
                return  R.drawable.icon_901;
            default:
                return R.drawable.icon_999;
        }
    }
}
