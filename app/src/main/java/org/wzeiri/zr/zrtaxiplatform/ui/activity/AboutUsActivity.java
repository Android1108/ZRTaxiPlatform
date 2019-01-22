package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.BuildConfig;
import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.VersionBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.common.Config;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IDownload;
import org.wzeiri.zr.zrtaxiplatform.network.api.ISundry;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItem;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.network.update.DownloadHelper;
import org.wzeiri.zr.zrtaxiplatform.network.update.DownloadRetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.update.ProgressListener;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.util.UriUtil;
import org.wzeiri.zr.zrtaxiplatform.widget.dialog.ProgressDialog;
import org.wzeiri.zr.zrtaxiplatform.widget.dialog.UpdateDialog;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 关于我们
 *
 * @author k-lm on 2017/12/20.
 */

public class AboutUsActivity extends ActionbarActivity {
    @BindView(R.id.aty_about_us_text_edition)
    TextView mEdition;

    private VersionBean mVersionBean = null;

    private UpdateDialog mUpdateDialog;

    private DownloadHelper mDownloadHelper;

    @OnClick(R.id.aty_about_us_text_edition)
    void versionUpdate() {
        if (mVersionBean == null || !mVersionBean.isHasUpdate()) {
            return;
        }
        showUpdateDialog();

    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("关于我们");
        mEdition.append(BuildConfig.VERSION_NAME);
    }

    @Override
    protected void initData() {
        super.initData();
        checkVersion();
    }

    /**
     * 更改成版本更新界面
     */
    private void changeUpdateUI(String versionName) {
        mEdition.setText("发现新版本 v");
        mEdition.setBackgroundResource(R.drawable.bg_fillet_rectangle_frame_orange_100);
        mEdition.setTextColor(ContextCompat.getColor(getThis(), R.color.orange1));
        mEdition.append(versionName);
    }

    /**
     * 检测版本
     */
    private void checkVersion() {
        ISundry iSundry = RetrofitHelper.create(ISundry.class);

        iSundry.versionCheck(
                RetrofitHelper.getBody(new JsonItem("clientVersionType", 20),
                        new JsonItem("versionCode", BuildConfig.VERSION_NAME))
        ).enqueue(new MsgCallBack<BaseBean<VersionBean>>(getThis()) {
            @Override
            public void onSuccess(Call<BaseBean<VersionBean>> call, Response<BaseBean<VersionBean>> response) {
                VersionBean bean = response.body().getResult();
                if (bean == null) {
                    return;
                }
                mVersionBean = bean;
                if (bean.isHasUpdate()) {
                    changeUpdateUI(mVersionBean.getVersionName());
                }

            }
        });
    }

    /**
     * 显示更新对话框
     */
    private void showUpdateDialog() {
        if (mVersionBean == null || !mVersionBean.isHasUpdate()) {
            return;
        }
        if (mUpdateDialog == null) {
            mUpdateDialog = new UpdateDialog(getThis());
            mUpdateDialog.setTitle_("发现新版本V" + mVersionBean.getVersionCode());
            mUpdateDialog.setMessage_(mVersionBean.getDescribe());
            mUpdateDialog.setNegativeButton_(null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            mUpdateDialog.setPositiveButton_(null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    downloadApk();
                    dialog.dismiss();
                }
            });
        }


        mUpdateDialog.show();
    }


    /**
     * 下载apk
     */
    private void downloadApk() {
        if (mVersionBean == null) {
            return;
        }


        if (mDownloadHelper == null) {
            mDownloadHelper = new DownloadHelper(getThis());
        }
        mDownloadHelper.setOnDownloadListener(new DownloadHelper.OnDownloadListener() {
            @Override
            public void onStartDownload() {

            }

            @Override
            public void onEndDownload(String path) {
                openApk(path);
            }

            @Override
            public void onError(String msg) {
                showToast(msg);
            }
        });

        String apkPath = Config.STORAGE_PATH + File.separator + "apk" + File.separator
                + getString(R.string.app_name) + ".apk";
        mDownloadHelper.downloadFile(mVersionBean.getResourceUrl(), apkPath);

    }

    /**
     * 打开apk
     *
     * @param path
     */
    private void openApk(String path) {
        File file = new File(path);
        Uri uri = UriUtil.getUri(getThis(), getThis().getPackageName() + ".fileprovider", file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        if (mUpdateDialog != null && mUpdateDialog.isShowing()) {
            mUpdateDialog.dismiss();
        }
        super.onDestroy();
    }
}
