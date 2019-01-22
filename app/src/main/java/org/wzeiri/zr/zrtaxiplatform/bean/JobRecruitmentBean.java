package org.wzeiri.zr.zrtaxiplatform.bean;

import java.util.Date;

/**
 * @author k-lm on 2017/12/12.
 */

public class JobRecruitmentBean {

    /**
     * jobRecruitmentType : 0
     * jobRecruitmentTypeName : string
     * title : string
     * coverPicture : string
     * content : string
     * contact : string
     * contactPerson : string
     * provinceName : string
     * cityName : string
     * areaName : string
     * creatorUserId : 0
     * creatorUserName : string
     * creatorProfile : string
     * creationTime : 2017-12-12T00:45:05.807Z
     * id : 0
     */

    private int jobRecruitmentType;
    private String jobRecruitmentTypeName;
    private String title;
    private String coverPicture;
    private String content;
    private String contact;
    private String contactPerson;
    private String provinceName;
    private String cityName;
    private String areaName;
    private int creatorUserId;
    private String creatorUserName;
    private String creatorProfile;
    private Date creationTime;
    private int id;

    public int getJobRecruitmentType() {
        return jobRecruitmentType;
    }

    public void setJobRecruitmentType(int jobRecruitmentType) {
        this.jobRecruitmentType = jobRecruitmentType;
    }

    public String getJobRecruitmentTypeName() {
        return jobRecruitmentTypeName;
    }

    public void setJobRecruitmentTypeName(String jobRecruitmentTypeName) {
        this.jobRecruitmentTypeName = jobRecruitmentTypeName;
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

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
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

    public String getCreatorProfile() {
        return creatorProfile;
    }

    public void setCreatorProfile(String creatorProfile) {
        this.creatorProfile = creatorProfile;
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
