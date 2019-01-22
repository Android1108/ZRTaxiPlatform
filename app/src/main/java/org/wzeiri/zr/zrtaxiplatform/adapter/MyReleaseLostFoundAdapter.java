package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseListAdapter;
import org.wzeiri.zr.zrtaxiplatform.adapter.inter.OnItemClickDeleteListener;
import org.wzeiri.zr.zrtaxiplatform.adapter.inter.OnItemClickEditListener;
import org.wzeiri.zr.zrtaxiplatform.bean.LostFoundBean;
import org.wzeiri.zr.zrtaxiplatform.util.GlideUtil;
import org.wzeiri.zr.zrtaxiplatform.util.TimeUtil;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我发布的失物招领
 *
 * @author k-lm on 2017/12/13.
 */

public class MyReleaseLostFoundAdapter extends BaseListAdapter<LostFoundBean> {
    private OnItemClickDeleteListener mItemClickDeleteListener;


    public MyReleaseLostFoundAdapter(List<LostFoundBean> list) {
        super(list);
    }

    @Override
    public void getItemView(final int position, View convertView, ViewGroup parent, int viewType) {
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        if (viewHolder == null) {
            viewHolder = new ViewHolder(convertView);
        }

        final LostFoundBean bean = getData(position);
        viewHolder.mTextTitle.setText(bean.getTitle());
        viewHolder.mTextContent.setText(bean.getContent());
        viewHolder.mVtvTypeDate.setTextRight(bean.getLostFoundStatusName());
        viewHolder.mVtvTypeDate.setText(TimeUtil.getServerDate(bean.getCreationTime()));
        viewHolder.mPlateNumber.setText("车牌号"+bean.getPlateNumber());
        GlideUtil.loadPath(getContext(), viewHolder.mImage, bean.getPicture());

        if (bean.getLostFoundStatus() == 0) {
            viewHolder.mLayoutOperation.setVisibility(View.GONE);
            viewHolder.mTextDelete.setOnClickListener(null);
        } else {
            viewHolder.mLayoutOperation.setVisibility(View.VISIBLE);
            viewHolder.mTextDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickDeleteListener != null) {
                        mItemClickDeleteListener.onItemClickDelete(position);
                    }
                }
            });
        }

    }

    public void setOnItemClickDeleteListener(OnItemClickDeleteListener listener) {
        mItemClickDeleteListener = listener;
    }


    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_my_release_lost_found;
    }

    static class ViewHolder {
        @BindView(R.id.item_my_release_lost_found_interaction_image)
        ImageView mImage;
        @BindView(R.id.item_my_release_lost_found_interaction_text_title)
        TextView mTextTitle;
        @BindView(R.id.item_my_release_lost_found_interaction_text_content)
        TextView mTextContent;
        @BindView(R.id.item_my_release_lost_found_interaction_vtv_type_date)
        ValueTextView mVtvTypeDate;
        @BindView(R.id.item_my_release_lost_found_interaction_layout_operation)
        LinearLayout mLayoutOperation;
        @BindView(R.id.item_my_release_lost_found_interaction_text_delete)
        TextView mTextDelete;
        @BindView(R.id.item_my_release_lost_found_interaction_vtv_plateNumber)
        TextView mPlateNumber;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }
    }


}
