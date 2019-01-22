package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;


import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.BillAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.BillBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.ICardOrder;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseListActivity;

import java.util.List;

import retrofit2.Call;

/**
 * 账单
 *
 * @author k-lm on 2017/11/25.
 */

public class BillActivity extends BaseListActivity<BillBean, BillAdapter> {

    private ICardOrder mICardOrder;

    @Override
    public BillAdapter getAdapter(List<BillBean> list) {
        return new BillAdapter(list);
    }

    @Override
    protected Call<BaseBean<List<BillBean>>> getNotItemNetCall(int currentIndex, int pagerSize) {
        if (mICardOrder == null) {
            mICardOrder = RetrofitHelper.create(ICardOrder.class);
        }


        return mICardOrder.getDriverCardOrderOutput(pagerSize, currentIndex);
    }

    @Override
    protected void init() {
        super.init();
        setIsItemField(false);
        setCenterTitle("账单");
        getListView().setDividerHeight(getResources().getDimensionPixelOffset(R.dimen.layout_margin));
        getListView().setDivider(new ColorDrawable(ContextCompat.getColor(getThis(),R.color.gray10)));

    }

    @Override
    protected void initData() {
        super.initData();
        onRefresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_commission, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_commission_list) {
            startActivity(CommissionInfoListActivity.class);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
