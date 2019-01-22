package org.wzeiri.zr.zrtaxiplatform.network.api;


import org.wzeiri.zr.zrtaxiplatform.bean.BingCarInfoBean;
import org.wzeiri.zr.zrtaxiplatform.bean.DriverBean;
import org.wzeiri.zr.zrtaxiplatform.bean.DriverInfoBean;
import org.wzeiri.zr.zrtaxiplatform.bean.LoginBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * Created by zz on 2017/12/12.
 */

public interface IDriver {
    /**
     * post  /Api/App/Driver/DriverAuth
     * 司机认证
     * 车辆认证
     */
    @POST("/Api/App/Driver/DriverAuth")
    Call<BaseBean<LoginBean>> driverAuth(@Body RequestBody body);

    /**
     * get  /Api/App/Driver/GetOpCompanies
     * 获取出租车
     *
     * @return
     */
    @GET("/Api/App/Driver/GetOpCompanies")
    Call<BaseBean<String>> getOpCompanies();

    /**
     * get  /Api/App/Driver/GetBrands
     * 获取品牌
     *
     * @return
     */
    @GET("/Api/App/Driver/GetBrands")
    Call<BaseBean<List<DriverBean>>> getBrands();

    /**
     * get  /Api/App/Driver/GetColors
     * 获取颜色
     *
     * @return
     */
    @GET("/Api/App/Driver/GetColors")
    Call<BaseBean<List<DriverBean>>> getColors();

    /**
     * GET /Api/App/Driver/GetBindingCars
     * 获取车辆
     *
     * @return
     */
    @GET("/Api/App/Driver/GetBindingCars")
    Call<BaseBean<BingCarInfoBean>> getBindingCars();
    /**
     * GET /Api/App/Driver/GetDriverInfo
     * 获取指定的司机信息
     *
     * @param id id
     * @return
     */
    @GET("/Api/App/Driver/GetDriverInfo")
    Call<BaseBean<DriverInfoBean>> getDriverInfo(@Query("driverId") long id);

    /**
     * POST /Api/App/Driver/BindingCar
     * 车辆绑定
     *
     * @param body plateNumber (string, optional): 车牌 ,
     *             brandId (integer, optional): 品牌Id ,
     *             carModel (string, optional): 车型 ,
     *             colorId (integer, optional): 颜色Id ,
     *             lisenceNumber (string, optional): 营运证号 ,
     *             lisencePicture (string, optional): 营运证图片 ,
     *             bindingCarRequestId (integer, optional): 申请Id
     * @return
     */
    @POST("/Api/App/Driver/BindingCar")
    Call<BaseBean<String>> bindingCar(@Body RequestBody body);

    /**
     * GET /Api/App/Driver/GetOpCompanies
     * 获取出租车公司
     *
     * @param tenantId
     * @return
     */
    @GET("/Api/App/Driver/GetOpCompanies")
    Call<BaseBean<List<DriverBean>>> getOpCompanies(@Query("tenantId") int tenantId);

    /**
     * POST /Api/App/Driver/SwitchCurrentCar
     * 切换车辆
     *
     * @param body id (integer, optional)
     * @return
     */
    @POST("/Api/App/Driver/SwitchCurrentCar")
    Call<BaseBean<String>> switchCurrentCar(@Body RequestBody body);

    /**
     * POST /Api/App/Driver/CheckIdNumber
     * 检测身份证号码是否被注册
     *
     * @param IdNumber IdNumber 身份证
     * @return
     */
    @POST("/Api/App/Driver/CheckIdNumber")
    Call<BaseBean<String>> checkIdNumber(@Query("IdNumber") String IdNumber);

}