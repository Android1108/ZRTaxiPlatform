package org.wzeiri.zr.zrtaxiplatform.ui.activity.base;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.util.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author k-lm on 2017/11/21.
 */

public abstract class BaseTabLayoutActivity<A extends PagerAdapter> extends ActionbarActivity {
    @BindView(R.id.layout_tab_layout_layout)
    protected TabLayout mLayoutLayout;
    @BindView(R.id.layout_tab_layout_pager)
    protected ViewPager mLayoutPager;
    View mEmptyView;

    private A mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_tab_layout;
    }

    @Override
    void initView() {
        super.initView();
        mEmptyView = findViewById(R.id.layout_tab_layout_empty);
        mAdapter = getPagerAdapter();
        mLayoutPager.setAdapter(mAdapter);
        mLayoutLayout.setupWithViewPager(mLayoutPager);
        mLayoutLayout.setTabMode(TabLayout.MODE_FIXED);


        mAdapter.registerDataSetObserver(new DataSetObserver() {
            public void onChanged() {


                int count = mAdapter.getCount();
                if (count >= 5) {
                    mLayoutLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                } else {
                    mLayoutLayout.setTabMode(TabLayout.MODE_FIXED);
                }
            }
        });

    }

    protected int getSelectedTabPosition() {
        return mLayoutLayout.getSelectedTabPosition();
    }

    protected abstract A getPagerAdapter();


    protected A getAdapter() {
        if (mAdapter == null) {
            mAdapter = getPagerAdapter();
        }
        return mAdapter;
    }

    /**
     * 显示空数据View
     */
    protected void showEmptyView() {
        if(mEmptyView == null){
            return;
        }
        mEmptyView.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏空数据View
     */
    protected void hideEmptyView() {
        if(mEmptyView == null){
            return;
        }
        mEmptyView.setVisibility(View.GONE);
    }

}
