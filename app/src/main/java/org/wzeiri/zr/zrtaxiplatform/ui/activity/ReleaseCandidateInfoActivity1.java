package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.UploadResultBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.common.Config;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IJobRecruitment;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItem;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.service.BaiduLocationService;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.util.BitmapUtil;
import org.wzeiri.zr.zrtaxiplatform.util.EditTextFormatUtil;
import org.wzeiri.zr.zrtaxiplatform.util.PhotoHelper;
import org.wzeiri.zr.zrtaxiplatform.util.ThreadSwitch;
import org.wzeiri.zr.zrtaxiplatform.util.UploadPictureHelper;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueEditText;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;
import org.wzeiri.zr.zrtaxiplatform.widget.flow.FlowLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 发布求职信息
 *
 * @author k-lm on 2017/11/23.
 */

public class ReleaseCandidateInfoActivity1 extends ActionbarActivity implements PhotoHelper.OnPhotoUpdateListener {


    @BindView(R.id.aty_release_job_info_edit_title)
    EditText mEditTitle;
    @BindView(R.id.aty_release_job_info_edit_content)
    EditText mEditContent;
    @BindView(R.id.aty_release_job_info_fl_layout)
    FlowLayout mFlLayout;
    @BindView(R.id.aty_release_job_info_vet_phone)
    ValueEditText mVetPhone;
    @BindView(R.id.aty_release_job_info_vtv_job_region)
    ValueTextView mVtvJobRegion;

    private PhotoHelper mPhotoHelper;

    private IJobRecruitment mIJobRecruitment;
    /**
     * 地址
     */
    private String mAddress = "";
    /**
     * 城市
     */
    private String mCityCode = "";
    /**
     * 地区
     */
    private String mAreaCode = "";
    /**
     * 省份
     */
    private String mProvinceCode = "";

    private UploadPictureHelper mUploadHelper;

    private List<Bitmap> mBitmapList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_release_job_info_content;
    }


    @Override
    protected void init() {
        super.init();
        setCenterTitle("发布求职信息");
        mPhotoHelper = new PhotoHelper(this, mFlLayout, 1, Config.STORAGE_PATH);
        mPhotoHelper.setOnPhotoUpdateListener(this);

        EditTextFormatUtil.formatRemoveEmoji(mEditContent);
        EditTextFormatUtil.formatRemoveEmoji(mEditTitle);

        BaiduLocationService.start(getThis(), new BaiduLocationService.OnLocationListener() {
            @Override
            public void onReceiveLocation() {
                String address = BaiduLocationService.getDistrict() + " "
                        + BaiduLocationService.getCity() + " "
                        + BaiduLocationService.getDistrict();
                mVtvJobRegion.setText(address);
            }

            @Override
            public void onError() {

            }
        });

        mUploadHelper = new UploadPictureHelper(getThis());
        mUploadHelper.setOnUploadListener(new UploadPictureHelper.OnUploadListener() {
            @Override
            public void onUploadSuccess(@NonNull List<UploadResultBean> beans) {
                closeProgressDialog();

                List<String> list = new ArrayList<>();
                for (UploadResultBean bean : beans) {
                    if (bean == null) {
                        continue;
                    }
                    list.add(bean.getUrl());
                }
                submit(list);
            }

            @Override
            public void onUploadError(String msg) {
                closeProgressDialog();
                mPhotoHelper.removeLastPhoto();
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPhotoHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BaiduLocationService.onRequestPermissionsResult(getThis(), requestCode, permissions, grantResults);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPhotoHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        mPhotoHelper.onDestroy();
        BaiduLocationService.stop(getThis());
        super.onDestroy();
    }

    @Override
    public void onStartCameraOrAlbum(boolean isSuccess) {

    }

    @Override
    public void onResult(boolean isSuccess) {
        if (isSuccess) {
            showProgressDialog("正在处理图片，请稍候");
        }
    }

    @Override
    public void onResultBitmap(final Bitmap bitmap) {
        closeProgressDialog();
        if (bitmap == null) {
            showToast("图片获取失败，请重试");
            return;
        }
        mBitmapList.add(bitmap);
        mPhotoHelper.addPhoto(bitmap);
    }


    /**
     * 保存所有文件，并上传到服务器上
     */
    private void saveFiles() {
        showProgressDialog();
        if (mBitmapList.size() == 0) {
            submit(new ArrayList<String>());
            return;
        }
        // 保存文件
        ThreadSwitch.createTask(new ThreadSwitch.OnCreateListener<List<File>>() {
            @Override
            public List<File> onCreate(ThreadSwitch threadSwitch) {
                List<File> files = new ArrayList<>();
                int count = mBitmapList.size();
                for (int i = 0; i < count; i++) {
                    Bitmap bitmap = mBitmapList.get(i);
                    BitmapUtil.saveBitmap(Config.STORAGE_PATH, i + Config.UPLOAD_IMAGE_NAME, bitmap, 100);
                    File file = new File(Config.STORAGE_PATH, i + Config.UPLOAD_IMAGE_NAME);
                    if (!file.exists()) {
                        continue;
                    }
                    files.add(file);
                }

                return files;
            }
        })
                .switchLooper(ThreadSwitch.MAIN_THREAD)
                .submit(new ThreadSwitch.OnSubmitListener<List<File>>() {
                    @Override
                    public void onSubmit(ThreadSwitch threadSwitch, List<File> value) {
                        if (value == null || value.size() == 0) {
                            closeProgressDialog();
                            showToast("图片上传失败，请重试");
                            return;
                        }

                        mUploadHelper.uploadFiles(value);
                    }
                });
    }

    @OnClick(R.id.aty_release_job_info_vtv_job_region)
    public void onmVtvJobRegionClicked() {
    }

    @OnClick(R.id.layout_release_info_text_release)
    public void onLayoutReleaseInfoTextReleaseClicked() {
        String title = mEditTitle.getText().toString().trim();
        String content = mEditContent.getText().toString().trim();
        String phone = mVetPhone.getText().toString().trim();
        String region = mVtvJobRegion.getText().toString().trim();


        if (TextUtils.isEmpty(title)) {
            showToast("请输入标题");
            return;
        }

        if (TextUtils.isEmpty(content)) {
            showToast("请输入描述信息");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            showToast("请输入联系电话");
            return;
        }

        if (phone.length() != 11) {
            showToast("请输入正确的联系电话");
            return;
        }

        /*if (TextUtils.isEmpty(region)) {
            showToast("请选择求职地区");
            return;
        }*/

        saveFiles();
    }

    /**
     * 提交信息
     *
     * @param coverPictures 图片的url
     */
    private void submit(@Nullable List<String> coverPictures) {


        String title = mEditTitle.getText().toString().trim();
        String content = mEditContent.getText().toString().trim();
        String phone = mVetPhone.getText().toString().trim();
        String region = mVtvJobRegion.getText().toString().trim();


        if (mIJobRecruitment == null) {
            mIJobRecruitment = RetrofitHelper.create(IJobRecruitment.class);
        }

        mIJobRecruitment.createJobRecruitment(RetrofitHelper
                .getBody(new JsonItem("title", title),
                        new JsonItem("content", content),
                        new JsonItem("contact", phone),
                        new JsonItem("coverPicture", coverPictures),
                        new JsonItem("address", region)))
                .enqueue(new MsgCallBack<BaseBean<String>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                        showToast("提交成功");
                        finish();
                    }
                });
    }

}
