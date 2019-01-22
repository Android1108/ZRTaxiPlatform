package org.wzeiri.zr.zrtaxiplatform.network.api;

import org.wzeiri.zr.zrtaxiplatform.bean.MerchantBean;
import org.wzeiri.zr.zrtaxiplatform.bean.MerchantDetailBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author k-lm on 2017/12/13.
 */

public interface IMerchant {
    /**
     * GET /Api/App/Merchant/GetMerchants
     * 获取餐饮合作商户
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @param PageSize
     * @param Page
     * @return
     */
    @GET("/Api/App/Merchant/GetMerchants")
    Call<BaseBean<BaseListBean<MerchantBean>>> getMerchants(@Query("Long") double longitude,
                                                            @Query("Lat") double latitude,
                                                            @Query("PageSize") int PageSize,
                                                            @Query("Page") int Page);

    /**
     * GET /Api/App/Merchant/GetMerchantDetail
     * 获取商户详情
     *
     * @param id
     * @return
     */
    @GET("/Api/App/Merchant/GetMerchantDetail")
    Call<BaseBean<MerchantDetailBean>> getMerchantDetail(@Query("Id") long id);

}
