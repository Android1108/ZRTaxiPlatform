package org.wzeiri.zr.zrtaxiplatform.adapter.base.recycler;

import android.content.Context;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 可添加Header和Footer
 * Created by Lcsunm on 2017/5/24.
 */

public abstract class HeaderAndFooterAdapter<T> extends BaseAdapter<T> {

    public static final int BASE_ITEM_TYPE_HEADER = 72000;
    public static final int BASE_ITEM_TYPE_FOOTER = 71000;

    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mFooterViews = new SparseArrayCompat<>();


    public HeaderAndFooterAdapter(Context context, List<T> list) {
        super(context, list);
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPos(position)) {
            return mHeaderViews.keyAt(position);
        } else if (isFooterViewPos(position)) {
            return mFooterViews.keyAt(position - getHeadersCount() - super.getItemCount());
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null) {
            View headerView = mHeaderViews.get(viewType);
            ViewGroup.LayoutParams layoutParams = parent.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            headerView.setLayoutParams(layoutParams);
            return new HeaderAndFooterViewHolder(headerView);
        }
        if (mFooterViews.get(viewType) != null) {
            View footerView = mFooterViews.get(viewType);
            ViewGroup.LayoutParams layoutParams = parent.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            footerView.setLayoutParams(layoutParams);
            return new HeaderAndFooterViewHolder(footerView);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderViewPos(holder.getAdapterPosition())) {
            return;
        }
        if (isFooterViewPos(holder.getAdapterPosition())) {
            return;
        }
        super.onBindViewHolder(holder, position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (getItemCount() == super.getItemCount()) {
            return;
        }
        WrapperUtils.onAttachedToRecyclerView(recyclerView, new WrapperUtils.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                int viewType = getItemViewType(position);
                if (mHeaderViews.get(viewType) != null) {
                    return layoutManager.getSpanCount();
                } else if (mFooterViews.get(viewType) != null) {
                    return layoutManager.getSpanCount();
                }
                if (oldLookup != null){
                    return oldLookup.getSpanSize(getRealPosition(position));
                }
                return 1;
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (getItemCount() == super.getItemCount()) {
            return;
        }
        int position = holder.getLayoutPosition();
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            WrapperUtils.setFullSpan(holder);
        }
    }

    @Override
    protected int getRealPosition(int adapterPosition) {
        if (isFooterViewPos(adapterPosition)) {
            return adapterPosition - getHeadersCount() - super.getItemCount();
        }
        return adapterPosition - getHeadersCount();
    }

    @Override
    public int getItemCount() {
        return getHeadersCount() + super.getItemCount() + getFootersCount();
    }





    private boolean isHeaderViewPos(int position) {
        return position < getHeadersCount();
    }

    public void addHeaderView(View view) {
        mHeaderViews.put(BASE_ITEM_TYPE_HEADER + mHeaderViews.size(), view);
    }

    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    private boolean isFooterViewPos(int position) {
        return position >= getHeadersCount() + super.getItemCount() &&
                position < getItemCount() - 1;
    }

    public void addFooterView(View view) {
        mFooterViews.put(BASE_ITEM_TYPE_FOOTER + mFooterViews.size(), view);
    }

    public int getFootersCount() {
        return mFooterViews.size();
    }

    public class HeaderAndFooterViewHolder extends RecyclerView.ViewHolder {

        public HeaderAndFooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
