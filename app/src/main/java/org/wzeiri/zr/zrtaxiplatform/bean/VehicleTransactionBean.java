package org.wzeiri.zr.zrtaxiplatform.bean;

import java.util.Date;

/**
 * @author k-lm on 2017/12/12.
 */

public class VehicleTransactionBean {

    /**
     * title : string
     * coverPicture : string
     * content : string
     * publishUserId : 0
     * publishUserProfile : string
     * vehicleTransactionType : 0
     * vehicleTransactionTypeName : string
     * creationTime : 2017-12-12T00:45:05.951Z
     * id : 0
     */

    private String title;
    private String coverPicture;
    private String content;
    private int publishUserId;
    private String publishUserProfile;
    private int vehicleTransactionType;
    private String vehicleTransactionTypeName;
    private Date creationTime;
    private int id;
    private String publishUserName;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverPicture() {
        return coverPicture;
    }

    public void setCoverPicture(String coverPicture) {
        this.coverPicture = coverPicture;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPublishUserId() {
        return publishUserId;
    }

    public void setPublishUserId(int publishUserId) {
        this.publishUserId = publishUserId;
    }

    public String getPublishUserProfile() {
        return publishUserProfile;
    }

    public void setPublishUserProfile(String publishUserProfile) {
        this.publishUserProfile = publishUserProfile;
    }

    public int getVehicleTransactionType() {
        return vehicleTransactionType;
    }

    public void setVehicleTransactionType(int vehicleTransactionType) {
        this.vehicleTransactionType = vehicleTransactionType;
    }

    public String getVehicleTransactionTypeName() {
        return vehicleTransactionTypeName;
    }

    public void setVehicleTransactionTypeName(String vehicleTransactionTypeName) {
        this.vehicleTransactionTypeName = vehicleTransactionTypeName;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPublishUserName() {
        return publishUserName;
    }

    public void setPublishUserName(String publishUserName) {
        this.publishUserName = publishUserName;
    }
}
