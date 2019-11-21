package com.dongxun.lichunkai.weather;

/**
 * 未来天气信息类
 */
public class FutureInfo {
    private String date;
    private String temperature;
    private String weather;
    private String wid_day;
    private String wid_night;
    private String direct;

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
