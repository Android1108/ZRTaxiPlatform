package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.AuthenticationDataBean;
import org.wzeiri.zr.zrtaxiplatform.bean.DeviceVehicleBean;
import org.wzeiri.zr.zrtaxiplatform.bean.TenantBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IDriver;
import org.wzeiri.zr.zrtaxiplatform.network.api.ITransport;
import org.wzeiri.zr.zrtaxiplatform.network.api.IUser;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItem;
import org.wzeiri.zr.zrtaxiplatform.network.callback.BaseCallBack;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.service.BaiduLocationService;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.util.EditTextFormatUtil;
import org.wzeiri.zr.zrtaxiplatform.util.UserInfoHelper;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueEditText;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;
import org.wzeiri.zr.zrtaxiplatform.widget.dialog.SendMessageCodeDialog;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 身份证认证检测
 *
 * @author k-lm on 2017/12/28.
 */

public class IdCardAuthenticationActivity extends ActionbarActivity {
    @BindView(R.id.aty_id_car_authentication_vtv_city)
    ValueTextView mVtvCity;
    @BindView(R.id.aty_id_car_authentication_vet_id_card_number)
    ValueEditText mVetIdCardNumber;

    public static final int REQUEST_CODE = 10010;


    private boolean mIsToMain = true;

    private IUser mIUser;

    private IDriver mIDriver;

    private AuthenticationDataBean mDataBean;
    /**
     * 当前选择或定位到的城市信息
     */
    private TenantBean mCurrentBean;

    /**
     * 发送验证码对话框
     */
    private SendMessageCodeDialog mSendMessageCodeDialog;

    private ITransport mTransport;

    private boolean isSendCode;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_id_card_authentication;
    }


    @Override
    protected void create() {
        super.create();
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        mIsToMain = intent.getBooleanExtra("isToMain", mIsToMain);
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("司机认证");
        EditTextFormatUtil.formatIdCard(mVetIdCardNumber);

        // 定位
        BaiduLocationService.start(getThis(), new BaiduLocationService.OnLocationListener() {
            @Override
            public void onReceiveLocation() {
                String city = BaiduLocationService.getCity();
                loadCurrentTenant(BaiduLocationService.getProvince(),
                        city, false);

            }

            @Override
            public void onError() {

            }
        });
    }

    @OnClick(R.id.aty_id_car_authentication_vtv_city)
    void onCityClick() {
        SelectRegionRecyclerActivity.start(getThis());
    }

    @OnClick(R.id.aty_id_aty_authentication_text_submit)
    public void onViewClicked() {
        final String idCardNum = mVetIdCardNumber.getText().toString().trim();
        final String city = mVtvCity.getText().toString().trim();

        if (TextUtils.isEmpty(idCardNum)) {
            showToast("请输入身份证号");
            return;
        } else if (idCardNum.length() != 18 && idCardNum.length() != 15) {
            showToast("请输入正确的身份证号");
            return;
        } else if (TextUtils.isEmpty(city) || mCurrentBean == null) {
            showToast("请选择城市");
            return;
        }

//        checkIdNumber(idCardNum);


        // 暂时只有在温州可以获取运管信息
        if (TextUtils.isEmpty(mCurrentBean.getCityName()) || !mCurrentBean.getCityName().contains("温州")) {
            if (mDataBean == null) {
                mDataBean = new AuthenticationDataBean();
            }

            mDataBean.setTenantId(mCurrentBean.getId());
            mDataBean.setIdCardNumber(idCardNum);
            DriverAuthenticationActivity.start(getThis(),
                    mDataBean,
                    mCurrentBean.getPlateNumberPrefix(),
                    mIsToMain);
            return;
        }
        if (isSendCode) {
            showSendMessageDialog();
        } else {
            sendMessageCode();
        }

    }

    /**
     * 检测身份证号码是否被注册
     *
     * @param idNumber 身份证
     */
    private void checkIdNumber(final String idNumber) {

        if (mIDriver == null) {
            mIDriver = RetrofitHelper.create(IDriver.class);
        }

        mIDriver.checkIdNumber(idNumber)
                .enqueue(new MsgCallBack<BaseBean<String>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                        String resultString = response.body().getResult();
                        if (!TextUtils.isEmpty(resultString)) {
                            showToast(resultString);
                            return;
                        }




                       /* mDataBean.setTenantId(mCurrentBean.getId());
                        mDataBean.setIdCardNumber(idNumber);
                        DriverAuthenticationActivity.start(getThis(),
                                mDataBean,
                                mCurrentBean.getPlateNumberPrefix(),
                                mIsToMain);*/


                    }
                });
    }

    /**
     * 根据定位信息获取服务器相应的地区code
     *
     * @param provinceName 省份
     * @param cityName     城市
     * @param isShowDialog 是否显示加载框
     */
    private void loadCurrentTenant(String provinceName, String cityName, boolean isShowDialog) {

        if (mIUser == null) {
            mIUser = RetrofitHelper.create(IUser.class);
        }

        mIUser.getCurrentTenantByRegionName(RetrofitHelper
                .getBody(new JsonItem("provinceName", provinceName),
                        new JsonItem("cityName", cityName)))
                .enqueue(new MsgCallBack<BaseBean<TenantBean>>(getThis(), isShowDialog) {
                    @Override
                    public void onSuccess(Call<BaseBean<TenantBean>> call, Response<BaseBean<TenantBean>> response) {
                        TenantBean bean = response.body().getResult();
                        if (bean == null) {
                            return;
                        }
                        mVtvCity.setText(bean.getCityName());
                        mCurrentBean = bean;

                    }
                });

    }

    /**
     * 显示发送验证码对话框
     */
    private void showSendMessageDialog() {
        if (mSendMessageCodeDialog == null) {
            mSendMessageCodeDialog = new SendMessageCodeDialog(getThis(), R.style.NoTitleDialog);

            mSendMessageCodeDialog.setPositiveButton_(null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getDeviceVehicleInfo(mSendMessageCodeDialog.getMessageCode());
                    dialog.dismiss();
                }
            });
            mSendMessageCodeDialog.setPositiveButton_1(null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final String idCardNum = mVetIdCardNumber.getText().toString().trim();
                    if (mDataBean == null) {
                        mDataBean = new AuthenticationDataBean();
                    }
                    mDataBean.setTenantId(mCurrentBean.getId());
                    mDataBean.setIdCardNumber(idCardNum);
                    DriverAuthenticationActivity.start(getThis(),
                            mDataBean,
                            mCurrentBean.getPlateNumberPrefix(),
                            mIsToMain);
                    dialog.dismiss();
                }
            });
            mSendMessageCodeDialog.setOnClickSendMessageCodeListener(new SendMessageCodeDialog.OnClickSendMessageCodeListener() {
                @Override
                public void onSendMessage() {
                    sendMessageCode();
                }
            });
            mSendMessageCodeDialog.setCanceledOnTouchOutside(false);
        }

        mSendMessageCodeDialog.show();

    }


    /**
     * 发送短信验证码
     */
    public void sendMessageCode() {
        if (mTransport == null) {
            mTransport = RetrofitHelper.create(ITransport.class);
        }
        final String idCardNum = mVetIdCardNumber.getText().toString().trim();

        if (TextUtils.isEmpty(idCardNum)) {
            showToast("请输入身份证号");
            return;
        } else if (idCardNum.length() != 18 && idCardNum.length() != 15) {
            showToast("请输入正确的身份证号");
            return;
        }

        showProgressDialog();
        mTransport.queryVehicleInfoSendSms(idCardNum)
                .enqueue(new BaseCallBack<BaseBean<String>>() {
                    @Override
                    public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                        closeProgressDialog();
                        if (isSendCode) {
                            showToast("消息发送成功");
                        }
                        String result= response.body().getResult().toString();
                        isSendCode = true;

                        if(null == result || "".equals(result)){
                            if (mDataBean == null) {
                                mDataBean = new AuthenticationDataBean();
                            }
                            mDataBean.setTenantId(mCurrentBean.getId());
                            mDataBean.setIdCardNumber(idCardNum);
                            DriverAuthenticationActivity.start(getThis(),
                                    mDataBean,
                                    mCurrentBean.getPlateNumberPrefix(),
                                    mIsToMain);
                        }else{
                            showSendMessageDialog();
                            mSendMessageCodeDialog.startTime();
                        }
                    }

                    @Override
                    public void onError(Call<BaseBean<String>> call, Throwable t) {
                        closeProgressDialog();
                        String message = t.getMessage();
                        if (TextUtils.equals(message, "无法获取服务器数据")) {
                            // 没有运管数据，直接进入认证页面
                            if (mDataBean == null) {
                                mDataBean = new AuthenticationDataBean();
                            }
                            mDataBean.setTenantId(mCurrentBean.getId());
                            mDataBean.setIdCardNumber(idCardNum);
                            DriverAuthenticationActivity.start(getThis(),
                                    mDataBean,
                                    mCurrentBean.getPlateNumberPrefix(),
                                    mIsToMain);
                            return;
                        }

                        showToast(message);
                    }
                });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    /**
     * 获取司机车辆信息
     *
     * @param code 验证码
     */
    private void getDeviceVehicleInfo(String code) {
        final String idCardNum = mVetIdCardNumber.getText().toString();

        if (TextUtils.isEmpty(code)) {
            showToast("请输入验证码");
        }


        if (TextUtils.isEmpty(idCardNum)) {
            showToast("请输入身份证号");
            return;
        } else if (idCardNum.length() != 18 && idCardNum.length() != 15) {
            showToast("请输入正确的身份证号");
            return;
        }

        if (mTransport == null) {
            mTransport = RetrofitHelper.create(ITransport.class);
        }

        showProgressDialog();
        mTransport.getDeviceVehicleInfo(idCardNum,
                code)
                .enqueue(new BaseCallBack<BaseBean<DeviceVehicleBean>>() {
                    @Override
                    public void onSuccess(Call<BaseBean<DeviceVehicleBean>> call, Response<BaseBean<DeviceVehicleBean>> response) {
                        closeProgressDialog();
                        DeviceVehicleBean bean = response.body().getResult();

                        if (mSendMessageCodeDialog != null) {
                            mSendMessageCodeDialog.cancel();
                        }

                        if (mDataBean == null) {
                            mDataBean = new AuthenticationDataBean();
                        }

                        mDataBean.setTenantId(mCurrentBean.getId());
                        mDataBean.setIdCardNumber(idCardNum);
                        DriverAuthenticationActivity.start(getThis(),
                                mDataBean,
                                bean,
                                mCurrentBean.getPlateNumberPrefix(),
                                mIsToMain);

                    }

                    @Override
                    public void onError(Call<BaseBean<DeviceVehicleBean>> call, Throwable t) {
                        closeProgressDialog();
                        String message = t.getMessage();
                        if (TextUtils.equals(message, "无法获取服务器数据")) {
                            // 没有运管数据，直接进入认证页面
                            if (mDataBean == null) {
                                mDataBean = new AuthenticationDataBean();
                            }
                            mDataBean.setTenantId(mCurrentBean.getId());
                            mDataBean.setIdCardNumber(idCardNum);
                            DriverAuthenticationActivity.start(getThis(),
                                    mDataBean,
                                    mCurrentBean.getPlateNumberPrefix(),
                                    mIsToMain);
                            return;
                        }

                        showToast(message);
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_skip, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_skip) {
            // 如果登录成功进入首页，否则回到登录页
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        if (mSendMessageCodeDialog != null && mSendMessageCodeDialog.isShowing()) {
            mSendMessageCodeDialog.cancel();
        }
        super.onDestroy();
    }

    @Override
    protected void onToolBarBackPressed() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (UserInfoHelper.getInstance().isLogin() && mIsToMain) {
            startActivity(MainActivity.class);
        }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != RESULT_OK && data == null) {
            return;
        }

        if (requestCode == SelectRegionRecyclerActivity.REQUEST_CODE) {
            mCurrentBean = SelectRegionRecyclerActivity.loadRegionInfo(data);
            mVtvCity.setText(mCurrentBean.getCityName());
        } else if (requestCode == DriverAuthenticationActivity.REQUEST_CODE) {
            setResult(RESULT_OK);
            onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BaiduLocationService.onRequestPermissionsResult(getThis(), requestCode, permissions, grantResults);
    }

    public static void start(Activity activity, boolean isToMain) {
        Intent starter = new Intent(activity, IdCardAuthenticationActivity.class);
        starter.putExtra("isToMain", isToMain);
        activity.startActivityForResult(starter, REQUEST_CODE);
    }

    public static void start(Fragment fragment, boolean isToMain) {
        Intent starter = new Intent(fragment.getContext(), IdCardAuthenticationActivity.class);
        starter.putExtra("isToMain", isToMain);
        fragment.startActivityForResult(starter, REQUEST_CODE);
    }
}
