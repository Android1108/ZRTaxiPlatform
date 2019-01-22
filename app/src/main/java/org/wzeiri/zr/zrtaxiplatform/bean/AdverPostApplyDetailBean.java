package org.wzeiri.zr.zrtaxiplatform.bean;

import java.util.Date;
import java.util.List;

/**
 * @author k-lm on 2018/1/23.
 */

public class AdverPostApplyDetailBean {

    /**
     * postTime : 2018-01-23T00:35:43.508Z
     * carId : 0
     * lisenceNumber : string
     * status : 0
     * statusName : string
     * isRewarded : true
     * describe : string
     * adverPostApplyPictures : ["string"]
     * id : 0
     */

    private String postTime;

    private int carId;
    private String lisenceNumber;
    private int status;
    private String statusName;
    private boolean isRewarded;
    private String describe;
    private int id;
    private List<String> adverPostApplyPictures;

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getLisenceNumber() {
        return lisenceNumber;
    }

    public void setLisenceNumber(String lisenceNumber) {
        this.lisenceNumber = lisenceNumber;
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

    public boolean isIsRewarded() {
        return isRewarded;
    }

    public void setIsRewarded(boolean isRewarded) {
        this.isRewarded = isRewarded;
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

    public List<String> getAdverPostApplyPictures() {
        return adverPostApplyPictures;
    }

    public void setAdverPostApplyPictures(List<String> adverPostApplyPictures) {
        this.adverPostApplyPictures = adverPostApplyPictures;
    }
}
