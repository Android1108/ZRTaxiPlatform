package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.OilCardRechargeAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.OilCardRechargeBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.ICardOrder;
import org.wzeiri.zr.zrtaxiplatform.network.api.IOilCard;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseListActivity;

import java.util.List;

import retrofit2.Call;

/**
 * 加油卡充值记录
 *
 * @author k-lm on 2018/1/9.
 */

public class OilCardRechargeActivity extends BaseListActivity<OilCardRechargeBean, OilCardRechargeAdapter> {

    private IOilCard mIOilCard;

    @Override
    public OilCardRechargeAdapter getAdapter(List<OilCardRechargeBean> list) {
        return new OilCardRechargeAdapter(list);
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("充值记录");
        int paddingLeft = getResources().getDimensionPixelOffset(R.dimen.layout_margin_tiny);
        getListView().setPadding(paddingLeft, 0, 0, 0);
    }

    @Override
    protected void initData() {
        super.initData();
        onRefresh();
    }

    @Override
    protected Call<BaseBean<BaseListBean<OilCardRechargeBean>>> getNetCall(int currentIndex, int pagerSize) {
        if (mIOilCard == null) {
            mIOilCard = RetrofitHelper.create(IOilCard.class);
        }
        return mIOilCard.getOilCardRecharges(pagerSize, currentIndex);
    }
}
