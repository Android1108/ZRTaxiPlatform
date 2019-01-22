package org.wzeiri.zr.zrtaxiplatform.adapter.base;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author k-lm on 2017/11/14.
 */

public abstract class BaseRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private List<T> mList;

    private OnItemClickListener mItemClickListener;


    public BaseRecyclerAdapter(List<T> list) {
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

    public void addData(T data) {
        mList.add(data);
    }

    public void addList(List<T> list) {
        if (list == null) {
            return;
        }
        mList.addAll(list);
    }

    public void removeData(T data) {
        mList.remove(data);
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
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(getItemLayoutId(viewType), parent, false);
        return getViewHolder(view, viewType);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onBindViewHolder(VH holder, @SuppressLint("RecyclerView") final int position) {
        T data = mList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(v, position);
                }
            }
        });

        onBindViewHolder(holder, data, position);
    }

    public abstract void onBindViewHolder(VH holder, T data, int position);

    public abstract int getItemLayoutId(int viewType);

    public abstract VH getViewHolder(View view, int viewType);


    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

}
