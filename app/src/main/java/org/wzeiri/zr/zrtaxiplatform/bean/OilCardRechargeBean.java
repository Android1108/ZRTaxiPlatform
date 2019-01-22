package org.wzeiri.zr.zrtaxiplatform.bean;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author k-lm on 2018/1/9.
 */

public class OilCardRechargeBean {


    /**
     * rechargeAmount : 0
     * payAmount : 0
     * status : 0
     * isDeleted : true
     * deleterUserId : 0
     * deletionTime : 2018-01-09T05:08:15.235Z
     * lastModificationTime : 2018-01-09T05:08:15.235Z
     * lastModifierUserId : 0
     * creationTime : 2018-01-09T05:08:15.235Z
     * creatorUserId : 0
     * id : 0
     */

    private BigDecimal rechargeAmount;
    private BigDecimal payAmount;
    private int status;
    private boolean isDeleted;
    private int deleterUserId;
    private Date deletionTime;
    private Date lastModificationTime;
    private int lastModifierUserId;
    private Date creationTime;
    private int creatorUserId;
    private int id;

    public BigDecimal getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(BigDecimal rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public int getDeleterUserId() {
        return deleterUserId;
    }

    public void setDeleterUserId(int deleterUserId) {
        this.deleterUserId = deleterUserId;
    }

    public Date getDeletionTime() {
        return deletionTime;
    }

    public void setDeletionTime(Date deletionTime) {
        this.deletionTime = deletionTime;
    }

    public Date getLastModificationTime() {
        return lastModificationTime;
    }

    public void setLastModificationTime(Date lastModificationTime) {
        this.lastModificationTime = lastModificationTime;
    }

    public int getLastModifierUserId() {
        return lastModifierUserId;
    }

    public void setLastModifierUserId(int lastModifierUserId) {
        this.lastModifierUserId = lastModifierUserId;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
