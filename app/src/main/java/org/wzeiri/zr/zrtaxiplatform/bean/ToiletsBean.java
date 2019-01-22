package org.wzeiri.zr.zrtaxiplatform.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author k-lm on 2017/12/19.
 */

public class ToiletsBean {

    /**
     * name : string
     * long : 0
     * lat : 0
     * address : string
     * id : 0
     */

    private String name;
    @SerializedName("long")
    private double longitude;
    @SerializedName("lat")
    private double latitude;
    private String address;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
