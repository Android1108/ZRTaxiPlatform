package org.wzeiri.zr.zrtaxiplatform.ui.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.FindPagerAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.TenantBean;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.base.ActionbarFragment;
import org.wzeiri.zr.zrtaxiplatform.util.UserInfoHelper;

import butterknife.BindView;

/**
 * 发现
 *
 * @author k-lm on 2017/11/14.
 */

public class FindFragment extends ActionbarFragment {


    @BindView(R.id.fragment_find_tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.fragment_find_view_pager)
    ViewPager mViewPager;

    private FindPagerAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_find;
    }


    @Override
    public void init() {
        super.init();
        setCenterTitle("发现");
        initTabLayout();
        mAdapter = new FindPagerAdapter(getChildFragmentManager(), 4);
        mViewPager.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        super.initData();
        UserInfoHelper.getInstance()
                .addOnChangeCityListener(getThis(), new UserInfoHelper.OnChangeCityListener() {
                    @Override
                    public void onChangeCity(TenantBean bean) {
                        int count = mAdapter.getCount();
                        for (int i = 0; i < count; i++) {
                            Fragment fragment = mAdapter.getItem(i);
                            if (fragment instanceof FindArticleFragment) {
                                ((FindArticleFragment) fragment).changeCityRefresh();
                            } else if (fragment instanceof FindTrainNoticeFragment) {
                                ((FindTrainNoticeFragment) fragment).changeCityRefresh();
                            }
                        }
                    }
                });
    }

    private void initTabLayout() {
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setupWithViewPager(mViewPager);

       /* TabLayout.Tab tab1 = mTabLayout.newTab();
        tab1.setText("运管动态");
        mTabLayout.addTab(tab1);

        TabLayout.Tab tab2 = mTabLayout.newTab();
        tab1.setText("公司动态");
        mTabLayout.addTab(tab2);

        TabLayout.Tab tab3 = mTabLayout.newTab();
        tab1.setText("文明创建");
        mTabLayout.addTab(tab3);

        TabLayout.Tab tab4 = mTabLayout.newTab();
        tab1.setText("司机培训");
        mTabLayout.addTab(tab4);*/
    }
}
