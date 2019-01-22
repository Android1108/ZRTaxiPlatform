package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.wzeiri.zr.zrtaxiplatform.ui.fragment.JobWantedFragment;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.RecruitFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author k-lm on 2017/11/21.
 */

public class JobHuntingPagerAdapter extends FragmentStatePagerAdapter {
    private String[] mTitles = new String[]{"求职信息", "招聘信息"};
    private List<Fragment> mList = new ArrayList<>();


    public JobHuntingPagerAdapter(FragmentManager fm) {
        super(fm);
        mList.add(new JobWantedFragment());
        mList.add(new RecruitFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
