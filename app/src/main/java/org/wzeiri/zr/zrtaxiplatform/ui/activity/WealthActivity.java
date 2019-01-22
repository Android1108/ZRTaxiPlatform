package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.WalletDetailBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IWallet;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.util.CalculateUtil;
import org.wzeiri.zr.zrtaxiplatform.util.UserInfoHelper;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 财富
 *
 * @author k-lm on 2017/11/23.
 */

public class WealthActivity extends ActionbarActivity {
    private WalletDetailBean mWalletDetailBean;

    @BindView(R.id.aty_my_wealth_text_money)
    TextView mMoney;


    private IWallet mIWallet;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_wealth;
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("T币");
    }

    @Override
    protected void initData() {
        super.initData();
        getData();

    }

    /**
     * 获取数据
     */
    private void getData() {
        if (mIWallet == null) {
            mIWallet = RetrofitHelper.create(IWallet.class);
        }

        mIWallet.getWalletDetails()
                .enqueue(new MsgCallBack<BaseBean<WalletDetailBean>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<WalletDetailBean>> call, Response<BaseBean<WalletDetailBean>> response) {
                        mWalletDetailBean = response.body().getResult();
                        if (mWalletDetailBean == null) {
                            mWalletDetailBean = new WalletDetailBean();
                            mMoney.setText("￥0.00");
                        } else {
                            mMoney.setText("￥" + CalculateUtil.getFormatToString(mWalletDetailBean.getWalletBalance().doubleValue()));
                        }

                    }
                });
    }

//    @OnClick(R.id.aty_my_wealth_text_my_ali_pay)
//    void onClickMyALiPay() {
//        MyAliPayActivity.start(getThis(), mWalletDetailBean);
//    }

    @OnClick(R.id.aty_my_wealth_text_my_bank_card)
    void onClickMyBankCard() {
        MyBankCardActivity.start(getThis(), mWalletDetailBean);
    }

    @OnClick(R.id.aty_my_wealth_text_withdrawals)
    void onClickWithdrawals() {


      /*  if (mWalletDetailBean == null ||
                (TextUtils.isEmpty(mWalletDetailBean.getBankCardNo()) &&
                        TextUtils.isEmpty(mWalletDetailBean.getZhifubaoAccount()))) {
            showToast("请绑定支付宝或银行卡");
            return;
        }*/
       Intent intent=new Intent(getApplication(),WebActivity1.class);
       intent.putExtra("url",UserInfoHelper.getInstance().getmUrl().toString());
       startActivity(intent);
//        WebActivity1.startContent(getApplication(), UserInfoHelper.getInstance().getmUrl());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_balance_info, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_balance_info:
                startActivity(BalanceInfoActivity.class);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }

        WalletDetailBean bean;
        switch (requestCode) {
//            case MyAliPayActivity.REQUEST_CODE:
//                bean = data.getParcelableExtra(MyAliPayActivity.KEY_DETAIL);
//                if (mWalletDetailBean == null) {
//                    mWalletDetailBean = new WalletDetailBean();
//                }
//                mWalletDetailBean.setZhifubaoAccountOwner(bean.getZhifubaoAccountOwner());
//                mWalletDetailBean.setZhifubaoAccount(bean.getZhifubaoAccount());
//
//                break;
            case MyBankCardActivity.REQUEST_CODE:
                bean = data.getParcelableExtra(MyBankCardActivity.KEY_DETAIL);
                if (mWalletDetailBean == null) {
                    mWalletDetailBean = new WalletDetailBean();
                }
                mWalletDetailBean.setBankCardOwner(bean.getBankCardOwner());
                mWalletDetailBean.setBankCardNo(bean.getBankCardNo());
                mWalletDetailBean.setBankCardBindPhoneNumber(bean.getBankCardBindPhoneNumber());
                mWalletDetailBean.setBankName(bean.getBankName());
                break;
            case ApplyWithdrawCashActivity.REQUEST_CODE:
                getData();
                break;

        }
    }
}
