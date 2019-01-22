package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseListAdapter;
import org.wzeiri.zr.zrtaxiplatform.widget.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author k-lm on 2017/11/29.
 */

public class CommentAdapter extends BaseListAdapter<Object> {
    public CommentAdapter(List<Object> list) {
        super(list);
    }

    @Override
    public void getItemView(int position, View convertView, ViewGroup parent, int viewType) {
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolder(convertView);
        }

        viewHolder.mTextStoreName.setText("管理员");
        viewHolder.mTextDate.setText("2017-11-29");
        viewHolder.mTextContent.setText("随便发一个内容看看");
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_driver_discount_comment;
    }

    static class ViewHolder {
        @BindView(R.id.item_driver_discount_comment_image)
        CircleImageView mImage;
        @BindView(R.id.item_driver_discount_comment_text_store_name)
        TextView mTextStoreName;
        @BindView(R.id.item_driver_discount_comment_text_date)
        TextView mTextDate;
        @BindView(R.id.item_driver_discount_comment_text_content)
        TextView mTextContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }
    }
}
