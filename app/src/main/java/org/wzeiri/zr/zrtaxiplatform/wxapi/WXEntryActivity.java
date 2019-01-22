package org.wzeiri.zr.zrtaxiplatform.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.wzeiri.zr.zrtaxiplatform.common.Config;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseActivity;

/**
 * Created by klm on 2017/1/13.
 */

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    private IWXAPI api = WXAPIFactory.createWXAPI(this, Config.WECHAT_APPID);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        api.handleIntent(intent, this);
    }

    /**
     * 响应
     *
     * @param resp
     */
    @Override
    public void onResp(BaseResp resp) {
       /* int result = 0;
        Message message = new Message();
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                //即为所需的code
                *//*String code = ((SendAuth.Resp) resp).code;
                Logger.d(code);*//*
                //如果code值不为空，则说明是微信登录
                *//*if(TextUtils.isEmpty(code)){

                }else{
                    //传入数据
                    message.obj = code;
                }*//*
                message.what = ShareUtils.SHARE_SUCCESS;
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                message.what = ShareUtils.SHARE_CANCEL;
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                message.what = ShareUtils.SHARE_DENIED;
                break;
            default:
                break;
        }
        if (ShareUtils.sharHandler != null) {
            ShareUtils.sharHandler.sendMessage(message);
        }*/
        finish();
    }


    /**
     * 请求
     *
     * @param arg0
     */
    @Override
    public void onReq(BaseReq arg0) {

    }

}
