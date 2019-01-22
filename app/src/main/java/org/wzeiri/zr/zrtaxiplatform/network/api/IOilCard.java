package org.wzeiri.zr.zrtaxiplatform.network.api;

import org.wzeiri.zr.zrtaxiplatform.bean.ALiPayBean;
import org.wzeiri.zr.zrtaxiplatform.bean.OilCardBean;
import org.wzeiri.zr.zrtaxiplatform.bean.OilCardRechargeBean;
import org.wzeiri.zr.zrtaxiplatform.bean.OilRechargeTempBean;
import org.wzeiri.zr.zrtaxiplatform.bean.RechargeSelectBean;
import org.wzeiri.zr.zrtaxiplatform.bean.WeChatPayBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author k-lm on 2017/12/14.
 */

public interface IOilCard {
    /**
     * GET /Api/App/OilCard/GetOilCards
     * 获取用户的充值油卡
     *
     * @return
     */
    @GET("/Api/App/OilCard/GetOilCards")
    Call<BaseBean<BaseListBean<OilCardBean>>> getOilCards(@Query("PageSize") int pageSize,
                                                          @Query("Page") int page);

    /**
     * GET /Api/App/OilCard/GetOilRechargeTemps
     * 根据油卡的类型,获取充值的档位
     *
     * @param oilCardType 油卡类型
     * @return
     */
    @GET("/Api/App/OilCard/GetOilRechargeTemps")
    Call<BaseBean<List<OilRechargeTempBean>>> getOilRechargeTemps(@Query("oilCardType") int oilCardType);

    /**
     * POST /Api/App/OilCard/CreateOrUpdateOilCard
     * 创建或更新加油卡
     *
     * @param body
     * @return
     */
    @POST("/Api/App/OilCard/CreateOrUpdateOilCard")
    Call<BaseBean<String>> createOrUpdateOilCard(@Body RequestBody body);

    /**
     * POST /Api/App/OilCard/DeleteOilCard
     * 删除加油卡
     *
     * @return
     */
    @POST("/Api/App/OilCard/DeleteOilCard")
    Call<BaseBean<String>> deleteOilCard();

    /**
     * GET /Api/App/OilCard/GetOilCardRechargeSelect
     * 返回该类型下的加油卡信息及充值信息
     *
     * @param type
     * @return
     */
    @GET("/Api/App/OilCard/GetOilCardRechargeSelect")
    Call<BaseBean<RechargeSelectBean>> getOilCardRechargeSelect(@Query("oilCardType") int type);

    /**
     * GET /Api/App/OilCard/GetOilCardRechargeSelect
     * 返回默认类型下的加油卡信息及充值信息
     *
     * @return
     */
    @GET("/Api/App/OilCard/GetOilCardRechargeSelect")
    Call<BaseBean<RechargeSelectBean>> getOilCardRechargeSelect();


    /**
     * POST /Api/App/OilCard/CreateOilCardRecharge
     * 油卡充值,返回支付宝或者微信需要的数据 微信会返回各个参数,支付宝直接返回签名过后的字符串(能直接进行提交的)
     *
     * @param body paymentType (integer, optional): 支付类型,1-微信支付,2-支付宝 ,
     *             oilCardId (integer, optional): 充值油卡Id ,
     *             oilRechargeTempId (integer, optional): 油卡充值档位(充值模板)Id
     * @return
     */
    @POST("/Api/App/OilCard/CreateOilCardRecharge")
    Call<BaseBean<WeChatPayBean>> createOilCardRechargeWeChatPay(@Body RequestBody body);

    /**
     * POST /Api/App/OilCard/CreateOilCardRecharge
     * 油卡充值,返回支付宝或者微信需要的数据 微信会返回各个参数,支付宝直接返回签名过后的字符串(能直接进行提交的)
     *
     * @param body paymentType (integer, optional): 支付类型,1-微信支付,2-支付宝 ,
     *             oilCardId (integer, optional): 充值油卡Id ,
     *             oilRechargeTempId (integer, optional): 油卡充值档位(充值模板)Id
     * @return
     */
    @POST("/Api/App/OilCard/CreateOilCardRecharge")
    Call<BaseBean<String>> createOilCardRechargeALiPay(@Body RequestBody body);


    /**
     * GET /Api/App/OilCard/GetOilCardRecharges
     * 获取油卡充值记录
     *
     * @param pageSize
     * @param page
     * @return
     */
    @GET("/Api/App/OilCard/GetOilCardRecharges")
    Call<BaseBean<BaseListBean<OilCardRechargeBean>>> getOilCardRecharges(@Query("PageSize") int pageSize,
                                                                          @Query("Page") int page);


}
