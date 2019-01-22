package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.OilCardBean;
import org.wzeiri.zr.zrtaxiplatform.bean.OilRechargeTempBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IOilCard;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItem;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItemArray;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueEditText;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;
import org.wzeiri.zr.zrtaxiplatform.widget.dialog.BottomDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 添加加油卡
 *
 * @author k-lm on 2018/1/5.
 */

public class AddRefuelingCardActivity extends ActionbarActivity {
    @BindView(R.id.aty_add_refueling_card_vtv_card_type)
    ValueTextView mCardType;
    @BindView(R.id.aty_add_refueling_card_vtv_card_number)
    ValueEditText mCardNumber;

    public static final int REQUEST_CODE = 10021;
    /**
     * 油卡类型
     */
    public static final String KEY_CARD_INFO = "cardType";

    /**
     * 加油卡类型id
     */
    private int mTypeId = -1;

    private BottomDialog mBottomDialog;

    private IOilCard mIOilCard;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_refueling_card;
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("添加新加油卡");
    }

    @Override
    protected void initData() {
        super.initData();

    }

    @OnClick(R.id.aty_add_refueling_card_text_submit)
    public void onViewClicked() {
        final String cardNumber = mCardNumber.getText().toString().trim();

        if (mTypeId < 0) {
            showToast("请选择加油类型");
            return;
        }

        if (TextUtils.isEmpty(cardNumber)) {
            showToast("请输入加油卡号");
            return;
        }

        if (cardNumber.length() != 16 && cardNumber.length() != 19) {
            showToast("请输入正确的加油卡号");
            return;
        }


        if (mIOilCard == null) {
            mIOilCard = RetrofitHelper.create(IOilCard.class);
        }

        mIOilCard.createOrUpdateOilCard(
                RetrofitHelper.getBody(
                        new JsonItem("oilCardType", mTypeId),
                        new JsonItem("oilCardNumber", cardNumber)))
                .enqueue(new MsgCallBack<BaseBean<String>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                        OilCardBean bean = new OilCardBean();
                        bean.setOilCardNumber(cardNumber);
                        bean.setOilCardType(mTypeId);
                        bean.setOilCardTypeName(mCardType.getText().toString());
                        Intent intent = new Intent();
                        intent.putExtra(KEY_CARD_INFO, bean);
                        setResult(RESULT_OK, intent);
                        showToast("绑卡成功");
                        finish();
                    }
                });

    }

    @OnClick(R.id.aty_add_refueling_card_vtv_card_type)
    void selectCardType() {
        String type = mCardType.getText().toString();
        showBottomDialog(type);
    }


    /**
     * 显示加油卡类型对话框
     *
     * @param selectDate 已选择的内容
     */
    private void showBottomDialog(String selectDate) {
        if (mBottomDialog == null) {
            mBottomDialog = new BottomDialog(this, R.style.NoTitleDialog);
            String[] stringArray = new String[]{"中国石油", "中国石化", "交运"};
            mBottomDialog.addDataArray(stringArray);
            mBottomDialog.setOnItemClickListener(new BottomDialog.OnItemClickListener() {
                @Override
                public void onItemClick(BottomDialog dialog, int position) {
                    mCardType.setText(dialog.getData(position));
                    int type = -1;
                    switch (position) {
                        case 0:
                            type = 1;
                            break;
                        case 1:
                            type = 2;
                            break;
                        case 2:
                            type = 4;
                            break;
                    }
                    mTypeId = type;

                }
            });
        }

        if (!TextUtils.isEmpty(selectDate)) {
            mBottomDialog.setSelectDate(selectDate);
        }
        mBottomDialog.show();
    }


}
