package org.wzeiri.zr.zrtaxiplatform.adapter.base.recycler;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

/**
 * 分割线
 * Created by LC on 2017/5/23.
 */
public class OffsetDecoration extends RecyclerView.ItemDecoration {
    //item间的间距
    private int mSpace;
    //边距(padding)
    private int mEdgeSpace;
    private int mHalfSpacing;

    /**
     * @param mSpace item间的间距 默认没有边距
     */
    public OffsetDecoration(Context context, int mSpace) {
        this(context, mSpace, 0);
    }

    /**
     * @param mSpace     item间的间距
     * @param mEdgeSpace 边距(padding)
     */
    public OffsetDecoration(Context context, int mSpace, int mEdgeSpace) {
        this.mSpace = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mSpace, context.getResources().getDisplayMetrics()) + 0.5f);
        this.mEdgeSpace = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mEdgeSpace, context.getResources().getDisplayMetrics()) + 0.5f);
        this.mHalfSpacing = mSpace / 2;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        int childPosition = parent.getChildAdapterPosition(view);

        if (isExcluded(parent.getAdapter(), childPosition)) {
            return;
        }
        int itemCount = parent.getAdapter().getItemCount();

        if (manager != null) {
            if (manager instanceof GridLayoutManager) {
                outRect.set(mHalfSpacing, mHalfSpacing, mHalfSpacing, mHalfSpacing);
                //setGridOffset(((GridLayoutManager) manager).getOrientation(), ((GridLayoutManager) manager).getSpanCount(), outRect, childPosition, itemCount, isExcluded(parent.getAdapter(), childPosition + 1));
            } else if (manager instanceof LinearLayoutManager) {
                if (isExcluded(parent.getAdapter(), childPosition, childPosition + 1)) {
                    return;
                }
                setLinearOffset(((LinearLayoutManager) manager).getOrientation(), outRect, childPosition, itemCount);
            }
        }
    }


    private void setLinearOffset(int orientation, Rect outRect, int childPosition, int itemCount) {

        if (orientation == LinearLayoutManager.HORIZONTAL) {
            if (childPosition == 0) {
                // 第一个要设置PaddingLeft
                outRect.set(mEdgeSpace, 0, mSpace, 0);
            } else if (childPosition == itemCount - 1) {
                // 最后一个设置PaddingRight
                outRect.set(0, 0, mEdgeSpace, 0);
            } else {
                outRect.set(0, 0, mSpace, 0);
            }
        } else {
            if (childPosition == 0) {
                // 第一个要设置PaddingTop
                outRect.set(0, mEdgeSpace, 0, mSpace);
            } else if (childPosition == itemCount - 1) {
                // 最后一个要设置PaddingBottom
                outRect.set(0, 0, 0, mEdgeSpace);
            } else {
                outRect.set(0, 0, 0, mSpace);
            }
        }
    }

    private void setGridOffset(int orientation, int spanCount, Rect outRect, int childPosition, int itemCount, boolean excluded) {
        float totalSpace = mSpace * (spanCount - 1) + mEdgeSpace * 2;
        float eachSpace = totalSpace / spanCount;
        int column = childPosition % spanCount;
        int row = childPosition / spanCount;

        float left;
        float right;
        float top;
        float bottom;
        if (orientation == GridLayoutManager.VERTICAL) {
            top = 0;
            bottom = mSpace;

            if (childPosition < spanCount) {
                top = mEdgeSpace;
            }
            if (itemCount % spanCount != 0 && itemCount / spanCount == row ||
                    itemCount % spanCount == 0 && itemCount / spanCount == row + 1) {
                bottom = mEdgeSpace;
            }

            if (spanCount == 1) {
                left = mEdgeSpace;
                right = left;
            } else {
                left = column * (eachSpace - mEdgeSpace - mEdgeSpace) / (spanCount - 1) + mEdgeSpace;
                right = eachSpace - left;
            }
            bottom = excluded ? 0 : mSpace;
        } else {
            left = 0;
            right = mSpace;

            if (childPosition < spanCount) {
                left = mEdgeSpace;
            }
            if (itemCount % spanCount != 0 && itemCount / spanCount == row ||
                    itemCount % spanCount == 0 && itemCount / spanCount == row + 1) {
                right = mEdgeSpace;
            }

            if (spanCount == 1) {
                top = mEdgeSpace;
                bottom = top;
            } else {
                top = column * (eachSpace - mEdgeSpace - mEdgeSpace) / (spanCount - 1) + mEdgeSpace;
                bottom = eachSpace - top;
            }
            right = excluded ? 0 : mSpace;
        }
        outRect.set((int) left, (int) top, (int) right, (int) bottom);
    }

    public static boolean isExcluded(RecyclerView.Adapter adapter, int... positions) {
        if (adapter == null || positions == null || positions.length == 0) {
            return false;
        }
        for (int position : positions) {
            int itemType = adapter.getItemViewType(position);
            if (itemType >= 70000 && itemType < 80000) {
                return true;
            }
        }
        return false;
    }
}
