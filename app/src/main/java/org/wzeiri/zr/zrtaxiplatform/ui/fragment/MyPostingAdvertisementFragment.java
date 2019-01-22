package org.wzeiri.zr.zrtaxiplatform.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import org.wzeiri.zr.zrtaxiplatform.adapter.MyPostingAdvertisementAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.AdverPostApplyBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IAdverPostApply;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.MyReleaseAdvertisementPhotoDetailActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.base.BaseListFragment;

import java.util.List;

import retrofit2.Call;

/**
 * 我发布的广告张贴
 *
 * @author k-lm on 2017/12/13.
 */

public class MyPostingAdvertisementFragment extends BaseListFragment<AdverPostApplyBean, MyPostingAdvertisementAdapter> {

    private IAdverPostApply mIAdverPostApply;

    private int mCarId = -1;

    @Override
    public MyPostingAdvertisementAdapter getAdapter(List<AdverPostApplyBean> list) {
        return new MyPostingAdvertisementAdapter(list);
    }

    @Override
    protected Call<BaseBean<BaseListBean<AdverPostApplyBean>>> getNetCall(int pagerIndex, int pagerSize) {
        if (mIAdverPostApply == null) {
            mIAdverPostApply = RetrofitHelper.create(IAdverPostApply.class);
        }
        return mIAdverPostApply.getMyAdverPostApplies(mCarId, pagerSize, pagerIndex);
    }

    @Override
    public void init() {
        super.init();
        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AdverPostApplyBean bean = getData(position);
                MyReleaseAdvertisementPhotoDetailActivity.start(getContext(), bean.getId());
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


    public static MyPostingAdvertisementFragment newInstance(int carId) {

        Bundle args = new Bundle();

        MyPostingAdvertisementFragment fragment = new MyPostingAdvertisementFragment();
        args.putInt("carId", carId);
        fragment.setArguments(args);
        return fragment;
    }
}
