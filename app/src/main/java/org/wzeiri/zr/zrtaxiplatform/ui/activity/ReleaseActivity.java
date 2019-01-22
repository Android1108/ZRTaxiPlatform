package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.amin.MainVisibilityIntoAnimation;
import org.wzeiri.zr.zrtaxiplatform.bean.WalletDetailBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IWallet;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.util.UserInfoHelper;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 发布管理
 *
 * @author k-lm on 2017/11/15.
 */

public class ReleaseActivity extends ActionbarActivity {

    public static final int REQURES_CODE = 30000;


    @BindView(R.id.aty_release_text_candidate_info)
    TextView mTextCandidateInfo;
    @BindView(R.id.aty_release_text_driver_interaction)
    TextView mTextDriverInteraction;
    @BindView(R.id.aty_release_text_lost_found)
    TextView mTextLostFound;
    @BindView(R.id.aty_release_text_car_seat_cover)
    TextView mTextCarSeatCover;
    @BindView(R.id.aty_release_text_car_posting_advertisement)
    TextView mTextCarPostingAdvertisement;
    @BindView(R.id.aty_release_text_car_gps_fault)
    TextView mTextCarGpsFault;


    @BindView(R.id.aty_release_img_close)
    ImageView mImageClose;

    @BindView(R.id.aty_release_img_add)
    ImageView mImageAdd;

    private WalletDetailBean mWalletDetailBean;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_release;
    }


    @OnClick(R.id.aty_release_text_candidate_info)
    public void onmTextCandidateInfoClicked() {
        // 检测是否认证
        if (!UserInfoHelper.getInstance().isAuthentication(getThis())) {
            return;
        }
        // 检测是否切换地区
        if (UserInfoHelper.getInstance().isSwitchRegion(getThis())) {
            return;
        }

        startActivityForResult(ReleaseCandidateInfoActivity.class,
                ReleaseCandidateInfoActivity.REQUEST_CODE);
    }

    @OnClick(R.id.aty_release_text_driver_interaction)
    public void onmTextDriverInteractionClicked() {
        // 检测是否认证
        if (!UserInfoHelper.getInstance().isAuthentication(getThis())) {
            return;
        }
        // 检测是否切换地区
        if (UserInfoHelper.getInstance().isSwitchRegion(getThis())) {
            return;
        }
        startActivityForResult(ReleaseDriverInteractionActivity.class,
                ReleaseDriverInteractionActivity.REQUEST_CODE);
    }

    @OnClick(R.id.aty_release_text_lost_found)
    public void onmTextLostFoundClicked() {
        // 检测是否认证
        if (!UserInfoHelper.getInstance().isAuthentication(getThis())) {
            return;
        }
        // 检测是否切换地区
        if (UserInfoHelper.getInstance().isSwitchRegion(getThis())) {
            return;
        }
        startActivityForResult(ReleaseLostFoundActivity.class, ReleaseLostFoundActivity.REQUEST_CODE);
    }

    @OnClick(R.id.aty_release_text_car_seat_cover)
    public void onmTextCarSeatCoverClicked() {
        // 检测是否认证
        if (!UserInfoHelper.getInstance().isAuthentication(getThis())) {
            return;
        }
        // 检测是否切换地区
        if (UserInfoHelper.getInstance().isSwitchRegion(getThis())) {
            return;
        }
        startActivityForResult(ReleaseChangeSeatCoverInfoActivity.class,
                ReleaseChangeSeatCoverInfoActivity.REQUEST_CODE);
    }

    @OnClick(R.id.aty_release_text_car_posting_advertisement)
    public void onmTextCarPostingAdvertisementClicked() {

        // 检测是否认证
        if (!UserInfoHelper.getInstance().isAuthentication(getThis())) {
            return;
        }
        // 检测是否切换地区
        if (UserInfoHelper.getInstance().isSwitchRegion(getThis())) {
            return;
        }
        if (UserInfoHelper.getInstance().getBankCardNo()==""){
            showToast("请绑定银行卡");
            MyBankCardActivity.start(getThis(),mWalletDetailBean);
            return;
        }
        else{
        Intent intent=new Intent(this,ReleaseAdvertisementPhotoInfoActivity.class);
        intent.putExtra("bankCardOwner",mWalletDetailBean.getBankCardOwner());
        intent.putExtra("bankCardNo",mWalletDetailBean.getBankCardNo());
        intent.putExtra("bankName",mWalletDetailBean.getBankName());
        intent.putExtra("bankCardBindPhoneNumber",mWalletDetailBean.getBankCardBindPhoneNumber());
        startActivityForResult(intent,REQURES_CODE);
        }
    }

    @OnClick(R.id.aty_release_text_car_gps_fault)
    public void onmTextCarGpsFaultClicked() {
        // 检测是否认证
        if (!UserInfoHelper.getInstance().isAuthentication(getThis())) {
            return;
        }
        // 检测是否切换地区
        if (UserInfoHelper.getInstance().isSwitchRegion(getThis())) {
            return;
        }
        startActivityForResult(ReleaseGpsFaultActivity.class, ReleaseGpsFaultActivity.REQUEST_CODE);
    }

    @OnClick(R.id.aty_release_img_close)
    public void onmImgCloseClicked(View view) {
        onBackPressed();
    }


    @Override
    protected void init() {
        super.init();
        initAnim();
        setCenterTitle("发布");
        getToolbar().setNavigationIcon(null);
        getToolbar().setNavigationOnClickListener(null);
        final IWallet mIWallet= RetrofitHelper.create(IWallet.class);
        mIWallet.getWalletDetails()
                .enqueue(new MsgCallBack<BaseBean<WalletDetailBean>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<WalletDetailBean>> call, Response<BaseBean<WalletDetailBean>> response) {
                            mWalletDetailBean=response.body().getResult();
                             if (mWalletDetailBean.getBankCardNo()!=null) {
                                   UserInfoHelper.getInstance().setBankCardNo(mWalletDetailBean.getBankCardNo().toString());
                             }
                    }
                });
    }


    private void initAnim() {
        // 设置进入当前activity的过渡动画
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            MainVisibilityIntoAnimation animation = new MainVisibilityIntoAnimation(mImageAdd,
                    mImageClose,
                    mTextCandidateInfo, mTextDriverInteraction, mTextLostFound,
                    mTextCarSeatCover, mTextCarPostingAdvertisement, mTextCarGpsFault
            );
            animation.setDuration(300);
            getWindow().setEnterTransition(animation);
        } else {
            mImageClose.setVisibility(View.VISIBLE);
            mImageAdd.setVisibility(View.GONE);

            // 旋转动画
           /* ValueAnimator anim = ValueAnimator.ofFloat(0, 1f);
            anim.setDuration(300);
            anim.setInterpolator(new LinearInterpolator());
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    float rotation = 135 * value - 135;
                    mImageClose.setRotation(rotation);

                    mTextCandidateInfo.setAlpha(value);
                    mTextDriverInteraction.setAlpha(value);
                    mTextLostFound.setAlpha(value);
                    mTextCarSeatCover.setAlpha(value);
                    mTextCarPostingAdvertisement.setAlpha(value);
                    mTextCarGpsFault.setAlpha(value);

                    // view.setAlpha(1 - value);
                }
            });

            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mImageClose.setVisibility(View.VISIBLE);
                    mImageAdd.setVisibility(View.GONE);
                    mImageClose.setRotation(-135);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            anim.start();*/


        }
    }


    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mImageClose.setVisibility(View.GONE);
            mImageAdd.setVisibility(View.VISIBLE);
        }

        super.onBackPressed();
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.release_activity_out);
    }


    public static int getRequestCode(Intent intent) {
        if (intent == null) {
            return -1;
        }
        return intent.getIntExtra("requestCode", -1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (data == null) {
            data = new Intent();
        }
        data.putExtra("requestCode", requestCode);
        setResult(RESULT_OK, data);
    }


}
