package org.wzeiri.zr.zrtaxiplatform.network.api;

import org.wzeiri.zr.zrtaxiplatform.bean.IntegralRuleBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author k-lm on 2017/12/20.
 */

public interface IIntegralRecord {
    /**
     * GET /Api/App/IntegralRecord/GetIntegralAgreement
     * 获取积分规则
     *
     * @return
     */
    @GET("/Api/App/IntegralRecord/GetIntegralAgreement")
    Call<BaseBean<IntegralRuleBean>> getIntegralAgreement();
}
