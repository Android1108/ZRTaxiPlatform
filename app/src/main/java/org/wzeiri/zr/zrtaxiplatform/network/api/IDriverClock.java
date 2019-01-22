package org.wzeiri.zr.zrtaxiplatform.network.api;

import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by wxy on 2018/5/21.
 */

public interface IDriverClock {
    @GET("/Api/App/Driver/DriverClock")
    Call<BaseBean<String>> getClock(@Query("ClockRecordType") String query);
}
