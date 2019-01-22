package org.wzeiri.zr.zrtaxiplatform.network.api;

import org.wzeiri.zr.zrtaxiplatform.bean.EquipmentFaultDetailBean;
import org.wzeiri.zr.zrtaxiplatform.bean.FaultInfoBean;
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

public interface IEquipmentFault {
    /**
     * GET /Api/App/EquipmentFault/GetEquipmentFaults
     * 司机获取自己的故障申请
     *
     * @param carId    车辆Id,如果查询全部的CarId不传
     * @param pageSize
     * @param page
     * @return
     */
    @GET("/Api/App/EquipmentFault/GetEquipmentFaults")
    Call<BaseBean<BaseListBean<FaultInfoBean>>> getLostFounds(@Query("CarId") int carId,
                                                              @Query("PageSize") int pageSize,
                                                              @Query("Page") int page);

    /**
     * POST /Api/App/EquipmentFault/CreateEquipmentFault
     * 创建设备故障或者Gps故障
     *
     * @param body carId (integer, optional): 车辆Id ,
     *             equipmentFaultType (integer, optional): 故障类型,1-Gps故障,2-广告设备故障,3-其他 ,
     *             faultDescribe (string, optional): 故障描述 ,
     *             createUserId (integer, optional): 创建用户Id ,
     *             equipmentFaultPictures (Array[string], optional): 图片Id
     * @return
     */
    @POST("/Api/App/EquipmentFault/CreateEquipmentFault")
    Call<BaseBean<String>> createEquipmentFault(@Body RequestBody body);

    /**
     * GET /Api/App/EquipmentFault/GetEquipmentFaultDetail 故障详情
     *
     * @param id 故障信息id
     * @return
     */
    @GET("/Api/App/EquipmentFault/GetEquipmentFaultDetail")
    Call<BaseBean<EquipmentFaultDetailBean>> getEquipmentFaultDetail(@Query("id") int id);
}
