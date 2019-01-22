package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;

import org.wzeiri.zr.zrtaxiplatform.MyApplication;
import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IUpdatePassword;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItem;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.util.UserInfoHelper;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 修改密码
 *
 * @author k-lm on 2017/11/20.
 */

public class ChangePasswordActivity extends ActionbarActivity {
    @BindView(R.id.aty_change_password_vet_older_password)
    ValueEditText atyChangePasswordVetOlderPassword;
    @BindView(R.id.aty_change_password_vet_new_password)
    ValueEditText atyChangePasswordVetNewPassword;
    @BindView(R.id.aty_change_password_vet_again_password)
    ValueEditText atyChangePasswordVetAgainPassword;

    private IUpdatePassword mIUpdatePassword;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_password;
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("修改密码");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.aty_change_password_text_submit)
    public void atyChangePasswordTextSubmit() {
        String currentPassword = atyChangePasswordVetOlderPassword.getText().toString();
        String newPassword = atyChangePasswordVetNewPassword.getText().toString();
        String newPasswordAgain = atyChangePasswordVetAgainPassword.getText().toString();

        if (TextUtils.isEmpty(currentPassword.trim())) {
            showToast("请输入原密码");
            return;
        }

        if (TextUtils.isEmpty(newPassword.trim())) {
            showToast("请输入新密码");
            return;
        }

        if (TextUtils.isEmpty(newPasswordAgain.trim())) {
            showToast("请再一次输入新密码");
            return;
        }

        if (newPassword.equals(newPasswordAgain)) {
            /*showToast("两次输入的密码一致");*/
        } else {
            showToast("两次输入的密码不一致");
            return;
        }

        if (mIUpdatePassword == null) {
            mIUpdatePassword = RetrofitHelper.create(IUpdatePassword.class);
        }

        mIUpdatePassword.updatePassword(RetrofitHelper
                    .getBody(new JsonItem("currentPassword", currentPassword),
                            new JsonItem("newPassword", newPassword)))
                    .enqueue(new MsgCallBack<BaseBean<String>>(this, true) {
                        @Override
                        public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                            showToast("修改成功");
                            UserInfoHelper.getInstance().clearUserInfo();
                            startActivity(LoginActivity.class);
                            //结束其他所有的Activity
                            ((MyApplication) getApplication()).finishAllActivity(LoginActivity.class);
                        }
                    });
    }
}
