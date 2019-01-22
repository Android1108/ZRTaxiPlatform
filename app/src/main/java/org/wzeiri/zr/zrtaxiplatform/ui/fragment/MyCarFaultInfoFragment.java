package org.wzeiri.zr.zrtaxiplatform.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.MyCarFaultInfoAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.FaultInfoBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IEquipmentFault;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.MyReleaseGpsFaultDetailActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.base.BaseListFragment;

import java.util.List;

import retrofit2.Call;

/**
 * 我发布的故障信息
 *
 * @author k-lm on 2017/12/13.
 */

public class MyCarFaultInfoFragment extends BaseListFragment<FaultInfoBean, MyCarFaultInfoAdapter> {
    private IEquipmentFault mIEquipmentFault;

    private int mCarId = -1;


    @Override
    public MyCarFaultInfoAdapter getAdapter(List<FaultInfoBean> list) {
        return new MyCarFaultInfoAdapter(list);
    }

    @Override
    protected Call<BaseBean<BaseListBean<FaultInfoBean>>> getNetCall(int pagerIndex, int pagerSize) {
        if (mIEquipmentFault == null) {
            mIEquipmentFault = RetrofitHelper.create(IEquipmentFault.class);
        }

        return mIEquipmentFault.getLostFounds(mCarId, pagerSize, pagerIndex);
    }

    @Override
    public void init() {
        super.init();
        int dividerHeight = getResources().getDimensionPixelOffset(R.dimen.layout_margin);
        getListView().setDividerHeight(dividerHeight);
        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FaultInfoBean bean = getData(position);
                MyReleaseGpsFaultDetailActivity.start(getContext(), bean.getId());
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        mCarId = bundle.getInt("carId", mCarId);
        onRefresh();
    }


    public static MyCarFaultInfoFragment newInstance(int carId) {

        Bundle args = new Bundle();

        MyCarFaultInfoFragment fragment = new MyCarFaultInfoFragment();
        args.putInt("carId", carId);
        fragment.setArguments(args);
        return fragment;
    }


}
