package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.UploadResultBean;
import org.wzeiri.zr.zrtaxiplatform.bean.WalletDetailBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.ISundry;
import org.wzeiri.zr.zrtaxiplatform.network.api.IWallet;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItem;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.util.UserInfoHelper;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueEditText;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;
import org.wzeiri.zr.zrtaxiplatform.widget.dialog.BottomDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author k-lm on 2017/11/25.
 */

public class MyBankCardActivity extends ActionbarActivity {
    @BindView(R.id.aty_my_bank_card_edt_name)
    ValueEditText mEdtName;
    @BindView(R.id.aty_my_bank_card_edt_bank_number)
    ValueEditText mEdtBankNumber;
    @BindView(R.id.aty_my_bank_card_vdt_bank)
    ValueTextView mEdtBank;
    @BindView(R.id.aty_my_bank_card_edt_phone)
    ValueEditText mEdtPhone;
    @BindView(R.id.aty_my_bank_card_text_select_bank_number)
    TextView mStNumber;

    public static final String KEY_DETAIL = MyAliPayActivity.KEY_DETAIL;

    public static final int REQUEST_CODE = 10087;

    private IWallet mIWallet;

    /**
     * 车型
     */
    private List<String> mBankList;

    /**
     * 选择车型对话框
     */
    private BottomDialog mSelectBankDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_bank_card;
    }

    @OnClick(R.id.aty_my_bank_card_text_select_bank_number)
    void Select(){
        mEdtBankNumber.setEnabled(true);
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("我的银行卡");
    }

    @Override
    protected void initData() {
        super.initData();
        Intent intent = getIntent();
        loadCarModeData();
        if (intent == null) {
            return;
        }

        WalletDetailBean bean = intent.getParcelableExtra(KEY_DETAIL);
        if (bean == null) {
            return;
        }

        mEdtName.setText(bean.getBankCardOwner());
        mEdtBankNumber.setText(bean.getBankCardNo());
        mEdtBank.setText(bean.getBankName());
        mEdtPhone.setText(bean.getBankCardBindPhoneNumber());
        if (mEdtBankNumber.getText().length()>0)
        {
            mEdtBankNumber.setEnabled(false);
        }
    }

    /**
     * 加载车型数据
     */
    private void loadCarModeData() {
        ISundry sundry = RetrofitHelper.create(ISundry.class);
        sundry.getCarModels()
                .enqueue(new MsgCallBack<BaseBean<List<String>>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<List<String>>> call, Response<BaseBean<List<String>>> response) {
                        mBankList = response.body().getResult();
                    }
                });
    }


    @OnClick(R.id.aty_my_bank_card_text_save)
    public void onViewClicked() {
        final String name = mEdtName.getText().toString();
        final String bankNum = mEdtBankNumber.getText().toString();
        final String bankName = mEdtBank.getText().toString();
        final String phone = mEdtPhone.getText().toString();

        if (TextUtils.isEmpty(name)) {
            showToast("请输入持卡人姓名");
            return;
        }
        if (TextUtils.isEmpty(bankNum)) {
            showToast("请输入银行卡号");
            return;
        }

        if (bankNum.length() < 16 || bankNum.length() > 19) {
            showToast("请输入正确的银行卡号");
            return;
        }
        if (TextUtils.isEmpty(bankName)) {
            showToast("请选择开户银行");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            showToast("请输入银行预留手机号");
            return;
        }

        if (phone.length() != 11) {
            showToast("请输入正确的银行预留手机号");
            return;
        }

        if (mIWallet == null) {
            mIWallet = RetrofitHelper.create(IWallet.class);
        }

        mIWallet.bindingBankCard(RetrofitHelper
                .getBody(new JsonItem("bankCardOwner", name),
                        new JsonItem("bankCardNo", bankNum),
                        new JsonItem("bankName", bankName),
                        new JsonItem("bankCardBindPhoneNumber", phone)))
                .enqueue(new MsgCallBack<BaseBean<String>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                        WalletDetailBean bean = new WalletDetailBean();
                        bean.setBankName(bankName);
                        bean.setBankCardBindPhoneNumber(phone);
                        bean.setBankCardNo(bankNum);
                        bean.setBankCardOwner(name);
                        Intent intent = new Intent();
                        intent.putExtra(KEY_DETAIL, bean);
                        setResult(RESULT_OK, intent);
                        showToast("绑定成功");
                        UserInfoHelper.getInstance().setBankCardNo(bankNum);
                        finish();
                    }
                });

    }


    @OnClick(R.id.aty_my_bank_card_text_select_bank)
    void OnSelectBank() {
        if (mBankList == null || mBankList.size() == 0) {
            showToast("当前没有可以选择的银行");
            return;
        }
        showSelectCarModelsDialog();
    }

    /**
     * 显示选择车型对话框
     */
    private void showSelectCarModelsDialog() {
        if (mSelectBankDialog == null) {
            mSelectBankDialog = new BottomDialog(getThis(), R.style.NoTitleDialog);
            mSelectBankDialog.setOnItemClickListener(new BottomDialog.OnItemClickListener() {
                @Override
                public void onItemClick(BottomDialog dialog, int position) {
                    mEdtBank.setText(mBankList.get(position));
                }
            });
            mSelectBankDialog.addDataList(mBankList);
        }
        mSelectBankDialog.show();
    }


    public static void start(Activity activity, WalletDetailBean bean) {
        Intent starter = new Intent(activity, MyBankCardActivity.class);
        starter.putExtra(KEY_DETAIL, bean);
        activity.startActivityForResult(starter, REQUEST_CODE);
    }

}
