package com.rpd.customClasses;

import java.io.Serializable;

/**
 * Created by neotv on 9/22/17.
 */

public class Region implements Serializable{

    String id;
    String name;
    String cityName;

    public Region() { 
    }

    public Region(String id, String name, String cityName) {
        this.id = id;
        this.name = name;
        this.cityName = cityName;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
