package org.wzeiri.zr.zrtaxiplatform.adapter.base.recycler;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 分页加载使用
 * Created by Lcsunm on 2017/5/23.
 */

public abstract class LoadMoreAdapter<T> extends HeaderAndFooterAdapter<T> {

    public static final int BASE_ITEM_TYPE_LOAD_MORE = 71100;


    public static final int FOOT_TYPE_DEFAULT = 1700;
    public static final int FOOT_TYPE_LOADING = 1701;
    public static final int FOOT_TYPE_LOADED = 1702;
    public static final int FOOT_TYPE_NO_MORE = 1703;
    public static final int FOOT_TYPE_FAILED = 1704;

    public int mFootType;

    private String mLoadingText = "加载中…";
    private String mLoadedCompleteText = "加载完成";
    private String mNoMoreText = "没有更多了";
    private String mLoadFailedText = "加载失败";


    public LoadMoreAdapter(Context context, List<T> list) {
        super(context, list);
        putItemType(BASE_ITEM_TYPE_LOAD_MORE, new RecyclerItem<T, LoadMoreViewHolder>() {
            @Override
            public int getLayoutId() {
                return R.layout.view_listview_footer;
            }

            @Override
            public LoadMoreViewHolder getViewHolder(View view) {
                return new LoadMoreViewHolder(view);
            }

            @Override
            public void onBindViewHolder(Context context, View itemView, LoadMoreViewHolder viewHolder, int viewType, T data, int adapterPosition, int readPosition) {
                String text = "";
                switch (mFootType) {
                    case FOOT_TYPE_LOADING:
                        text = mLoadingText;
                        break;
                    case FOOT_TYPE_LOADED:
                        text = mLoadedCompleteText;
                        break;
                    case FOOT_TYPE_NO_MORE:
                        text = mNoMoreText;
                        break;
                    case FOOT_TYPE_FAILED:
                        text = mLoadFailedText;
                        break;
                    default:
                        text = "";
                        break;
                }
                viewHolder.text.setText(text);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoadMoreViewPos(position)) {
            return BASE_ITEM_TYPE_LOAD_MORE;
        } else {
            return super.getItemViewType(position);
        }
    }

    public void setFooterState(int state) {
        setFooterState(state, true);
    }

    public void setFooterState(int state, boolean notify) {
        mFootType = state;
        if (notify) {
            notifyItemRangeChanged(getItemCount() - 1, 1);
        }
    }


    public void setLoadingText(String loadingText) {
        this.mLoadingText = loadingText;
    }

    public void setLoadedCompleteText(String loadedCompleteText) {
        this.mLoadedCompleteText = loadedCompleteText;
    }

    public void setNoMoreText(String noMoreText) {
        this.mNoMoreText = noMoreText;
    }

    public void setLoadFailedText(String loadFailedText) {
        this.mLoadFailedText = loadFailedText;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        WrapperUtils.onAttachedToRecyclerView(recyclerView, new WrapperUtils.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                int viewType = getItemViewType(position);
                if (viewType == BASE_ITEM_TYPE_LOAD_MORE) {
                    return layoutManager.getSpanCount();
                }
                if (oldLookup != null)
                    return oldLookup.getSpanSize(position);
                return 1;
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isLoadMoreViewPos(position)) {
            WrapperUtils.setFullSpan(holder);
        }
    }


    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    private boolean isLoadMoreViewPos(int position) {
        return position >= getItemCount() - 1;
    }

    public class LoadMoreViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.view_listview_footer_TextView)
        TextView text;

        public LoadMoreViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
