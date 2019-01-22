package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.IconMarginSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseListAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.DriverInteractionPostBean;
import org.wzeiri.zr.zrtaxiplatform.common.Config;
import org.wzeiri.zr.zrtaxiplatform.util.DensityUtil;
import org.wzeiri.zr.zrtaxiplatform.util.DriverInteractionUtil;
import org.wzeiri.zr.zrtaxiplatform.util.GlideUtil;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author k-lm on 2017/11/29.
 */

public class DriverInteractionAdapter extends BaseListAdapter<DriverInteractionPostBean> {
    public DriverInteractionAdapter(List<DriverInteractionPostBean> list) {
        super(list);
    }

    @Override
    public void getItemView(int position, View convertView, ViewGroup parent, int viewType) {

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        if (viewHolder == null) {
            viewHolder = new ViewHolder(convertView);
        }

        DriverInteractionPostBean bean = getData(position);


        viewHolder.mTextName.setText(bean.getCreatorUserName());

        viewHolder.mTextUpdateTime.setText(DriverInteractionUtil.getUpdateString(bean.getLastModificationTime()));

        viewHolder.mTextContent.setText(bean.getContent());

        viewHolder.mCommentNumber.setText(bean.getCommentNumber() + "");
        viewHolder.mTextLikeNumber.setText(bean.getGreateNumber() + "");

        CharSequence title = bean.getTitle();

        // 判断是否有头像，没有头像隐藏
        if (TextUtils.isEmpty(bean.getPicture())) {
            viewHolder.mImageCover.setVisibility(View.GONE);
        } else {
            viewHolder.mImageCover.setVisibility(View.VISIBLE);
            GlideUtil.loadPath(getContext(), viewHolder.mImageCover, bean.getPicture());
        }

        // 是否置顶
        if (bean.isIsTop()) {
            title = DriverInteractionUtil.getTopTitle(getContext(), title);
        }
        // 是否热门
        if (bean.isIsHot()) {
            DriverInteractionUtil.setHostTitle(getContext(), viewHolder.mTextTitle, title);
        } else {
            viewHolder.mTextTitle.setText(title);
        }

        String userAvatar = bean.getCreatorUserProfile();
        if (TextUtils.isEmpty(userAvatar) || Config.NO_IMAGE_URL.equals(userAvatar)) {
            viewHolder.mImageReleaseAvatar.setImageResource(R.drawable.ic_default_avatar);
        } else {
            GlideUtil.loadPath(getContext(), viewHolder.mImageReleaseAvatar, userAvatar);
        }

    }


    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_driver_interaction;
    }

    static class ViewHolder {
        @BindView(R.id.item_driver_interaction_image_release_avatar)
        ImageView mImageReleaseAvatar;
        @BindView(R.id.item_driver_interaction_text_title)
        TextView mTextTitle;
        @BindView(R.id.item_driver_interaction_text_content)
        TextView mTextContent;
        @BindView(R.id.item_driver_interaction_image_cover)
        ImageView mImageCover;
        @BindView(R.id.item_driver_interaction_text_like_number)
        TextView mTextLikeNumber;
        @BindView(R.id.item_driver_interaction_text_comment_number)
        TextView mCommentNumber;
        @BindView(R.id.item_driver_discount_comment_text_name)
        TextView mTextName;
        @BindView(R.id.item_driver_interaction_text_update)
        TextView mTextUpdateTime;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }
    }
}
