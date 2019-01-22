package org.wzeiri.zr.zrtaxiplatform.network.api;

import org.wzeiri.zr.zrtaxiplatform.bean.VehicleTransactionBean;
import org.wzeiri.zr.zrtaxiplatform.bean.VehicleTransactionDetailBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author k-lm on 2017/12/12.
 */

public interface IVehicleTransaction {
    /**
     * GET /Api/App/VehicleTransaction/GetVehicleTransactions
     * 查询车辆交易
     *
     * @param type     车辆交易信息类型,1-车辆转卖,2-经营权交易
     * @param pageSize
     * @param page
     * @return
     */
    @GET("/Api/App/VehicleTransaction/GetVehicleTransactions")
    Call<BaseBean<BaseListBean<VehicleTransactionBean>>> getVehicleTransactions(@Query("VehicleTransactionType") int type,
                                                                                @Query("PageSize") int pageSize,
                                                                                @Query("Page") int page);

    /**
     * GET /Api/App/VehicleTransaction/GetVehicleTransactionDetail
     * 查询车辆交易信息详情
     *
     * @param id 详情id
     * @return
     */
    @GET("/Api/App/VehicleTransaction/GetVehicleTransactionDetail")
    Call<BaseBean<VehicleTransactionDetailBean>> getVehicleTransactionDetail(@Query("Id") int id);
}
