package org.wzeiri.zr.zrtaxiplatform.ui.fragment.base;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.recycler.BaseAdapter;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.recycler.OnItemChildClickListener;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.widget.RecyclerRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author k-lm on 2017/11/20.
 */

public abstract class BaseRecyclerFragment<T, A extends BaseAdapter> extends ActionbarFragment implements
        SwipeRefreshLayout.OnRefreshListener, RecyclerRefreshLayout.OnLoadListener,
        OnItemChildClickListener<T> {

    @BindView(R.id.layout_recycler_view_recycler)
    RecyclerView mRecycler;
    @BindView(R.id.layout_recycler_view_refresh_layout)
    RecyclerRefreshLayout mRefreshLayout;
    @BindView(R.id.layout_refresh_empty)
    View mEmptyView;

    private final List<T> mList = new ArrayList<>();

    private A mAdapter;
    /**
     * 是否toast
     */
    private boolean mIsShowToast = true;

    /**
     * 是否显示加载对话框
     */
    private boolean mIsShowProgressDialog = true;

    /**
     * 当前页数
     */
    private int mCurrentPagerIndex = 1;
    /**
     * 每次加载数量
     */
    private int mLoadPagerSize = 15;

    /**
     * 是否有Item字段
     */
    private boolean mIsItemField = true;

    @Override
    public int getLayoutId() {
        return R.layout.layout_refresh_recycler_view;
    }


    @Override
    void initView() {
        mRefreshLayout.setOnRefreshListener(this);
        mAdapter = getAdapter(mList);
        mAdapter.setOnItemChildClickListener(this);
        RecyclerView.LayoutManager layoutManager = getLayoutManager();

        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setAdapter(mAdapter);


    }


    protected RecyclerView getRecyclerView() {
        return mRecycler;
    }


    @Override
    public void onItemClick(View view, int viewType, T data, int position) {

    }

    @Override
    public boolean onItemLongClick(View view, int viewType, T data, int position) {
        return false;
    }

    @Override
    public void onItemChildClick(View parent, int viewType, T data, int position, View child, long childId) {

    }

    @Override
    public boolean onItemChildLongClick(View parent, int viewType, T data, int position, View child, long childId) {
        return false;
    }

    @Override
    public void onRefresh() {
        mIsShowProgressDialog = true;
        mCurrentPagerIndex = 1;
        mRefreshLayout.setRefreshing(true);
        loadData();
    }

    @Override
    public void onLoad() {
        mIsShowProgressDialog = false;
        mRefreshLayout.setLoading(true);
        loadData();
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

    protected T getData(int position) {
        return mList.get(position);
    }

    protected int getSize() {
        return mList.size();
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
     * 加载item字段的网络数据
     */
    private void loadItemData() {
        Call<BaseBean<BaseListBean<T>>> call = getNetCall(mCurrentPagerIndex, mLoadPagerSize);
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
                             // 获取数据后再清空list，否则刷新会出现java.lang.IndexOutOfBoundsException异常
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
        call.enqueue(new MsgCallBack<BaseBean<List<T>>>(this, mIsShowProgressDialog) {
                         @Override
                         public void onSuccess(Call<BaseBean<List<T>>> call, Response<BaseBean<List<T>>> response) {
                             List<T> baseBean = response.body().getResult();
                             // 获取数据后再清空list，否则刷新会出现java.lang.IndexOutOfBoundsException异常
                             if (mCurrentPagerIndex == 1) {
                                 mList.clear();
                             }
                             if (baseBean == null) {
                                 onLoadSuccess(baseBean);
                             } else {
                                 onLoadSuccess(baseBean);
                             }
                             loadEnd(baseBean);

                         }

                         @Override
                         public void onError(Call<BaseBean<List<T>>> call, Throwable t) {
                             // 获取数据后再清空list，否则刷新会出现java.lang.IndexOutOfBoundsException异常
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
     * 没有数据时显示视图
     */
    protected void updateEmptyStatus() {
        if (mEmptyView == null) {
            return;
        }

        if (mList.size() == 0 || mList == null) {
            mEmptyView.setVisibility(View.VISIBLE);
            mRecycler.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.GONE);
            mRecycler.setVisibility(View.VISIBLE);
        }
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
        updateEmptyStatus();
        if (mRefreshLayout != null) {
            mRefreshLayout.setRefreshing(false);
            mRefreshLayout.setLoading(false);
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
     * @param isShowToast
     */
    public void setShowToast(boolean isShowToast) {
        mIsShowToast = isShowToast;
    }

    /**
     * 设置是否toast
     *
     * @param isShowProgressDialog
     */
    public void setShowProgressDialog(boolean isShowProgressDialog) {
        mIsShowProgressDialog = isShowProgressDialog;
    }

    public abstract RecyclerView.LayoutManager getLayoutManager();

    public abstract A getAdapter(List<T> list);

    protected Call<BaseBean<BaseListBean<T>>> getNetCall(int currentIndex, int pagerSize) {
        return null;
    }

    protected Call<BaseBean<List<T>>> getNotItemNetCall(int currentIndex, int pagerSize) {
        return null;
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

        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
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
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

}
