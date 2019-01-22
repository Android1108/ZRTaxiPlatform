package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.wzeiri.zr.zrtaxiplatform.bean.ArticleTypeBean;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.TrainNoticeFragment;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.ArticleFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author k-lm on 2017/11/21.
 */

public class DepartmentNoticePagerAdapter extends FragmentStatePagerAdapter {
    private List<String> mTitles = new ArrayList<>();
    private List<Fragment> mList = new ArrayList<>();

    private int mType = -1;

    /**
     * @param fm
     *  类型,1-运管,2-交警,4-公司
     */
    public DepartmentNoticePagerAdapter(FragmentManager fm) {
        super(fm);

    }

    /**
     * 设置标题
     *
     * @param list
     */
    public void addDate(List<ArticleTypeBean> list,int type) {
        if (list == null || list.size() == 0) {
            mType=type;
            mList.add(TrainNoticeFragment.newInstance(mType));
            mTitles.add("培训公告");
            return;
        }

        for (ArticleTypeBean bean : list) {
            if (bean == null) {
                continue;
            }
            mTitles.add(bean.getName());
            mList.add(ArticleFragment.newInstance(bean.getId()));
        }
        mType = type;
        mList.add(TrainNoticeFragment.newInstance(mType));
        mTitles.add("培训公告");
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mTitles.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
