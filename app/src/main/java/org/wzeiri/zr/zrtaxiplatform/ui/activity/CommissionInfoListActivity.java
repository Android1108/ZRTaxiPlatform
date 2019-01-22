package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import org.wzeiri.zr.zrtaxiplatform.adapter.CommissionAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.CommissionBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.ICard;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseListActivity;

import java.util.List;

import retrofit2.Call;

/**
 * 佣金列表
 *
 * @author k-lm on 2018/1/25.
 */

public class CommissionInfoListActivity extends BaseListActivity<CommissionBean, CommissionAdapter> {

    private ICard mICard;

    @Override
    public CommissionAdapter getAdapter(List<CommissionBean> list) {
        return new CommissionAdapter(list);
    }


    @Override
    protected void init() {
        super.init();
        setCenterTitle("佣金列表");
        setIsItemField(false);
    }

    @Override
    protected void initData() {
        super.initData();
        onRefresh();
    }


    @Override
    protected Call<BaseBean<List<CommissionBean>>> getNotItemNetCall(int currentIndex, int pagerSize) {
        if (mICard == null) {
            mICard = RetrofitHelper.create(ICard.class);
        }
        return mICard.getCardCommission(pagerSize, currentIndex);
    }
}
