package org.wzeiri.zr.zrtaxiplatform.network.api;

import org.wzeiri.zr.zrtaxiplatform.bean.PhoneRechargeTempBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * @author k-lm on 2017/12/27.
 */

public interface IPhoneRecharge {
    /**
     * GET /Api/App/PhoneRecharge/GetPhoneRechargeTemps
     * 获取充值模板
     *
     * @return
     */
    @GET("/Api/App/PhoneRecharge/GetPhoneRechargeTemps")
    Call<BaseBean<List<PhoneRechargeTempBean>>> getPhoneRechargeTemps();

    /**
     * POST /Api/App/PhoneRecharge/CreatePhoneRechargeFromTemp
     * 根据手机充值模板进行充值支付
     *
     * @param body phoneRechargeTempId (integer, optional): 充值模板Id ,
     *             phoneNumber (string, optional): 手机号码 ,
     *             paymentType (integer, optional): 支付类型,1-微信支付,2-支付宝
     * @return
     */
    @POST("/Api/App/PhoneRecharge/CreatePhoneRechargeFromTemp")
    Call<BaseBean<String>> createPhoneRechargeFromTemp(@Body RequestBody body);
}
