package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.recycler.BaseAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author k-lm on 2017/12/25.
 */

public class SelectRecyclerAdapter extends BaseAdapter<String> {
    private int mTextmGravity = -1;

    public SelectRecyclerAdapter(Context context, List<String> list) {
        super(context, list);
        init();
    }

    private int mSelectPosition = -1;

    @Override
    public int getItemViewType(String data, int position) {
        return 0;
    }

    private void init() {
        RecyclerItem<String, ViewHolder> item = new RecyclerItem<String, ViewHolder>() {
            @Override
            public int getLayoutId() {
                return R.layout.item_text_view;
            }

            @Override
            public ViewHolder getViewHolder(View view) {
                return new ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(Context context, View itemView, ViewHolder viewHolder, int viewType, String data, int adapterPosition, int readPosition) {
                viewHolder.mContent.setText(data);
                int gravity = viewHolder.mContent.getGravity();
                if (mTextmGravity < 0) {
                    mTextmGravity = gravity;
                } else if (mTextmGravity != gravity) {
                    viewHolder.mContent.setGravity(mTextmGravity);
                }
                if (mSelectPosition == readPosition) {

                }
            }
        };

        putItemType(0, item);
    }


    /**
     * 设置当前选择的位置
     *
     * @param position
     */
    public void setSelectPosition(int position) {
        mSelectPosition = position;
    }

    public int getSelectPositon() {
        return mSelectPosition;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_text_view_content)
        TextView mContent;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 设置textView的对齐方式
     *
     * @param gravity
     */
    public void setTextmGravity(int gravity) {
        mTextmGravity = gravity;
    }
}
