package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.wzeiri.zr.zrtaxiplatform.util.CarHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 显示车牌号的pagerAdapter
 *
 * @author k-lm on 2017/12/14.
 */

public class MyCarInfoPagerAdapter extends FragmentStatePagerAdapter {

    private List<? extends Fragment> mFragmentList;

    private List<CarHelper.CarInfo> mCarInfoLst;


    public MyCarInfoPagerAdapter(FragmentManager fm, List<? extends Fragment> fragmentList, List<CarHelper.CarInfo> carInfoList) {
        super(fm);
        mFragmentList = fragmentList;
        mCarInfoLst = carInfoList;
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
        CarHelper.CarInfo carInfo = mCarInfoLst.get(position);
        return carInfo.getPlateNumber();
    }


}
