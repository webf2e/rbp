package com.rbp.transferJar.mongo.bean;

import org.springframework.data.annotation.Id;

/**
 * Created by liuwenbin on 2017/8/31.
 */
public class RbpInfo {
    @Id
    private String _id;
    private String serverName;
    private String ip;
    private String teamviewerId;
    private String startUpTime;
    private int serverId;
    private String updateTime;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getTeamviewerId() {
        return teamviewerId;
    }

    public void setTeamviewerId(String teamviewerId) {
        this.teamviewerId = teamviewerId;
    }

    public String getStartUpTime() {
        return startUpTime;
    }

    public void setStartUpTime(String startUpTime) {
        this.startUpTime = startUpTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }
}
