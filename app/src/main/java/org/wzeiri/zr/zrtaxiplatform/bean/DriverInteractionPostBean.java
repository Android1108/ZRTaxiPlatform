package org.wzeiri.zr.zrtaxiplatform.bean;

import java.util.Date;

/**
 * @author k-lm on 2017/12/12.
 */

public class DriverInteractionPostBean {


    /**
     * title : string
     * picture : string
     * content : string
     * greateNumber : 0
     * commentNumber : 0
     * isTop : true
     * isHot : true
     * creationTime : 2017-12-12T00:45:05.869Z
     * creatorUserId : 0
     * creatorUserName : string
     * creatorUserProfile : string
     * lastModificationTime : 2017-12-12T00:45:05.869Z
     * id : 0
     */

    private String title;
    private String picture;
    private String content;
    private int greateNumber;
    private int commentNumber;
    private boolean isTop;
    private boolean isHot;
    private Date creationTime;
    private int creatorUserId;
    private String creatorUserName;
    private String creatorUserProfile;
    private Date lastModificationTime;
    private int id;

    public String getTitle() {
        return title;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getGreateNumber() {
        return greateNumber;
    }

    public void setGreateNumber(int greateNumber) {
        this.greateNumber = greateNumber;
    }

    public int getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(int commentNumber) {
        this.commentNumber = commentNumber;
    }

    public boolean isIsTop() {
        return isTop;
    }

    public void setIsTop(boolean isTop) {
        this.isTop = isTop;
    }

    public boolean isIsHot() {
        return isHot;
    }

    public void setIsHot(boolean isHot) {
        this.isHot = isHot;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public int getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(int creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public String getCreatorUserName() {
        return creatorUserName;
    }

    public void setCreatorUserName(String creatorUserName) {
        this.creatorUserName = creatorUserName;
    }

    public String getCreatorUserProfile() {
        return creatorUserProfile;
    }

    public void setCreatorUserProfile(String creatorUserProfile) {
        this.creatorUserProfile = creatorUserProfile;
    }

    public Date getLastModificationTime() {
        return lastModificationTime;
    }

    public void setLastModificationTime(Date lastModificationTime) {
        this.lastModificationTime = lastModificationTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
