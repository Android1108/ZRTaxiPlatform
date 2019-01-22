package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.wzeiri.zr.zrtaxiplatform.ui.fragment.IntegralRuleFragment;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.MyIntegralRecordFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author k-lm on 2017/11/27.
 */

public class MyIntegralPagerAdapter extends FragmentStatePagerAdapter {
    private String[] mTitles = new String[]{"积分明细", "积分规则"};
    private List<Fragment> mList = new ArrayList<>(mTitles.length);

    public MyIntegralPagerAdapter(FragmentManager fm) {
        super(fm);
        mList.add(new MyIntegralRecordFragment());
        mList.add(new IntegralRuleFragment());
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
