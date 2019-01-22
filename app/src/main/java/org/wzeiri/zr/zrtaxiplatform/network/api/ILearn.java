package org.wzeiri.zr.zrtaxiplatform.network.api;

import org.wzeiri.zr.zrtaxiplatform.bean.LearnRecordBean;
import org.wzeiri.zr.zrtaxiplatform.bean.LearnVideoBean;
import org.wzeiri.zr.zrtaxiplatform.bean.VideoDetailBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author k-lm on 2017/12/22.
 */

public interface ILearn {
    /**
     * GET /Api/App/Learn/GetLearnVedios
     * 获取视频学习的视频列表
     *
     * @param pageSize
     * @param page
     * @return
     */
    @GET("/Api/App/Learn/GetLearnVedios")
    Call<BaseBean<BaseListBean<LearnVideoBean>>> getLearnVideos(@Query("PageSize") int pageSize,
                                                                @Query("Page") int page);

    /**
     * GET /Api/App/Learn/GetLearnVedioDetail
     * 获取学习视频详情
     *
     * @param id
     * @return
     */
    @GET("/Api/App/Learn/GetLearnVedioDetail")
    Call<BaseBean<VideoDetailBean>> getLearnVideoDetail(@Query("Id") int id);

    /**
     * POST /Api/App/Learn/CreateLearnRecord
     * 创建学习记录
     *
     * @param body learnVedioId (integer, optional): 学习视频Id
     * @return
     */
    @POST("/Api/App/Learn/CreateLearnRecord")
    Call<BaseBean<String>> createLearnRecord(@Body RequestBody body);

    /**
     * GET /Api/App/Learn/GetLearnRecords
     * App获取自己的学习记录
     *
     * @param pageSize
     * @param page
     * @return
     */
    @GET("/Api/App/Learn/GetLearnRecords")
    Call<BaseBean<BaseListBean<LearnRecordBean>>> getLearnRecords(@Query("PageSize") int pageSize,
                                                                  @Query("Page") int page);


}
