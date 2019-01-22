package org.wzeiri.zr.zrtaxiplatform.ui.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;


import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.VehicleTransactionAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.VehicleTransactionBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IVehicleTransaction;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.CarTransactionDetailActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.base.BaseListFragment;

import java.util.List;

import retrofit2.Call;

/**
 * 车辆交易
 *
 * @author k-lm on 2017/11/21.
 */

public class VehicleTransactionFragment extends BaseListFragment<VehicleTransactionBean, VehicleTransactionAdapter> {
    private IVehicleTransaction mIVehicleTransaction;

    private int mType = 1;

    @Override
    public VehicleTransactionAdapter getAdapter(List<VehicleTransactionBean> list) {
        return new VehicleTransactionAdapter(list);
    }

    @Override
    protected Call<BaseBean<BaseListBean<VehicleTransactionBean>>> getNetCall(int pagerIndex, int pagerSize) {
        if (mIVehicleTransaction == null) {
            mIVehicleTransaction = RetrofitHelper.create(IVehicleTransaction.class);
        }

        return mIVehicleTransaction.getVehicleTransactions(mType, pagerSize, pagerIndex);
    }

    @Override
    public void create() {
        super.create();
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        mType = bundle.getInt("type", mType);
    }

    @Override
    public void init() {
        super.init();
        getListView().setDivider(new ColorDrawable(ContextCompat.getColor(getContext(),R.color.gray10)));
        getListView().setDividerHeight(getResources().getDimensionPixelOffset(R.dimen.layout_margin));
        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VehicleTransactionBean bean = getAdapter().getData(position);
                CarTransactionDetailActivity.start(getContext(), bean.getId());
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        onRefresh();
    }

    /**
     * @param type 车辆交易信息类型,1-车辆转卖,2-经营权交易
     * @return
     */
    public static VehicleTransactionFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        VehicleTransactionFragment fragment = new VehicleTransactionFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
