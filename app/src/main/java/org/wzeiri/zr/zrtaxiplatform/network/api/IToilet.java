package org.wzeiri.zr.zrtaxiplatform.network.api;

import org.wzeiri.zr.zrtaxiplatform.bean.ToiletsBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author k-lm on 2017/12/19.
 */

public interface IToilet {
    /**
     * GET /Api/App/Toilet/GetAllToilets
     * 获取所有公共厕所位置
     *
     * @return
     */
    @GET("/Api/App/Toilet/GetAllToilets")
    Call<BaseBean<List<ToiletsBean>>> getAllToilets();

}
