package com.adityathakker.wmb.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by adityajthakker on 23/8/16.
 */
public class LocationUpdatesPojo {

    @SerializedName("user_id")
    @Expose
    private int userId;

    @SerializedName("bus_no")
    @Expose
    private String busNo;

    @SerializedName("latitude")
    @Expose
    private String latitude;

    @SerializedName("longitude")
    @Expose
    private String longitude;

    @SerializedName("time_stamp")
    @Expose
    private String timeStamp;

    public int getUserId() {
        return userId;
    }

    public String getBusNo() {
        return busNo;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setBusNo(String busNo) {
        this.busNo = busNo;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
