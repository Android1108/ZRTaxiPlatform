package org.wzeiri.zr.zrtaxiplatform.network.api;

import org.wzeiri.zr.zrtaxiplatform.bean.CommissionBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author k-lm on 2018/1/26.
 */

public interface ICard {
    /**
     * GET /Api/App/Card/GetCardAmountOfTheCommission 返回佣金列表
     *
     * @return
     */
    @GET("/Api/App/Card/GetCardAmountOfTheCommission")
    Call<BaseBean<List<CommissionBean>>> getCardCommission(@Query("PageSize") int pageSize,
                                                           @Query("Page") int page);

}
