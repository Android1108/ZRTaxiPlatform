package org.wzeiri.zr.zrtaxiplatform.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.HomeUserBean;
import org.wzeiri.zr.zrtaxiplatform.bean.UploadResultBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.common.Config;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IUser;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItem;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.AboutUsActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.ChangePasswordActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.CodeScanActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.IdCardAuthenticationActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.LearningRecordActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.MyIntegralActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.MyLegalAdviceReplyActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.MyQRCodeActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.MyReleaseCarFaultInfoActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.MyReleaseDriverInteractionActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.MyReleaseJobInfoActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.MyReleaseLostFoundActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.MyReleasePostingAdvertisementActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.MyReleaseSeatCoverActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.MyUserInfoActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.NotificationActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.OpinionFeedbackActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.base.ActionbarFragment;
import org.wzeiri.zr.zrtaxiplatform.util.BitmapUtil;
import org.wzeiri.zr.zrtaxiplatform.util.GlideUtil;
import org.wzeiri.zr.zrtaxiplatform.util.LogUtil;
import org.wzeiri.zr.zrtaxiplatform.util.PhotographUtil;
import org.wzeiri.zr.zrtaxiplatform.util.ThreadSwitch;
import org.wzeiri.zr.zrtaxiplatform.util.UploadPictureHelper;
import org.wzeiri.zr.zrtaxiplatform.util.UserInfoHelper;
import org.wzeiri.zr.zrtaxiplatform.widget.CircleImageView;
import org.wzeiri.zr.zrtaxiplatform.widget.DrawTextTextView;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;
import org.wzeiri.zr.zrtaxiplatform.widget.dialog.BottomDialog;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 我的
 *
 * @author k-lm on 2017/11/15.
 */

public class MyFragment extends ActionbarFragment implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.fragment_my_image_avatar)
    CircleImageView mImageAvatar;
    @BindView(R.id.fragment_my_user_type)
    TextView mUserType;
    @BindView(R.id.fragment_my_dttv_study)
    DrawTextTextView mDttvRelease;
    @BindView(R.id.fragment_my_dttv_integral)
    DrawTextTextView mDttvIntegral;

    @BindView(R.id.fragment_my_vtv_re_certification)
    ValueTextView mVtvCertification;

    @BindView(R.id.fragment_my_text_star_month_count)
    TextView mTextStarMonthCount;

    @BindView(R.id.fragment_my_swl_refresh)
    SwipeRefreshLayout mRefreshLayout;

    private MenuItem mMessageMenu;

    private IUser mIUser;

    /**
     * 头像对话框
     */
    private BottomDialog mAvatarDialog;

    private Uri mAvatarUri;

    private Bitmap mBitmap;

    private UploadPictureHelper mUploadHelper;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_my;
    }

    @OnClick(R.id.fragment_my_vtv_re_my_qr_code)
    void qrCodeClick() {
        if (!UserInfoHelper.getInstance().isAuthentication(getThis())) {
            return;
        }

        if (UserInfoHelper.getInstance().getBindCode()==""){
            CodeScanActivity.start(getThis());
            return;
        }
        else {
            startActivity(MyQRCodeActivity.class);
        }
    }

    @OnClick(R.id.fragment_my_image_avatar)
    void avatarClick() {
        showAvatarDialog();
    }

    //求职信息
    @OnClick(R.id.fragment_my_text_job_info)
    public void onTextJobInfoClicked() {
        startActivity(MyReleaseJobInfoActivity.class);
    }

    //司机互动
    @OnClick(R.id.fragment_my_text_driver_interaction)
    public void onTextDriverInteractionClicked() {
        startActivity(MyReleaseDriverInteractionActivity.class);
    }

    //失物招领
    @OnClick(R.id.fragment_my_text_lost_found)
    public void onTextLostFoundClicked() {
        startActivity(MyReleaseLostFoundActivity.class);
    }

    /**
     * 法律咨询
     */
    @OnClick(R.id.fragment_my_text_legal_advice)
    void onLegalAdviceClick() {
        startActivity(MyLegalAdviceReplyActivity.class);
    }

    // 车换座套
    @OnClick(R.id.fragment_my_text_car_seat_cover)
    public void onTextCarSeatCoverClicked() {
        if (!UserInfoHelper.getInstance().isAuthentication(getThis())) {
            return;
        }

        startActivity(MyReleaseSeatCoverActivity.class);
    }

    // 张贴广告
    @OnClick(R.id.fragment_my_text_advertising_posting)
    public void onTextAdvertisingPostingClicked() {
        if (!UserInfoHelper.getInstance().isAuthentication(getThis())) {
            return;
        }
        startActivity(MyReleasePostingAdvertisementActivity.class);
    }

    // gps故障
    @OnClick(R.id.fragment_my_text_gps_fault)
    public void onTextGpsFaultClicked() {
        if (!UserInfoHelper.getInstance().isAuthentication(getThis())) {
            return;
        }
        startActivity(MyReleaseCarFaultInfoActivity.class);
    }

    /**
     * 重新认证
     */
    @OnClick(R.id.fragment_my_vtv_re_certification)
    void onCertificationClick() {
        int state = UserInfoHelper.getInstance().getAuthenticationState();
        if (state == UserInfoHelper.AUTHENTICATION_AUDIT) {
            showToast("当前正在审核，请稍候尝试");
            return;
        }

        IdCardAuthenticationActivity.start(getThis(), false);
    }

    //个人信息
    @OnClick(R.id.fragment_my_vtv_user_info)
    public void onVtvUserInfoClicked() {
       /* if (!UserInfoHelper.getInstance().isAuthentication(getContext())) {
            return;
        }*/
        startActivity(MyUserInfoActivity.class);
    }

    // 修改密码
    @OnClick(R.id.fragment_my_vtv_change_password)
    public void onVtvChangePasswordClicked() {
        startActivity(ChangePasswordActivity.class);
    }

    // 意见反馈
    @OnClick(R.id.fragment_my_vtv_opinion_feedback)
    public void onVtvOpinionFeedbackClicked() {
        startActivity(OpinionFeedbackActivity.class);
    }

    // 关于我们
    @OnClick(R.id.fragment_my_vtv_about_us)
    public void onVtvAboutUsClicked() {
        startActivity(AboutUsActivity.class);
    }

    @OnClick(R.id.fragment_my_dttv_study)
    void onStudyClick() {
        if (!UserInfoHelper.getInstance().isAuthentication(getThis())) {
            return;
        }
        startActivity(LearningRecordActivity.class);
    }

    @OnClick(R.id.fragment_my_dttv_integral)
    void onIntegralClick() {
        if (!UserInfoHelper.getInstance().isAuthentication(getThis())) {
            return;
        }
        startActivity(MyIntegralActivity.class);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //showToast("点击消息");
        if (item.getItemId() == R.id.menu_message_message) {
            startActivity(NotificationActivity.class);
            return true;
        }
        return false;
    }

    @Override
    public void create() {
        super.create();
        // 监听认证状态
        UserInfoHelper.getInstance()
                .addOnAuthenticationListener(getThis(),
                        new UserInfoHelper.OnAuthenticationChangeListener() {
                            @Override
                            public void onAuthenticationChangeListener() {
                                onRefresh();
                            }
                        });
    }

    @Override
    public void init() {
        super.init();
        setCenterTitle("我的");
        mRefreshLayout.setOnRefreshListener(this);
        setCenterTitleColor(ContextCompat.getColor(getContext(), R.color.black90));
        setNoticeBarColor(ContextCompat.getDrawable(getContext(), R.drawable.toolbar_bg));
        setBarBackgroundColor(ContextCompat.getDrawable(getContext(), R.drawable.toolbar_bg));
        initQrCode();
    }


    @Override
    public void initData() {
        super.initData();
        mIUser = RetrofitHelper.create(IUser.class);
        onRefresh();
    }

    private void loadData() {
        mIUser.getUserHome()
                .enqueue(new MsgCallBack<BaseBean<HomeUserBean>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<HomeUserBean>> call, Response<BaseBean<HomeUserBean>> response) {
                        mRefreshLayout.setRefreshing(false);
                        HomeUserBean bean = response.body().getResult();
                        if (bean == null) {
                            return;
                        }

                        mDttvIntegral.setTopDrawText(bean.getIntegral() + "");
                        mDttvRelease.setTopDrawText(bean.getLearnCount() + "");
                        String picture = bean.getPicture();
                        if (TextUtils.isEmpty(picture)
                                || TextUtils.equals(picture, Config.NO_IMAGE_URL)) {
                            mImageAvatar.setImageResource(R.drawable.ic_default_avatar);
                        } else {
                            GlideUtil.loadPath(getThis(), mImageAvatar, picture);
                        }

                        UserInfoHelper.getInstance().setDriverStatus(bean.getDriverStatus());

                        if (UserInfoHelper.getInstance().isAuthentication()) {
                            mVtvCertification.setText("重新认证");
                        } else {
                            mVtvCertification.setText("司机认证");
                        }

                        mVtvCertification.setTextRight(UserInfoHelper.getInstance().getDriverStatusStr());


                        UserInfoHelper.getInstance().setArtyStarTimes(bean.getArtyStarCount());

                        int starMonthCount = UserInfoHelper.getInstance().getArtyStarTimes();
                        mTextStarMonthCount.setText(starMonthCount + "次");


                    }

                    @Override
                    public void onError(Call<BaseBean<HomeUserBean>> call, Throwable t) {
                        super.onError(call, t);
                        mRefreshLayout.setRefreshing(false);
                    }
                });


    }

    /**
     * 初始化我的二维码
     */
    private void initQrCode() {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(R.drawable.ic_qr_code);
        addToolBarLeftView(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UserInfoHelper.getInstance().isAuthentication(getThis())) {
                    return;
                }

                if (!UserInfoHelper.getInstance().isBindCode()) {
                    CodeScanActivity.start(getThis());
                    return;
                }

                startActivity(MyQRCodeActivity.class);

            }
        });
    }


    private void showAvatarDialog() {
        if (mAvatarDialog == null) {
            mAvatarDialog = new BottomDialog(getActivity(), R.style.NoTitleDialog);

            mAvatarDialog.addDataArray("相册", "相机");
            mAvatarDialog.setOnItemClickListener(new BottomDialog.OnItemClickListener() {
                @Override
                public void onItemClick(BottomDialog dialog, int position) {
                    if (position == 0) {
                        // 进入相册
                        PhotographUtil.choosePicture(getThis());
                    } else if (position == 1) {
                        // 生成指定uri
                        mAvatarUri = PhotographUtil.createImageUri("certificate", getActivity());
                        // 进入相机
                        boolean isSuccess = PhotographUtil.takeCamera(getThis(), mAvatarUri);
                        if (!isSuccess) {
                            showToast("没有sd卡目录");
                        }
                    }
                    dialog.dismiss();
                }
            });
        }


        mAvatarDialog.show();
    }


    /**
     * 将uri转换成bitmap
     */
    private void saveUri() {
        showProgressDialog("正在处理图片，请稍候");
        ThreadSwitch.createTask(new ThreadSwitch.OnCreateListener<Bitmap>() {
            @Override
            public Bitmap onCreate(ThreadSwitch threadSwitch) {
                try {
                    return BitmapUtil.getUriBitmap(getActivity(),
                            mAvatarUri,
                            Config.STORAGE_PATH,
                            Config.UPLOAD_IMAGE_NAME,
                            768,
                            432);
                } catch (IOException e) {
                    return null;
                }
            }
        })
                .addTaskListener(new ThreadSwitch.OnTaskListener<Bitmap, File>() {
                    @Override
                    public File onNext(ThreadSwitch threadSwitch, Bitmap value) {
                        mAvatarUri = null;
                        mBitmap = value;
                        //BitmapUtil.saveBitmap(Config.STORAGE_PATH, Config.UPLOAD_IMAGE_NAME, mPhotoBitmap, 100);

                        return new File(Config.STORAGE_PATH, Config.UPLOAD_IMAGE_NAME);
                    }
                })
                .switchLooper(ThreadSwitch.MAIN_THREAD)
                .submit(new ThreadSwitch.OnSubmitListener<File>() {
                    @Override
                    public void onSubmit(ThreadSwitch threadSwitch, File value) {
                        if (value == null) {
                            closeProgressDialog();
                            showToast("获取图片失败，请重试");
                            return;
                        }
                        uploadImage(value);

                    }
                });
    }

    private void uploadImage(File file) {
        if (mUploadHelper == null) {
            mUploadHelper = new UploadPictureHelper(getThis());

            mUploadHelper.setOnUploadListener(new UploadPictureHelper.OnUploadListener() {
                @Override
                public void onUploadSuccess(@NonNull List<UploadResultBean> beans) {
                    UploadResultBean bean = beans.get(0);
                    updateUserPicture(bean.getFileName());
                }

                @Override
                public void onUploadError(String msg) {
                    closeProgressDialog();
                    showToast("处理图片失败，请重试");
                }
            });
        }

        mUploadHelper.uploadSingleFile(file);

    }

    /**
     * 绑定二维码
     *
     * @param url 扫描的二维码结果
     */
    private void bindingDriverCode(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        mIUser.bindingDriverCode(
                RetrofitHelper.getBody(new JsonItem("url", url)))
                .enqueue(new MsgCallBack<BaseBean<String>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                        showToast("绑定二维码成功");
                        UserInfoHelper.getInstance().setIsBindCode(true);
                    }
                });
    }

    /**
     * 修改头像
     *
     * @param fileName
     */
    private void updateUserPicture(String fileName) {
        mIUser.updateUserPicture(RetrofitHelper
                .getBody(new JsonItem("picture", fileName)))
                .enqueue(new MsgCallBack<BaseBean<String>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                        MyFragment.this.closeProgressDialog();
                        showToast("修改头像成功");
                        mImageAvatar.setImageBitmap(mBitmap);
                        LogUtil.d(response.body().getResult());
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_message, menu);
        mMessageMenu = menu.findItem(R.id.menu_message_message);
        mMessageMenu.setIcon(R.drawable.ic_message);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case IdCardAuthenticationActivity.REQUEST_CODE:
                onRefresh();
                break;
            case PhotographUtil.ALBUM_REQUEST_CODE:
                mAvatarUri = data.getData();
            case PhotographUtil.CAMERA_REQUEST_CODE:
                if (mAvatarUri == null) {
                    showToast("获取图片失败，请重试");
                    break;
                }
                saveUri();
                break;
        }


        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            String content = CodeScanActivity.getScanningResult(requestCode,
                    resultCode,
                    data);
            bindingDriverCode(content);

            return;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PhotographUtil.onRequestPermissionsResult(getThis(), requestCode, permissions, grantResults, mAvatarUri);
    }


    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(true);
        loadData();
    }
}

