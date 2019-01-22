package org.wzeiri.zr.zrtaxiplatform.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;


/**
 * 继承自SwipeRefreshLayout,从而实现滑动到底部时上拉加载更多的功能.
 */
public class RecyclerRefreshLayout extends RefreshLayout {

    /**
     * 按下时的y坐标
     */
    private int mYDown;
    /**
     * 抬起时的y坐标, 与mYDown一起用于滑动到底部时判断是上拉还是下拉
     */
    private int mLastY;

    /**
     * 滑动到最下面时的上拉操作
     */

    private int mTouchSlop;
    /**
     * listview实例
     */
    private RecyclerView mRecyclerView;

    /**
     * 上拉监听器, 到了最底部的上拉加载操作
     */
    private OnLoadListener mOnLoadListener;

    /**
     * 是否在加载中 ( 上拉加载更多 )
     */
    private boolean isLoading = false;

    /**
     * 是否在允许加载更多
     */
    private boolean isCanLoad = true;

    /**
     * 是否显示加载
     */
    private boolean isShowRefresh = true;

    private boolean isScrolled = true;
    int mPastVisibleItems, mVisibleItemCount, mTotalItemCount;
    private LinearLayoutManager mLayoutManager;
    private int mAutoLoadNumber = 2;
    private int mDistanceY = 0;

    /**
     * @param context
     */
    public RecyclerRefreshLayout(Context context) {
        this(context, null);
    }

    @SuppressLint("InflateParams")
    public RecyclerRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop() * 3;

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // 初始化ListView对象
        if (mRecyclerView == null) {
            getListView();
        }
    }

    /**
     * 获取ListView对象
     */
    private void getListView() {
        int children = getChildCount();
        if (children > 0 && mRecyclerView == null) {
            for (int i = 0; i < children; i++) {
                View childView = getChildAt(i);
                if (childView instanceof RecyclerView) {
                    mRecyclerView = (RecyclerView) childView;
                    if (mRecyclerView.getLayoutManager() == null || !(mRecyclerView.getLayoutManager() instanceof LinearLayoutManager)) {
                        return;
                    }
                    mLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                    // 设置滚动监听器给ListView, 使得滚动的情况下也可以自动加载
                    mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                        }

                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                            //mDistanceY = dy;
                            mVisibleItemCount = mLayoutManager.getChildCount();
                            mTotalItemCount = mLayoutManager.getItemCount();
                            mPastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

                            if (canLoad()) {
                                loadData();
                            }
                        }
                    });
                    //Log.d(VIEW_LOG_TAG, "### 找到listview");
                    break;
                }
            }

        }
    }

    //判断是否是加载中，若加载中则不执行官方刷新事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isShowRefresh) {
            if (isLoading) {
                return false;
            } else {
                return super.onInterceptTouchEvent(ev);
            }
        } else {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 按下
                mYDown = (int) event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                // 移动
                mLastY = (int) event.getRawY();
                break;

            case MotionEvent.ACTION_UP:
                // 抬起
                mLastY = (int) event.getRawY();
                if (canLoad()) {
                    loadData();
                }
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    /**
     * 是否可以加载更多, 条件是到了最底部, listview不在加载中, 且为上拉操作 且是超过最小长度,且没有执行下拉动作.
     *
     * @return
     */
    private boolean canLoad() {
        return isBottom() && !isLoading && isPullUp() && !isRefreshing() && mRecyclerView.getChildCount() > 0 && (isShowRefresh && isCanLoad);
    }

    /**
     * 判断是否到了最底部
     */
    private boolean isBottom() {

        if (mRecyclerView != null && mRecyclerView.getAdapter() != null) {
            return ((mVisibleItemCount + mPastVisibleItems) >= mTotalItemCount - mAutoLoadNumber);
        }
        return false;
    }

    /**
     * 是否是上拉操作
     *
     * @return
     */
    private boolean isPullUp() {
        if ((mYDown - mLastY) == mYDown) {
            return false;
        }
        return (mYDown - mLastY) >= mTouchSlop;
        //return mDistanceY >= mTouchSlop;
    }

    /**
     * 如果到了最底部,而且是上拉操作.那么执行onLoad方法
     */
    private void loadData() {
        if (mOnLoadListener != null) {
            setLoading(true);
            mOnLoadListener.onLoad();
        }
    }

    public boolean isLoading() {
        return isLoading;
    }

    /**
     * @param loading
     */
    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public void setLoadingTriggerThreshold(int autoLoadNumber) {
        this.mAutoLoadNumber = autoLoadNumber;
    }

    /**
     * @param loadListener
     */
    public void setOnLoadListener(OnLoadListener loadListener) {
        mOnLoadListener = loadListener;
    }

    /**
     * 设置是否允许刷新及加载更多
     *
     * @param isShow
     */
    public void isShowRefresh(boolean isShow) {
        isShowRefresh = isShow;
    }



    @Override
    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
        if (!isLoading)
            super.setOnRefreshListener(listener);
    }

    /**
     * 加载更多的监听器
     *
     * @author mrsimple
     */
    public interface OnLoadListener {
        void onLoad();
    }
}