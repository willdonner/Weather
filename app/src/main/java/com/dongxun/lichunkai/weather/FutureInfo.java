package com.dongxun.lichunkai.weather;

/**
 * 未来天气信息类
 */
public class FutureInfo {
    private String date;//日期
    private String temperature;//温度
    private String weather;//天气
    private String wid_day;//天气标识id-白天
    private String wid_night;//天气标识id-夜晚
    private int wid_img;//天气图标
    private String direct;//风向
    private Boolean today;//是否是今天
    private String week;//周几

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public Boolean getToday() {
        return today;
    }

    public void setToday(Boolean today) {
        this.today = today;
    }

    public int getWid_img() {
        return wid_img;
    }

    public void setWid_img(int wid_img) {
        this.wid_img = wid_img;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getWid_day() {
        return wid_day;
    }

    public void setWid_day(String wid_day) {
        this.wid_day = wid_day;
    }

    public String getWid_night() {
        return wid_night;
    }

    public void setWid_night(String wid_night) {
        this.wid_night = wid_night;
    }

    public String getDirect() {
        return direct;
    }

    public void setDirect(String direct) {
        this.direct = direct;
    }
}
