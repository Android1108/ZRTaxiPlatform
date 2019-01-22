package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseListAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.AdverPostApplyBean;
import org.wzeiri.zr.zrtaxiplatform.util.GlideUtil;
import org.wzeiri.zr.zrtaxiplatform.util.TimeUtil;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我发布的广告张贴
 *
 * @author k-lm on 2017/12/13.
 */

public class MyPostingAdvertisementAdapter extends BaseListAdapter<AdverPostApplyBean> {
    public MyPostingAdvertisementAdapter(List<AdverPostApplyBean> list) {
        super(list);
    }

    @Override
    public void getItemView(int position, View convertView, ViewGroup parent, int viewType) {
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolder(convertView);
        }

        AdverPostApplyBean bean = getData(position);

        GlideUtil.loadPath(getContext(), viewHolder.mImage, bean.getAdverPicture());
        String date = "张贴时间: " + TimeUtil.getServerDate(bean.getPostTime());
        viewHolder.mTextDate.setText(date);
        viewHolder.mTextTitle.setText(bean.getDescribe());
        viewHolder.mVtvTypeDate.setText(TimeUtil.getServerDate(bean.getPostTime()));
        viewHolder.mVtvTypeDate.setTextRight(getState(bean.getStatus()));

    }

    /**
     * 返回状态
     *
     * @param state
     * @return
     */
    private String getState(int state) {
        String stateStr = "";

        switch (state) {
            case 1:
                stateStr = "待审核";
                break;
            case 2:
                stateStr = "已审核";
                break;
        }

        return stateStr;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_my_posting_advertisement;
    }

    static class ViewHolder {
        @BindView(R.id.item_my_posting_advertisement_image)
        ImageView mImage;
        @BindView(R.id.item_my_posting_advertisement_text_title)
        TextView mTextTitle;
        @BindView(R.id.item_my_posting_advertisement_text_date)
        TextView mTextDate;
        @BindView(R.id.item_my_posting_advertisement_vtv_type_date)
        ValueTextView mVtvTypeDate;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }
    }
}
