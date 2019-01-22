package org.wzeiri.zr.zrtaxiplatform.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import org.wzeiri.zr.zrtaxiplatform.adapter.TrainNoticeAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.TenantBean;
import org.wzeiri.zr.zrtaxiplatform.bean.TrainingBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IArticle;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.TrainDetailActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.base.BaseListFragment;
import org.wzeiri.zr.zrtaxiplatform.util.UserInfoHelper;

import java.util.List;

import retrofit2.Call;

/**
 * 发现 培训公告
 *
 * @author k-lm on 2017/11/21.
 */

public class FindTrainNoticeFragment extends BaseListFragment<TrainingBean, TrainNoticeAdapter> {

    private IArticle mIArticle;
    /**
     * 是否切换城市
     */
    private boolean mIsChangeCity = false;

    @Override
    public void create() {
        super.create();
        Bundle bundle = getArguments();

        if (bundle == null) {
            return;
        }

    }

    @Override
    public TrainNoticeAdapter getAdapter(List<TrainingBean> list) {
        return new TrainNoticeAdapter(list);
    }

    @Override
    protected Call<BaseBean<BaseListBean<TrainingBean>>> getNetCall(int pagerIndex, int pagerSize) {
        if (mIArticle == null) {
            mIArticle = RetrofitHelper.create(IArticle.class);
        }

        return mIArticle.getDiscoveryAnnouncements(pagerSize, pagerIndex);
    }

    @Override
    public void init() {
        super.init();
        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TrainingBean bean = getData(position);
                TrainDetailActivity.start(getContext(), bean);
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        onRefresh();
    }
    /**
     * 切换城市刷新数据
     */
    public void changeCityRefresh() {
        mIsChangeCity = true;
        if (getView() != null) {
            setShowProgressDialog(false);
            onRefresh();
            mIsChangeCity = false;
            setShowProgressDialog(true);
        }
    }

    @Override
    public void onLoadData() {
        super.onLoadData();
        if (mIsChangeCity && getView() != null) {
            setShowProgressDialog(false);
            onRefresh();
            mIsChangeCity = false;
            setShowProgressDialog(true);
        }
    }


    /**
     * @param type 类型,1-运管,2-交警,4-公司
     * @return
     */
    public static FindTrainNoticeFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        FindTrainNoticeFragment fragment = new FindTrainNoticeFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onDestroy() {
        UserInfoHelper.getInstance().removeOnChangeCityListener(getThis());
        super.onDestroy();
    }
}
