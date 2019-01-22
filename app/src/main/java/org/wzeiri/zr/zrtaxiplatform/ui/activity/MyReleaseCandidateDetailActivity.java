package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.JobRecruitmentDetailBean;
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
import org.wzeiri.zr.zrtaxiplatform.util.DensityUtil;
import org.wzeiri.zr.zrtaxiplatform.util.EditTextFormatUtil;
import org.wzeiri.zr.zrtaxiplatform.util.PhotoHelper;
import org.wzeiri.zr.zrtaxiplatform.util.ThreadSwitch;
import org.wzeiri.zr.zrtaxiplatform.util.UploadPictureHelper;
import org.wzeiri.zr.zrtaxiplatform.util.UserInfoHelper;
import org.wzeiri.zr.zrtaxiplatform.widget.PhotoView;
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
 * 我发布求职信息详情
 *
 * @author k-lm on 2017/11/23.
 */

public class MyReleaseCandidateDetailActivity extends ActionbarActivity {

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

    @BindView(R.id.aty_release_job_info_layout_submit)
    View mViewSubmit;
    private int mId = -1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_release_job_info_content;
    }

    @Override
    protected void create() {
        super.create();
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        mId = intent.getIntExtra("id", mId);
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("求职信息详情");
        mVtvJobRegion.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        mViewSubmit.setVisibility(View.GONE);
        mEditContent.setHint("");
        mEditTitle.setHint("");
        mVetPhone.setHint("");
        mEditContent.setEnabled(false);
        mEditTitle.setEnabled(false);
        mVetPhone.setEnabled(false);
        EditTextFormatUtil.formatRemoveEmoji(mEditContent);
        EditTextFormatUtil.formatRemoveEmoji(mEditTitle);

    }


    @Override
    protected void initData() {
        super.initData();
        IJobRecruitment iJobRecruitment = RetrofitHelper.create(IJobRecruitment.class);
        iJobRecruitment.getJobRecruitmentDetail(mId)
                .enqueue(new MsgCallBack<BaseBean<JobRecruitmentDetailBean>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<JobRecruitmentDetailBean>> call, Response<BaseBean<JobRecruitmentDetailBean>> response) {
                        JobRecruitmentDetailBean bean = response.body().getResult();
                        if (bean == null) {
                            return;
                        }

                        mEditContent.setText(bean.getContent());
                        mEditTitle.setText(bean.getTitle());
                        mVetPhone.setTextLeft(bean.getContact());
                        String address = bean.getProvinceName()
                                + " " + bean.getCityName()
                                + " " + bean.getAreaName();
                        mVtvJobRegion.setText(address);
                        addImages(bean.getPictures());

                    }
                });

    }

    /**
     * 添加图片
     *
     * @param imageUrlList
     */
    private void addImages(List<String> imageUrlList) {
        if (imageUrlList == null || imageUrlList.size() == 0) {
            return;
        }
        int size = imageUrlList.size();
        int viewSize = DensityUtil.dip2px(getThis(), 80);
        for (int i = 0; i < size; i++) {
            String url = imageUrlList.get(i);
            PhotoView photoView = new PhotoView(getThis());
            ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(viewSize, viewSize);
            layoutParams.rightMargin = getResources().getDimensionPixelOffset(R.dimen.layout_margin_mini);
            mFlLayout.addView(photoView, layoutParams);
            photoView.setPhotoUrl(url);
            photoView.setShowClose(false);
            photoView.setTag(url);
            photoView.setOnPhotoClickListener(new PhotoView.OnPhotoClickListener() {
                @Override
                public void onClickPhoto(PhotoView photoView, Bitmap bitmap) {
                    ImageZoomActivity.startUrl(getThis(), photoView, (String) photoView.getTag());
                }

                @Override
                public void onClickClose(PhotoView photoView) {

                }
            });

        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BaiduLocationService.onRequestPermissionsResult(getThis(), requestCode, permissions, grantResults);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null || resultCode != RESULT_OK) {
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    public static void start(Context context, int id) {
        Intent starter = new Intent(context, MyReleaseCandidateDetailActivity.class);
        starter.putExtra("id", id);
        context.startActivity(starter);
    }

}
