package org.wzeiri.zr.zrtaxiplatform.network;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.wzeiri.zr.zrtaxiplatform.util.UserInfoHelper;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author klm on 2017/9/27.
 */

public class LoginInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        //添加请求头
//        Request.Builder requestBuilder = request.newBuilder().header("APIKEY", "XXXXX");

        Request.Builder deviceRequestBuilder = request
                .newBuilder();

        String token = UserInfoHelper.getInstance().getAccessToken();
        if (!TextUtils.isEmpty(token)) {
            deviceRequestBuilder.header("Authorization", "Bearer " + token);
        }

        deviceRequestBuilder.header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .method(request.method(), request.body());

        okhttp3.Response response = chain.proceed(deviceRequestBuilder.build());


        okhttp3.MediaType mediaType = response.body().contentType();
        String content = response.body().string();

        return response.newBuilder()
                .body(okhttp3.ResponseBody.create(mediaType, content))
                .build();
    }
}
