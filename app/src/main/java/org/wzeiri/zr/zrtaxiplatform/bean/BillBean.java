package org.wzeiri.zr.zrtaxiplatform.bean;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author k-lm on 2018/1/5.
 */

public class BillBean {


    /**
     * cardName : string
     * cardCover : string
     * quantity : 0
     * unitPrice : 0
     * orderTotal : 0
     * totalAmountOfTheCommission : 0
     * isDeleted : true
     * deleterUserId : 0
     * deletionTime : 2018-01-05T00:45:28.696Z
     * lastModificationTime : 2018-01-05T00:45:28.696Z
     * lastModifierUserId : 0
     * creationTime : 2018-01-05T00:45:28.696Z
     * creatorUserId : 0
     * id : 0
     */

    private String cardName;
    private String cardCover;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal orderTotal;
    private BigDecimal totalAmountOfTheCommission;
    private boolean isDeleted;
    private int deleterUserId;
    private Date deletionTime;
    private Date lastModificationTime;
    private int lastModifierUserId;
    private Date creationTime;
    private int creatorUserId;
    private int id;

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardCover() {
        return cardCover;
    }

    public void setCardCover(String cardCover) {
        this.cardCover = cardCover;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(BigDecimal orderTotal) {
        this.orderTotal = orderTotal;
    }

    public BigDecimal getTotalAmountOfTheCommission() {
        return totalAmountOfTheCommission;
    }

    public void setTotalAmountOfTheCommission(BigDecimal totalAmountOfTheCommission) {
        this.totalAmountOfTheCommission = totalAmountOfTheCommission;
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
