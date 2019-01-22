package org.wzeiri.zr.zrtaxiplatform.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author k-lm on 2018/1/10.
 */

public class WeChatPayBean {
    /**
     * appId : wx0540b1d00fab34db
     * partnerId : 1494443982
     * prepayId : wx2018011008460495626b9cfe0561009411
     * package : Sign=WXPay
     * nonceStr : 2eb8bce023904761bd4341922e297ed8
     * timestamp : 1515545165
     * sign : CDE69DF57796CF0F81BA01A7901204DC
     */

    private String appId;
    private String partnerId;
    private String prepayId;
    @SerializedName("package")
    private String packageX;
    private String nonceStr;
    private String timestamp;
    private String sign;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }


}
