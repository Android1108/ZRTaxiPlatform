package org.wzeiri.zr.zrtaxiplatform.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import org.wzeiri.zr.zrtaxiplatform.adapter.MyReleaseSeatCoverAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.SeatCoverBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IChangeSeat;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.MyReleaseChangeSeatCoverDetailActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.base.BaseListFragment;

import java.util.List;

import retrofit2.Call;

/**
 * 我发布的车换座套
 *
 * @author k-lm on 2017/12/13.
 */

public class MyReleaseSeatCoverFragment extends BaseListFragment<SeatCoverBean, MyReleaseSeatCoverAdapter> {


    private IChangeSeat mIChangeSeat;

    private int mCarId = -1;

    @Override
    public MyReleaseSeatCoverAdapter getAdapter(List<SeatCoverBean> list) {
        return new MyReleaseSeatCoverAdapter(list);
    }

    @Override
    protected Call<BaseBean<BaseListBean<SeatCoverBean>>> getNetCall(int pagerIndex, int pagerSize) {
        if (mIChangeSeat == null) {
            mIChangeSeat = RetrofitHelper.create(IChangeSeat.class);
        }

        return mIChangeSeat.getLostFounds(mCarId, pagerSize, pagerIndex);
    }

    @Override
    public void init() {
        super.init();
        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SeatCoverBean bean = getData(position);

                MyReleaseChangeSeatCoverDetailActivity.start(getContext(), bean.getCarId(), bean.getDescribe());
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


    public static MyReleaseSeatCoverFragment newInstance(int carId) {

        Bundle args = new Bundle();

        MyReleaseSeatCoverFragment fragment = new MyReleaseSeatCoverFragment();
        args.putInt("carId", carId);
        fragment.setArguments(args);
        return fragment;
    }
}
