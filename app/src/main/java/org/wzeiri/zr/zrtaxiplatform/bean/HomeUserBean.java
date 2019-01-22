package org.wzeiri.zr.zrtaxiplatform.bean;

/**
 * @author k-lm on 2017/12/29.
 */

public class HomeUserBean {

    /**
     * integral : 0
     * learnCount : 0
     * picture : string
     */

    private int integral;
    private int learnCount;
    private String picture;
    private int driverStatus;
    private int artyStarCount;

    public int getArtyStarCount() {
        return artyStarCount;
    }

    public void setArtyStarCount(int artyStarCount) {
        this.artyStarCount = artyStarCount;
    }

    public int getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(int driverStatus) {
        this.driverStatus = driverStatus;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public int getLearnCount() {
        return learnCount;
    }

    public void setLearnCount(int learnCount) {
        this.learnCount = learnCount;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
