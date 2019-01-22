package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.Intent;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.MyApplication;
import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.LocationRegionBean;
import org.wzeiri.zr.zrtaxiplatform.bean.MyUserInfoBean;
import org.wzeiri.zr.zrtaxiplatform.bean.TenantBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IMy;
import org.wzeiri.zr.zrtaxiplatform.network.api.IUser;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItem;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.util.CarHelper;
import org.wzeiri.zr.zrtaxiplatform.util.StringUtil;
import org.wzeiri.zr.zrtaxiplatform.util.UserInfoHelper;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;
import org.wzeiri.zr.zrtaxiplatform.widget.dialog.BottomDialog;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 个人资料
 *
 * @author k-lm on 2017/11/20.
 */

public class MyUserInfoActivity extends ActionbarActivity {

    @BindView(R.id.aty_my_user_info_vtv_name)
    ValueTextView mName;
    @BindView(R.id.aty_my_user_info_vtv_sex)
    ValueTextView mSex;
    @BindView(R.id.aty_my_user_info_vtv_age)
    ValueTextView mAge;
    @BindView(R.id.aty_my_user_info_vtv_phone)
    ValueTextView mPhone;
    @BindView(R.id.aty_my_user_info_vtv_operation_number)
    ValueTextView mLisenceNumber;
    @BindView(R.id.aty_my_user_info_vtv_taxi_company)
    ValueTextView mCompanyName;
    @BindView(R.id.aty_my_user_info_vtv_city)
    ValueTextView mCity;
    @BindView(R.id.aty_my_user_info_vtv_address)
    ValueTextView mAddress;

    private TenantBean mTenantBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_user_info;
    }

    private IUser mUser;

    @OnClick(R.id.aty_my_user_info_text_login_out)
    void onLoginOut() {
        UserInfoHelper.getInstance().clearUserInfo();
        CarHelper.getInstance().clear();
        startActivity(LoginActivity.class);
        // 结束其他所有activity
        ((MyApplication) getApplication()).finishAllActivity(LoginActivity.class);
    }


    @OnClick(R.id.aty_my_user_info_vtv_city)
    void cityClick() {
        if (!UserInfoHelper.getInstance().isAuthentication(getThis())) {
            return;
        }
        SelectRegionRecyclerActivity.start(getThis());
    }

    @OnClick(R.id.aty_my_user_info_vtv_address)
    void onAddressClick() {
        if (!UserInfoHelper.getInstance().isAuthentication(getThis())) {
            return;
        }
        ChangeAddressActivity.start(getThis(), mAddress.getTextRight());
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("个人资料");
    }

    @Override
    protected void initData() {
        super.initData();
        Intent intent = getIntent();
        if (intent == null) {
            showToast("没有获得个人信息");
            finish();
            return;
        }

        mUser = RetrofitHelper.create(IUser.class);
        mUser.getUserBasicInfo()
                .enqueue(new MsgCallBack<BaseBean<MyUserInfoBean>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<MyUserInfoBean>> call, Response<BaseBean<MyUserInfoBean>> response) {
                        MyUserInfoBean bean = response.body().getResult();

                        mPhone.setTextRight(bean.getPhoneNumber());

                        if (UserInfoHelper.getInstance().isAuthentication()) {
                            mName.setTextRight(bean.getName());
                            mSex.setTextRight(bean.getGender());
                            mAge.setTextRight(bean.getAge());
                            String provinceName = bean.getProvinceName() == null ? "" : bean.getProvinceName();
                            String cityName = bean.getCityName() == null ? "" : bean.getCityName();
                            String city = provinceName + " " + cityName;
                            mCity.setTextRight(city);
                            mAddress.setTextRight(bean.getAddress());
                            mCompanyName.setTextRight(bean.getCompanyName());
                            mLisenceNumber.setTextRight(bean.getCarLisenceNumber());
                            return;
                        }


                        String stateStr = "";
                        int state = UserInfoHelper.getInstance().getAuthenticationState();
                        if (state == UserInfoHelper.AUTHENTICATION_AUDIT) {
                            //stateStr = "审核中";
                        } else if (state == UserInfoHelper.UN_AUTHENTICATION) {
                            stateStr = "未认证";
                        }

                        mName.setTextRight(StringUtil.getString(bean.getName(), stateStr));
                        mSex.setTextRight(StringUtil.getString(bean.getGender(), stateStr));
                        mAge.setTextRight(StringUtil.getString(bean.getAge(), stateStr));
                        mCity.setTextRight(StringUtil.getString(bean.getCityName(), stateStr));
                        mAddress.setTextRight(StringUtil.getString(bean.getAddress(), stateStr));
                        mCompanyName.setTextRight(StringUtil.getString(bean.getCompanyName(), stateStr));
                        mLisenceNumber.setTextRight(StringUtil.getString(bean.getCarLisenceNumber(), stateStr));


                        if (mTenantBean == null) {
                            mTenantBean = new TenantBean();
                        }

                        mTenantBean.setCityCode(bean.getCityCode());
                        mTenantBean.setCityName(bean.getCompanyName());
                        mTenantBean.setProvinceCode(bean.getProvinceCode());
                        mTenantBean.setProvinceName(bean.getProvinceName());
                    }
                });
    }

    /**
     * 切换城市
     *
     * @param provinceCode 省份代码
     * @param cityCode     城市代码
     */
    private void changeUserRegion(String provinceCode, final String provinceName, String cityCode, final String cityName) {
        mUser.updateUserRegion(RetrofitHelper
                .getBody(new JsonItem("provinceCode", provinceCode),
                        new JsonItem("cityCode", cityCode)))
                .enqueue(new MsgCallBack<BaseBean<String>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                        showToast("修改成功");
                        String city = provinceName + " " + cityName;
                        mCity.setTextRight(city);
                    }
                });
    }

    /**
     * 修改城市
     *
     * @param address 地址
     */
    private void changeUserAddress(final String address) {
        mUser.updateUserAddress(RetrofitHelper
                .getBody(new JsonItem("address", address)))
                .enqueue(new MsgCallBack<BaseBean<String>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                        showToast("修改成功");
                        mAddress.setTextRight(address);
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }

        if (requestCode == SelectRegionRecyclerActivity.REQUEST_CODE) {
            TenantBean bean = SelectRegionRecyclerActivity.loadRegionInfo(data);
            String provinceCode = bean.getProvinceCode();
            String provinceName = bean.getProvinceName();
            String cityCode = bean.getCityCode();
            String cityName = bean.getCityName();
            changeUserRegion(provinceCode, provinceName, cityCode, cityName);
        } else if (requestCode == ChangeAddressActivity.REQUEST_CODE) {
            String address = data.getStringExtra(ChangeAddressActivity.KEY_ADDRESS);
            changeUserAddress(address);
        } else if (requestCode == ChangeAddressActivity.REQUEST_CODE) {
            String address = data.getStringExtra(ChangeAddressActivity.KEY_ADDRESS);
            mAddress.setTextRight(address);
        }
    }
}

