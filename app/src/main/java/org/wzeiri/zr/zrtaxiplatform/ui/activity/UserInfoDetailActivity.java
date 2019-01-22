package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.DriverInfoBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IDriver;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.util.GlideUtil;
import org.wzeiri.zr.zrtaxiplatform.widget.CircleImageView;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 其他用户-个人信息详情页
 *
 * @author k-lm on 2017/11/23.
 */

public class UserInfoDetailActivity extends ActionbarActivity {
    @BindView(R.id.aty_user_info_image_avatar)
    CircleImageView mImageAvatar;
    @BindView(R.id.aty_user_info_vtv_sex)
    ValueTextView mVtvSex;
    @BindView(R.id.aty_user_info_vtv_age)
    ValueTextView mVtvAge;
    @BindView(R.id.aty_user_info_vtv_phone)
    ValueTextView mVtvPhone;
    @BindView(R.id.aty_user_info_vtv_taxi_company)
    ValueTextView mVtvTaxiCompany;
    @BindView(R.id.aty_user_info_vtv_city)
    ValueTextView mVtvCity;
    @BindView(R.id.aty_user_info_vtv_address)
    ValueTextView mVtvAddress;

    private long mUserId = -1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info;
    }


    @Override
    protected void create() {
        super.create();


    }

    @Override
    protected void init() {
        super.init();

        Intent intent = getIntent();
        if (intent == null) {
            showToast("无法获取用户信息");
            finish();
            return;
        }

        mUserId = intent.getLongExtra("userId", mUserId);
        String name = intent.getStringExtra("name");
        setCenterTitle(name);

        initUserInfo();
    }


    private void initUserInfo() {
        IDriver driver = RetrofitHelper.create(IDriver.class);

        driver.getDriverInfo(mUserId)
                .enqueue(new MsgCallBack<BaseBean<DriverInfoBean>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<DriverInfoBean>> call, Response<BaseBean<DriverInfoBean>> response) {
                        DriverInfoBean bean = response.body().getResult();
                        if (bean == null) {
                            showToast("无法获取用户信息");
                            return;
                        }

                        mVtvAddress.setTextRight(bean.getAddress());
                        mVtvAge.setTextRight(bean.getAge());
                        GlideUtil.loadPath(getThis(), mImageAvatar, bean.getProfilePictureUrl());
                        mVtvCity.setTextRight(bean.getCity());
                        mVtvSex.setTextRight(bean.getSexy());
                        setCenterTitle(bean.getName());
                        mVtvTaxiCompany.setTextRight(bean.getCompanyName());
                        mVtvPhone.setText(bean.getPhoneNumber());
                    }
                });
    }


    /**
     * @param context
     * @param userId  用户id
     */
    public static void start(Context context, long userId, String name) {
        Intent starter = new Intent(context, UserInfoDetailActivity.class);
        starter.putExtra("userId", userId);
        starter.putExtra("name", name);
        context.startActivity(starter);
    }

}
