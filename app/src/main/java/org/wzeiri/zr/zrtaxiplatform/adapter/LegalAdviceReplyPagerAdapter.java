package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.wzeiri.zr.zrtaxiplatform.ui.fragment.MyLegalAdviceReplyFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author k-lm on 2017/12/6.
 */

public class LegalAdviceReplyPagerAdapter extends FragmentStatePagerAdapter {
    private String[] mTitles = new String[]{"已回复", "未回复"};
    private List<Fragment> mList = new ArrayList<>(2);

    public LegalAdviceReplyPagerAdapter(FragmentManager fm) {
        super(fm);
        mList.add(MyLegalAdviceReplyFragment.newInstance(true));
        mList.add(MyLegalAdviceReplyFragment.newInstance(false));


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
