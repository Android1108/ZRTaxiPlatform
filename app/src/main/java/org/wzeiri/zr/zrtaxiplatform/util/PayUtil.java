package org.wzeiri.zr.zrtaxiplatform.util;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import java.util.Map;


/**
 * Created by klm on 2017/2/7.
 */

public class PayUtil {
    /**
     * 成功
     */
    public static final int SUCCESS = 1;
    /**
     * 失败
     */
    public static final int DENIED = -1;
    /**
     * 取消
     */
    public static final int CANCEL = -2;
    /**
     * 支付宝
     */
    public static final int ALPAY = 100;
    /**
     * 微信支付
     */
    public static final int WECHAT_PAY = 100;

    private static final String APPID = "2016111802948296";
    /**
     * 支付使用的hander
     */
    public static Handler payHandler = null;

    /**
     * 微信支付
     *
     * @param iwxapi
     * @return 返回false 表示未安装微信或不支持当前微信版本
     */
    public static boolean wechatPay(IWXAPI iwxapi, String appId, String partnerId,
                                    String prepayId, String packageValue, String nonceStr,
                                    String timeStamp, String sign) {
        // 判断微信版本及是否安装
        if (!iwxapi.isWXAppInstalled() || !iwxapi.isWXAppSupportAPI()) {
            return false;
        }


        PayReq request = new PayReq();
        request.appId = appId;
        request.partnerId = partnerId;
        request.prepayId = prepayId;
        request.packageValue = packageValue;
        request.nonceStr = nonceStr;
        request.timeStamp = timeStamp;
        request.sign = sign;
        iwxapi.sendReq(request);
        return true;
    }


    /**
     * 支付宝支付
     *
     * @param activity
     * @param orderInfo 签名信息
     */
    public static void alpay(final Activity activity, final String orderInfo) {
        String o = orderInfo;
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                PayResult payResult = new PayResult((Map<String, String>) result);
                String resultStatus = payResult.getResultStatus();
                int what;
                if (TextUtils.equals(resultStatus, "9000")) {
                    what = PayUtil.SUCCESS;
                } else if (TextUtils.equals(resultStatus, "6001")) {
                    what = PayUtil.CANCEL;
                } else {
                    what = PayUtil.DENIED;
                }
                Message msg = new Message();
                msg.what = what;
                msg.obj = result;
                if (payHandler != null) {
                    payHandler.sendMessage(msg);
                }
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    public static class PayResult {
        private String resultStatus;
        private String result;
        private String memo;

        public PayResult(Map<String, String> rawResult) {
            if (rawResult == null) {
                return;
            }

            for (String key : rawResult.keySet()) {
                if (TextUtils.equals(key, "resultStatus")) {
                    resultStatus = rawResult.get(key);
                } else if (TextUtils.equals(key, "result")) {
                    result = rawResult.get(key);
                } else if (TextUtils.equals(key, "memo")) {
                    memo = rawResult.get(key);
                }
            }
        }

        @Override
        public String toString() {
            return "resultStatus={" + resultStatus + "};memo={" + memo
                    + "};result={" + result + "}";
        }

        /**
         * @return the resultStatus
         */
        public String getResultStatus() {
            return resultStatus;
        }

        /**
         * @return the memo
         */
        public String getMemo() {
            return memo;
        }

        /**
         * @return the result
         */
        public String getResult() {
            return result;
        }
    }


}
