package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.wzeiri.zr.zrtaxiplatform.bean.LegalAdviceTypeBean;

import java.util.List;

/**
 * @author k-lm on 2017/12/14.
 */

public class LegalAdvicePagerAdapter extends FragmentPagerAdapter {
    private List<LegalAdviceTypeBean> mList;

    private List<Fragment> mFragmentList;

    public LegalAdvicePagerAdapter(FragmentManager fm, List<LegalAdviceTypeBean> beanList, List<Fragment> fragmentList) {
        super(fm);
        mList = beanList;
        mFragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mList.get(position).getDisplayValue();
    }
}
