package org.wzeiri.zr.zrtaxiplatform.bean;

import java.util.Date;

/**
 * @author k-lm on 2017/12/13.
 */

public class FaultInfoBean {

    /**
     * equipmentFaultType : 0
     * equipmentFaultTypeName : string
     * equipmentFaultStatus : 0
     * equipmentFaultStatusName : string
     * contact : string
     * faultDescribe : string
     * creationTime : 2017-12-13T01:16:41.568Z
     * id : 0
     */

    private int equipmentFaultType;
    private String equipmentFaultTypeName;
    private int equipmentFaultStatus;
    private String equipmentFaultStatusName;
    private String contact;
    private String faultDescribe;
    private Date creationTime;
    private int id;

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
}
