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
 * 培训公告
 *
 * @author k-lm on 2017/11/21.
 */

public class TrainNoticeFragment extends BaseListFragment<TrainingBean, TrainNoticeAdapter> {
    /**
     * 公告类型
     */
    private int mType = -1;

    private IArticle mIArticle;

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
    public TrainNoticeAdapter getAdapter(List<TrainingBean> list) {
        return new TrainNoticeAdapter(list);
    }

    @Override
    protected Call<BaseBean<BaseListBean<TrainingBean>>> getNetCall(int pagerIndex, int pagerSize) {
        if (mIArticle == null) {
            mIArticle = RetrofitHelper.create(IArticle.class);
        }
        return mIArticle.getTrainings(mType, pagerSize, pagerIndex);
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
     * @param type 类型,1-运管,2-交警,4-公司
     * @return
     */
    public static TrainNoticeFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        TrainNoticeFragment fragment = new TrainNoticeFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
