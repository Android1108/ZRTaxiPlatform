package org.wzeiri.zr.zrtaxiplatform.bean;

import java.util.Date;

/**
 * @author k-lm on 2017/12/13.
 */

public class AdverPostApplyBean {


    /**
     * adverPicture : string
     * status : 0
     * statusName : string
     * postTime : 2017-12-13T01:16:41.511Z
     * creationTime : 2017-12-13T01:16:41.511Z
     * describe : string
     * id : 0
     */

    private String adverPicture;
    private int status;
    private String statusName;
    private String postTime;
    private Date creationTime;
    private String describe;
    private int id;

    public String getAdverPicture() {
        return adverPicture;
    }

    public void setAdverPicture(String adverPicture) {
        this.adverPicture = adverPicture;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
