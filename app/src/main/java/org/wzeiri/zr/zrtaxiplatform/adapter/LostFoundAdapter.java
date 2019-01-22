package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseListAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.LostFoundBean;
import org.wzeiri.zr.zrtaxiplatform.util.GlideUtil;
import org.wzeiri.zr.zrtaxiplatform.util.TimeUtil;
import org.wzeiri.zr.zrtaxiplatform.widget.CircleImageView;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 失物招领
 *
 * @author k-lm on 2017/11/20.
 */

public class LostFoundAdapter extends BaseListAdapter<LostFoundBean> {

    private SimpleDateFormat mFormatTime = new SimpleDateFormat("HH:mm", Locale.US);

    public LostFoundAdapter(List<LostFoundBean> list) {
        super(list);
    }

    @Override
    public void getItemView(int position, View convertView, ViewGroup parent, int type) {
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolder(convertView);
        }
        LostFoundBean bean = getData(position);


        /*viewHolder.mTextTitle.setText("标题" + (position + 1));
        viewHolder.mTextContent.setText("内容" + (position + 1));
        viewHolder.mVtvInfo.setText("管理员");
        viewHolder.mVtvInfo.setTextRight("挂失中");*/

        viewHolder.mTextTitle.setText(bean.getTitle());

        Date date = null;
        try {
            date = TimeUtil.mFormatServer.parse(bean.getBoardingTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String content = bean.getBoardingPoint() + "(" + mFormatTime.format(date) + ")";
        content += "→";
        try {
            date = TimeUtil.mFormatServer.parse(bean.getAlightingTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        content += bean.getAlightingPoint() + "(" + mFormatTime.format(date) + ")";


        viewHolder.mTextContent.setText(content);
        viewHolder.mVtvInfo.setText(bean.getContactPerson());
        viewHolder.mVtvInfo.setTextRight(bean.getLostFoundStatusName());
        GlideUtil.loadPath(getContext(), viewHolder.mImage, bean.getPicture());
        // viewHolder.mVtvInfo.setTextRight(bean.getLostFoundStatus());
        String avatarUrl = bean.getPublishUserProfile();
        if(TextUtils.isEmpty(avatarUrl)){
            viewHolder.mImageAvatar.setImageResource(R.drawable.ic_default_avatar);
        }else {
            GlideUtil.loadPath(getContext(), viewHolder.mImageAvatar, avatarUrl);
        }


    }

    @Override
    public int getItemLayoutId(int type) {
        return R.layout.item_lost_found;
    }

    static class ViewHolder {
        @BindView(R.id.item_lost_found_image)
        ImageView mImage;
        @BindView(R.id.item_lost_found_text_title)
        TextView mTextTitle;
        @BindView(R.id.item_lost_found_text_content)
        TextView mTextContent;
        @BindView(R.id.item_lost_found_vtv_info)
        ValueTextView mVtvInfo;
        @BindView(R.id.item_lost_found_image_avatar)
        CircleImageView mImageAvatar;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }
    }
}
