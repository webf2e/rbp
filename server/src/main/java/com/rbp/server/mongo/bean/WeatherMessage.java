package com.rbp.server.mongo.bean;

import java.util.Date;

/**
 * Created by liuwenbin on 2017/9/17.
 */
public class WeatherMessage {
    private String _id;
    private String temperature;
    private String humidity;
    private String rain;
    private String info;
    private String img;
    private String dateTime;
    private long timestramp;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public String getRain() {
        return rain;
    }

    public void setRain(String rain) {
        this.rain = rain;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public long getTimestramp() {
        return timestramp;
    }

    public void setTimestramp(long timestramp) {
        this.timestramp = timestramp;
    }
}
