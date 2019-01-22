package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import org.wzeiri.zr.zrtaxiplatform.adapter.VehicleTransactionPagerAdapter;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseTabLayoutActivity;

/**
 * 车辆交易
 *
 * @author k-lm on 2017/11/21.
 */

public class VehicleTransactionActivity extends BaseTabLayoutActivity<VehicleTransactionPagerAdapter> {
    @Override
    protected VehicleTransactionPagerAdapter getPagerAdapter() {
        return new VehicleTransactionPagerAdapter(getSupportFragmentManager());
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("车辆交易");
    }
}
