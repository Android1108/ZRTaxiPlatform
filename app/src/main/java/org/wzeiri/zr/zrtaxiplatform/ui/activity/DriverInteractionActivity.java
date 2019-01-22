package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.DriverInteractionAdapter;
import org.wzeiri.zr.zrtaxiplatform.adapter.DriverInteractionPagerAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.ForumSectionBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IPost;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseTabLayoutActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.DriverInteractionListFragment;
import org.wzeiri.zr.zrtaxiplatform.util.UserInfoHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 司机互动
 *
 * @author k-lm on 2017/11/29.
 */

public class DriverInteractionActivity extends BaseTabLayoutActivity<DriverInteractionPagerAdapter> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_driver_interaction;
    }

    @Override
    protected DriverInteractionPagerAdapter getPagerAdapter() {
        return new DriverInteractionPagerAdapter(getSupportFragmentManager());
    }


    @OnClick(R.id.aty_driver_interaction_image_release)
    void onRelease() {
        // 未认证获取审核中提示认证
        if (!UserInfoHelper.getInstance().isAuthentication(getThis())) {
            return;
        }
        // 检测是否切换地区
        if (UserInfoHelper.getInstance().isSwitchRegion(getThis())) {
            return;
        }

        int position = mLayoutLayout.getSelectedTabPosition();
        if (position < 0) {
            ReleaseDriverInteractionActivity.start(getThis());
            return;
        }
        DriverInteractionListFragment fragment = (DriverInteractionListFragment) getAdapter().getItem(position);
        ReleaseDriverInteractionActivity.start(getThis(), getAdapter().getPageTitle(position).toString(), fragment.getSelectId());
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("司机之家");
    }


    @Override
    protected void initData() {
        super.initData();

        IPost post = RetrofitHelper.create(IPost.class);

        post.getForumSections()
                .enqueue(new MsgCallBack<BaseBean<List<ForumSectionBean>>>(getThis()) {
                    @Override
                    public void onSuccess(Call<BaseBean<List<ForumSectionBean>>> call, Response<BaseBean<List<ForumSectionBean>>> response) {
                        List<ForumSectionBean> list = response.body().getResult();
                        if (list == null || list.size() == 0) {
                            return;
                        }

                        List<String> mTitleList = new ArrayList<>();
                        List<Fragment> mFragmentList = new ArrayList<>();
                        for (ForumSectionBean bean : list) {
                            mTitleList.add(bean.getName());
                            mFragmentList.add(DriverInteractionListFragment.newInstance(bean.getId()));
                        }

                        getAdapter().addFragments(mFragmentList);
                        getAdapter().addTitles(mTitleList);
                        getAdapter().notifyDataSetChanged();

                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == ReleaseDriverInteractionActivity.REQUEST_CODE) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
