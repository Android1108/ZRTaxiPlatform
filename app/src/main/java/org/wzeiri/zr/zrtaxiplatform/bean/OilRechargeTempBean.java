package org.wzeiri.zr.zrtaxiplatform.bean;

import java.math.BigDecimal;

/**
 * @author k-lm on 2017/12/14.
 */

public class OilRechargeTempBean {


    /**
     * oilCardType : 0
     * amount : 0
     * realPrice : 0
     * id : 0
     */

    private BigDecimal amount;
    private BigDecimal realPrice;
    private int id;


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
