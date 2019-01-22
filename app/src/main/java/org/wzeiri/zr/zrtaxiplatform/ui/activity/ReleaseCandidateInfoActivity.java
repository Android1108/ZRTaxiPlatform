package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.EditText;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.LocationRegionBean;
import org.wzeiri.zr.zrtaxiplatform.bean.UploadResultBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.common.Config;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IJobRecruitment;
import org.wzeiri.zr.zrtaxiplatform.network.api.IUser;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItem;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItemArray;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.service.BaiduLocationService;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.util.BitmapUtil;
import org.wzeiri.zr.zrtaxiplatform.util.EditTextFormatUtil;
import org.wzeiri.zr.zrtaxiplatform.util.PhotoHelper;
import org.wzeiri.zr.zrtaxiplatform.util.ThreadSwitch;
import org.wzeiri.zr.zrtaxiplatform.util.UploadPictureHelper;
import org.wzeiri.zr.zrtaxiplatform.util.UserInfoHelper;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueEditText;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;
import org.wzeiri.zr.zrtaxiplatform.widget.flow.FlowLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 发布求职信息
 *
 * @author k-lm on 2017/11/23.
 */

public class ReleaseCandidateInfoActivity extends ActionbarActivity implements PhotoHelper.OnPhotoUpdateListener {

    /**
     * 发布求职信息
     */
    public static final int REQUEST_CODE = 30001;


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

    private List<String> mImages = new ArrayList<>(3);

    private UploadPictureHelper mUploadHelper;

    private Bitmap mBitmap;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_release_job_info_content;
    }


    @Override
    protected void init() {
        super.init();
        setCenterTitle("发布求职信息");
        mPhotoHelper = new PhotoHelper(this, mFlLayout, 3, Config.STORAGE_PATH);
        mPhotoHelper.setOnPhotoUpdateListener(this);

        EditTextFormatUtil.formatRemoveEmoji(mEditContent);
        EditTextFormatUtil.formatRemoveEmoji(mEditTitle);

        BaiduLocationService.start(getThis(), new BaiduLocationService.OnLocationListener() {
            @Override
            public void onReceiveLocation() {
                /*String address = BaiduLocationService.getDistrict() + " "
                        + BaiduLocationService.getCity() + " "
                        + BaiduLocationService.getDistrict();
                mVtvJobRegion.setText(address);*/

            }

            @Override
            public void onError() {

            }
        });

        mUploadHelper = new UploadPictureHelper(getThis());
        mUploadHelper.setOnUploadListener(new UploadPictureHelper.OnUploadListener() {
            @Override
            public void onUploadSuccess(@NonNull List<UploadResultBean> beans) {
                UploadResultBean bean = beans.get(0);
                if (bean == null) {
                    return;
                }
                closeProgressDialog();
                mPhotoHelper.addPhoto(mBitmap);
                mImages.add(bean.getFileName());
            }

            @Override
            public void onUploadError(String msg) {
                showToast("图片处理失败");
                closeProgressDialog();
                //mPhotoHelper.removeLastPhoto();
            }
        });

        mPhotoHelper.setOnPhotoOperationListener(new PhotoHelper.OnPhotoOperationListener() {
            @Override
            public void onPhotoDelete(int position) {
                mImages.remove(position);
            }

            @Override
            public void onPhotoClick(int position, Bitmap bitmap) {

            }
        });


    }


    @Override
    protected void initData() {
        super.initData();
        String phone = UserInfoHelper.getInstance().getPhone();
        if (TextUtils.isEmpty(phone)) {
            loadUserPhone();
        } else {
            mVetPhone.setText(phone);
        }


    }

    /**
     * 获取用户手机号
     */
    private void loadUserPhone() {
        IUser user = RetrofitHelper.create(IUser.class);

        user.getUserPhoneNumber()
                .enqueue(new MsgCallBack<BaseBean<String>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                        String phone = response.body().getResult();
                        UserInfoHelper.getInstance().savePhone(phone);
                        mVetPhone.setText(phone);
                    }

                    @Override
                    public void onError(Call<BaseBean<String>> call, Throwable t) {
                        super.onError(call, t);
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

        if (data == null || resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == SelectRegionActivity.REQUEST_CODE) {
            LocationRegionBean bean = SelectRegionActivity.loadRegionInfo(data);
            mProvinceCode = bean.getProvinceCode();
            mCityCode = bean.getCityCode();
            mAreaCode = bean.getAreaCode();

            String name = bean.getProvinceName() + " " + bean.getCityName() + " " + bean.getAreaName();
            mVtvJobRegion.setText(name);

        }

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
        if (bitmap == null) {
            closeProgressDialog();
            showToast("图片获取失败，请重试");
            return;
        }
        mBitmap = bitmap;
        // 保存文件
        ThreadSwitch.createTask(new ThreadSwitch.OnCreateListener<Boolean>() {
            @Override
            public Boolean onCreate(ThreadSwitch threadSwitch) {
                BitmapUtil.saveBitmap(Config.STORAGE_PATH, Config.UPLOAD_IMAGE_NAME, bitmap, 100);
                return true;
            }
        })
                .switchLooper(ThreadSwitch.MAIN_THREAD)
                .submit(new ThreadSwitch.OnSubmitListener<Boolean>() {
                    @Override
                    public void onSubmit(ThreadSwitch threadSwitch, Boolean value) {
                        File file = new File(Config.STORAGE_PATH + Config.UPLOAD_IMAGE_NAME);
                        if (!file.exists()) {
                            showToast("图片获取失败，请重试");
                            closeProgressDialog();
                            return;
                        }
                        mUploadHelper.removeAllUploadFile();
                        mUploadHelper.addUploadFile(file);
                        mUploadHelper.upload();
                    }
                });


    }

    @OnClick(R.id.aty_release_job_info_vtv_job_region)
    public void onmVtvJobRegionClicked() {
        SelectRegionActivity.start(getThis(), SelectRegionActivity.PROVINCE, SelectRegionActivity.REGION);
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

        if (TextUtils.isEmpty(region)) {
            showToast("请选择求职地区");
            return;
        }

        if (mIJobRecruitment == null) {
            mIJobRecruitment = RetrofitHelper.create(IJobRecruitment.class);
        }

        mIJobRecruitment.createJobRecruitment(RetrofitHelper
                .getBody(new JsonItemArray("pictures", mImages),
                        new JsonItem("title", title),
                        new JsonItem("content", content),
                        new JsonItem("contact", phone),
                        new JsonItem("provinceCode", mProvinceCode),
                        new JsonItem("cityCode", mCityCode),
                        new JsonItem("areaCode", mAreaCode)))
                .enqueue(new MsgCallBack<BaseBean<String>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                        showToast("提交成功");
                        setResult(RESULT_OK);
                        finish();
                    }
                });

    }


}
