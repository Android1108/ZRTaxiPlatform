package org.wzeiri.zr.zrtaxiplatform.bean;

import com.google.gson.annotations.SerializedName;

import org.wzeiri.zr.zrtaxiplatform.bean.base.ErrorBean;

/**
 * Created by zz on 2017/12/12.
 */

public class RegisterBean<T> {


    /**
     * result : null
     * targetUrl : null
     * success : false
     * error : {"code":0,"message":"你的请求已经过期！","details":"手机号码格式不正确","validationErrors":null}
     * unAuthorizedRequest : true
     * __abp : true
     */

    private T result;
    private T targetUrl;
    private boolean success;
    private ErrorBean error;
    private boolean authorizedRequest;
    @SerializedName("__abp")
    private boolean _adp;


    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public T getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(T targetUrl) {
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

    public boolean isAuthorizedRequest() {
        return authorizedRequest;
    }

    public void setAuthorizedRequest(boolean authorizedRequest) {
        this.authorizedRequest = authorizedRequest;
    }

    public boolean is_adp() {
        return _adp;
    }

    public void set_adp(boolean _adp) {
        this._adp = _adp;
    }

}
