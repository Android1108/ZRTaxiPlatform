package org.wzeiri.zr.zrtaxiplatform.recycler.decoration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

/**
 * @author k-lm on 2017/11/22.
 */

public class DividerGridItemDecoration extends RecyclerView.ItemDecoration {
    /**
     * 水平分割线高度
     */
    private int mHorizontalDividerHeight = 0;

    private int mVerticalDividerWidth = 0;

    /**
     * 水平方向
     */
    public static final int HORIZONTAL = 1;
    /**
     * 垂直方向
     */
    public static final int VERTICAL = 2;

    private int mLineColor = Color.TRANSPARENT;

    private int mCurrentOrientation = HORIZONTAL;

    private Rect mDividingRect;


    private Paint mDividingPaint;

    public DividerGridItemDecoration(int orientation) {
        if ((orientation & HORIZONTAL) != HORIZONTAL && (orientation & VERTICAL) != VERTICAL) {
            throw new RuntimeException("传入参数错误");
        }

        mCurrentOrientation = orientation;
        mDividingRect = new Rect();
        mDividingPaint = new Paint();
        mDividingPaint.setColor(mLineColor);
        mDividingPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }


    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getLeft() - params.leftMargin;
            final int right = child.getRight() + getCurrentVerticalDividerWidth();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + getCurrentHorizontalDividerHeight();

            mDividingRect.set(left, top, right, bottom);

            c.drawRect(mDividingRect, mDividingPaint);

        }

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        int spanCount = getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();

        if (isLastColum(parent, position, spanCount, childCount)) {
            outRect.set(0, 0, 0, getCurrentHorizontalDividerHeight());
        } else if (isLastRaw(parent, position, spanCount, childCount)) {
            outRect.set(0, 0, getCurrentVerticalDividerWidth(), 0);
        } else {
            outRect.set(0, 0, getCurrentVerticalDividerWidth(), getCurrentHorizontalDividerHeight());
        }

    }


    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {

            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager)
                    .getSpanCount();
        }
        return spanCount;
    }

    /**
     * 是否最后一列
     *
     * @param parent
     * @param pos
     * @param spanCount
     * @param childCount
     * @return
     */
    private boolean isLastColum(RecyclerView parent, int pos, int spanCount,
                                int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
            {
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
                {
                    return true;
                }
            } else {
                childCount = childCount - childCount % spanCount;
                if (pos >= childCount)// 如果是最后一列，则不需要绘制右边
                    return true;
            }
        }
        return false;
    }

    /**
     * 是否最后一行
     *
     * @param parent
     * @param pos
     * @param spanCount
     * @param childCount
     * @return
     */
    private boolean isLastRaw(RecyclerView parent, int pos, int spanCount,
                              int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            childCount = childCount - childCount % spanCount;
            if (pos >= childCount)// 如果是最后一行，则不需要绘制底部
                return true;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                childCount = childCount - childCount % spanCount;
                // 如果是最后一行，则不需要绘制底部
                if (pos >= childCount)
                    return true;
            } else
            // StaggeredGridLayoutManager 且横向滚动
            {
                // 如果是最后一行，则不需要绘制底部
                if ((pos + 1) % spanCount == 0) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

    }

    /**
     * 返回当前水平高度
     *
     * @return 水平高度
     */
    private int getCurrentHorizontalDividerHeight() {
        return (mCurrentOrientation & HORIZONTAL) != HORIZONTAL ? 0 : mHorizontalDividerHeight;
    }

    /**
     * 返回当前垂直宽度
     *
     * @return 垂直宽度
     */
    private int getCurrentVerticalDividerWidth() {
        return (mCurrentOrientation & VERTICAL) != VERTICAL ? 0 : mVerticalDividerWidth;
    }

    /**
     * 设置水平高度
     *
     * @param height
     */
    public void setHorizontalmDividerHeight(int height) {
        mHorizontalDividerHeight = height;
    }

    /**
     * 设置垂直宽度
     *
     * @param width
     */
    public void setVerticalDividerWidth(int width) {
        mVerticalDividerWidth = width;
    }

    /**
     * 设置分隔线颜色
     *
     * @param color 颜色
     */
    public void setDividingLineColor(int color) {
        mLineColor = color;
        mDividingPaint.setColor(color);
    }

}
