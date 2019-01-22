package org.wzeiri.zr.zrtaxiplatform.network.update;

import android.content.Context;
import android.widget.Toast;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.common.Config;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IDownload;
import org.wzeiri.zr.zrtaxiplatform.util.ThreadSwitch;
import org.wzeiri.zr.zrtaxiplatform.widget.dialog.ProgressDialog;

import java.io.File;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author k-lm on 2018/1/31.
 */

public class DownloadHelper {

    private IDownload mIDownload;
    private Context mContext;

    private ProgressDialog mProgressDialog;

    private OnDownloadListener mDownloadListener;
    /**
     * 是否开始下载
     */
    private boolean isStartDownload = false;

    public DownloadHelper(Context context) {
        mContext = context;
    }

    /**
     * 下载文件
     *
     * @param downloadUrl 文件下载地址
     * @param savePath    文件的保存地址
     */
    public void downloadFile(String downloadUrl, final String savePath) {
        // 暂时先定义成单文件下载 , 正在下载时无法进行下一个文件的下载
        if (isStartDownload) {
            return;
        }

        if (mIDownload == null) {
            mIDownload = DownloadRetrofitHelper.create(IDownload.class);
        }

        DownloadRetrofitHelper.setOnProgressListener(new ProgressListener() {
            @Override
            public void onProgress(long progress, long total, boolean done) {
                if (!isStartDownload) {
                    if (mDownloadListener != null) {
                        mDownloadListener.onStartDownload();
                    }
                    isStartDownload = true;
                }

                showDownloadProgressDialog(progress, total);

                if (done) {
                    isStartDownload = false;
                }

                // 下载完成后关闭对话框
                if (done && mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
            }
        });

        mIDownload.downloadBigFile(downloadUrl)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        ResponseBody body = response.body();
                        if (body == null) {
                            mDownloadListener.onError("下载失败请重试");
                            return;
                        }
                        saveFile(body, savePath);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        showToast("下载失败请重试");
                    }
                });

    }

    /**
     * 保存文件
     *
     * @param body 文件下载返回的body
     * @param path 文件保存路径
     */
    private void saveFile(final ResponseBody body, final String path) {

        ThreadSwitch.createTask(new ThreadSwitch.OnCreateListener<Boolean>() {
            @Override
            public Boolean onCreate(ThreadSwitch threadSwitch) {
                return DownloadRetrofitHelper.writeResponseBodyToDisk(body, path);
            }
        })
                .switchLooper(ThreadSwitch.MAIN_THREAD)
                .submit(new ThreadSwitch.OnSubmitListener<Boolean>() {
                    @Override
                    public void onSubmit(ThreadSwitch threadSwitch, Boolean value) {
                        if (mDownloadListener == null) {
                            return;
                        }

                        if (value) {
                            mDownloadListener.onEndDownload(path);
                        } else {
                            mDownloadListener.onError("文件保存失败");
                        }
                    }
                });
    }


    /**
     * 显示进度条对话框
     *
     * @param progress 已经下载或上传字节数
     * @param total    总字节数
     */
    private void showDownloadProgressDialog(long progress, long total) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext, R.style.NoTitleDialog);
        }

        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }

        int percentage = (int) (progress * 1.0 / total * 100);
        String percentageStr = "已下载" + percentage + "%";
        mProgressDialog.setProgress(percentage);
        mProgressDialog.setProgress(percentageStr);


    }

    /**
     * 设置回调
     *
     * @param listener 回调
     */
    public void setOnDownloadListener(OnDownloadListener listener) {
        mDownloadListener = listener;
    }

    /**
     * 土司
     *
     * @param msg
     */
    private void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }


    public interface OnDownloadListener {
        void onStartDownload();

        void onEndDownload(String path);

        void onError(String msg);
    }

}
