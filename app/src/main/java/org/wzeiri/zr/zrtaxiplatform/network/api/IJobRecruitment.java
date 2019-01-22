package org.wzeiri.zr.zrtaxiplatform.network.api;

import org.wzeiri.zr.zrtaxiplatform.bean.JobRecruitmentBean;
import org.wzeiri.zr.zrtaxiplatform.bean.JobRecruitmentDetailBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author k-lm on 2017/12/12.
 */

public interface IJobRecruitment {
    /**
     * GET /Api/App/JobRecruitment/GetJobRecruitments
     * 获取求职招聘信息
     *
     * @param type      求职招聘信息,1-求职信息,2-招聘信息,如果查询全部,那么不传(不能为0)
     * @param pagerSize
     * @param page
     * @return
     */
    @GET("/Api/App/JobRecruitment/GetJobRecruitments")
    Call<BaseBean<BaseListBean<JobRecruitmentBean>>> getJobRecruitments(@Query("JobRecruitmentType") int type,
                                                                        @Query("PageSize") int pagerSize,
                                                                        @Query("Page") int page);

    /**
     * GET /Api/App/JobRecruitment/GetMyJobRecruitments
     * 获取本人的求职信息
     *
     * @param type      求职招聘信息,1-求职信息,2-招聘信息,如果查询全部,那么不传(不能为0)
     * @param pagerSize
     * @param page
     * @return
     */
    @GET("/Api/App/JobRecruitment/GetMyJobRecruitments")
    Call<BaseBean<BaseListBean<JobRecruitmentBean>>> getMyJobRecruitments(@Query("JobRecruitmentType") int type,
                                                                          @Query("PageSize") int pagerSize,
                                                                          @Query("Page") int page);

    /**
     * GET /Api/App/JobRecruitment/GetJobRecruitmentDetail
     * 获取招聘信息详情
     *
     * @param id 列表id
     * @return
     */
    @GET("/Api/App/JobRecruitment/GetJobRecruitmentDetail")
    Call<BaseBean<JobRecruitmentDetailBean>> getJobRecruitmentDetail(@Query("Id") int id);

    /**
     * POST /Api/App/JobRecruitment/CreateJobRecruitment
     * 创建司机求职信息
     *
     * @param body title (string, optional): 标题 ,
     *             coverPicture (string, optional): 封面图 ,
     *             content (string, optional): 求职招聘内容 ,
     *             contact (string, optional): 联系方式 ,
     *             provinceCode (string, optional): 省份 ,
     *             cityCode (string, optional): 城市 ,
     *             areaCode (string, optional): 地区 ,
     *             address (string, optional): 地址
     * @return
     */
    @POST("/Api/App/JobRecruitment/CreateJobRecruitment")
    Call<BaseBean<String>> createJobRecruitment(@Body RequestBody body);

    /**
     * POST /Api/App/JobRecruitment/DeleteJobRecruitment
     * 删除本人的求职招聘
     *
     * @param body id (integer, optional)
     * @return
     */
    @POST("/Api/App/JobRecruitment/DeleteJobRecruitment")
    Call<BaseBean<String>> deleteJobRecruitment(@Body RequestBody body);

}
