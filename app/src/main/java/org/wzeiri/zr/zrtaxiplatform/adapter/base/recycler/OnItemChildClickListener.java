package org.wzeiri.zr.zrtaxiplatform.adapter.base.recycler;

import android.view.View;

/**
 * Created by LC on 2017/5/20.
 */

public interface OnItemChildClickListener<T> {

    void onItemClick(View view, int viewType, T data, int position);

    boolean onItemLongClick(View view, int viewType, T data, int position);

    void onItemChildClick(View parent, int viewType, T data, int position, View child, long childId);

    boolean onItemChildLongClick(View parent, int viewType, T data, int position, View child, long childId);

}
