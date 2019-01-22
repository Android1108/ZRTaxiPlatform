package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseListAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.TrainingBean;
import org.wzeiri.zr.zrtaxiplatform.bean.TrainingBean;
import org.wzeiri.zr.zrtaxiplatform.util.GlideUtil;
import org.wzeiri.zr.zrtaxiplatform.util.TimeUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author k-lm on 2017/11/21.
 */

public class TrainNoticeAdapter extends BaseListAdapter<TrainingBean> {
    public TrainNoticeAdapter(List<TrainingBean> list) {
        super(list);
    }

    @Override
    public void getItemView(int position, View convertView, ViewGroup parent, int type) {
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolder(convertView);
        }

        TrainingBean bean = getData(position);

        viewHolder.mTextTitle.setText(bean.getTitle());
        viewHolder.mTextDate.setText(TimeUtil.getServerDateAndTime(bean.getTrainingTime()));
        viewHolder.mTextPlace.setText(bean.getTrainingAddress());
        viewHolder.mTextPublisher.setText("温州管理局");

        String url = bean.getCoverPicture();
        if (TextUtils.isEmpty(url)) {
            viewHolder.mImage.setImageResource(0);
            viewHolder.mImage.setVisibility(View.GONE);
        } else {
            viewHolder.mImage.setVisibility(View.VISIBLE);
            GlideUtil.loadPath(getContext(), viewHolder.mImage, url);
        }
    }

    @Override
    public int getItemLayoutId(int type) {
        return R.layout.item_train_notice;
    }

    static class ViewHolder {
        @BindView(R.id.item_train_notice_text_title)
        TextView mTextTitle;
        @BindView(R.id.item_train_notice_image)
        ImageView mImage;
        @BindView(R.id.item_train_notice_text_date)
        TextView mTextDate;
        @BindView(R.id.item_train_notice_text_place)
        TextView mTextPlace;
        @BindView(R.id.item_train_notice_text_publisher)
        TextView mTextPublisher;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }
    }
}
