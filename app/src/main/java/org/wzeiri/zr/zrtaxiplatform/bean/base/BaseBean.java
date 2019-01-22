package org.wzeiri.zr.zrtaxiplatform.bean.base;

import com.google.gson.annotations.SerializedName;

/**
 * @author k-lm on 2017/11/17.
 */

public class BaseBean<T> {


    /**
     * result : null
     * targetUrl : null
     * success : false
     * error : {"code":0,"message":"当前用户没有登录到系统！","details":null,"validationErrors":null}
     * unAuthorizedRequest : true
     * __abp : true
     */

    private T result;
    private String targetUrl;
    private boolean success;
    private ErrorBean error;
    private boolean unAuthorizedRequest;
    @SerializedName("__abp")
    private boolean abp;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ErrorBean getError() {
        return error;
    }

    public void setError(ErrorBean error) {
        this.error = error;
    }

    public boolean isUnAuthorizedRequest() {
        return unAuthorizedRequest;
    }

    public void setUnAuthorizedRequest(boolean unAuthorizedRequest) {
        this.unAuthorizedRequest = unAuthorizedRequest;
    }

    public boolean isAbp() {
        return abp;
    }

    public void setAbp(boolean abp) {
        this.abp = abp;
    }


}
