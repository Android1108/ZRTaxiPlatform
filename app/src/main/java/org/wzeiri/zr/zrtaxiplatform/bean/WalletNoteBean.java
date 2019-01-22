package org.wzeiri.zr.zrtaxiplatform.bean;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author k-lm on 2017/12/13.
 */

public class WalletNoteBean {


    /**
     * note : 体现到银行卡
     * amount : 1
     * walletNoteType : 1
     * cashRequestStatus : 1
     * isDeleted : false
     * deleterUserId : null
     * deletionTime : null
     * lastModificationTime : 2018-02-01T17:20:24.8161285
     * lastModifierUserId : 2
     * creationTime : 2018-02-01T17:20:01.1047555
     * creatorUserId : 9
     * id : 10
     */

    private String note;
    private BigDecimal amount;
    private int walletNoteType;
    private int cashRequestStatus;
    private boolean isDeleted;
    private Integer deleterUserId;
    private Date deletionTime;
    private String lastModificationTime;
    private int lastModifierUserId;
    private String creationTime;
    private int creatorUserId;
    private int id;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getWalletNoteType() {
        return walletNoteType;
    }

    public void setWalletNoteType(int walletNoteType) {
        this.walletNoteType = walletNoteType;
    }

    public int getCashRequestStatus() {
        return cashRequestStatus;
    }

    public void setCashRequestStatus(int cashRequestStatus) {
        this.cashRequestStatus = cashRequestStatus;
    }

    public boolean isIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getDeleterUserId() {
        return deleterUserId;
    }

    public void setDeleterUserId(Integer deleterUserId) {
        this.deleterUserId = deleterUserId;
    }

    public Date getDeletionTime() {
        return deletionTime;
    }

    public void setDeletionTime(Date deletionTime) {
        this.deletionTime = deletionTime;
    }

    public String getLastModificationTime() {
        return lastModificationTime;
    }

    public void setLastModificationTime(String lastModificationTime) {
        this.lastModificationTime = lastModificationTime;
    }

    public int getLastModifierUserId() {
        return lastModifierUserId;
    }

    public void setLastModifierUserId(int lastModifierUserId) {
        this.lastModifierUserId = lastModifierUserId;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
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
