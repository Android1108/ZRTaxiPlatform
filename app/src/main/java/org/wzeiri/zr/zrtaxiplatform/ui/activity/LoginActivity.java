package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;

import org.wzeiri.zr.zrtaxiplatform.MyApplication;
import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.LoginBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.ITokenAuth;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItem;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.WidgetActivity;
import org.wzeiri.zr.zrtaxiplatform.util.UserInfoHelper;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 登录
 *
 * @author k-lm on 2017/11/15.
 */

public class LoginActivity extends WidgetActivity {

    public static int REQUEST_CODE = 10010;


    @BindView(R.id.aty_login_edit_phone)
    EditText atyLoginEditPhone;
    @BindView(R.id.aty_login_edit_password)
    EditText atyLoginEditPassword;

    @BindView(R.id.aty_login_img_logo)
    ImageView mImageLogo;

    private ITokenAuth mITokenAuth;

    /**
     * 点击退出时间
     */
    private long mBackTime = 0;


    @Override
    protected void create() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @OnClick(R.id.aty_login_text_forget_password)
    void onForgetPasswordCLick() {
        startActivity(ForgetPasswordActivity.class);
    }

    @OnClick(R.id.aty_login_text_login)
    public void onAtyLoginTextLoginClicked() {
        final String userName = atyLoginEditPhone.getText().toString();
        String password = atyLoginEditPassword.getText().toString();

        if (TextUtils.isEmpty(userName.trim())) {
            showToast("请输入手机号码");
            return;
        }

        if (userName.trim().length() != 11) {
            showToast("请输入正确的手机号码");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            showToast("请输入密码");
            return;
        }


        if (mITokenAuth == null) {
            mITokenAuth = RetrofitHelper.create(ITokenAuth.class);


        }

        mITokenAuth.login(RetrofitHelper
                .getBody(new JsonItem("userNameOrEmailOrPhone", userName),
                        new JsonItem("password", password),
                        new JsonItem("registrationId", JPushInterface.getRegistrationID(getThis()))))
                .enqueue(new MsgCallBack<BaseBean<LoginBean>>(this, true) {
                    @Override
                    public void onSuccess(Call<BaseBean<LoginBean>> call, Response<BaseBean<LoginBean>> response) {
                        UserInfoHelper.getInstance().save(response.body().getResult());
                        UserInfoHelper.getInstance().savePhone(userName);
                        UserInfoHelper.getInstance().loginSuccess();
                        // 获取MainActivity的实例
                        Activity activity = ((MyApplication) getApplication()).getActivity(MainActivity.class);
                        //如果MainActivity为null ，说明MainActivity没有打开，则进入MainActivity
                        if (activity == null) {
                            startActivity(MainActivity.class);
                        }

                        setResult(RESULT_OK, getIntent());
                        finish();
                    }


                });


    }

    @OnClick(R.id.aty_login_text_register)
    public void onAtyLoginTextRegisterClicked() {
        startActivity(RegisterActivity.class);
    }


  /*  @OnClick(R.id.aty_login_img_logo)
    void onImageClick(View view) {
        startActivity(MainActivity.class);
    }*/

    @Override
    protected void init() {
        super.init();
        // 判断是否登录
        if (UserInfoHelper.getInstance().isLogin()) {
            // 判断是否失效
            if (UserInfoHelper.getInstance().isInvalid()) {
                UserInfoHelper.getInstance().clearUserInfo();
            } else {
                startActivity(MainActivity.class);
                finish();
            }

        }

        /*if (Config.DEBUG) {
            atyLoginEditPhone.setText("18767736210");
            atyLoginEditPassword.setText("123456");
        }*/
    }


    public static void start(Activity activity) {
        Intent starter = new Intent(activity, LoginActivity.class);
        activity.startActivityForResult(starter, REQUEST_CODE);
    }

    public static void start(Fragment fragment) {
        Intent starter = new Intent(fragment.getContext(), LoginActivity.class);
        fragment.startActivityForResult(starter, REQUEST_CODE);
    }


    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        // 设置时间间隔3s
        if (currentTime - mBackTime >= 3000) {
            mBackTime = currentTime;
            showToast("再按一次退出" + getString(R.string.app_name));
        } else {
            // 关闭所有页面
            ((MyApplication) getApplication()).finishAllActivity();
        }
    }
}
