package org.wzeiri.zr.zrtaxiplatform.bean;

import java.util.Date;

/**
 * Created by zz on 2017-12-15.
 */

public class GetRecordsBean {

    /**
     * "userId": 0,
     *"integralChangeType": 0,
     *"integralChangeTypeName": "string",
     *"integralCreaseType": 0,
     *"integralCreaseTypeName": "string",
     *"quantity": 0,
     *"describe": "string",
     *"creationTime": "2017-12-15T05:23:16.209Z",
     *"id": 0
     */

    private int userId;
    private int integralChangeType;
    private String integralChangeTypeName;
    private int integralCreaseType;
    private String integralCreaseTypeName;
    private int quantity;
    private String describe;
    private Date creationTime;
    private int id;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getIntegralChangeType() {
        return integralChangeType;
    }

    public void setIntegralChangeType(int integralChangeType) {
        this.integralChangeType = integralChangeType;
    }

    public String getIntegralChangeTypeName() {
        return integralChangeTypeName;
    }

    public void setIntegralChangeTypeName(String integralChangeTypeName) {
        this.integralChangeTypeName = integralChangeTypeName;
    }

    public int getIntegralCreaseType() {
        return integralCreaseType;
    }

    public void setIntegralCreaseType(int integralCreaseType) {
        this.integralCreaseType = integralCreaseType;
    }

    public String getIntegralCreaseTypeName() {
        return integralCreaseTypeName;
    }

    public void setIntegralCreaseTypeName(String integralCreaseTypeName) {
        this.integralCreaseTypeName = integralCreaseTypeName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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
