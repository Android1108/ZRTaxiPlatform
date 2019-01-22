package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.wzeiri.zr.zrtaxiplatform.ui.fragment.VehicleTransactionFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 车辆交易
 *
 * @author k-lm on 2017/11/21.
 */

public class VehicleTransactionPagerAdapter extends FragmentStatePagerAdapter {
    private String[] mTitle = new String[]{"车辆转卖", "经营权交易"};
    private List<Fragment> mList = new ArrayList<>();


    public VehicleTransactionPagerAdapter(FragmentManager fm) {
        super(fm);
        mList.add(VehicleTransactionFragment.newInstance(1));
        mList.add(VehicleTransactionFragment.newInstance(2));
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mTitle.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitle[position];
    }
}
