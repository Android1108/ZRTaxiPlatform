package org.wzeiri.zr.zrtaxiplatform.ui.activity.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import org.wzeiri.zr.zrtaxiplatform.adapter.SelectRecyclerAdapter;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.recycler.OnItemChildClickListener;

import java.util.List;

/**
 * @author k-lm on 2017/12/25.
 */

public abstract class SelectRecyclerActivity extends BaseRecyclerActivity<String, SelectRecyclerAdapter> {

    private OnSelectItemListener mSelectItemListener;

    protected int mSelectId = -1;

    public static final String KEY_SELECT_ID = "selectId";

    public static final String KEY_SELECT_NAME = "selectName";


    @Override
    void initView() {
        super.initView();
        mRefreshLayout.isShowLoad(false);
        getAdapter().setOnItemChildClickListener(new OnItemChildClickListener<String>() {
            @Override
            public void onItemClick(View view, int viewType, String data, int position) {
                getAdapter().setSelectPosition(position);
                if (mSelectItemListener != null) {
                    mSelectItemListener.onSelectItem(data, position);
                }
                getAdapter().notifyDataSetChanged();
            }

            @Override
            public boolean onItemLongClick(View view, int viewType, String data, int position) {
                return false;
            }

            @Override
            public void onItemChildClick(View parent, int viewType, String data, int position, View child, long childId) {

            }

            @Override
            public boolean onItemChildLongClick(View parent, int viewType, String data, int position, View child, long childId) {
                return false;
            }
        });
    }

    @Override
    public void onRefresh() {
        setCurrentPagerIndex(1);
        mRefreshLayout.setRefreshing(true);
        getData();
    }

    @Override
    public void onLoad() {
    }

    protected void getData() {

    }

    protected void loadDateEnd() {
        closeProgressDialog();
        updateEmptyStatus();
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public SelectRecyclerAdapter getAdapter(List<String> list) {
        return new SelectRecyclerAdapter(getThis(), list);
    }

    public void setOnSelectItemListener(OnSelectItemListener listener) {
        mSelectItemListener = listener;
    }


    public interface OnSelectItemListener {
        void onSelectItem(String data, int position);
    }


}
