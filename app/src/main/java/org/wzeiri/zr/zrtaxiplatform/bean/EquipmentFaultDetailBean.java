package org.wzeiri.zr.zrtaxiplatform.bean;

import java.util.Date;
import java.util.List;

/**
 * @author k-lm on 2018/1/25.
 */

public class EquipmentFaultDetailBean {

    /**
     * carId : 0
     * driverId : 0
     * equipmentFaultType : 0
     * equipmentFaultTypeName : string
     * equipmentFaultStatus : 0
     * equipmentFaultStatusName : string
     * contact : string
     * faultDescribe : string
     * creationTime : 2018-01-25T01:33:15.173Z
     * pictures : ["string"]
     * id : 0
     */

    private int carId;
    private int driverId;
    private int equipmentFaultType;
    private String equipmentFaultTypeName;
    private int equipmentFaultStatus;
    private String equipmentFaultStatusName;
    private String contact;
    private String faultDescribe;
    private Date creationTime;
    private int id;
    private List<String> pictures;

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public int getEquipmentFaultType() {
        return equipmentFaultType;
    }

    public void setEquipmentFaultType(int equipmentFaultType) {
        this.equipmentFaultType = equipmentFaultType;
    }

    public String getEquipmentFaultTypeName() {
        return equipmentFaultTypeName;
    }

    public void setEquipmentFaultTypeName(String equipmentFaultTypeName) {
        this.equipmentFaultTypeName = equipmentFaultTypeName;
    }

    public int getEquipmentFaultStatus() {
        return equipmentFaultStatus;
    }

    public void setEquipmentFaultStatus(int equipmentFaultStatus) {
        this.equipmentFaultStatus = equipmentFaultStatus;
    }

    public String getEquipmentFaultStatusName() {
        return equipmentFaultStatusName;
    }

    public void setEquipmentFaultStatusName(String equipmentFaultStatusName) {
        this.equipmentFaultStatusName = equipmentFaultStatusName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getFaultDescribe() {
        return faultDescribe;
    }

    public void setFaultDescribe(String faultDescribe) {
        this.faultDescribe = faultDescribe;
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

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }
}
