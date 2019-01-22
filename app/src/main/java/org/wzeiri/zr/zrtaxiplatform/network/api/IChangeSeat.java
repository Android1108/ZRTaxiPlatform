package org.wzeiri.zr.zrtaxiplatform.network.api;

import org.wzeiri.zr.zrtaxiplatform.bean.SeatCoverBean;
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

public interface IChangeSeat {
    /**
     * GET /Api/App/ChangeSeat/GetChangeSeats
     * 司机获取自己的更换座套申请
     *
     * @param carId    车辆的Id,如果查询全部的,那么此参数不传(不要设置为0)
     * @param pageSize
     * @param page
     * @return
     */
    @GET("/Api/App/ChangeSeat/GetChangeSeats")
    Call<BaseBean<BaseListBean<SeatCoverBean>>> getLostFounds(@Query("CarId") int carId,
                                                              @Query("PageSize") int pageSize,
                                                              @Query("Page") int page);

    /**
     * POST /Api/App/ChangeSeat/CreateChangeSeat
     * 创建更换座套申请
     *
     * @param body carId (integer, optional): 车辆Id ,
     *             describe (string, optional): 备注
     * @return
     */
    @POST("/Api/App/ChangeSeat/CreateChangeSeat")
    Call<BaseBean<String>> createChangeSeat(@Body RequestBody body);

}
