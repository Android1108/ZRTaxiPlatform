package org.wzeiri.zr.zrtaxiplatform.network.api;

import org.wzeiri.zr.zrtaxiplatform.bean.BillBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author k-lm on 2018/1/5.
 */

public interface ICardOrder {
    /**
     * GET /Api/App/CardOrder/GetDriverCardOrderOutput
     * 司机关联订单
     *
     * @param pageSize
     * @param page
     * @return
     */
    @GET("/Api/App/CardOrder/GetDriverCardOrderOutput")
    Call<BaseBean<List<BillBean>>> getDriverCardOrderOutput(@Query("PageSize") int pageSize,
                                                            @Query("Page") int page);

}
