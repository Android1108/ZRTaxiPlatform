package org.wzeiri.zr.zrtaxiplatform.network.api;

import org.wzeiri.zr.zrtaxiplatform.bean.UserCodeBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by zz on 2017-12-14.
 */

public interface IUserQrCode {

    /**
     * get  /Api/App/User/GetUserQrCode
     * 获取用户二维码信息（包含用户车牌号等）
     */

    @GET("/Api/App/User/GetUserQrCode ")
    Call<BaseBean<UserCodeBean>> getUserQrCode();

}
