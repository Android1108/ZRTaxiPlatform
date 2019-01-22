package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.WalletDetailBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IWallet;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItem;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.util.CalculateUtil;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueEditText;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;
import org.wzeiri.zr.zrtaxiplatform.widget.tag.TagRadioGroup;
import org.wzeiri.zr.zrtaxiplatform.widget.tag.TagRatioButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 申请提现
 *
 * @author k-lm on 2017/12/13.
 */

public class ApplyWithdrawCashActivity extends ActionbarActivity {
    @BindView(R.id.aty_apply_withdraw_cash_vet_money)
    ValueEditText mVetMoney;
    @BindView(R.id.aty_apply_withdraw_cash_vtv_max_money)
    ValueTextView mVtvMaxMoney;
    @BindView(R.id.aty_apply_withdraw_cash_trb_withdrawals_alipay)
    TagRatioButton mTrbWithdrawalsAlipay;
    @BindView(R.id.aty_apply_withdraw_cash_trb_withdrawals_bank)
    TagRatioButton mTrbWithdrawalsBank;
    @BindView(R.id.aty_apply_withdraw_cash_trg_withdrawals_mode)
    TagRadioGroup mTrgWithdrawalsMode;
    @BindView(R.id.aty_apply_withdraw_cash_text_rule)
    TextView mTextRule;

    private WalletDetailBean mWalletDetailBean;

    public static final String KEY_DETAIL = MyAliPayActivity.KEY_DETAIL;

    public static final int REQUEST_CODE = 10088;
    /**
     * 提现类型
     */
    private WithdrawalsType mCashRequestType;

    private IWallet mIWallet;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_apply_withdraw_cash;
    }


    @OnClick(R.id.aty_apply_withdraw_cash_text_whole_withdrawals)
    public void onmTextWholeWithdrawalsClicked() {
        if (mWalletDetailBean == null || mWalletDetailBean.getWalletBalance() == null) {
            mVetMoney.setText("0.00");
            return;
        }

        mVetMoney.setText(CalculateUtil
                .getFormatToString(mWalletDetailBean
                        .getWalletBalance()
                        .doubleValue()));


    }

    @OnClick(R.id.aty_apply_withdraw_cash_text_withdrawals)
    public void onmTextWithdrawalsClicked() {
        String moneyStr = mVetMoney.getText().toString();
        if (TextUtils.isEmpty(moneyStr)) {
            showToast("请输入提现金额");
            return;
        }

        double money = Double.valueOf(moneyStr);

        if (money == 0) {
            showToast("提现金额必须大于0");
            return;
        }

        if (mWalletDetailBean.getWalletBalance() == null
                || money > mWalletDetailBean.getWalletBalance().doubleValue()) {
            showToast("输入的金额大于可提现金额");
            return;
        }

        if (mCashRequestType == null) {
            showToast("请选择提现方式");
            return;
        }


        if (mIWallet == null) {
            mIWallet = RetrofitHelper.create(IWallet.class);
        }

        mIWallet.requestCash(RetrofitHelper
                .getBody(new JsonItem("amount", moneyStr),
                        new JsonItem("cashRequestType", mCashRequestType.toString())))
                .enqueue(new MsgCallBack<BaseBean<String>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                        setResult(RESULT_OK, getIntent());
                        showToast("提现成功");
                        finish();
                    }
                });

    }


    @Override
    protected void init() {
        super.init();
        setCenterTitle("申请提现");

        mTrgWithdrawalsMode.setOnCheckedChangeListener(new TagRadioGroup.OnCheckChangeListener() {
            @Override
            public void onCheckChange(TagRadioGroup group, TagRatioButton childButton, boolean isCheck) {
                switch (childButton.getId()) {
                    case R.id.aty_apply_withdraw_cash_trb_withdrawals_alipay:
                        mCashRequestType = WithdrawalsType.AL_PLAY;
                        break;
                    case R.id.aty_apply_withdraw_cash_trb_withdrawals_bank:
                        mCashRequestType = WithdrawalsType.BANK;
                        break;
                }
            }
        });

        mVetMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s + "";
                int dotCount = text.indexOf(".");
                if (dotCount != -1 && text.length() - dotCount > 3) {
                    text = text.substring(0, dotCount + 3);
                    mVetMoney.setText(text);
                    mVetMoney.setSelection(dotCount + 3);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        String rule = "1、单笔最小提现金额为100元 \n" +
                "2、提现需要1~3个工作日内到账，如遇节假日顺延";

        mTextRule.setText(rule);
    }


    @Override
    protected void initData() {
        super.initData();
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        mWalletDetailBean = intent.getParcelableExtra(KEY_DETAIL);

        if (mWalletDetailBean == null) {
            return;
        }

        mTrgWithdrawalsMode.setVisibility(View.VISIBLE);

        // 余额
        if (mWalletDetailBean.getWalletBalance() != null) {
            mVtvMaxMoney.setText(CalculateUtil
                    .getFormatToString(mWalletDetailBean
                            .getWalletBalance()
                            .doubleValue()));
        }

        // 支付宝
        if (!TextUtils.isEmpty(mWalletDetailBean.getZhifubaoAccount())) {
            mTrbWithdrawalsAlipay.setVisibility(View.VISIBLE);
            mTrbWithdrawalsAlipay.setBottomText(mWalletDetailBean.getZhifubaoAccount());
        } else {
            mTrbWithdrawalsAlipay.setVisibility(View.GONE);
        }


        //银行卡
        if (!TextUtils.isEmpty(mWalletDetailBean.getBankCardNo())) {
            mTrbWithdrawalsBank.setVisibility(View.VISIBLE);
            mTrbWithdrawalsBank.setBottomText(mWalletDetailBean.getBankCardNo());
        } else {
            mTrbWithdrawalsBank.setVisibility(View.GONE);
        }

    }

    /**
     * 体现类型
     */
    private enum WithdrawalsType {
        /**
         * 支付宝
         */
        AL_PLAY("zhifubao"),
        /**
         * 银行卡
         */
        BANK("bankCard");

        private final String type;

        private WithdrawalsType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return type;
        }
    }


    public static void start(Activity activity, WalletDetailBean bean) {
        Intent starter = new Intent(activity, ApplyWithdrawCashActivity.class);
        starter.putExtra(KEY_DETAIL, bean);
        activity.startActivityForResult(starter, REQUEST_CODE);
    }


}
