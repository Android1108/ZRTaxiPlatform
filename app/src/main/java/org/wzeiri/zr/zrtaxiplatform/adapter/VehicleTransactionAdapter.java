package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseListAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.VehicleTransactionBean;
import org.wzeiri.zr.zrtaxiplatform.util.GlideUtil;
import org.wzeiri.zr.zrtaxiplatform.util.TimeUtil;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 车辆交易
 *
 * @author k-lm on 2017/11/21.
 */

public class VehicleTransactionAdapter extends BaseListAdapter<VehicleTransactionBean> {
    public VehicleTransactionAdapter(List<VehicleTransactionBean> list) {
        super(list);
    }

    @Override
    public void getItemView(int position, View convertView, ViewGroup parent, int type) {
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolder(convertView);
        }

        VehicleTransactionBean bean = getData(position);
      /*  viewHolder.mTextTitle.setText("标题" + (position + 1));
        viewHolder.mTextContent.setText("内容" + (position + 1));
        viewHolder.mVtvInfo.setText("管理员");
        viewHolder.mVtvInfo.setTextRight("2017-10-20");*/

        viewHolder.mTextTitle.setText(bean.getTitle());
        viewHolder.mTextContent.setText(bean.getContent());
        viewHolder.mVtvInfo.setText(bean.getPublishUserName());
        viewHolder.mVtvInfo.setTextRight(TimeUtil.getServerDate(bean.getCreationTime()));
        GlideUtil.loadPath(getContext(), viewHolder.mImage, bean.getCoverPicture());

        String avatarUrl = bean.getPublishUserProfile();
        if (TextUtils.isEmpty(avatarUrl)) {
            viewHolder.mImageAvatar.setImageResource(R.drawable.ic_default_avatar);
        } else {
            GlideUtil.loadPath(getContext(), viewHolder.mImageAvatar, avatarUrl);
        }


    }

    @Override
    public int getItemLayoutId(int type) {
        return R.layout.item_notice_vehicle_lost_article;
    }

    static class ViewHolder {
        @BindView(R.id.item_notice_vehicle_lost_article_image)
        ImageView mImage;
        @BindView(R.id.item_notice_vehicle_lost_article_text_title)
        TextView mTextTitle;
        @BindView(R.id.item_notice_vehicle_lost_article_text_content)
        TextView mTextContent;
        @BindView(R.id.item_notice_vehicle_lost_article_vtv_info)
        ValueTextView mVtvInfo;

        @BindView(R.id.item_notice_vehicle_lost_article_image_avatar)
        ImageView mImageAvatar;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }
    }
}
