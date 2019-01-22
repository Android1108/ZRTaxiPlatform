package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.WalletDetailBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IWallet;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItem;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 我的支付宝
 *
 * @author k-lm on 2017/11/25.
 */

public class MyAliPayActivity extends ActionbarActivity {

    public static final int REQUEST_CODE = 10086;

    public static final String KEY_DETAIL = "detail";

    @BindView(R.id.aty_my_ali_pay_edt_full_name)
    ValueEditText mEdtFullName;
    @BindView(R.id.aty_my_ali_pay_edt_ali_user_name)
    ValueEditText mEdtAliUserName;
    @BindView(R.id.aty_my_ali_pay_edt_again_ali_user_name)
    ValueEditText mEdtAgainAliUserName;


    private IWallet mIWallet;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_ali_pay;
    }


    @Override
    protected void init() {
        super.init();
        setCenterTitle("我的支付宝");
    }


    @Override
    protected void initData() {
        super.initData();

        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        WalletDetailBean bean = intent.getParcelableExtra("detail");
        if (bean == null) {
            return;
        }

        mEdtFullName.setText(bean.getZhifubaoAccountOwner());
        mEdtAliUserName.setText(bean.getZhifubaoAccount());

    }

    @OnClick(R.id.aty_my_ali_pay_text_save)
    public void onViewClicked() {
        final String name = mEdtFullName.getText().toString();
        final String userName = mEdtAliUserName.getText().toString();
        final String alginUserName = mEdtAgainAliUserName.getText().toString();


        if (TextUtils.isEmpty(name)) {
            showToast("请输入姓名");
            return;
        }

        if (TextUtils.isEmpty(userName)) {
            showToast("请输入支付宝帐号");
            return;
        }

        if (!TextUtils.equals(userName, alginUserName)) {
            showToast("两次输入的支付宝帐号不一致");
            return;
        }


        if (mIWallet == null) {
            mIWallet = RetrofitHelper.create(IWallet.class);
        }

        mIWallet.bindingZhifubao(RetrofitHelper
                .getBody(new JsonItem("owner", name),
                        new JsonItem("account", userName)))
                .enqueue(new MsgCallBack<BaseBean<String>>(getThis()) {
                    @Override
                    public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                        WalletDetailBean bean = new WalletDetailBean();
                        bean.setZhifubaoAccountOwner(name);
                        bean.setZhifubaoAccount(userName);

                        Intent intent = new Intent();
                        intent.putExtra(KEY_DETAIL, bean);
                        setResult(RESULT_OK, intent);
                        showToast("保存成功");
                        onBackPressed();
                    }
                });


    }

    public static void start(Activity activity, WalletDetailBean bean) {
        Intent starter = new Intent(activity, MyAliPayActivity.class);
        starter.putExtra(KEY_DETAIL, bean);
        activity.startActivityForResult(starter, REQUEST_CODE);
    }
}
