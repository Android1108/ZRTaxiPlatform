package org.wzeiri.zr.zrtaxiplatform.network.api;

import org.wzeiri.zr.zrtaxiplatform.bean.AgreementBean;
import org.wzeiri.zr.zrtaxiplatform.bean.DriverStatusBean;
import org.wzeiri.zr.zrtaxiplatform.bean.GetUserPaypalPictureBean;
import org.wzeiri.zr.zrtaxiplatform.bean.HomeIndexBean;
import org.wzeiri.zr.zrtaxiplatform.bean.HomeUserBean;
import org.wzeiri.zr.zrtaxiplatform.bean.LoginBean;
import org.wzeiri.zr.zrtaxiplatform.bean.MyUserInfoBean;
import org.wzeiri.zr.zrtaxiplatform.bean.TenantBean;
import org.wzeiri.zr.zrtaxiplatform.bean.UserInfoBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * @author k-lm on 2017/12/11.
 */

public interface IUser {
    /**
     * POST /Api/App/User/SendRegisterCode
     * 发送注册验证码
     *
     * @param body 手机号
     * @return
     */
    @POST("/Api/App/User/SendRegisterCode")
    Call<BaseBean<String>> sendRegisterCode(@Body RequestBody body);

    @POST("/Api/App/User/RegisterUser")
    Call<BaseBean<LoginBean>> registerUser(@Body RequestBody body);

    /**
     * GET /Api/App/User/GetUserQrCode
     * 获取用户二维码信息(包含用户车牌号等)
     *
     * @return
     */
    @GET("/Api/App/User/GetUserQrCode")
    Call<BaseBean<UserInfoBean>> getUserQrCode();


    /**
     * POST /Api/App/User/UpdateUserAddress
     * 修改用户的地址
     *
     * @param body address (string, optional): 地址
     */
    @POST("/Api/App/User/UpdateUserAddress")
    Call<BaseBean<String>> updateUserAddress(@Body RequestBody body);

    /**
     * POST /Api/App/User/UpdateUserRegion
     * 修改用户的省份和城市
     *
     * @param body provinceCode (string, optional): 省份代码 ,
     *             cityCode (string, optional): 城市代码
     * @return
     */
    @POST("/Api/App/User/UpdateUserRegion")
    Call<BaseBean<String>> updateUserRegion(@Body RequestBody body);

    /**
     * GET /Api/App/User/GetUserBasicInfo
     * 获取用户基本资料
     *
     * @return
     */
    @GET("/Api/App/User/GetUserBasicInfo")
    Call<BaseBean<MyUserInfoBean>> getUserBasicInfo();

    /**
     * POST /Api/App/User/SendForgetPasswordCode
     * 发送忘记密码验证码
     *
     * @param body phoneNumber (string, optional): 手机号码
     * @return
     */
    @POST("/Api/App/User/SendForgetPasswordCode")
    Call<BaseBean<String>> sendForgetPasswordCode(@Body RequestBody body);

    /**
     * POST /Api/App/User/ForgetPassword
     * 忘记密码
     *
     * @param body phoneNumber (string, optional): 手机号码 ,
     *             password (string, optional): 密码 ,
     *             key (string, optional): 验证码的Key ,
     *             code (string, optional): 验证码
     * @return
     */
    @POST("/Api/App/User/ForgetPassword")
    Call<BaseBean<String>> forgetPassword(@Body RequestBody body);

    /**
     * GET /Api/App/User/GetTenants
     * 获取可切换的地区(租户)
     *
     * @return
     */
    @GET("/Api/App/User/GetTenants")
    Call<BaseBean<List<TenantBean>>> getTenants();

    /**
     * POST /Api/App/User/ChangeCurrentTenantByRegionName
     * 根据定位的省市名称,切换当前的城市
     *
     * @param body provinceName (string, optional): 省份名称 ,
     *             cityName (string, optional): 城市名称
     * @return
     */
    @POST("/Api/App/User/ChangeCurrentTenantByRegionName")
    Call<BaseBean<TenantBean>> changeCurrentTenantByRegionName(@Body RequestBody body);

    /**
     * POST /Api/App/User/GetCurrentTenantByRegionName  根据定位的省市名称,获取当前的城市
     *
     * @param body provinceName (string, optional): 省份名称 ,
     *             cityName (string, optional): 城市名称
     * @return
     */
    @POST("/Api/App/User/GetCurrentTenantByRegionName")
    Call<BaseBean<TenantBean>> getCurrentTenantByRegionName(@Body RequestBody body);

    /**
     * GET /Api/App/User/GetUserPhoneNumber
     * 获取用户的手机号码
     *
     * @return
     */
    @GET("/Api/App/User/GetUserPhoneNumber")
    Call<BaseBean<String>> getUserPhoneNumber();

    /**
     * POST /Api/App/User/ChangeCurrentTenant
     * 切换地区
     *
     * @param body
     * @return
     */
    @POST("/Api/App/User/ChangeCurrentTenant")
    Call<BaseBean<TenantBean>> changeCurrentTenant(@Body RequestBody body);

    /**
     * GET /Api/App/User/GetCurrentDriverStatus
     * 获取司机的认证状态,1-注册已完成,待提交认证 2-已提交认证,等待审核;4-审核成功(能提交发布);8审核失败(审核不通过)
     *
     * @return
     */
    @GET("/Api/App/User/GetCurrentDriverStatus")
    Call<BaseBean<Integer>> getCurrentDriverStatus();

    /**
     * GET /Api/App/User/GetCurrentDriverStatus
     * 获取司机的认证状态,1-注册已完成,待提交认证 2-已提交认证,等待审核;4-审核成功(能提交发布);8审核失败(审核不通过)
     *
     * @return
     */
    @GET("/Api/App/User/GetDriverStatus")
    Call<BaseBean<DriverStatusBean>> getDriverStatus();

    /**
     * GET /Api/App/User/GetHomeIndex
     * 获取首页数据
     *
     * @return
     */
    @GET("/Api/App/User/GetHomeIndex")
    Call<BaseBean<HomeIndexBean>> getHomeIndex();

    /**
     * GET /Api/App/User/GetUserHome
     * App用户中心数据
     *
     * @return
     */
    @GET("/Api/App/User/GetUserHome")
    Call<BaseBean<HomeUserBean>> getUserHome();

    /**
     * POST /Api/App/User/UpdateUserPicture
     * 修改用户头像
     *
     * @param body picture (string, optional): 图片
     * @return
     */
    @POST("/Api/App/User/UpdateUserPicture")
    Call<BaseBean<String>> updateUserPicture(@Body RequestBody body);

    /**
     * GET /Api/App/User/GetUserPaypalPicture
     * 获取用户收付款图片Url地址,如果未设置则返回空字符串
     *
     * @return
     */
    @GET("/Api/App/User/GetUserPaypalPicture")
    Call<BaseBean<GetUserPaypalPictureBean>> getUserPaypalPicture();

    /**
     * POST /Api/App/User/UpdateUserPaypalPicture
     * 修改用户收付款图片
     *
     * @return
     */
    @POST("/Api/App/User/UpdateUserPaypalPicture")
    Call<BaseBean<String>> updateUserPaypalPicture(@Body RequestBody body);

    /**
     * GET /Api/App/User/GetRegisterAgreement
     * 获取注册协议
     *
     * @return
     */
    @GET("/Api/App/User/GetRegisterAgreement")
    Call<BaseBean<AgreementBean>> getRegisterAgreement();

    /**
     * POST /Api/App/User/BindingDriverCode
     * 绑定司机二维码code
     *
     * @param body Url	 二维码url
     * @return
     */
    @POST("/Api/App/User/BindingCarQrCodeUrl")
    Call<BaseBean<String>> bindingDriverCode(@Body RequestBody body);

}
