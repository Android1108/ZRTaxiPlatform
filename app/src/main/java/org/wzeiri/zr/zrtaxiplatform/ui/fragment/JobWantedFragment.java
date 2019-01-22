package org.wzeiri.zr.zrtaxiplatform.ui.fragment;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;


import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.JobWantedAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.JobRecruitmentBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IJobRecruitment;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.JobInfoDetailActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.base.BaseListFragment;

import java.util.List;

import retrofit2.Call;

/**
 * 求职信息
 *
 * @author k-lm on 2017/11/21.
 */

public class JobWantedFragment extends BaseListFragment<JobRecruitmentBean, JobWantedAdapter> {
    private IJobRecruitment mIJobRecruitment;


    @Override
    public JobWantedAdapter getAdapter(List<JobRecruitmentBean> list) {
        return new JobWantedAdapter(list);
    }

    @Override
    protected Call<BaseBean<BaseListBean<JobRecruitmentBean>>> getNetCall(int currentIndex, int pagerSize) {
        if (mIJobRecruitment == null) {
            mIJobRecruitment = RetrofitHelper.create(IJobRecruitment.class);
        }
        return mIJobRecruitment.getJobRecruitments(1, pagerSize, currentIndex);
    }

    @Override
    public void init() {
        super.init();
        getListView().setDivider(new ColorDrawable(ContextCompat.getColor(getContext(),R.color.gray10)));
        getListView().setDividerHeight(getResources().getDimensionPixelOffset(R.dimen.layout_margin));


        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(JobInfoDetailActivity.class);
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        onRefresh();
    }
}
