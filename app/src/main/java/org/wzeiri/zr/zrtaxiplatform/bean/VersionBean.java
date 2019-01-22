package org.wzeiri.zr.zrtaxiplatform.bean;

/**
 * @author k-lm on 2018/1/26.
 */

public class VersionBean {


    /**
     * hasUpdate : true
     * versionName : 1.0.3
     * versionCode : 1.0.3
     * resourceId :
     * resourceUrl : null
     * describe :
     */

    private boolean hasUpdate;
    private String versionName;
    private String versionCode;
    private String resourceId;
    private String resourceUrl;
    private String describe;

    public boolean isHasUpdate() {
        return hasUpdate;
    }

    public void setHasUpdate(boolean hasUpdate) {
        this.hasUpdate = hasUpdate;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
