package com.dongxun.lichunkai.weather;

import java.io.Serializable;

/**
 * 当前天气信息类
 */
public class RealtimeInfo implements Serializable {
    private String temperature;
    private String humidity;
    private String info;
    private String wid;
    private String power;
    private String direct;
    private String aqi;
    private String city;
    private Boolean currentCity;

    public Boolean getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(Boolean currentCity) {
        this.currentCity = currentCity;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getDirect() {
        return direct;
    }

    public void setDirect(String direct) {
        this.direct = direct;
    }

    public String getAqi() {
        return aqi;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }
}
