package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.wzeiri.zr.zrtaxiplatform.ui.fragment.AnswerFragment;

import java.util.List;

/**
 * @author k-lm on 2017/12/8.
 */

public class AnswerCheckErrorPagerAdapter extends FragmentStatePagerAdapter {

    private List<Integer> mErrorSubjectsIndexList;


    public AnswerCheckErrorPagerAdapter(FragmentManager fm, List<Integer> list) {
        super(fm);
        mErrorSubjectsIndexList = list;
    }

    @Override
    public Fragment getItem(int position) {
        return AnswerFragment.newInstanceCheck(mErrorSubjectsIndexList.get(position) - 1);
    }

    @Override
    public int getCount() {
        return mErrorSubjectsIndexList.size();
    }
}
