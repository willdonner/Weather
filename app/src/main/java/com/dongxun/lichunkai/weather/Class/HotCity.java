package com.dongxun.lichunkai.weather.Class;

/**
 * 热门城市类
 */
public class HotCity {

    private String name;//城市名称
    private Boolean location;//是否显示定位图标

    public Boolean getLocation() {
        return location;
    }

    public void setLocation(Boolean location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
