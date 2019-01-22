package org.wzeiri.zr.zrtaxiplatform.network.api;

import org.wzeiri.zr.zrtaxiplatform.bean.LostFoundDetailBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;
import org.wzeiri.zr.zrtaxiplatform.bean.LostFoundBean;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author k-lm on 2017/12/12.
 */

public interface ILostFound {
    /**
     * GET /Api/App/LostFound/GetLostFounds
     * 司机获取财务丢失列表
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GET("/Api/App/LostFound/GetLostFounds")
    Call<BaseBean<BaseListBean<LostFoundBean>>> getLostFounds(@Query("Page") int page, @Query("PageSize") int pageSize);

    /**
     * POST /Api/App/LostFound/CreateLostFound
     * 创建失物招领
     *
     * @param body
     * @return
     */
    @POST("/Api/App/LostFound/CreateLostFound")
    Call<BaseBean<String>> sendCreateLostFound(@Body RequestBody body);

    /**
     * GET /Api/App/LostFound/GetLostFoundDetail
     * 获取财务丢失详情
     *
     * @param id 失物招领id
     * @return
     */
    @GET("/Api/App/LostFound/GetLostFoundDetail")
    Call<BaseBean<LostFoundDetailBean>> getLostFoundDetail(@Query("Id") int id);

    /**
     * GET /Api/App/LostFound/GetLostFounds
     * 司机获取财务丢失列表,乘客获取失物招领列表
     *
     * @param type     失物招领和财务丢失类型,1-财务丢失,2-失物招领
     * @param pageSize
     * @param page
     * @return
     */
    @GET("/Api/App/LostFound/GetMyLostFounds")
    Call<BaseBean<BaseListBean<LostFoundBean>>> getMyLostFounds(@Query("LostFoundType") int type,
                                                                @Query("PageSize") int pageSize,
                                                                @Query("Page") int page);

    /**
     * POST /Api/App/LostFound/DeleteLostFound
     * App删除失物招领
     *
     * @param body id (integer, optional)
     * @return
     */
    @POST("/Api/App/LostFound/DeleteLostFound")
    Call<BaseBean<String>> deleteLostFound(@Body RequestBody body);
}
