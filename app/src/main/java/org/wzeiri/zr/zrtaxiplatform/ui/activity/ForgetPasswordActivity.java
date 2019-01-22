package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IUser;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItem;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.util.CountDownHelper;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 忘记密码
 *
 * @author k-lm on 2017/12/22.
 */

public class ForgetPasswordActivity extends ActionbarActivity implements CountDownHelper.OnCountDownListener {
    @BindView(R.id.aty_forget_password_vet_phone)
    ValueEditText mVetPhone;
    @BindView(R.id.aty_forget_password_vet_code)
    ValueEditText mVetCode;
    @BindView(R.id.aty_forget_password_text_send_code)
    TextView mTextSendCode;
    @BindView(R.id.aty_forget_password_vet_password)
    ValueEditText mVetPassword;
    @BindView(R.id.aty_forget_password_vet_again_password)
    ValueEditText mVetAgainPassword;

    private String mCodeKey;
    private CountDownHelper mCountDownHelper;


    private IUser mIUser;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget_password;
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("找回密码");
        mCountDownHelper = new CountDownHelper();
        mCountDownHelper.setOnCountDownListener(this);
    }

    @OnClick(R.id.aty_forget_password_text_send_code)
    public void onmTextSendCodeClicked() {
        String phone = mVetPhone.getText().toString();

        if (TextUtils.isEmpty(phone)) {
            showToast("请输入手机号码");
            return;
        } else if (phone.length() != 11) {
            showToast("请输入正确的手机号码");
            return;
        }

        if (mIUser == null) {
            mIUser = RetrofitHelper.create(IUser.class);
        }

        mIUser.sendForgetPasswordCode(RetrofitHelper
                .getBody(new JsonItem("phoneNumber", phone)))
                .enqueue(new MsgCallBack<BaseBean<String>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                        mCodeKey = response.body().getResult();
                        showToast("验证码发送成功");

                        mTextSendCode.setEnabled(false);
                        mTextSendCode.setTextColor(ContextCompat.getColor(getThis(), R.color.gray50));
                        mCountDownHelper.startTime();
                    }
                });




    }

    @OnClick(R.id.aty_forget_password_text_submit)
    public void onmTextForgetPasswordClicked() {
        String phone = mVetPhone.getText().toString().trim();
        String code = mVetCode.getText().toString().trim();
        String password = mVetPassword.getText().toString().trim();
        String againPassword = mVetAgainPassword.getText().toString().trim();


        if (TextUtils.isEmpty(phone)) {
            showToast("请输入手机号");
            return;
        }

        if (phone.length() != 11) {
            showToast("请输入正确的手机号");
            return;
        }

        if (TextUtils.isEmpty(mCodeKey)) {
            showToast("请发送验证码");
            return;
        }

        if (TextUtils.isEmpty(code)) {
            showToast("请输入验证码");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            showToast("请输入密码");
            return;
        }

        if (!TextUtils.equals(againPassword, password)) {
            showToast("两次密码输入的不一致");
            return;
        }

        if (mIUser == null) {
            mIUser = RetrofitHelper.create(IUser.class);
        }

        mIUser.forgetPassword(RetrofitHelper
                .getBody(new JsonItem("phoneNumber", phone),
                        new JsonItem("password", password),
                        new JsonItem("key", mCodeKey),
                        new JsonItem("code", code)))
                .enqueue(new MsgCallBack<BaseBean<String>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                        showToast("密码修改成功");
                        finish();
                    }
                });

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
