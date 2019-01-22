package org.wzeiri.zr.zrtaxiplatform.network.update;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.google.gson.GsonBuilder;

import org.wzeiri.zr.zrtaxiplatform.common.Config;
import org.wzeiri.zr.zrtaxiplatform.network.LogInterceptor;
import org.wzeiri.zr.zrtaxiplatform.network.LoginInterceptor;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.util.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author k-lm on 2018/1/26.
 */

public class DownloadRetrofitHelper {

    private static final String KEY_PROGRESS = "progress";
    private static final String KEY_TOTAL = "total";
    private static final String KEY_DONE = "done";

    private ProgressListener mProgressListener;

    private ProgressListener mOnProgressListener;

    private static DownloadRetrofitHelper mHelp;

    private Retrofit mRetrofit;

    public static DownloadRetrofitHelper getInstance() {
        if (mHelp == null) {
            mHelp = new DownloadRetrofitHelper();
        }
        return mHelp;
    }

    /**
     * 返回retrofit 实例
     *
     * @return
     */
    public Retrofit getRetrofit() {
        if (mRetrofit == null) {
            synchronized (RetrofitHelper.class) {
                if (mRetrofit == null) {
                    OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(10, TimeUnit.SECONDS);


                    Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                            .baseUrl(Config.MOBILE_SERVER_RELEASE)
                            .addConverterFactory(
                                    GsonConverterFactory.create(
                                            new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS").create()
                                    )
                            );

                    retrofitMachining(clientBuilder, retrofitBuilder);
                    mRetrofit = retrofitBuilder.client(clientBuilder.build())
                            .build();

                }
            }
        }

        return mRetrofit;
    }


    /**
     * 用于子类加工 retrofit
     *
     * @param clientBuilder
     * @param retrofitBuilder
     */
    protected void retrofitMachining(OkHttpClient.Builder clientBuilder, Retrofit.Builder retrofitBuilder) {
        mProgressListener = new ProgressListener() {
            @Override
            public void onProgress(long progress, long total, boolean done) {
                if (mOnProgressListener == null) {
                    return;
                }

                //该方法在子线程中运行
                Bundle bundle = new Bundle();
                bundle.putLong(KEY_PROGRESS, progress);
                bundle.putLong(KEY_TOTAL, total);
                bundle.putBoolean(KEY_DONE, done);
                Message message = new Message();
                message.obj = mOnProgressListener;
                message.setData(bundle);
                mHandler.sendMessage(message);
                // 完成后，将回调设置为空
                if (done) {
                    mOnProgressListener = null;
                }
            }
        };


        clientBuilder.networkInterceptors()
                .add(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Response originalResponse = chain.proceed(chain.request());
                        return originalResponse.newBuilder().body(
                                new ProgressResponseBody(originalResponse.body(), mProgressListener))
                                .build();

                    }
                });

    }


    private static Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg == null || msg.obj == null) {
                return;
            }
            Bundle bundle = msg.getData();
            long progress = bundle.getLong(KEY_PROGRESS, 0);
            long total = bundle.getLong(KEY_TOTAL, 0);
            boolean done = bundle.getBoolean(KEY_DONE, false);

            ProgressListener listener = (ProgressListener) msg.obj;
            listener.onProgress(progress, total, done);
        }
    };

    /**
     * 设置进度回调</p>
     * 每次完成一个下载任务，会将回调设置为null,因此下载完后，需要再次下载需要重新设置回调内容
     *
     * @param listener 进度回调
     */
    public static void setOnProgressListener(ProgressListener listener) {
        getInstance().mOnProgressListener = listener;
    }

    /**
     * 创建网络接口
     *
     * @param service 接口class类
     * @param <T>     接口类
     * @return 接口类实例
     */
    public static <T> T create(Class<T> service) {
        return getInstance().getRetrofit().create(service);
    }

    /**
     * 将下载好的内容存储到本地
     *
     * @param body        下载接口返回的内容
     * @param storagePath 存储路径
     * @return
     */
    public static boolean writeResponseBodyToDisk(ResponseBody body, String storagePath) {
        try {
            if (body == null || TextUtils.isEmpty(storagePath)) {
                return false;
            }

            File futureStudioIconFile = new File(storagePath);

            File parentFile = futureStudioIconFile.getParentFile();
            // 如果没有创建文件夹，则创建一个文件夹
            if (parentFile != null && !parentFile.exists()) {
                parentFile.mkdirs();
            }

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);
                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                }
                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

}
