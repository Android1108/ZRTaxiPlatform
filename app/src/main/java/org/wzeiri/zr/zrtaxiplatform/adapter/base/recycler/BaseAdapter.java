package org.wzeiri.zr.zrtaxiplatform.adapter.base.recycler;

import android.content.Context;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lcsunm on 2017/5/23.
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int BASE_ITEM_TYPE_NORMAL = 0;

    public List<T> mList;
    private Context mContext;

    private OnItemChildClickListener<T> mOnItemChildClickListener;

    private SparseArrayCompat<RecyclerItem<T, ? extends RecyclerView.ViewHolder>> mItemType = new SparseArrayCompat<>();

    public BaseAdapter(Context context, List<T> list) {
        this.mList = list;
        if (this.mList == null) {
            this.mList = new ArrayList<>();
        }
        this.mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public BaseAdapter putItemType(int viewType, RecyclerItem<T, ? extends RecyclerView.ViewHolder> itemView) {
        mItemType.put(viewType, itemView);
        return this;
    }

    public BaseAdapter putItemType(RecyclerItem<T, ? extends RecyclerView.ViewHolder> itemView) {
        return putItemType(BASE_ITEM_TYPE_NORMAL, itemView);
    }

    public int getDataCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    public abstract int getItemViewType(T data, int position);

    @Override
    public int getItemViewType(int position) {
        if (mItemType.size() > 0) {
            T data = null;
            int realPosition = getRealPosition(position);
            if (realPosition < mList.size()) {
                data = mList.get(realPosition);
            }
            return getItemViewType(data, realPosition);
        } else {
            return BASE_ITEM_TYPE_NORMAL;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerItem itemView = mItemType.get(viewType);
        if (itemView == null) {
            return null;
        }
        return itemView.getViewHolder(LayoutInflater.from(mContext).inflate(itemView.getLayoutId(), parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null) {
            return;
        }
        final int adapterPosition = holder.getAdapterPosition();
        final int viewType = getItemViewType(adapterPosition);
        final int readPosition = getRealPosition(adapterPosition);

        RecyclerItem itemView = mItemType.get(viewType);
        if (itemView == null) {
            return;
        }

        T data = null;
        if (readPosition < mList.size()) {
            data = mList.get(readPosition);
        }
        final T data2 = data;

        if (data2 != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemChildClickListener != null) {
                        mOnItemChildClickListener.onItemClick(v, viewType, data2, readPosition);
                    }
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mOnItemChildClickListener != null) {
                        return mOnItemChildClickListener.onItemLongClick(v, viewType, data2, readPosition);
                    }
                    return false;
                }
            });
        }

        itemView.onBindViewHolder(mContext, holder.itemView, holder, viewType, data2, adapterPosition, readPosition);


    }

    protected int getRealPosition(int adapterPosition) {
        return adapterPosition;
    }

    public interface RecyclerItem<T, V extends RecyclerView.ViewHolder> {

        int getLayoutId();

        V getViewHolder(View view);

        void onBindViewHolder(Context context, View itemView, V viewHolder, int viewType, T data, int adapterPosition, int readPosition);

    }

    public void setOnItemChildClickListener(OnItemChildClickListener<T> onItemChildClickListener) {
        this.mOnItemChildClickListener = onItemChildClickListener;
    }

    public void setItemChildClick(final View parent, final int viewType, final int position, View... children) {
        if (children == null || children.length == 0 || position < 0 || position >= mList.size() || mOnItemChildClickListener == null) {
            return;
        }
        for (int i = 0; i < children.length; i++) {
            children[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemChildClickListener != null) {
                        mOnItemChildClickListener.onItemChildClick(parent, viewType, mList.get(position), position, v, v.getId());
                    }
                }
            });
        }
    }

    public void setItemChildLongClick(final View parent, final int viewType, final int position, View... children) {
        if (children == null || children.length == 0 || position < 0 || position >= mList.size() || mOnItemChildClickListener == null) {
            return;
        }
        for (int i = 0; i < children.length; i++) {
            children[i].setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mOnItemChildClickListener != null) {
                        return mOnItemChildClickListener.onItemChildLongClick(parent, viewType, mList.get(position), position, v, v.getId());
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return getDataCount();
    }

    /**
     * 判断字符串是否为空
     *
     * @return
     */
    public String getString(Object obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    /**
     * 从资源文件获取字符串
     *
     * @param resId
     * @return
     */
    public String getStringResId(int resId) {
        if (mContext == null) {
            return "";
        }
        return mContext.getResources().getString(resId);
    }
}
