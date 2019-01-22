package org.wzeiri.zr.zrtaxiplatform.network;

import android.text.TextUtils;

import org.wzeiri.zr.zrtaxiplatform.common.Config;
import org.wzeiri.zr.zrtaxiplatform.util.LogUtil;
import org.wzeiri.zr.zrtaxiplatform.util.UserInfoHelper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;

/**
 * @author k-lm on 2017/11/14.
 */

public class LogInterceptor implements Interceptor {
    private static final String TAG = "LogInterceptor";

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Request newRequest;
        Request.Builder builder = request.newBuilder();

        String token = UserInfoHelper.getInstance().getAccessToken();
        if (!TextUtils.isEmpty(token)) {
            token = "Bearer " + token;
            builder.header("Authorization", token);
        }

        newRequest = builder
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build();

        String url = newRequest.url().toString();
        String header = newRequest.headers().toString();
        okhttp3.Response response = chain.proceed(newRequest);
        okhttp3.MediaType mediaType = response.body().contentType();
        String content = response.body().string();
        if (Config.DEBUG) {
            if (!newRequest.method().equals("GET")) {
                String requestContent = bodyToString(newRequest.body());
                LogUtil.d(TAG, "url: " + url + "\n" + "header: " + header + "\n" + "request body: " + requestContent
                        + "\n" + "response body: " + content);
            } else {
                LogUtil.d(TAG, "url: " + url + "\n" + "header: " + header + "\n" + "response body: " + content);
            }
        }

        return response.newBuilder()
                .body(okhttp3.ResponseBody.create(mediaType, content))
                .build();

    }

    private String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            copy.writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }


    /**
     * URL编码转义
     *
     * @param strUrl
     * @return
     */
    public static String getUrlEncode(String strUrl) {
        try {
            return URLEncoder.encode(strUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        return strUrl.replace("+", "%2b").replace(" ", "%20").replace("=", "%3d").replace("&", "%26");
    }
}
