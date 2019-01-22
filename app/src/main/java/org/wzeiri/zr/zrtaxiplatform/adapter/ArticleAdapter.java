package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseListAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.CivilizationArticleBean;
import org.wzeiri.zr.zrtaxiplatform.util.GlideUtil;
import org.wzeiri.zr.zrtaxiplatform.util.TimeUtil;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 工作动态
 *
 * @author k-lm on 2017/11/21.
 */

public class ArticleAdapter extends BaseListAdapter<CivilizationArticleBean> {
    public ArticleAdapter(List<CivilizationArticleBean> list) {
        super(list);
    }

    @Override
    public void getItemView(int position, View convertView, ViewGroup parent, int type) {
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolder(convertView);
        }
        CivilizationArticleBean bean = getData(position);
        viewHolder.mTextTitle.setText(bean.getTitle());
        viewHolder.mVtvDateSource.setText("来源：" + bean.getSource());
        viewHolder.mVtvDateSource.setTextLeft(TimeUtil.getServerDate(bean.getCreationTime()));

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
        return R.layout.item_notice_announcement;
    }

    static class ViewHolder {
        @BindView(R.id.item_notice_announcement_text_title)
        TextView mTextTitle;
        @BindView(R.id.item_notice_announcement_vtv_date_source)
        ValueTextView mVtvDateSource;
        @BindView(R.id.item_notice_announcement_image)
        ImageView mImage;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }
    }
}
