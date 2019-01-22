package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.recycler.BaseAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.NotificationBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;

import java.util.List;

/**
 * @author k-lm on 2018/1/2.
 */

public class NotificationAdapter extends BaseAdapter<NotificationBean.ItemsBean> {
    public NotificationAdapter(Context context, List<NotificationBean.ItemsBean> list) {
        super(context, list);
        initItem();
    }

    @Override
    public int getItemViewType(NotificationBean.ItemsBean data, int position) {
        return data.getReadStatus() < 0 ? 1 : 2;
    }


    private void initItem() {
        RecyclerItem<NotificationBean.ItemsBean, DayViewHolder> dayItem = new RecyclerItem<NotificationBean.ItemsBean, DayViewHolder>() {
            @Override
            public int getLayoutId() {
                return R.layout.item_notification_date;
            }

            @Override
            public DayViewHolder getViewHolder(View view) {
                return new DayViewHolder(view);
            }

            @Override
            public void onBindViewHolder(Context context, View itemView, DayViewHolder viewHolder, int viewType, NotificationBean.ItemsBean data, int adapterPosition, int readPosition) {
                viewHolder.mDate.setText(data.getCreationTimeStr());
            }
        };


        RecyclerItem<NotificationBean.ItemsBean, DataViewHolder> notificationItem = new RecyclerItem<NotificationBean.ItemsBean, DataViewHolder>() {
            @Override
            public int getLayoutId() {
                return R.layout.item_notification_data;
            }

            @Override
            public DataViewHolder getViewHolder(View view) {
                return new DataViewHolder(view);
            }

            @Override
            public void onBindViewHolder(Context context, View itemView, DataViewHolder viewHolder, int viewType, NotificationBean.ItemsBean data, int adapterPosition, int readPosition) {
                viewHolder.mTitle.setText(data.getTitle());
                viewHolder.mContent.setText(data.getMessage());
            }
        };


        putItemType(1, dayItem);
        putItemType(2, notificationItem);
    }


    private class DayViewHolder extends RecyclerView.ViewHolder {
        private TextView mDate;

        public DayViewHolder(View itemView) {
            super(itemView);
            mDate = itemView.findViewById(R.id.item_notification_date_text_day);
        }
    }


    private class DataViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private TextView mContent;

        public DataViewHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.item_notification_data_text_title);
            mContent = itemView.findViewById(R.id.item_notification_data_text_content);
        }
    }

}
