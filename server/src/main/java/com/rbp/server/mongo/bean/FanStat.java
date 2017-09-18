package com.rbp.server.mongo.bean;

/**
 * Created by liuwenbin on 2017/9/6.
 */
public class FanStat implements Comparable<FanStat>{

    private String fanName;
    private int state;
    private long timestramp;
    private String dateTime;

    public String getFanName() {
        return fanName;
    }

    public void setFanName(String fanName) {
        this.fanName = fanName;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getTimestramp() {
        return timestramp;
    }

    public void setTimestramp(long timestramp) {
        this.timestramp = timestramp;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public int compareTo(FanStat o) {
        return Integer.parseInt(this.timestramp - o.timestramp + "");
    }
}
