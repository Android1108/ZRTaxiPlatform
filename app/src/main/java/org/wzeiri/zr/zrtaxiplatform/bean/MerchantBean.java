package org.wzeiri.zr.zrtaxiplatform.bean;

/**
 * @author k-lm on 2017/12/13.
 */

public class MerchantBean {


    /**
     * name : string
     * converPicture : string
     * address : string
     * contact : string
     * merchantFeature : string
     * businessHours : string
     * detail : string
     * distance : 0
     * id : 0
     */

    private String name;
    private String converPicture;
    private String address;
    private String contact;
    private String merchantFeature;
    private String businessHours;
    private String detail;
    private double distance;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConverPicture() {
        return converPicture;
    }

    public void setConverPicture(String converPicture) {
        this.converPicture = converPicture;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getMerchantFeature() {
        return merchantFeature;
    }

    public void setMerchantFeature(String merchantFeature) {
        this.merchantFeature = merchantFeature;
    }

    public String getBusinessHours() {
        return businessHours;
    }

    public void setBusinessHours(String businessHours) {
        this.businessHours = businessHours;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
