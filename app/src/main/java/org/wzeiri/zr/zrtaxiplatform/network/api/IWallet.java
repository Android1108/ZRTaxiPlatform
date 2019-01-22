package org.wzeiri.zr.zrtaxiplatform.network.api;

import org.wzeiri.zr.zrtaxiplatform.bean.WalletDetailBean;
import org.wzeiri.zr.zrtaxiplatform.bean.WalletNoteBean;
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
 * @author k-lm on 2017/12/13.
 */

public interface IWallet {
    /**
     * GET /Api/App/Wallet/GetWalletNotes
     * 获取余额明细
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GET("/Api/App/Wallet/GetWalletNotes")
    Call<BaseBean<List<WalletNoteBean>>> getWalletNotes(@Query("pageIndex") int pageIndex,
                                                        @Query("pageSize") int pageSize);

    /**
     * GET /Api/App/Wallet/GetWalletDetails
     * 获取钱包详情
     *
     * @return
     */
    @GET("/Api/App/Wallet/GetWalletDetails")
    Call<BaseBean<WalletDetailBean>> getWalletDetails();

    /**
     * POST /Api/App/Wallet/BindingZhifubao
     * 绑定支付宝
     *
     * @param body
     * @return
     */
    @POST("/Api/App/Wallet/BindingZhifubao")
    Call<BaseBean<String>> bindingZhifubao(@Body RequestBody body);


    /**
     * POST /Api/App/Wallet/BindingBankCard
     * 绑定银行卡
     *
     * @param body
     * @return
     */
    @POST("/Api/App/Wallet/BindingBankCard")
    Call<BaseBean<String>> bindingBankCard(@Body RequestBody body);

    /**
     * POST /Api/App/Wallet/RequestCash
     * 申请提现
     *
     * @param body
     * @return
     */
    @POST("/Api/App/Wallet/RequestCash")
    Call<BaseBean<String>> requestCash(@Body RequestBody body);
}
