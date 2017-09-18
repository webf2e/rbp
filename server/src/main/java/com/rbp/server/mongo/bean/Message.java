package com.rbp.server.mongo.bean;

/**
 * Created by liuwenbin on 2017/9/11.
 */
public class Message implements Comparable<Message>{

    private String _id;
    private String dateTime;
    private long timestramp;
    private String ip;
    private String url;
    private String type;
    private String content;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public int compareTo(Message o) {
        return Integer.parseInt(o.timestramp - this.timestramp + "");
    }
}
