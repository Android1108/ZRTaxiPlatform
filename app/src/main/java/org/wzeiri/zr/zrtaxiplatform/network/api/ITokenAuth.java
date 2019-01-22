package org.wzeiri.zr.zrtaxiplatform.network.api;

import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.LoginBean;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @author k-lm on 2017/12/11.
 */

public interface ITokenAuth {
    /**
     * 登录
     *
     * @return
     */
    @POST("/Api/App/TokenAuth/Authenticate")
    Call<BaseBean<LoginBean>> login(@Body RequestBody body);


}
