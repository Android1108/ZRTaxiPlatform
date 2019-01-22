package org.wzeiri.zr.zrtaxiplatform.bean;

import java.math.BigDecimal;

/**
 * @author k-lm on 2018/1/26.
 */

public class CommissionBean {


    /**
     * name : string
     * cover : string
     * amountOfTheCommission : 0
     * stockQuantity : 0
     * id : 0
     */

    private String name;
    private String cover;
    private BigDecimal amountOfTheCommission;
    private int stockQuantity;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public BigDecimal getAmountOfTheCommission() {
        return amountOfTheCommission;
    }

    public void setAmountOfTheCommission(BigDecimal amountOfTheCommission) {
        this.amountOfTheCommission = amountOfTheCommission;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
