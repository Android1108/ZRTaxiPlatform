package org.wzeiri.zr.zrtaxiplatform.wxapi;


import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.wzeiri.zr.zrtaxiplatform.common.Config;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseActivity;
import org.wzeiri.zr.zrtaxiplatform.util.PayUtil;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";
    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(getThis(), Config.WECHAT_APPID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        int result = 0;
        Message message = new Message();
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                //即为所需的code
                /*String code = ((SendAuth.Resp) resp).code;
                //如果code值不为空，则说明是微信登录
                if (TextUtils.isEmpty(code)) {

                } else {
                    //传入数据
                    message.obj = code;
                }*/
                message.what = PayUtil.SUCCESS;
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                message.what = PayUtil.CANCEL;
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                message.what = PayUtil.DENIED;
                break;
            default:
                break;
        }
        if (PayUtil.payHandler != null) {
            PayUtil.payHandler.sendMessage(message);
        }
        finish();

    }


}