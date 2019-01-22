package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.JobHuntingPagerAdapter;
import org.wzeiri.zr.zrtaxiplatform.adapter.RecruitAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.JobRecruitmentBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IJobRecruitment;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseListActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseTabLayoutActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.base.BaseListFragment;
import org.wzeiri.zr.zrtaxiplatform.util.UserInfoHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * 求职招聘
 *
 * @author k-lm on 2017/11/21.
 */

public class JobHuntingActivity extends BaseListActivity<JobRecruitmentBean, RecruitAdapter> {

    private IJobRecruitment mIJobRecruitment;

    @Override
    protected void init() {
        super.init();
        setCenterTitle("招聘信息");

        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int i = getData(position).getId();
                JobInfoDetailActivity.start(getThis(), i);
            }
        });

    }

    @Override
    protected void initData() {
        super.initData();
        onRefresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_release_job_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_release_job_info) {
            // 未认证获取审核中提示认证
            if (!UserInfoHelper.getInstance().isAuthentication(getThis())) {
                return false;
            }
            // 检测是否切换地区
            if (UserInfoHelper.getInstance().isSwitchRegion(getThis())) {
                return false;
            }
            startActivityForResult(ReleaseCandidateInfoActivity.class, ReleaseCandidateInfoActivity.REQUEST_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public RecruitAdapter getAdapter(List<JobRecruitmentBean> list) {
        return new RecruitAdapter(list);
    }

    @Override
    protected Call<BaseBean<BaseListBean<JobRecruitmentBean>>> getNetCall(int currentIndex, int pagerSize) {
        if (mIJobRecruitment == null) {
            mIJobRecruitment = RetrofitHelper.create(IJobRecruitment.class);
        }
        return mIJobRecruitment.getJobRecruitments(2, pagerSize, currentIndex);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == ReleaseCandidateInfoActivity.REQUEST_CODE) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
