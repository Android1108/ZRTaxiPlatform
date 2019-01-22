package org.wzeiri.zr.zrtaxiplatform.bean;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author k-lm on 2017/12/19.
 */

public class LostFoundDetailBean {


    /**
     * title : string
     * contactPerson : string
     * contact : string
     * lostFoundStatus : 0
     * lostFoundStatusName : string
     * boardingPoint : 2017-12-19T03:02:38.041Z
     * boardingTime : 2017-12-19T03:02:38.041Z
     * alightingPoint : string
     * alightingTime : 2017-12-19T03:02:38.041Z
     * ridingTime : 2017-12-19T03:02:38.041Z
     * tipAmount : 0
     * content : string
     * creationTime : 2017-12-19T03:02:38.041Z
     * publishUserId : 0
     * publishUserProfile : string
     * pictures : ["string"]
     * id : 0
     */

    private String title;
    private String contactPerson;
    private String contact;
    private int lostFoundStatus;
    private String lostFoundStatusName;
    private String boardingPoint;
    private String boardingTime;
    private String alightingPoint;
    private String alightingTime;
    private String ridingTime;
    private BigDecimal tipAmount;
    private String content;
    private Date creationTime;
    private int publishUserId;
    private String publishUserProfile;
    private int id;
    private String plateNumber;
    private List<String> pictures;

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
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

    public BigDecimal getTipAmount() {
        return tipAmount;
    }

    public void setTipAmount(BigDecimal tipAmount) {
        this.tipAmount = tipAmount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }
}
