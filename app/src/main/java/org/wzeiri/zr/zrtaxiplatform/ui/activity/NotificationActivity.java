package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.NotificationAdapter;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.recycler.OnItemChildClickListener;
import org.wzeiri.zr.zrtaxiplatform.bean.NotificationBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.ISundry;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.util.TimeUtil;
import org.wzeiri.zr.zrtaxiplatform.widget.RecyclerRefreshLayout;
import org.wzeiri.zr.zrtaxiplatform.widget.RefreshLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 系统通知页面
 *
 * @author k-lm on 2018/1/2.
 */

public class NotificationActivity extends ActionbarActivity implements SwipeRefreshLayout.OnRefreshListener,
        RefreshLayout.OnLoadListener {
    @BindView(R.id.layout_recycler_view_recycler)
    RecyclerView mRecycler;
    @BindView(R.id.layout_recycler_view_refresh_layout)
    RecyclerRefreshLayout mRefreshLayout;

    private ISundry mISundry;

    /**
     * 当前页数
     */
    private int mCurrentPagerIndex = 1;
    /**
     * 每次加载数量
     */
    private int mLoadPagerSize = 15;

    private List<NotificationBean.ItemsBean> mItemBeanList = new ArrayList<>();

    private NotificationAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_refresh_recycler_view;
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("系统消息");
        mRefreshLayout.setOnLoadListener(this);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setBackgroundColor(Color.TRANSPARENT);
        int padding = getResources().getDimensionPixelOffset(R.dimen.layout_margin);
        mRecycler.setPadding(padding, padding, padding, 0);


        mAdapter = new NotificationAdapter(getThis(), mItemBeanList);
        LinearLayoutManager manager = new LinearLayoutManager(getThis(), LinearLayoutManager.VERTICAL, false);
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(manager);


        mAdapter.setOnItemChildClickListener(new OnItemChildClickListener<NotificationBean.ItemsBean>() {
            @Override
            public void onItemClick(View view, int viewType, NotificationBean.ItemsBean data, int position) {
                String title = data.getTitle();
                String content = data.getMessage();

                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
                    return;
                }
                MessageDetailActivity.start(getThis(), title, content);
            }

            @Override
            public boolean onItemLongClick(View view, int viewType, NotificationBean.ItemsBean data, int position) {
                return false;
            }

            @Override
            public void onItemChildClick(View parent, int viewType, NotificationBean.ItemsBean data, int position, View child, long childId) {

            }

            @Override
            public boolean onItemChildLongClick(View parent, int viewType, NotificationBean.ItemsBean data, int position, View child, long childId) {
                return false;
            }
        });

    }

    @Override
    protected void initData() {
        super.initData();
        mISundry = RetrofitHelper.create(ISundry.class);
        onRefresh();

    }

    @Override
    public void onRefresh() {
        mCurrentPagerIndex = 1;
        mRefreshLayout.setRefreshing(true);
        loadData();
    }

    @Override
    public void onLoad() {
        mRefreshLayout.setLoading(true);
        loadData();
    }

    private void loadData() {
        mISundry.getAppUserNotifications(null,
                mLoadPagerSize, mCurrentPagerIndex)
                .enqueue(new MsgCallBack<BaseBean<NotificationBean>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<NotificationBean>> call, Response<BaseBean<NotificationBean>> response) {
                        NotificationBean bean = response.body().getResult();
                        // 获取数据后再清空list，否则刷新会出现java.lang.IndexOutOfBoundsException异常
                        if (mCurrentPagerIndex == 1) {
                            mItemBeanList.clear();
                        }
                        if (bean == null) {
                            mRefreshLayout.setRefreshing(false);
                            mRefreshLayout.setLoading(false);
                            return;
                        }

                        List<NotificationBean.ItemsBean> itemsBeanList = bean.getItems();

                        if (itemsBeanList == null || itemsBeanList.size() == 0) {
                            mRefreshLayout.setRefreshing(false);
                            mRefreshLayout.setLoading(false);
                            return;
                        }
                        mCurrentPagerIndex++;
                        addData(itemsBeanList);
                        mRefreshLayout.setRefreshing(false);
                        mRefreshLayout.setLoading(false);

                    }

                    @Override
                    public void onError(Call<BaseBean<NotificationBean>> call, Throwable t) {
                        super.onError(call, t);
                        if (mCurrentPagerIndex == 1) {
                            mItemBeanList.clear();
                        }
                        mRefreshLayout.setRefreshing(false);
                        mRefreshLayout.setLoading(false);
                    }
                });
    }

    private void addData(@NonNull List<NotificationBean.ItemsBean> list) {
        int count = list.size() * 2;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = null;

        for (int i = 0; i < count; i++) {
            NotificationBean.ItemsBean bean;
            if (i == 0 || i % 2 == 0) {
                int index = i / 2;
                NotificationBean.ItemsBean itemsBean = list.get(index);
                bean = new NotificationBean.ItemsBean();
                calendar.setTime(itemsBean.getCreationTime());
                // 消息发布时间
                int notificationYear = calendar.get(Calendar.YEAR);
                int notificationMonth = calendar.get(Calendar.MONTH);
                int notificationDay = calendar.get(Calendar.DAY_OF_MONTH);

                calendar = Calendar.getInstance();
                // 当前时间
                int currentYear = calendar.get(Calendar.YEAR);
                int currentMonth = calendar.get(Calendar.MONTH);
                int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

                // 当年份、月份 不一样 或者天数 小于1天 则显示全部日期
                if (notificationYear != currentYear ||
                        currentMonth != notificationMonth ||
                        notificationDay < currentDay - 1) {
                    bean.setCreationTimeStr(TimeUtil.mFormatDateAndTime.format(itemsBean.getCreationTime()));
                } else {
                    if (dateFormat == null) {
                        dateFormat = new SimpleDateFormat("hh:mm", Locale.CHINESE);
                    }
                    String dayStr;
                    if (notificationDay == currentDay) {
                        dayStr = "今天 " + dateFormat.format(itemsBean.getCreationTime());
                    } else {
                        dayStr = "昨天 " + dateFormat.format(itemsBean.getCreationTime());
                    }

                    bean.setCreationTimeStr(dayStr);
                }

            } else {
                int index = i / 2;
                bean = list.get(index);
            }

            mItemBeanList.add(bean);
            mAdapter.notifyDataSetChanged();
        }
    }


}
