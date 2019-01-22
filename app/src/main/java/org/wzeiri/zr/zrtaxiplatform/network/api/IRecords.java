package org.wzeiri.zr.zrtaxiplatform.network.api;

import org.wzeiri.zr.zrtaxiplatform.bean.GetIntegralAgreementBean;
import org.wzeiri.zr.zrtaxiplatform.bean.GetRecordsBean;
import org.wzeiri.zr.zrtaxiplatform.bean.IntegralBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zz on 2017-12-15.
 */

public interface IRecords {

    /**
     *  get  /Api/App/IntegralRecord/GetIntegralRecords
     * 司机获取积分记录
     * @param IntegralCreaseType 增减类型,1-增加,2-减少,不传为全部的
     * @param PageSize
     * @param Page
     * @return
     */

    @GET("/Api/App/IntegralRecord/GetIntegralRecords")
    Call<BaseBean<BaseListBean<GetRecordsBean>>> getIntegralRecords(
            @Query("IntegralCreaseType") int IntegralCreaseType,
                                                      @Query("PageSize") int PageSize,
                                                      @Query("Page") int Page);

    /**
     *  get  /Api/App/IntegralRecord/GetIntegralRecords
     * 司机获取积分记录
     * @param PageSize
     * @param Page
     * @return
     */

    @GET("/Api/App/IntegralRecord/GetIntegralRecords")
    Call<BaseBean<BaseListBean<GetRecordsBean>>> getIntegralRecords(
            @Query("PageSize") int PageSize,
            @Query("Page") int Page);

    @GET("/Api/App/IntegralRecord/GetIntegralRecords")
    Call<BaseBean<IntegralBean>> getIntegral(
            @Query("PageSize") int PageSize,
            @Query("Page") int Page);


    /**
     * get  /Api/App/IntegralRecord/GetIntegralAgreement
     * 获取积分规则
     *
     * @return
     */
    @GET("/Api/App/IntegralRecord/GetIntegralAgreement")
    Call<BaseBean<GetIntegralAgreementBean>> getIntegralAgreement();
}
