package org.wzeiri.zr.zrtaxiplatform.network.api;

import org.wzeiri.zr.zrtaxiplatform.bean.AdverPostApplyBean;
import org.wzeiri.zr.zrtaxiplatform.bean.AdverPostApplyDetailBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author k-lm on 2017/12/13.
 */

public interface IAdverPostApply {
    /**
     * GET /Api/App/AdverPostApply/GetMyAdverPostApplies
     * 获取我的车辆张贴信息
     * 提交司机张贴广告信息
     *
     * @param carId    车辆的Id,如果查询全部的,那么此参数不传(不要设置为0)
     * @param pageSize
     * @param page
     * @return
     */
    @GET("/Api/App/AdverPostApply/GetMyAdverPostApplies")
    Call<BaseBean<BaseListBean<AdverPostApplyBean>>> getMyAdverPostApplies(@Query("CarId") int carId,
                                                                           @Query("PageSize") int pageSize,
                                                                           @Query("Page") int page);

    /**
     * POST /Api/App/AdverPostApply/CreateAdverPostApply
     * 提交司机张贴广告信息
     *
     * @param body adverPicture (string, optional): 广告图片 ,
     *             carId (integer, optional): 车辆Id,可以根据司机Id和车辆Id获取具体的绑定车辆 ,
     *             describe (string, optional): 备注
     * @return
     */
    @POST("/Api/App/AdverPostApply/CreateAdverPostApply")
    Call<BaseBean<String>> createAdverPostApply(@Body RequestBody body);

    /**
     * GET /Api/App/AdverPostApply/GetAdverPostApplyDetail
     * 获取广告张贴详情
     *
     * @param id
     * @return
     */
    @GET("/Api/App/AdverPostApply/GetAdverPostApplyDetail")
    Call<BaseBean<AdverPostApplyDetailBean>> getAdverPostApplyDetail(@Query("Id") int id);
}
