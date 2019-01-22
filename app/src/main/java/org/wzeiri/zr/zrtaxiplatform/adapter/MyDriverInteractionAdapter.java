package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseListAdapter;
import org.wzeiri.zr.zrtaxiplatform.util.DriverInteractionUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author k-lm on 2017/11/29.
 */

public class MyDriverInteractionAdapter extends BaseListAdapter<Object> {
    public MyDriverInteractionAdapter(List<Object> list) {
        super(list);
    }

    @Override
    public void getItemView(int position, View convertView, ViewGroup parent, int viewType) {

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        if (viewHolder == null) {
            viewHolder = new ViewHolder(convertView);
        }

        viewHolder.mTextUpdateTime.setText("1小时前更新");

        viewHolder.mTextContent.setText("这是一个男默女泪的内容");

        viewHolder.mTextCommentNumber.setText(position + "");
        viewHolder.mTextLikeNumber.setText(position + "");


       /* if (position == 0) {
            viewHolder.mTextTitle.setText(DriverInteractionUtil.getTopTitle(getContext(), "标题 " + position));
        } else if (position == 1) {
            viewHolder.mTextTitle.setText(DriverInteractionUtil.getHostTitle(getContext(), "标题 " + position));
        } else {
            viewHolder.mTextTitle.setText("标题 " + position);
        }*/


    }


    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_my_driver_interaction;
    }


    static class ViewHolder {
        @BindView(R.id.item_my_driver_interaction_text_title)
        TextView mTextTitle;
        @BindView(R.id.item_my_driver_interaction_text_content)
        TextView mTextContent;
        @BindView(R.id.item_my_driver_interaction_image_cover)
        ImageView mImageCover;
        @BindView(R.id.item_my_driver_interaction_text_like_number)
        TextView mTextLikeNumber;
        @BindView(R.id.item_my_driver_interaction_text_comment_number)
        TextView mTextCommentNumber;
        @BindView(R.id.item_my_driver_interaction_text_update)
        TextView mTextUpdateTime;
        @BindView(R.id.item_my_driver_interaction_text_edit)
        TextView mTextEdit;
        @BindView(R.id.item_my_driver_interaction_text_delete)
        TextView mTextDelete;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
