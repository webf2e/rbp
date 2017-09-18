package com.rbp.transferJar.mongo.bean;

/**
 * Created by liuwenbin on 2017/8/31.
 */
public class RbpStat implements Comparable<RbpStat>{
    private String serverName;
    private String cpuTemp;
    private String gpuTemp;
    private String cpuUse;
    private String memoryTotal;
    private String memoryUsed;
    private String memoryFree;
    private String memoryAvail;
    private String diskTotal;
    private String diskUsed;
    private String diskUsedPercent;
    private long timestramp;
    private String dateTime;

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getCpuTemp() {
        return cpuTemp;
    }

    public void setCpuTemp(String cpuTemp) {
        this.cpuTemp = cpuTemp;
    }

    public String getGpuTemp() {
        return gpuTemp;
    }

    public void setGpuTemp(String gpuTemp) {
        this.gpuTemp = gpuTemp;
    }

    public String getCpuUse() {
        return cpuUse;
    }

    public void setCpuUse(String cpuUse) {
        this.cpuUse = cpuUse;
    }

    public String getMemoryTotal() {
        return memoryTotal;
    }

    public void setMemoryTotal(String memoryTotal) {
        this.memoryTotal = memoryTotal;
    }

    public String getMemoryUsed() {
        return memoryUsed;
    }

    public void setMemoryUsed(String memoryUsed) {
        this.memoryUsed = memoryUsed;
    }

    public String getMemoryFree() {
        return memoryFree;
    }

    public void setMemoryFree(String memoryFree) {
        this.memoryFree = memoryFree;
    }

    public String getMemoryAvail() {
        return memoryAvail;
    }

    public void setMemoryAvail(String memoryAvail) {
        this.memoryAvail = memoryAvail;
    }

    public String getDiskTotal() {
        return diskTotal;
    }

    public void setDiskTotal(String diskTotal) {
        this.diskTotal = diskTotal;
    }

    public String getDiskUsed() {
        return diskUsed;
    }

    public void setDiskUsed(String diskUsed) {
        this.diskUsed = diskUsed;
    }

    public String getDiskUsedPercent() {
        return diskUsedPercent;
    }

    public void setDiskUsedPercent(String diskUsedPercent) {
        this.diskUsedPercent = diskUsedPercent;
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
    public int compareTo(RbpStat o) {
        return Integer.parseInt(this.timestramp - o.timestramp + "");
    }
}
