package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.PhoneRechargeTempBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IPhoneRecharge;
import org.wzeiri.zr.zrtaxiplatform.network.api.IUser;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.util.DensityUtil;
import org.wzeiri.zr.zrtaxiplatform.util.UserInfoHelper;
import org.wzeiri.zr.zrtaxiplatform.widget.DrawTextTextView;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueEditText;
import org.wzeiri.zr.zrtaxiplatform.widget.flow.RadioFlowLayout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 话费充值
 *
 * @author k-lm on 2017/11/27.
 */

public class TelephoneFareRechargeActivity extends ActionbarActivity {
    @BindView(R.id.aty_telephone_fare_recharge_vet_phone)
    ValueEditText mVetPhone;

    @BindView(R.id.aty_telephone_fare_recharge_fl_recharge)
    RadioFlowLayout mRadioFlowLayout;


    private IPhoneRecharge mIPhoneRecharge;
    private List<PhoneRechargeTempBean> mPhoneRechargeList = new ArrayList<>();
    /**
     * 当前选择的id
     */
    private int mSelectId = -1;

    @OnClick(R.id.aty_telephone_fare_recharge_text_play)
    void onPlay() {

    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_telephone_fare_recharge;
    }


    @Override
    protected void init() {
        super.init();
        setCenterTitle("手机充值");

        mVetPhone.setText(UserInfoHelper.getInstance().getName());

        mRadioFlowLayout.setOnSelectItemViewListener(new RadioFlowLayout.OnSelectItemViewListener() {
            @Override
            public void onSelectItemView(View view, boolean isSelect) {
                if (!(view instanceof DrawTextTextView) && !isSelect) {
                    return;
                }
                mSelectId = view.getId();
                showToast(((DrawTextTextView) view).getText().toString());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recharge_record, menu);
        return true;
    }

    @Override
    protected void initData() {
        super.initData();
        // 获取手机号码
        String phone = UserInfoHelper.getInstance().getPhone();
        if (TextUtils.isEmpty(phone)) {
            loadUserPhone();
        } else {
            mVetPhone.setText(phone);
        }
        // 模拟数据
      /*  for (int i = 1; i < 11; i++) {
            PhoneRechargeTempBean bean = new PhoneRechargeTempBean();
            bean.setId(i);
            bean.setAmount(new BigDecimal(i));
            bean.setRealPrice(new BigDecimal(i));
            addRadioItem(bean);
        }*/

        loadPhoneRechargeDate();

    }

    /**
     * 获取用户手机号
     */
    private void loadUserPhone() {
        IUser user = RetrofitHelper.create(IUser.class);
        user.getUserPhoneNumber()
                .enqueue(new MsgCallBack<BaseBean<String>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                        String phone = response.body().getResult();
                        UserInfoHelper.getInstance().savePhone(phone);
                        mVetPhone.setText(phone);
                    }

                    @Override
                    public void onError(Call<BaseBean<String>> call, Throwable t) {
                        super.onError(call, t);
                    }
                });
    }

    /**
     * 加载话费充值信息
     */
    private void loadPhoneRechargeDate() {
        mIPhoneRecharge = RetrofitHelper.create(IPhoneRecharge.class);

        mIPhoneRecharge.getPhoneRechargeTemps()
                .enqueue(new MsgCallBack<BaseBean<List<PhoneRechargeTempBean>>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<List<PhoneRechargeTempBean>>> call, Response<BaseBean<List<PhoneRechargeTempBean>>> response) {
                        List<PhoneRechargeTempBean> beans = response.body().getResult();
                        mPhoneRechargeList.clear();
                        if (beans == null || beans.size() == 0) {
                            return;
                        }
                        mPhoneRechargeList.addAll(beans);

                        for (PhoneRechargeTempBean bean : mPhoneRechargeList) {
                            addRadioItem(bean);
                        }
                    }
                });
    }


    /**
     * 添加充值选项
     */
    private void addRadioItem(PhoneRechargeTempBean bean) {

        DrawTextTextView drawTextTextView = (DrawTextTextView) LayoutInflater.from(this)
                .inflate(R.layout.layout_draw_text, mRadioFlowLayout, false);
        drawTextTextView.setId(bean.getId());
        mRadioFlowLayout.addView(drawTextTextView);

        drawTextTextView.setBackgroundResource(R.drawable.bg_select_fuel_card);
        int viewWidth = DensityUtil.WINDOW_WIDTH / 2 - getResources().getDimensionPixelOffset(R.dimen.layout_margin) * 5;
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) drawTextTextView.getLayoutParams();
        layoutParams.width = viewWidth;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.bottomMargin = getResources().getDimensionPixelOffset(R.dimen.layout_margin);
        layoutParams.rightMargin = getResources().getDimensionPixelOffset(R.dimen.layout_margin);


        drawTextTextView.setTopDrawText(bean.getAmount().intValue() + "元");
        String discountMoneyStr = "优惠价 " + bean.getRealPrice().intValue() + "元";
        drawTextTextView.setText(discountMoneyStr);
    }


    private void submit(){

    }


}
