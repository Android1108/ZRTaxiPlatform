package org.wzeiri.zr.zrtaxiplatform.ui.activity.base;

import android.view.View;
import android.widget.AdapterView;

import org.wzeiri.zr.zrtaxiplatform.adapter.SelectListAdapter;

import java.util.List;

/**
 * @author k-lm on 2017/12/18.
 */

public abstract class SelectListActivity extends BaseListActivity<String, SelectListAdapter> {

    private OnSelectItemListener mSelectItemListener;

    protected int mSelectId = -1;

    public static final String KEY_SELECT_ID = "selectId";

    public static final String KEY_SELECT_NAME = "selectName";

    @Override
    public SelectListAdapter getAdapter(List<String> list) {
        return new SelectListAdapter(list);
    }

    @Override
    void initView() {
        super.initView();
        mRefreshLayout.isShowLoad(false);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mSelectItemListener == null) {
                    return;
                }
                getAdapter().setSelectPosition(position);
                mSelectItemListener.onSelectItem(getAdapter().getData(position), position);
                getAdapter().notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onLoad() {
    }


    @Override
    public void onRefresh() {
        setCurrentPagerIndex(1);
        mRefreshLayout.setRefreshing(true);
        getData();
    }


    protected void loadDateEnd(){
        closeProgressDialog();
        mRefreshLayout.setRefreshing(false);
    }

    protected void setRefreshing(boolean isRefreshing){
        mRefreshLayout.setRefreshing(isRefreshing);
    }

    protected void getData() {

    }

    @Override
    protected void setOnItemClickListener(final AdapterView.OnItemClickListener listener) {

    }

    public void setOnSelectItemListener(OnSelectItemListener listener) {
        mSelectItemListener = listener;
    }

    public interface OnSelectItemListener {
        void onSelectItem(String data, int position);
    }


}
