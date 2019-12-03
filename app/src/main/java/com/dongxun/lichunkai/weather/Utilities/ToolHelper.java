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
            case "00": return isWhite? R.drawable.wid_00:R.drawable.info_00;
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
}
