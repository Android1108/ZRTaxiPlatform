package org.wzeiri.zr.zrtaxiplatform.adapter.base.recycler;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by LC on 2017/5/25.
 */

public class ListSpacingDecoration extends RecyclerView.ItemDecoration {

    private static final int VERTICAL = OrientationHelper.VERTICAL;

    private int orientation = -1;
    private int spanCount = -1;
    private int spacing;
    private int halfSpacing;
    private int edgeSpacing;

    /**
     * @param mSpace item间的间距 默认没有边距
     */
    public ListSpacingDecoration(Context context, int mSpace) {
        this(context, mSpace, 0);
    }

    /**
     * @param mSpace     item间的间距
     * @param mEdgeSpace 边距(padding)
     */
    public ListSpacingDecoration(Context context, int mSpace, int mEdgeSpace) {
        this.spacing = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mSpace, context.getResources().getDisplayMetrics()) + 0.5f);
        this.edgeSpacing = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mEdgeSpace, context.getResources().getDisplayMetrics()) + 0.5f);
        halfSpacing = this.spacing / 2;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        super.getItemOffsets(outRect, view, parent, state);

        if (orientation == -1) {
            orientation = getOrientation(parent);
        }

        if (spanCount == -1) {
            spanCount = getTotalSpan(parent);
        }

        int childCount = parent.getLayoutManager().getItemCount();
        int childIndex = parent.getChildAdapterPosition(view);

        int itemSpanSize = getItemSpanSize(parent, childIndex);
        int spanIndex = getItemSpanIndex(parent, childIndex);

        if (WrapperUtils.isExcluded(parent.getAdapter(),childIndex)){
            return;
        }

    /* INVALID SPAN */
        if (spanCount < 1) return;

        setSpacings(outRect, parent, childCount, childIndex, itemSpanSize, spanIndex);
    }

    protected void setSpacings(Rect outRect, RecyclerView parent, int childCount, int childIndex, int itemSpanSize, int spanIndex) {

        outRect.top = halfSpacing;
        outRect.bottom = halfSpacing;
        outRect.left = halfSpacing;
        outRect.right = halfSpacing;

        if (isTopEdge(parent, childCount, childIndex, itemSpanSize, spanIndex)) {
            outRect.top = edgeSpacing;
        }

        if (isLeftEdge(parent, childCount, childIndex, itemSpanSize, spanIndex)) {
            outRect.left = edgeSpacing;
        }

        if (isRightEdge(parent, childCount, childIndex, itemSpanSize, spanIndex)) {
            outRect.right = edgeSpacing;
        }

        if (isBottomEdge(parent, childCount, childIndex, itemSpanSize, spanIndex)) {
            outRect.bottom = edgeSpacing;
        }
    }

    @SuppressWarnings("all")
    protected int getTotalSpan(RecyclerView parent) {

        RecyclerView.LayoutManager mgr = parent.getLayoutManager();
        if (mgr instanceof GridLayoutManager) {
            return ((GridLayoutManager) mgr).getSpanCount();
        } else if (mgr instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) mgr).getSpanCount();
        } else if (mgr instanceof LinearLayoutManager) {
            return 1;
        }

        return -1;
    }

    @SuppressWarnings("all")
    protected int getItemSpanSize(RecyclerView parent, int childIndex) {

        RecyclerView.LayoutManager mgr = parent.getLayoutManager();
        if (mgr instanceof GridLayoutManager) {
            return ((GridLayoutManager) mgr).getSpanSizeLookup().getSpanSize(childIndex);
        } else if (mgr instanceof StaggeredGridLayoutManager) {
            return 1;
        } else if (mgr instanceof LinearLayoutManager) {
            return 1;
        }

        return -1;
    }

    @SuppressWarnings("all")
    protected int getItemSpanIndex(RecyclerView parent, int childIndex) {

        RecyclerView.LayoutManager mgr = parent.getLayoutManager();
        if (mgr instanceof GridLayoutManager) {
            return ((GridLayoutManager) mgr).getSpanSizeLookup().getSpanIndex(childIndex, spanCount);
        } else if (mgr instanceof StaggeredGridLayoutManager) {
            return childIndex % spanCount;
        } else if (mgr instanceof LinearLayoutManager) {
            return 0;
        }

        return -1;
    }

    @SuppressWarnings("all")
    protected int getOrientation(RecyclerView parent) {

        RecyclerView.LayoutManager mgr = parent.getLayoutManager();
        if (mgr instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) mgr).getOrientation();
        } else if (mgr instanceof GridLayoutManager) {
            return ((GridLayoutManager) mgr).getOrientation();
        } else if (mgr instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) mgr).getOrientation();
        }

        return VERTICAL;
    }

    protected boolean isLeftEdge(RecyclerView parent, int childCount, int childIndex, int itemSpanSize, int spanIndex) {

        if (orientation == VERTICAL) {

            return spanIndex == 0;

        } else {

            return (childIndex == 0) || isFirstItemEdgeValid((childIndex < spanCount), parent, childIndex);
        }
    }

    protected boolean isRightEdge(RecyclerView parent, int childCount, int childIndex, int itemSpanSize, int spanIndex) {

        if (orientation == VERTICAL) {

            return (spanIndex + itemSpanSize) == spanCount;

        } else {

            return isLastItemEdgeValid((childIndex >= childCount - spanCount), parent, childCount, childIndex, spanIndex);
        }
    }

    protected boolean isTopEdge(RecyclerView parent, int childCount, int childIndex, int itemSpanSize, int spanIndex) {

        if (orientation == VERTICAL) {

            return (childIndex == 0) || isFirstItemEdgeValid((childIndex < spanCount), parent, childIndex);

        } else {

            return spanIndex == 0;
        }
    }

    protected boolean isBottomEdge(RecyclerView parent, int childCount, int childIndex, int itemSpanSize, int spanIndex) {

        if (orientation == VERTICAL) {

            return isLastItemEdgeValid((childIndex >= childCount - spanCount), parent, childCount, childIndex, spanIndex);

        } else {

            return (spanIndex + itemSpanSize) == spanCount;
        }
    }

    protected boolean isFirstItemEdgeValid(boolean isOneOfFirstItems, RecyclerView parent, int childIndex) {

        int totalSpanArea = 0;
        if (isOneOfFirstItems) {
            for (int i = childIndex; i >= 0; i--) {
                totalSpanArea = totalSpanArea + getItemSpanSize(parent, i);
            }
        }

        return isOneOfFirstItems && totalSpanArea <= spanCount;
    }

    protected boolean isLastItemEdgeValid(boolean isOneOfLastItems, RecyclerView parent, int childCount, int childIndex, int spanIndex) {

        int totalSpanRemaining = 0;
        if (isOneOfLastItems) {
            for (int i = childIndex; i < childCount; i++) {
                totalSpanRemaining = totalSpanRemaining + getItemSpanSize(parent, i);
            }
        }

        return isOneOfLastItems && (totalSpanRemaining <= spanCount - spanIndex);
    }
}