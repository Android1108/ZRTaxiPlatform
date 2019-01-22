package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.PostDetailBean;
import org.wzeiri.zr.zrtaxiplatform.bean.UpdatePostBean;
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
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 编辑司机互动
 *
 * @author k-lm on 2017/11/29.
 */

public class EditDriverInteractionActivity extends ActionbarActivity implements PhotoHelper.OnPhotoUpdateListener {

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
    /**
     * 图片最大数量
     */
    private int mMaxImageSize = 3;

    /**
     * 上传图片
     */
    private UploadPictureHelper mUploadPictureHelper;
    /**
     * 当前显示的图片信息
     */
    private List<PostDetailBean.PostPicturesBean> mImageDataList = new ArrayList<>(mMaxImageSize);

    /**
     * 已经删除的图片信息
     */
    private List<PostDetailBean.PostPicturesBean> mDeleteDataList = new ArrayList<>();
    /**
     * 新添加的url
     */
    private List<String> mAddUrlList = new ArrayList<>(mMaxImageSize);


    private IPost mPost;


    public static final String KEY_SELECT_ID = SelectDriverInteractionPlateActivity.KEY_SELECT_ID;
    public static final String KEY_SELECT_TITLE = "selectTitle";
    public static final String KEY_SELECT_CONTENT = "selectContent";
    public static final String KEY_SELECT_POSITION = "selectPosition";


    public static final int REQUEST_CODE = 10013;

    /**
     * 当前编辑的id
     */
    private int mId = -1;

    /**
     * 选择的板块id
     */
    private int mSelectPlateId = -1;

    private Gson mGson;

    @OnClick(R.id.aty_release_driver_interaction_vtv_plate)
    void onSelectPlateClick() {
        SelectDriverInteractionPlateActivity.start(getThis(), mSelectPlateId);
    }

    @OnClick(R.id.layout_release_info_text_release)
    void onReleaseClick() {
        final String title = mInfoEditTitle.getText().toString();
        final String content = mInfoEditContent.getText().toString();
        final String plate = mVtvPlate.getText().toString();


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


        UpdatePostBean updatePostBean = new UpdatePostBean();

        updatePostBean.setContent(content);
        updatePostBean.setTitle(title);
        updatePostBean.setSectionId(mSelectPlateId);
        updatePostBean.setId(mId);
        updatePostBean.setPictures(mAddUrlList);

        if (mDeleteDataList.size() > 0) {
            // 记录删除的id
            List<Integer> deleteIdList = new ArrayList<>();
            for (PostDetailBean.PostPicturesBean bean : mDeleteDataList) {
                deleteIdList.add(bean.getId());
            }
            updatePostBean.setDeletePictures(deleteIdList);
        }

        if (mGson == null) {
            mGson = new Gson();
        }
        String jsonData = mGson.toJson(updatePostBean);
        RequestBody body = RetrofitHelper.getBody(jsonData);

        mPost.updatePost(body)
                .enqueue(new MsgCallBack<BaseBean<String>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                        showToast("修改成功");
                        Intent intent = getIntent();
                        if (intent == null) {
                            intent = new Intent();
                        }
                        intent.putExtra(KEY_SELECT_TITLE, title);
                        intent.putExtra(KEY_SELECT_CONTENT, content);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_release_driver_interaction;
    }


    @Override
    protected void create() {
        super.create();
        Intent intent = getIntent();

        if (intent == null) {
            return;
        }
        mId = intent.getIntExtra(KEY_SELECT_ID, mId);
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("编辑司机之家");
        EditTextFormatUtil.formatRemoveEmoji(mInfoEditTitle);
        EditTextFormatUtil.formatRemoveEmoji(mInfoEditContent);
        mPhotoHelper = new PhotoHelper(this, mInfoFlLayout, mMaxImageSize, Config.STORAGE_PATH);
        mPhotoHelper.setOnPhotoUpdateListener(this);


        mUploadPictureHelper = new UploadPictureHelper(getThis());
        mUploadPictureHelper.setOnUploadListener(new UploadPictureHelper.OnUploadListener() {
            @Override
            public void onUploadSuccess(@NonNull List<UploadResultBean> beans) {
                closeProgressDialog();
                if (beans.size() <= 0) {
                    return;
                }

                UploadResultBean bean = beans.get(0);

                mAddUrlList.add(bean.getFileName());
                mPhotoHelper.addPhoto(mBitmap);
            }

            @Override
            public void onUploadError(String msg) {
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
                //原有图片数量
                int defaultUrlSize = mImageDataList.size();
                // 已删除数量
                int deleteUrlSize = mDeleteDataList.size();
                // 剩余数量
                int surplusUrlSize = defaultUrlSize - deleteUrlSize;

                if (position <= surplusUrlSize) {
                    // 删除已有的图片信息
                    PostDetailBean.PostPicturesBean bean = mImageDataList.remove(position);
                    mDeleteDataList.add(bean);
                } else {
                    // 删除新增图片
                    mAddUrlList.remove(surplusUrlSize - position - 1);
                }
            }

            @Override
            public void onPhotoClick(int position, Bitmap bitmap) {

            }
        });


    }


    @Override
    protected void initData() {
        super.initData();
        loadData();

    }

    /**
     * 获取司机互动详情
     */
    private void loadData() {
        mPost = RetrofitHelper.create(IPost.class);
        mPost.getPostDetail(mId)
                .enqueue(new MsgCallBack<BaseBean<PostDetailBean>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<PostDetailBean>> call, Response<BaseBean<PostDetailBean>> response) {
                        PostDetailBean bean = response.body().getResult();
                        if (bean == null) {
                            return;
                        }
                        mSelectPlateId = bean.getSectionId();
                        mInfoEditTitle.setText(bean.getTitle());
                        mInfoEditContent.setText(bean.getContent());
                        mVtvPlate.setText(bean.getSectionName());
                        List<PostDetailBean.PostPicturesBean> urlList = bean.getPostPictures();
                        if (urlList != null && urlList.size() > 0) {
                            mImageDataList.addAll(urlList);
                            for (PostDetailBean.PostPicturesBean postPicturesBean : urlList) {
                                mPhotoHelper.addPhoto(postPicturesBean.getPicture());
                            }
                        }
                    }
                });
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
            String str = data.getStringExtra(SelectDriverInteractionPlateActivity.KEY_SELECT_NAME);
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


    public static void start(Context context) {
        Intent starter = new Intent(context, ReleaseDriverInteractionActivity.class);
        context.startActivity(starter);
    }

    /**
     * 编辑司机互动内容
     *
     * @param activity
     * @param id       需要编辑的id
     * @param position 编辑的位置
     */
    public static void start(Activity activity, int id, int position) {
        Intent starter = new Intent(activity, EditDriverInteractionActivity.class);
        starter.putExtra(KEY_SELECT_ID, id);
        starter.putExtra(KEY_SELECT_POSITION, position);
        activity.startActivityForResult(starter, REQUEST_CODE);
    }


}
