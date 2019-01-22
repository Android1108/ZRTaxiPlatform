package org.wzeiri.zr.zrtaxiplatform.network.api;

import org.wzeiri.zr.zrtaxiplatform.bean.NotificationBean;
import org.wzeiri.zr.zrtaxiplatform.bean.VersionBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by zz on 2017-12-19.
 */

public interface ISundry {

    /**
     * post  /Api/App/Sundry/CreateOpinionReport
     * 创建意见反馈
     */

    @POST("/Api/App/Sundry/CreateOpinionReport")
    Call<BaseBean<String>> createOpinionReport(@Body RequestBody body);

    /**
     * GET /Api/App/Sundry/GetAppUserNotifications
     * 获取App端用户的消息通知信息分页查询
     *
     * @param stare    读取状态,0-未读,1-已读,传null为全部
     * @param pageSize
     * @param page
     * @return
     */
    @GET("/Api/App/Sundry/GetAppUserNotifications")
    Call<BaseBean<NotificationBean>> getAppUserNotifications(@Query("ReadState") String stare,
                                                             @Query("PageSize") int pageSize,
                                                             @Query("Page") int page);

    /**
     * GET /Api/App/Sundry/GetArtyStar
     * 获取每月之星
     *
     * @return
     */
    @GET("/Api/App/Sundry/GetArtyStar")
    Call<BaseBean<String>> getArtyStar();

    /**
     * POST /Api/App/Sundry/VersionCheck
     * 版本号检测
     *
     * @param body clientVersionType (integer, optional): 版本类型10 ios，20 android ,
     *             versionCode (integer, optional): 版本号
     * @return
     */
    @POST("/Api/App/Sundry/VersionCheck")
    Call<BaseBean<VersionBean>> versionCheck(@Body RequestBody body);

    /**
     * GET /Api/App/Sundry/GetCarModels 获取车型
     *
     * @return
     */
    @GET("/Api/App/Sundry/GetCarModels")
    Call<BaseBean<List<String>>> getCarModels();

    /**
     * GET /Api/App/Sundry/GetBanks  获取银行
     *
     * @return
     */
    @GET("/Api/App/Sundry/GetBanks")
    Call<BaseBean<List<String>>> getBanks();

}
