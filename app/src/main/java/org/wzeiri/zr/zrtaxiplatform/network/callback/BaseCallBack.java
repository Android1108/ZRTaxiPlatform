package org.wzeiri.zr.zrtaxiplatform.network.callback;


import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.wzeiri.zr.zrtaxiplatform.MyApplication;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.ErrorBean;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.base.BaseFragment;
import org.wzeiri.zr.zrtaxiplatform.util.LogUtil;
import org.wzeiri.zr.zrtaxiplatform.util.NetWorkHelp;
import org.wzeiri.zr.zrtaxiplatform.util.UserInfoHelper;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author klm on 2017/9/25.
 */

public abstract class BaseCallBack<T extends BaseBean> implements Callback<T> {


    @Override
    public void onResponse(Call<T> call, Response<T> response) {

        T t = response.body();

        okhttp3.Response errorResponse = response.raw();
        // 登录失效
        if (errorResponse != null && errorResponse.code() == 401) {
            onError(call, new Throwable("登录信息已失效，请重新登录"), errorResponse.code());
            UserInfoHelper.getInstance().loginInvalid();
            return;
        }


        if (t == null) {

            try {
                ResponseBody errorBody = response.errorBody();
                if (errorBody != null) {
                    String errorStr = errorBody.string();
                    if (!TextUtils.isEmpty(errorStr)) {
                        Gson gson = new Gson();
                        BaseBean baseBean = gson.fromJson(errorStr, BaseBean.class);
                        if (baseBean != null) {
                            ErrorBean errorBean = baseBean.getError();
                            if (errorBean != null) {
                                String message = errorBean.getMessage();
//                                String detail = errorBean.getDetails();
                                String detail = null;
                                if (message == null) {
                                    message = "";
                                }

                                if (detail == null) {
                                    detail = "";
                                } else {
                                    detail = " : " + detail;
                                }
                                onError(call, new Throwable(message + detail), errorResponse.code());
                                return;
                            }
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }


            onError(call, new Throwable("无法获取服务器数据"));
            return;
        }
        if (!t.isSuccess()) {
            ErrorBean errorBean = t.getError();
            if (errorBean == null) {
                onError(call, new Throwable("无法获取服务器数据"));
            } else {
                onError(call, new Throwable(errorBean.getMessage()));
            }
            return;
        } else {
            Object object = t.getResult();
            if (object == null) {
                onError(call, new Throwable("无法获取服务器数据"));
                return;
            }
        }
        onSuccess(call, response);
    }

    @Override
    publicy void onFailure(Call<T> call, Throwable t) {
       /* String errorMsg = t.getMessage();
        if (errorMsg.contains("failed to connect to")) {
            onError(call, new Throwable("连接超时，请检查是否连接网络"));
            return;
        }

        onError(call, t);*/
        LogUtil.d(t.getMessage());
        boolean isConnectNetWord = NetWorkHelp.getInstance().isNetworkConnected(MyApplication.getApplication());

        if (!isConnectNetWord) {
            onError(call, new Throwable("当前网络不可用"));
            return;
        }

        onError(call, new Throwable("服务器连接异常，请重试"));
    }

    public abstract void onSuccess(Call<T> call, Response<T> response);

    public abstract void onError(Call<T> call, Throwable t);

    public void onError(Call<T> call, Throwable t, int code) {
        onError(call, t);
    }
}
