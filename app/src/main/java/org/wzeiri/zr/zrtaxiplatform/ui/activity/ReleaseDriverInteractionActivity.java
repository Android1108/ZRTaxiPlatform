package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.UploadResultBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.common.Config;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IPost;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItem;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItemArray;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.util.BitmapUtil;
import org.wzeiri.zr.zrtaxiplatform.util.EditTextFormatUtil;
import org.wzeiri.zr.zrtaxiplatform.util.PhotoHelper;
import org.wzeiri.zr.zrtaxiplatform.util.ThreadSwitch;
import org.wzeiri.zr.zrtaxiplatform.util.UploadPictureHelper;
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
 * 发布司机互动
 *
 * @author k-lm on 2017/11/29.
 */

public class ReleaseDriverInteractionActivity extends ActionbarActivity implements PhotoHelper.OnPhotoUpdateListener {


    @BindView(R.id.layout_release_info_edit_title)
    EditText mInfoEditTitle;
    @BindView(R.id.layout_release_info_edit_content)
    EditText mInfoEditContent;
    @BindView(R.id.layout_release_info_fl_layout)
    FlowLayout mInfoFlLayout;

    @BindView(R.id.aty_release_driver_interaction_vtv_plate)
    ValueTextView mVtvPlate;

    private PhotoHelper mPhotoHelper;

    private Bitmap mBitmap;

    private UploadPictureHelper mUploadPictureHelper;

    private List<String> mImageUrlList = new ArrayList<>(3);

    private IPost mPost;


    public static final String KEY_SELECT_ID = SelectDriverInteractionPlateActivity.KEY_SELECT_ID;
    public static final String KEY_SELECT_NAME = SelectDriverInteractionPlateActivity.KEY_SELECT_NAME;

    public static final int REQUEST_CODE = 30002;


    /**
     * 选择的板块id
     */
    private int mSelectPlateId = -1;

    @OnClick(R.id.aty_release_driver_interaction_vtv_plate)
    void onSelectPlateClick() {
        SelectDriverInteractionPlateActivity.start(getThis(), mSelectPlateId);
    }

    @OnClick(R.id.layout_release_info_text_release)
    void onReleaseClick() {
        String title = mInfoEditTitle.getText().toString();
        String content = mInfoEditContent.getText().toString();
        String plate = mVtvPlate.getText().toString();


        if (TextUtils.isEmpty(title.trim())) {
            showToast("请输入标题");
            return;
        }

        if (TextUtils.isEmpty(content.trim())) {
            showToast("请输入描述信息");
            return;
        }

        if (TextUtils.isEmpty(plate.trim())) {
            showToast("请选择板块");
            return;
        }

        if (mPost == null) {
            mPost = RetrofitHelper.create(IPost.class);
        }

        mPost.createPost(RetrofitHelper
                .getBody(new JsonItemArray("pictures", mImageUrlList),
                        new JsonItem("content", content),
                        new JsonItem("title", title),
                        new JsonItem("sectionId", mSelectPlateId)))
                .enqueue(new MsgCallBack<BaseBean<String>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                        showToast("提交成功");
                        setResult(RESULT_OK);
                        finish();
                    }
                });


    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_release_driver_interaction;
    }


    @Override
    protected void init() {
        super.init();
        setCenterTitle("发布司机之家");
        EditTextFormatUtil.formatRemoveEmoji(mInfoEditTitle);
        EditTextFormatUtil.formatRemoveEmoji(mInfoEditContent);
        mPhotoHelper = new PhotoHelper(this, mInfoFlLayout, 3, Config.STORAGE_PATH);
        mPhotoHelper.setOnPhotoUpdateListener(this);


        Intent intent = getIntent();

        if (intent != null) {
            mSelectPlateId = intent.getIntExtra(KEY_SELECT_ID, mSelectPlateId);
            String str = intent.getStringExtra(KEY_SELECT_NAME);
            mVtvPlate.setText(str);
        }

        mUploadPictureHelper = new UploadPictureHelper(getThis());
        mUploadPictureHelper.setOnUploadListener(new UploadPictureHelper.OnUploadListener() {
            @Override
            public void onUploadSuccess(@NonNull List<UploadResultBean> beans) {
                closeProgressDialog();
                if (beans.size() <= 0) {
                    return;
                }

                UploadResultBean bean = beans.get(0);
                mImageUrlList.add(bean.getFileName());
                mPhotoHelper.addPhoto(mBitmap);
            }

            @Override
            public void onUploadError(String msg) {
                showToast("图片处理失败");
                closeProgressDialog();
            }
        });


        mPhotoHelper.setOnPhotoUpdateListener(new PhotoHelper.OnPhotoUpdateListener() {
            @Override
            public void onStartCameraOrAlbum(boolean isSuccess) {

            }

            @Override
            public void onResult(boolean isSuccess) {
                if (isSuccess) {
                    showProgressDialog("正在处理图片请稍候");
                }
            }

            @Override
            public void onResultBitmap(Bitmap bitmap) {
                if (bitmap == null) {
                    showToast("图片获取失败，请重试");
                    closeProgressDialog();
                    return;
                }
                mBitmap = bitmap;
                // 保存文件
                ThreadSwitch.createTask(new ThreadSwitch.OnCreateListener<Boolean>() {
                    @Override
                    public Boolean onCreate(ThreadSwitch threadSwitch) {
                        BitmapUtil.saveBitmap(Config.STORAGE_PATH, Config.UPLOAD_IMAGE_NAME, mBitmap, 100);
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
                                mUploadPictureHelper.removeAllUploadFile();
                                mUploadPictureHelper.addUploadFile(file);
                                mUploadPictureHelper.upload();
                            }
                        });
            }
        });


        mPhotoHelper.setOnPhotoOperationListener(new PhotoHelper.OnPhotoOperationListener() {
            @Override
            public void onPhotoDelete(int position) {
                mImageUrlList.remove(position);
            }

            @Override
            public void onPhotoClick(int position, Bitmap bitmap) {

            }
        });

    }


    @Override
    protected void initData() {
        super.initData();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPhotoHelper.onActivityResult(requestCode, resultCode, data);

        if (data == null || resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == SelectDriverInteractionPlateActivity.REQUEST_CODE) {
            mSelectPlateId = data.getIntExtra(KEY_SELECT_ID, mSelectPlateId);
            String str = data.getStringExtra(KEY_SELECT_NAME);
            mVtvPlate.setText(str);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPhotoHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        mPhotoHelper.onDestroy();
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
    public void onResultBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            showToast("图片获取失败，请重试");
        }
        closeProgressDialog();
    }


    public static void start(Activity activity) {
        Intent starter = new Intent(activity, ReleaseDriverInteractionActivity.class);
        activity.startActivityForResult(starter, REQUEST_CODE);
    }

    /**
     * @param activity
     * @param name     选择的板块名称
     * @param id       选择的板块id
     */
    public static void start(Activity activity, String name, int id) {
        Intent starter = new Intent(activity, ReleaseDriverInteractionActivity.class);
        starter.putExtra(KEY_SELECT_NAME, name);
        starter.putExtra(KEY_SELECT_ID, id);
        activity.startActivityForResult(starter, REQUEST_CODE);
    }


}
