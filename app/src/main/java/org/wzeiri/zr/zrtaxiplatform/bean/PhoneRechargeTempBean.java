package org.wzeiri.zr.zrtaxiplatform.bean;

import java.math.BigDecimal;

/**
 * @author k-lm on 2017/12/27.
 */

public class PhoneRechargeTempBean {


    /**
     * phoneISP : 0
     * phoneISPName : string
     * amount : 0
     * realPrice : 0
     * id : 0
     */

    private int phoneISP;
    private String phoneISPName;
    private BigDecimal amount;
    private BigDecimal realPrice;
    private int id;

    public int getPhoneISP() {
        return phoneISP;
    }

    public void setPhoneISP(int phoneISP) {
        this.phoneISP = phoneISP;
    }

    public String getPhoneISPName() {
        return phoneISPName;
    }

    public void setPhoneISPName(String phoneISPName) {
        this.phoneISPName = phoneISPName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(BigDecimal realPrice) {
        this.realPrice = realPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
