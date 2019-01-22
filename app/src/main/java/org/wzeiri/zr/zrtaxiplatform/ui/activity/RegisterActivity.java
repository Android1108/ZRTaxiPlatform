package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.LoginBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IUser;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItem;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.util.CountDownHelper;
import org.wzeiri.zr.zrtaxiplatform.util.UserInfoHelper;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueEditText;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 注册
 *
 * @author k-lm on 2017/11/17.
 */

public class RegisterActivity extends ActionbarActivity implements CountDownHelper.OnCountDownListener {
    @BindView(R.id.aty_register_vet_phone)
    ValueEditText mVetPhone;

    @BindView(R.id.aty_register_vet_code)
    ValueEditText mVetCode;
    @BindView(R.id.aty_register_text_send_code)
    TextView mTextSendCode;
    @BindView(R.id.aty_register_vet_password)
    ValueEditText mVetPassword;
    @BindView(R.id.aty_register_cb_registration_agreement)
    CheckBox mCheckBox;


    private CountDownHelper mCountDownHelper;

    private IUser mIUser = null;

    private String mKey;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @OnClick(R.id.aty_register_text_registration_agreement)
    void onAgreementClick() {
        startActivity(RegistrationAgreementActivity.class);
    }

    @OnClick(R.id.aty_register_text_register)
    public void onClickRegister() {
        final String phone = mVetPhone.getText().toString();
        String code = mVetCode.getText().toString();
        final String password = mVetPassword.getText().toString();

        if (TextUtils.isEmpty(phone.trim())) {
            showToast("请输入手机号码");
            return;
        }

        if (phone.trim().length() != 11) {
            showToast("请输入正确的手机号码");
            return;
        }

        if (TextUtils.isEmpty(code.trim())) {
            showToast("请输入验证码");
            return;
        }

        if (TextUtils.isEmpty(password.trim())) {
            showToast("请输入密码");
            return;
        }

        if (!mCheckBox.isChecked()) {
            showToast("请阅读并勾选《注册协议》");
            return;
        }

        if (mKey == null) {
            showToast("没有发送过验证码");
            return;
        }
        showProgressDialog();
        mIUser.registerUser(RetrofitHelper
                .getBody(new JsonItem("phoneNumber", phone),
                        new JsonItem("password", password),
                        new JsonItem("key", mKey),
                        new JsonItem("code", code)))
                .enqueue(new MsgCallBack<BaseBean<LoginBean>>(this, true) {
                    @Override
                    public void onSuccess(Call<BaseBean<LoginBean>> call, Response<BaseBean<LoginBean>> response) {
                        LoginBean bean = response.body().getResult();
                        if (bean == null) {
                            showToast("无法获取服务器数据");
                            return;
                        }
                        UserInfoHelper.getInstance().save(bean);
                        UserInfoHelper.getInstance().savePhone(phone);
                        showToast("注册成功");
                        // 登录数据由接口返回
                        startActivity(IdCardAuthenticationActivity.class);
                        //DriverAuthenticationActivity.start(getThis(), id);
                        finish();
                    }
                });
    }


    @OnClick(R.id.aty_register_text_send_code)
    public void onSendCode() {
        String phone = mVetPhone.getText().toString();
        if (TextUtils.isEmpty(phone.trim())) {
            showToast("请输入手机号码");
            return;
        }

        if (phone.trim().length() != 11) {
            showToast("请输入正确的手机号码");
            return;
        }

        // 获取接口实例l
        if (mIUser == null) {
            mIUser = RetrofitHelper.create(IUser.class);
        }

        mIUser.sendRegisterCode(RetrofitHelper
                .getBody(new JsonItem("phoneNumber", phone)))
                .enqueue(new MsgCallBack<BaseBean<String>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                        showToast("发送成功");
                        mKey = response.body().getResult();

                        mTextSendCode.setEnabled(false);
                        mTextSendCode.setTextColor(ContextCompat.getColor(getThis(), R.color.gray50));
                        mCountDownHelper.startTime();
                    }
                });


    }


    @Override
    protected void init() {
        super.init();
        setCenterTitle("注册");
        mCountDownHelper = new CountDownHelper();
        mCountDownHelper.setOnCountDownListener(this);

        setCenTerTitleColor(ContextCompat.getColor(this, R.color.black75));
        setToolBarBackgroundColor(ContextCompat.getColor(this, R.color.white));
        setNoticeBarColor(ContextCompat.getColor(this, R.color.white));

    }

    @Override
    public void onCountDownFinish() {
        mTextSendCode.setTextColor(ContextCompat.getColor(getThis(), R.color.orange1));
        mTextSendCode.setText("获取验证码");
        mTextSendCode.setEnabled(true);
    }

    @Override
    public void onCountDownTick(long millisUntilFinished) {
        int time = (int) (millisUntilFinished / 1000);
        mTextSendCode.setText(time + "S");
    }

    @Override
    protected void onDestroy() {
        mCountDownHelper.cancel();
        super.onDestroy();
    }
}
