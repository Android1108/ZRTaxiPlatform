package org.wzeiri.zr.zrtaxiplatform.bean;

import java.util.Date;

/**
 * @author k-lm on 2017/12/19.
 */

public class VehicleTransactionDetailBean {


    /**
     * title : string
     * coverPicture : string
     * content : string
     * contact : string
     * contactPhone : string
     * contactUserName : string
     * publishUserId : 0
     * publishUserProfile : string
     * vehicleTransactionType : 0
     * vehicleTransactionTypeName : string
     * creationTime : 2017-12-19T03:02:38.163Z
     * id : 0
     */

    private String title;
    private String coverPicture;
    private String content;
    private String contact;
    private String contactPhone;
    private String contactUserName;
    private long publishUserId;
    private String publishUserProfile;
    private int vehicleTransactionType;
    private String vehicleTransactionTypeName;
    private Date creationTime;
    private String address;
    private int id;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactUserName() {
        return contactUserName;
    }

    public void setContactUserName(String contactUserName) {
        this.contactUserName = contactUserName;
    }

    public long getPublishUserId() {
        return publishUserId;
    }

    public void setPublishUserId(long publishUserId) {
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
}
