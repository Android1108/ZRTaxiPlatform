package org.wzeiri.zr.zrtaxiplatform.bean;

import java.util.Date;

/**
 * @author k-lm on 2017/12/13.
 */

public class SeatCoverBean {


    /**
     * carId : 0
     * changeSeatStatus : 0
     * changeSeatStatusName : string
     * describe : string
     * creationTime : 2017-12-13T01:16:41.522Z
     * id : 0
     */

    private int carId;
    private int changeSeatStatus;
    private String changeSeatStatusName;
    private String describe;
    private Date creationTime;
    private int id;

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public int getChangeSeatStatus() {
        return changeSeatStatus;
    }

    public void setChangeSeatStatus(int changeSeatStatus) {
        this.changeSeatStatus = changeSeatStatus;
    }

    public String getChangeSeatStatusName() {
        return changeSeatStatusName;
    }

    public void setChangeSeatStatusName(String changeSeatStatusName) {
        this.changeSeatStatusName = changeSeatStatusName;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
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
