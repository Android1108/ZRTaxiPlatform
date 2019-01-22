package org.wzeiri.zr.zrtaxiplatform.bean;


import java.util.Date;

/**
 * @author k-lm on 2017/12/12.
 */

public class LostFoundBean {

    /**
     * title : string
     * picture : string
     * contactPerson : string
     * boardingPoint : string
     * boardingTime : 2017-12-12T00:45:05.836Z
     * alightingPoint : string
     * alightingTime : 2017-12-12T00:45:05.836Z
     * ridingTime : 2017-12-12T00:45:05.836Z
     * isDisplay : true
     * lostFoundStatus : 0
     * lostFoundStatusName : string
     * publishUserProfile : string
     * id : 0
     */

    private String title;
    private String picture;
    private String content;
    private String contactPerson;
    private String boardingPoint;
    private String boardingTime;
    private String alightingPoint;
    private String alightingTime;
    private String ridingTime;
    private boolean isDisplay;
    private int lostFoundStatus;
    private String lostFoundStatusName;
    private String publishUserProfile;
    private String plateNumber;
    private Date creationTime;
    private int id;

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getBoardingPoint() {
        return boardingPoint;
    }

    public void setBoardingPoint(String boardingPoint) {
        this.boardingPoint = boardingPoint;
    }

    public String getBoardingTime() {
        return boardingTime;
    }

    public void setBoardingTime(String boardingTime) {
        this.boardingTime = boardingTime;
    }

    public String getAlightingPoint() {
        return alightingPoint;
    }

    public void setAlightingPoint(String alightingPoint) {
        this.alightingPoint = alightingPoint;
    }

    public String getAlightingTime() {
        return alightingTime;
    }

    public void setAlightingTime(String alightingTime) {
        this.alightingTime = alightingTime;
    }

    public String getRidingTime() {
        return ridingTime;
    }

    public void setRidingTime(String ridingTime) {
        this.ridingTime = ridingTime;
    }

    public boolean isIsDisplay() {
        return isDisplay;
    }

    public void setIsDisplay(boolean isDisplay) {
        this.isDisplay = isDisplay;
    }

    public int getLostFoundStatus() {
        return lostFoundStatus;
    }

    public void setLostFoundStatus(int lostFoundStatus) {
        this.lostFoundStatus = lostFoundStatus;
    }

    public String getLostFoundStatusName() {
        return lostFoundStatusName;
    }

    public void setLostFoundStatusName(String lostFoundStatusName) {
        this.lostFoundStatusName = lostFoundStatusName;
    }

    public String getPublishUserProfile() {
        return publishUserProfile;
    }

    public void setPublishUserProfile(String publishUserProfile) {
        this.publishUserProfile = publishUserProfile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
