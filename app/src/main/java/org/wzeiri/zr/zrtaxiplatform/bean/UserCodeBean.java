package org.wzeiri.zr.zrtaxiplatform.bean;

/**
 * Created by zz on 2017-12-15.
 */

public class UserCodeBean {
    /**
     *"profile": "string",
     *"userName": "string",
     *"lisenceNumber": "string",
     *"base64QrCode": "string",
     *"id": 0
     */

    private String profile;
    private String userName;
    private String lisenceNumber;
    private String base64QrCode;
    private int id;

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLisenceNumber() {
        return lisenceNumber;
    }

    public void setLisenceNumber(String lisenceNumber) {
        this.lisenceNumber = lisenceNumber;
    }

    public String getBase64QrCode() {
        return base64QrCode;
    }

    public void setBase64QrCode(String base64QrCode) {
        this.base64QrCode = base64QrCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
