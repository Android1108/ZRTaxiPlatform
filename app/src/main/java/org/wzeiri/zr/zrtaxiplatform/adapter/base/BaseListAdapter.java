package org.wzeiri.zr.zrtaxiplatform.adapter.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.wzeiri.zr.zrtaxiplatform.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author k-lm on 2017/11/14.
 */

public abstract class BaseListAdapter<T> extends BaseAdapter {
    private List<T> mList;
    private Context mContext;

    public BaseListAdapter(List<T> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        mList = list;
    }


    public void setList(List<T> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        mList = list;
    }

    public void addList(List<T> list) {
        if (list == null) {
            return;
        }
        mList.addAll(list);
    }

    public void removeList(List<T> list) {
        if (list == null) {
            return;
        }
        mList.remove(list);
    }

    public void clearList() {
        mList.clear();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        Integer viewType = getItemViewType(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(getItemLayoutId(viewType), parent, false);
            convertView.setTag(R.id.adapter_item_view_type, viewType);
        } else {
            Integer type = (Integer) convertView.getTag(R.id.adapter_item_view_type);
            if (!viewType.equals(type)) {
                convertView = LayoutInflater.from(parent.getContext())
                        .inflate(getItemLayoutId(viewType), parent, false);
                convertView.setTag(R.id.adapter_item_view_type, viewType);
            }


        }

        getItemView(position, convertView, parent, viewType);

        return convertView;
    }

    /**
     * 返回数据
     *
     * @param position
     * @return
     */
    public T getData(int position) {
        if (mList == null || position < 0 || position >= mList.size()) {
            return null;
        }
        return mList.get(position);
    }


    @Override
    public int getItemViewType(int position) {
        return getItemViewType(mList.get(position), position);
    }

    public abstract void getItemView(int position, View convertView, ViewGroup parent, int viewType);


    public abstract int getItemLayoutId(int viewType);


    public int getItemViewType(T data, int position) {
        return 0;
    }

    /**
     * 返回Context ,只有在执行{{@link #getView(int, View, ViewGroup)} 后才被赋值}
     *
     * @return context
     */
    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }

}
