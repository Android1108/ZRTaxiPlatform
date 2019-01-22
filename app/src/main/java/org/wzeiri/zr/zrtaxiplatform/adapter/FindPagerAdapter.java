package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.wzeiri.zr.zrtaxiplatform.ui.fragment.ArticleFragment;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.FindArticleFragment;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.FindTrainNoticeFragment;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.TrainNoticeFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 发现
 *
 * @author k-lm on 2017/11/17.
 */

public class FindPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mList;
    private String[] title = new String[]{"运管动态", "公司动态", "文明创建", "司机培训"};

    public FindPagerAdapter(FragmentManager fm, int count) {
        super(fm);
        mList = new ArrayList<>();


        FindArticleFragment fragment1 = FindArticleFragment.newInstance(1);
        mList.add(fragment1);
        FindArticleFragment fragment2 = FindArticleFragment.newInstance(2);
        mList.add(fragment2);
        FindArticleFragment fragment3 = FindArticleFragment.newInstance(3);
        mList.add(fragment3);
        FindTrainNoticeFragment fragment4 = new FindTrainNoticeFragment();
        mList.add(fragment4);


    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
