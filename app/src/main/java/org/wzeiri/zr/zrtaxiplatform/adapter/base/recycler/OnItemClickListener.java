package org.wzeiri.zr.zrtaxiplatform.adapter.base.recycler;

import android.view.View;

/**
 * Created by Lcsunm on 2017/5/24.
 */

public abstract class OnItemClickListener<T> implements OnItemChildClickListener<T> {

    @Override
    public void onItemChildClick(View parent, int viewType, T data, int position, View child, long childId) {

    }

    @Override
    public boolean onItemChildLongClick(View parent, int viewType, T data, int position, View child, long childId) {
        return false;
    }
}
