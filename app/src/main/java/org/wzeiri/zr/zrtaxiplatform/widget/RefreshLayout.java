package org.wzeiri.zr.zrtaxiplatform.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;


import org.wzeiri.zr.zrtaxiplatform.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 继承自SwipeRefreshLayout,从而实现滑动到底部时上拉加载更多的功能.
 *
 * @author mrsimple
 */
public class RefreshLayout extends SwipeRefreshLayout implements
        OnScrollListener {

    /**
     * 滑动到最下面时的上拉操作
     */

    private int mTouchSlop;
    /**
     * listview实例
     */
    private ListView mListView;

    /**
     * 上拉监听器, 到了最底部的上拉加载操作
     */
    private OnLoadListener mOnLoadListener;

    /**
     * ListView的加载中footer
     */
    private View mListViewFooter;

    /**
     * 按下时的y坐标
     */
    private int mYDown;
    /**
     * 抬起时的y坐标, 与mYDown一起用于滑动到底部时判断是上拉还是下拉
     */
    private int mLastY;
    /**
     * 是否在加载中 ( 上拉加载更多 )
     */
    private boolean isLoading = false;
    /**
     * 是否显示刷新
     */
    private boolean isShowRefresh = true;

    /**
     * 是否在允许加载更多
     */
    private boolean isCanLoad = true;
    /**
     * 是否循序加载
     */
    private boolean isShowLoad = true;
    private int lastCount = -1;

    private int currentCoutn = 0;


    /**
     * @param context
     */
    public RefreshLayout(Context context) {
        this(context, null);
    }

    @SuppressLint("InflateParams")
    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop() * 3;

        mListViewFooter = LayoutInflater.from(context).inflate(R.layout.view_listview_footer, null, false);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // 初始化ListView对象
        if (mListView == null) {
            getListView();
        }
    }

    /**
     * 获取ListView对象
     */
    private void getListView() {
        int childs = getChildCount();
        if (childs > 0 && mListView == null) {
            for (int i = 0; i < childs; i++) {
                View childView = getChildAt(i);
                if (childView instanceof ListView) {
                    mListView = (ListView) childView;
                    // 设置滚动监听器给ListView, 使得滚动的情况下也可以自动加载
                    mListView.setOnScrollListener(this);
                    Log.d(VIEW_LOG_TAG, "### 找到listview");
                }
            }

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
                if (canLoad() && isShowLoad) {
                    loadData();
                }
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    //判断是否是加载中，若加载中则不执行官方刷新事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub

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

    /**
     * 是否可以加载更多, 条件是到了最底部, listview不在加载中, 且为上拉操作 且是超过最小长度,且没有执行下拉动作.
     *
     * @return
     */
    private boolean canLoad() {
        return isBottom() && !isLoading && isPullUp() && !isRefreshing() && mListView.getChildCount() > 0 && isCanLoad;
    }
//	private boolean canLoad() {
//		return isBottom() && !isLoading && isPullUp()&& isExceedMin();
//	}

    /**
     * 判断是否到了最底部
     */
    private boolean isBottom() {

        if (mListView != null && mListView.getAdapter() != null) {
            return mListView.getLastVisiblePosition() == (mListView
                    .getAdapter().getCount() - 1);
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
    }

//	private boolean isExceedMin(){
//		// 设置状态
//		lastCount=currentCoutn;
//		currentCoutn=mListView.getAdapter().getCount();
//		return lastCount!=currentCoutn;
//	}

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
        if (isLoading) {
            addFooterView();
        } else {
            removeFooterView();
            mYDown = 0;
            mLastY = 0;
        }
    }

    /**
     * @param loadListener
     */
    public void setOnLoadListener(OnLoadListener loadListener) {
        mOnLoadListener = loadListener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // 滚动时到了最底部也可以加载更多
        if (canLoad() && isShowLoad) {
            loadData();
        }
    }

    /**
     * 设置是否允许加载更多
     *
     * @param isCan
     */
    public void setCanLoad(boolean isCan) {
        isCanLoad = isCan;
    }

    /**
     * 设置是否允许刷新及加载更多
     *
     * @param isShow
     */
    public void isShowRefresh(boolean isShow) {
        isShowRefresh = isShow;
    }

    /**
     * 设置是否允许刷新及加载更多
     *
     * @param isShow
     */
    public void isShowLoad(boolean isShow) {
        isShowLoad = isShow;
    }

    public void addFooterView() {
        if (mListView != null) {
            if (mListView.getFooterViewsCount() <= 0) {
                mListView.addFooterView(mListViewFooter, null, false);
            }
        }
    }

    public void removeFooterView() {
        if (mListView != null) {
            if (mListView.getFooterViewsCount() > 0) {
                mListView.removeFooterView(mListViewFooter);
            }
        }
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener listener) {
        // TODO Auto-generated method stub
        if (!isLoading)
            super.setOnRefreshListener(listener);
    }


    /**
     * 设置刷新
     */
    public static void setRefreshing(SwipeRefreshLayout refreshLayout,
                                     boolean refreshing, boolean notify) {
        Class<? extends SwipeRefreshLayout> refreshLayoutClass = refreshLayout
                .getClass();
        if (refreshLayoutClass != null) {

            try {
                Method setRefreshing = refreshLayoutClass.getDeclaredMethod(
                        "setRefreshing", boolean.class, boolean.class);
                setRefreshing.setAccessible(true);
                setRefreshing.invoke(refreshLayout, refreshing, notify);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }



    /**
     * 加载更多的监听器
     *
     * @author mrsimple
     */
    public static interface OnLoadListener {
        public void onLoad();
    }
}