package com.adityathakker.wmb.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UpdatesPojo {
    @SerializedName("status")
    @Expose
    String status;
    @SerializedName("message")
    @Expose
    String message;

    @SerializedName("locations")
    @Expose
    ArrayList<LocationUpdatesPojo> locations;

    public ArrayList<LocationUpdatesPojo> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<LocationUpdatesPojo> locations) {
        this.locations = locations;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
