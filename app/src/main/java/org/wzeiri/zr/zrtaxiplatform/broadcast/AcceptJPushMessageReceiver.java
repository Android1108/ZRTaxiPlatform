package org.wzeiri.zr.zrtaxiplatform.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.wzeiri.zr.zrtaxiplatform.bean.DriverStatusBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IUser;
import org.wzeiri.zr.zrtaxiplatform.network.callback.BaseCallBack;
import org.wzeiri.zr.zrtaxiplatform.util.LogUtil;
import org.wzeiri.zr.zrtaxiplatform.util.UserInfoHelper;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author k-lm on 2018/2/11.
 */

public class AcceptJPushMessageReceiver extends BaseJPushBroadcastReceiver {

    @Override
    protected void onReceivedNotification(Context context, Intent intent, Bundle bundle) {
        super.onReceivedNotification(context, intent, bundle);
        checkAuthentication(bundle);
    }

    /**
     * 检查是否认证
     *
     * @param bundle
     */
    private void checkAuthentication(Bundle bundle) {
        if (bundle == null) {
            return;
        }
        String extraExtraStr = bundle.getString(JPushInterface.EXTRA_EXTRA);
        try {
            JSONObject jsonObject = new JSONObject(extraExtraStr);
            String code = jsonObject.getString("Code");
            if (!TextUtils.equals("AuditBindingDriverRequest", code)) {
                return;
            }

            String value = jsonObject.getString("CodeValue");
            // 认证成功
            if (TextUtils.equals(value, "True") &&
                    UserInfoHelper.getInstance().isLogin()) {
                checkAuthentication();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过接口获取是否认证
     */
    private void checkAuthentication() {
        IUser iUser = RetrofitHelper.create(IUser.class);
        iUser.getDriverStatus()
                .enqueue(new BaseCallBack<BaseBean<DriverStatusBean>>() {
                    @Override
                    public void onSuccess(Call<BaseBean<DriverStatusBean>> call, Response<BaseBean<DriverStatusBean>> response) {
                        DriverStatusBean bean = response.body().getResult();
                        if (bean == null) {
                            return;
                        }
                        if (bean.getDriverStatus() == UserInfoHelper.AUTHENTICATION_CERTIFIED) {
                            UserInfoHelper.getInstance().setDriverStatus(bean.getDriverStatus());
                            UserInfoHelper.getInstance().setTenantId(bean.getTenantId());
                        }
                    }

                    @Override
                    public void onError(Call<BaseBean<DriverStatusBean>> call, Throwable t) {

                    }
                });
    }


}
