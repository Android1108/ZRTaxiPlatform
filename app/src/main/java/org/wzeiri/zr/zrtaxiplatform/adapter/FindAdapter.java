package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseListAdapter;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author
 * 发现k-lm on 2017/11/17.
 */

public class FindAdapter extends BaseListAdapter<Object> {
    public FindAdapter(List<Object> list) {
        super(list);
    }

    @Override
    public void getItemView(int position, View convertView, ViewGroup parent, int type) {
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        if (viewHolder == null) {
            viewHolder = new ViewHolder(convertView);
        }

        viewHolder.mTextTitle.setText("标题" + (position + 1));
        viewHolder.mDateSource.setText("来源：管理员");
        viewHolder.mDateSource.setTextLeft("2017-11-17");
    }

    @Override
    public int getItemLayoutId( int type) {
        return R.layout.item_notice_announcement;
    }


    static class ViewHolder {
        @BindView(R.id.item_notice_announcement_text_title)
        TextView mTextTitle;
        @BindView(R.id.item_notice_announcement_vtv_date_source)
        ValueTextView mDateSource;
        @BindView(R.id.item_notice_announcement_image)
        ImageView mImage;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }
    }
}
