package org.wzeiri.zr.zrtaxiplatform.network.api;

import org.wzeiri.zr.zrtaxiplatform.bean.MyUserInfoBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;

/**
 * Created by zz on 2017-12-13.
 */

public interface IMy {
    /**
     * get  /Api/App/Driver/GetCurrentDriverInfo
     * 获取当前司机信息（个人资料）
     *
     */

    @GET("/Api/App/Driver/GetCurrentDriverInfo")
    Call<BaseBean<MyUserInfoBean>> getCurrentDriverInfo();

}
