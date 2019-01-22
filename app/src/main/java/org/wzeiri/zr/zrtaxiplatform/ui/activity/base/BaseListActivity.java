package org.wzeiri.zr.zrtaxiplatform.ui.activity.base;

import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.util.DensityUtil;
import org.wzeiri.zr.zrtaxiplatform.widget.RefreshLayout;
import org.wzeiri.zr.zrtaxiplatform.widget.RefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author k-lm on 2017/11/17.
 */

public abstract class BaseListActivity<T, A extends BaseAdapter> extends ActionbarActivity implements RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener {
    /**
     * 当前页数
     */
    private int mCurrentPagerIndex = 1;
    /**
     * 每次加载数量
     */
    private int mLoadPagerSize = 15;
    /**
     * 底部没有更多数据的提示View
     */
    private TextView mHintFoodView;
    /**
     * 是否添加底部提示View
     */
    private boolean isAddHintFoodView = false;

    @BindView(R.id.layout_refresh_list_view)
    protected RefreshListView mListView;
    @BindView(R.id.layout_refresh_layout)
    protected RefreshLayout mRefreshLayout;
    View mEmptyView;
    /**
     * 是否有Item字段
     */
    private boolean mIsItemField = true;

    /**
     * 是否显示加载对话框
     */
    private boolean mIsShowProgressDialog = true;


    /**
     * 是否toast
     */
    private boolean mIsShowToast = true;

    private List<T> mList = new ArrayList<>();

    private A mAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.layout_refresh_list_view;
    }


    @Override
    void initView() {
        super.initView();
        mRefreshLayout.setOnLoadListener(this);
        mRefreshLayout.setOnRefreshListener(this);
        mAdapter = getAdapter(mList);
        if (mAdapter != null) {
            mListView.setAdapter(mAdapter);
        }

        mEmptyView = findViewById(R.id.layout_refresh_empty);
        if (mEmptyView != null) {
            mListView.setEmptyView(mEmptyView);
        }

    }


    @Override
    public void onLoad() {
        mRefreshLayout.setLoading(true);
        mIsShowProgressDialog = false;
        loadData();
    }

    @Override
    public void onRefresh() {
        mCurrentPagerIndex = 1;
        mRefreshLayout.setRefreshing(true);
        mIsShowProgressDialog = true;
        loadData();
    }

    /**
     * 加载网络请求
     */
        protected final void loadData() {
        if (mIsItemField) {
            loadItemData();
        } else {
            loadNoItemData();
        }

    }

    /**
     * 设置是否加载item字段
     *
     * @param isItemField
     */
    protected void setIsItemField(boolean isItemField) {
        mIsItemField = isItemField;
    }

    /**
     * 设置是否toast
     *
     * @param isShowProgressDialog
     */
    protected void setShowProgressDialog(boolean isShowProgressDialog) {
        mIsShowProgressDialog = isShowProgressDialog;
    }

    /**
     * 加载item字段的网络数据
     */
    private void loadItemData() {
        Call<BaseBean<BaseListBean<T>>> call = getNetCall(mCurrentPagerIndex, mLoadPagerSize);
        if (call == null) {
            mRefreshLayout.setRefreshing(false);
            mRefreshLayout.setLoading(false);
            closeProgressDialog();
            return;
        }

        call.enqueue(new MsgCallBack<BaseBean<BaseListBean<T>>>(this, mIsShowProgressDialog) {
                         @Override
                         public void onSuccess(Call<BaseBean<BaseListBean<T>>> call, Response<BaseBean<BaseListBean<T>>> response) {
                             BaseListBean<T> baseBean = response.body().getResult();
                             List<T> list = null;
                             // 获取数据后再清空list，否则刷新会出现java.lang.IndexOutOfBoundsException异常
                             if (mCurrentPagerIndex == 1) {
                                 mList.clear();
                             }
                             if (baseBean == null) {
                                 onLoadSuccess(list);
                             } else {
                                 list = baseBean.getItems();
                                 onLoadSuccess(list);
                             }
                             loadEnd(list);

                         }

                         @Override
                         public void onError(Call<BaseBean<BaseListBean<T>>> call, Throwable t) {
                             if (mCurrentPagerIndex == 1) {
                                 mList.clear();
                             }
                             closeProgressDialog();
                             onLoadError(t.getMessage());
                             loadEnd(null);

                         }
                     }

        );
    }

    /**
     * 加载没有Item字段的网络数据
     */
    private void loadNoItemData() {
        Call<BaseBean<List<T>>> call = getNotItemNetCall(mCurrentPagerIndex, mLoadPagerSize);
        if (call == null) {
            mRefreshLayout.setRefreshing(false);
            mRefreshLayout.setLoading(false);
            closeProgressDialog();
            return;
        }
        call.enqueue(new MsgCallBack<BaseBean<List<T>>>(this, mIsShowProgressDialog) {
                         @Override
                         public void onSuccess(Call<BaseBean<List<T>>> call, Response<BaseBean<List<T>>> response) {
                             List<T> baseBean = response.body().getResult();
                             // 获取数据后再清空list，否则刷新会出现java.lang.IndexOutOfBoundsException异常
                             if (mCurrentPagerIndex == 1) {
                                 mList.clear();
                             }
                             if (baseBean == null) {
                                 onLoadSuccess(null);
                             } else {
                                 onLoadSuccess(baseBean);
                             }
                             loadEnd(baseBean);

                         }

                         @Override
                         public void onError(Call<BaseBean<List<T>>> call, Throwable t) {
                             if (mCurrentPagerIndex == 1) {
                                 mList.clear();
                             }
                             closeProgressDialog();
                             onLoadError(t.getMessage());
                             loadEnd(null);

                         }
                     }
        );
    }

    /**
     * 加载结束
     *
     * @param list
     */
    private void loadEnd(List<T> list) {
        if (list != null && list.size() >= 0) {
            mCurrentPagerIndex++;
        }
        if (mRefreshLayout != null) {
            mRefreshLayout.setRefreshing(false);
            mRefreshLayout.setLoading(false);
        }
    }

    /**
     * 设置是否toast
     *
     * @param isShowToast
     */
    public void setShowToast(boolean isShowToast) {
        mIsShowToast = isShowToast;
    }


    public final void setCurrentPagerIndex(int index) {
        if (index < 1) {
            index = 1;
        }
        mCurrentPagerIndex = index;
    }


    public final void setLoadPagerSize(int size) {
        if (size <= 0) {
            size = 1;
        }

        mLoadPagerSize = size;
    }

    /**
     * 加载网络成功
     *
     * @param list
     */
    protected void onLoadSuccess(List<T> list) {
        if (list != null && list.size() > 0) {
            mList.addAll(list);
        }

        if (list == null || list.size() < mLoadPagerSize) {
            mRefreshLayout.setCanLoad(false);
            addHintFoodView();
        } else {
            mRefreshLayout.setCanLoad(true);
            removeHintFoodView();
        }

        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 添加底部提示内容
     */
    private void addHintFoodView() {
        if (mAdapter == null) {
            return;
        }
        if (isAddHintFoodView && mHintFoodView != null) {
            mListView.removeFooterView(mHintFoodView);
        }

        if (mHintFoodView == null) {
            TextView textView = new TextView(getThis());
            textView.setText("无更多内容");
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimension(R.dimen.text_size_small));
            textView.setTextColor(ContextCompat.getColor(getThis(), R.color.gray40));
            textView.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
            mHintFoodView = textView;
        }
        // foodView 高度
        int listViewHeight = mListView.getHeight();
        // foodView 最小高度
        int foodViewHeight = DensityUtil.dip2px(getThis(), 20);
        View itemView = null;

        // 如果listView内部有View，则获取ListView的子View ， 没有则从取适配器中，生成一个新的View
        if (mListView.getChildCount() > 0) {
            itemView = mListView.getChildAt(0);
        } else if (mList.size() > 0) {
            itemView = mAdapter.getView(0, null, mListView);
        }

        if (itemView != null) {
            // 获取View的高度
            int itemViewHeight = itemView.getHeight();
            if (itemViewHeight == 0) {
                // 如果View的高度为0 ，则测量View，并获取view高度
                itemView.measure(0, 0);
                itemViewHeight = itemView.getMeasuredHeight();
            }
            // 默认认为每个itemView的高度一样， 所有View的高度总和 + foodView的最小高度
            itemViewHeight = itemViewHeight * mList.size() + foodViewHeight;

            if (listViewHeight > itemViewHeight) {
                foodViewHeight = listViewHeight - itemViewHeight;
            }
        } else {
            foodViewHeight = listViewHeight;
        }
        isAddHintFoodView = true;
        mHintFoodView.setWidth(mListView.getWidth());
        mHintFoodView.setHeight(foodViewHeight);
        mListView.addFooterView(mHintFoodView, null, false);
    }

    /**
     * 移除底部提示内容
     */
    private void removeHintFoodView() {
        if (mHintFoodView == null || !isAddHintFoodView) {
            return;
        }
        mListView.removeFooterView(mHintFoodView);
        isAddHintFoodView = false;
    }

    /**
     * 返回适配器
     *
     * @return
     */
    protected final A getAdapter() {
        return mAdapter;
    }

    /**
     * 添加数据
     *
     * @param data
     */
    protected final void addData(T data) {
        mList.add(data);
    }

    /**
     * 添加数据
     *
     * @param data
     */
    protected final void addDatas(List<T> data) {
        if (data == null) {
            return;
        }
        mList.addAll(data);
    }

    /**
     * 删除数据
     *
     * @param datas
     */
    protected final void removeDatas(List<T> datas) {
        if (datas == null) {
            return;
        }
        mList.removeAll(datas);
    }

    /**
     * 删除数据
     *
     * @param data
     */
    protected final void removeData(T data) {
        mList.remove(data);
    }

    /**
     * 删除数据
     *
     * @param position
     */
    protected final void removeData(int position) {
        mList.remove(position);
    }

    /**
     * 删除数据
     */
    protected final void removeAll() {
        mList.clear();
    }

    protected final T getData(int position) {
        return mList.get(position);
    }

    protected int getSize() {
        return mList.size();
    }

    /**
     * 没有数据时显示视图
     */
    protected void updateEmptyStatus() {
        if (mEmptyView == null) {
            return;
        }

        if (mList.size() == 0 || mList == null) {
            mEmptyView.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 加载网络失败
     *
     * @param msg
     */
    protected void onLoadError(String msg) {
        if (mIsShowToast) {
            showToast(msg);
        }
        if (mAdapter != null && mCurrentPagerIndex == 1) {
            mAdapter.notifyDataSetChanged();
        }
    }


    protected void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        mListView.setOnItemClickListener(listener);
    }


    protected final ListView getListView() {
        return mListView;
    }

    public abstract A getAdapter(List<T> list);


    protected Call<BaseBean<BaseListBean<T>>> getNetCall(int currentIndex, int pagerSize) {
        return null;
    }

    protected Call<BaseBean<List<T>>> getNotItemNetCall(int currentIndex, int pagerSize) {
        return null;
    }


}
