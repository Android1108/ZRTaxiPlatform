package org.wzeiri.zr.zrtaxiplatform.network.api;

import org.wzeiri.zr.zrtaxiplatform.bean.DeviceVehicleBean;
import org.wzeiri.zr.zrtaxiplatform.bean.QueryIllegalInfoBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author k-lm on 2018/3/5.
 */

public interface ITransport {
    /**
     * /Api/App/Transport/QueryVehicleInfo
     * 查询司机车辆信息
     *
     * @param idNumber 身份证
     * @param phone    手机
     * @param code     验证码
     */
    @GET("/Api/App/Transport/QueryVehicleInfo")
    Call<BaseBean<DeviceVehicleBean>> getDeviceVehicleInfo(@Query("IdNumber") String idNumber,
                                                           @Query("VerificationCode") String code);

    /**
     * 发送查询司机车辆信息的验证码
     *
     * @param idNumber 身份证
     */
    @GET("/Api/App/Transport/QueryVehicleInfoSendSms")
    Call<BaseBean<String>> queryVehicleInfoSendSms(@Query("IdNumber") String idNumber);

    /**
     * 获取司机卡信息
     *
     * @param verificationCode 验证码
     * @return
     */
    @GET("/Api/App/Transport/QueryCard7Info")
    Call<BaseBean<String>> queryCard7Info(@Query("VerificationCode") String verificationCode);

    /**
     * 发送获取司机卡信息的短信
     *
     * @return
     */
    @GET("/Api/App/Transport/QueryCard7InfoSendSms")
    Call<BaseBean<String>> sendCard7InfoSendSms();

    /**
     * 获取司机违章信息
     *
     * @param verificationCode 验证码
     * @return
     */
    @GET("/Api/App/Transport/QueryIllegalInfo")
    Call<BaseBean<List<QueryIllegalInfoBean>>> queryIllegalInfo(@Query("VerificationCode") String verificationCode);


    /**
     * 获取司机违章信息
     *
     * @return
     */
    @GET("/Api/App/Transport/QueryIllegalInfo")
    Call<BaseBean<List<QueryIllegalInfoBean>>> queryIllegalInfo();

    /**
     * 发送获取司机违章信息的短信
     *
     * @return
     */
    @GET("/Api/App/Transport/QueryIllegalInfoSendSms")
    Call<BaseBean<String>> sendIllegalInfoSendSms();
}
