package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.BalanceInfoAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.WalletNoteBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IWallet;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseListActivity;

import java.util.List;

import retrofit2.Call;

/**
 * 余额明细
 *
 * @author k-lm on 2017/11/23.
 */

public class BalanceInfoActivity extends BaseListActivity<WalletNoteBean, BalanceInfoAdapter> {

    private IWallet mIWallet;

    @Override
    public BalanceInfoAdapter getAdapter(List<WalletNoteBean> list) {
        return new BalanceInfoAdapter(list);
    }

    @Override
    protected Call<BaseBean<List<WalletNoteBean>>> getNotItemNetCall(int currentIndex, int pagerSize) {
        if (mIWallet == null) {
            mIWallet = RetrofitHelper.create(IWallet.class);
        }
        return mIWallet.getWalletNotes(currentIndex, pagerSize);
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("余额明细");

       /* getListView().setPadding(getResources().getDimensionPixelSize(R.dimen.layout_margin_tiny),
                0, 0, 0);*/
        getListView().setBackgroundResource(R.drawable.bg_white_bottom_line_gray20);
    }

    @Override
    protected void initData() {
        super.initData();
        setIsItemField(false);
        onRefresh();
    }


}
