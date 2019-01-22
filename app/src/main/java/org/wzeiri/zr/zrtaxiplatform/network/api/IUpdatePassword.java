package org.wzeiri.zr.zrtaxiplatform.network.api;

import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by zz on 2017-12-25.
 */

public interface IUpdatePassword {
    /**
     * post  /Api/App/User/UpdatePassword
     * 修改密码
     */

    @POST("/Api/App/User/UpdatePassword")
    Call<BaseBean<String>> updatePassword(@Body RequestBody body);

}
