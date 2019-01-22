package org.wzeiri.zr.zrtaxiplatform.network.api;

import org.wzeiri.zr.zrtaxiplatform.bean.LegalAdviceTypeBean;
import org.wzeiri.zr.zrtaxiplatform.bean.LegalBean;
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
 * @author k-lm on 2017/12/14.
 */

public interface ILegal {
    /**
     * GET /Api/App/Legal/GetLegalTypies
     * 获取法律咨询类型
     *
     * @return
     */
    @GET("/Api/App/Legal/GetLegalTypies")
    Call<BaseBean<List<LegalAdviceTypeBean>>> getLegalTypies();

    /**
     * POST /Api/App/Legal/CreateLegal
     * 创建法律咨询
     *
     * @param body legalType (integer, optional): 咨询类型 ,
     *             content (string, optional): 咨询内容
     * @return
     */
    @POST("/Api/App/Legal/CreateLegal")
    Call<BaseBean<String>> createLegal(@Body RequestBody body);

    /**
     * GET /Api/App/Legal/GetMyLegals
     * 获取我的法律咨询与回复
     *
     * @param isReplied 是否已回复,如果不传则为全部的
     * @param pageSize
     * @param page
     * @return
     */
    @GET("/Api/App/Legal/GetMyLegals")
    Call<BaseBean<BaseListBean<LegalBean>>> getMyLegals(@Query("IsReplied") boolean isReplied,
                                                        @Query("PageSize") int pageSize,
                                                        @Query("Page") int page);

    /**
     * GET /Api/App/Legal/GetMyLegals
     * 获取我的法律咨询
     *
     * @param isReplied 是否已回复,如果不传则为全部的
     * @param legalType 咨询类型
     * @param pageSize
     * @param page
     * @return
     */
    @GET("/Api/App/Legal/GetLegals")
    Call<BaseBean<BaseListBean<LegalBean>>> getLegals(@Query("IsReplied") boolean isReplied,
                                                      @Query("LegalType") int legalType,
                                                      @Query("PageSize") int pageSize,
                                                      @Query("Page") int page);

}
