package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.DriverDiscountPrivilegeAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.MerchantBean;
import org.wzeiri.zr.zrtaxiplatform.bean.OilCardBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IMerchant;
import org.wzeiri.zr.zrtaxiplatform.network.api.IOilCard;
import org.wzeiri.zr.zrtaxiplatform.network.callback.BaseCallBack;
import org.wzeiri.zr.zrtaxiplatform.service.BaiduLocationService;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseListActivity;
import org.wzeiri.zr.zrtaxiplatform.util.RefuelCardHelper;
import org.wzeiri.zr.zrtaxiplatform.util.UserInfoHelper;

import java.util.List;

import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 司机特惠
 *
 * @author k-lm on 2017/11/21.
 */

public class DriverDiscountPrivilegeActivity extends BaseListActivity<MerchantBean, DriverDiscountPrivilegeAdapter> {
    private IMerchant mIMerchant;


    @Override
    public DriverDiscountPrivilegeAdapter getAdapter(List<MerchantBean> list) {
        return new DriverDiscountPrivilegeAdapter(list);
    }

    @Override
    protected Call<BaseBean<BaseListBean<MerchantBean>>> getNetCall(int currentIndex, int pagerSize) {
        if (mIMerchant == null) {
            mIMerchant = RetrofitHelper.create(IMerchant.class);
        }
        return mIMerchant.getMerchants(BaiduLocationService.getLongitude(),
                BaiduLocationService.getLatitude(),
                pagerSize, currentIndex);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_driver_discount;
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("司机特惠");


        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MerchantBean bean = getData(position);
                DriverDiscountDetailActivity.start(getThis(), bean.getId());
            }
        });
    }

    @Override
    protected void initData() {
        onRefresh();
        loadOilCarData();
    }

    /**
     * 加载油卡信息
     */
    private void loadOilCarData() {
        int size = RefuelCardHelper.getInstance().getCardDateSize();
        if (size > 0) {
            return;
        }

        int pagerIndex = 1;
        int pagerSize = RefuelCardHelper.getInstance().getPagerSize();
        IOilCard oilCard = RetrofitHelper.create(IOilCard.class);
        oilCard.getOilCards(pagerSize, pagerIndex)
                .enqueue(new BaseCallBack<BaseBean<BaseListBean<OilCardBean>>>() {
                    @Override
                    public void onSuccess(Call<BaseBean<BaseListBean<OilCardBean>>> call, Response<BaseBean<BaseListBean<OilCardBean>>> response) {
                        BaseListBean<OilCardBean> baseListBean = response.body().getResult();
                        if (baseListBean == null) {
                            return;
                        }
                        List<OilCardBean> list = baseListBean.getItems();
                        RefuelCardHelper.getInstance().clearCardDate();
                        RefuelCardHelper.getInstance().addCardDateList(list);
                    }

                    @Override
                    public void onError(Call<BaseBean<BaseListBean<OilCardBean>>> call, Throwable t) {

                    }
                });
    }

    @OnClick(R.id.aty_driver_discount_layout_refuel_card_recharge)
    public void onAtyDriverDiscountRefuelingCardRechargeClicked() {
        RefuelCardHelper helper = RefuelCardHelper.getInstance();

        if (!UserInfoHelper.getInstance().isAuthentication(getThis())) {
            return;
        }

        if (UserInfoHelper.getInstance().isSwitchRegion(getThis())) {
            return;
        }

        if (helper.getCardDateSize() == 0) {
            startActivityForResult(AddRefuelingCardActivity.class, AddRefuelingCardActivity.REQUEST_CODE);
        } else {
            startActivity(RefuelingCardRechargeActivity.class);
        }

    }

    @OnClick(R.id.aty_driver_discount_layout_phone_recharge)
    public void onAtyDriverDiscountPhoneRechargeClicked() {

        if (!UserInfoHelper.getInstance().isAuthentication(getThis())) {
            return;
        }

        if (UserInfoHelper.getInstance().isSwitchRegion(getThis())) {
            return;
        }

        startActivity(TelephoneFareRechargeActivity.class);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (AddRefuelingCardActivity.REQUEST_CODE == requestCode) {
            OilCardBean bean = null;
            if (data != null) {
                bean = data.getParcelableExtra(AddRefuelingCardActivity.KEY_CARD_INFO);
            }

            if (bean != null) {
                RefuelingCardRechargeActivity.start(getThis(), bean);
            } else {
                startActivity(RefuelingCardRechargeActivity.class);
            }

        }
    }
}
