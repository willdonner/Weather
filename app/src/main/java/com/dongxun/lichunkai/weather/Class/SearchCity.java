package com.dongxun.lichunkai.weather.Class;

/**
 * 存储搜索城市的类（地点，城市）
 */
public class SearchCity{

    private String location;    //地名
    private String parent_city;     //城市名

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getParent_city() {
        return parent_city;
    }

    public void setParent_city(String parent_city) {
        this.parent_city = parent_city;
    }
}
