package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author k-lm on 2017/12/12.
 */

public class DriverInteractionPagerAdapter extends FragmentStatePagerAdapter {

    public List<String> mTitleList = new ArrayList<>();
    public List<Fragment> mFragmentList = new ArrayList<>();


    public DriverInteractionPagerAdapter(FragmentManager fm) {
        super(fm);
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
        return mTitleList.get(position);
    }

    /**
     * 添加标题
     *
     * @param list
     */
    public void addTitles(List<String> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        mTitleList.addAll(list);
    }

    /**
     * 添加fragment
     *
     * @param list
     */
    public void addFragments(List<Fragment> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        mFragmentList.addAll(list);
    }
}
