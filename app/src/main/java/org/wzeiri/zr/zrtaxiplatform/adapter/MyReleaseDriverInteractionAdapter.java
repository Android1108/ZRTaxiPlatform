package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseListAdapter;
import org.wzeiri.zr.zrtaxiplatform.adapter.inter.OnItemClickDeleteListener;
import org.wzeiri.zr.zrtaxiplatform.adapter.inter.OnItemClickEditListener;
import org.wzeiri.zr.zrtaxiplatform.bean.DriverInteractionPostBean;
import org.wzeiri.zr.zrtaxiplatform.util.DriverInteractionUtil;
import org.wzeiri.zr.zrtaxiplatform.util.GlideUtil;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的司机互动
 *
 * @author k-lm on 2017/12/13.
 */

public class MyReleaseDriverInteractionAdapter extends BaseListAdapter<DriverInteractionPostBean> {
    private OnItemClickDeleteListener mItemClickDeleteListener;
    private OnItemClickEditListener mItemClickEditListener;


    public MyReleaseDriverInteractionAdapter(List<DriverInteractionPostBean> list) {
        super(list);
    }

    @Override
    public void getItemView(final int position, View convertView, ViewGroup parent, int viewType) {
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        if (viewHolder == null) {
            viewHolder = new ViewHolder(convertView);
        }

        DriverInteractionPostBean bean = getData(position);
        viewHolder.mTextDate.setText(DriverInteractionUtil.getUpdateString(bean.getLastModificationTime()));

        viewHolder.mTextContent.setText(bean.getContent());

        viewHolder.mCommentNumber.setText(bean.getCommentNumber() + "");
        viewHolder.mTextLikeNumber.setText(bean.getGreateNumber() + "");


        CharSequence title = bean.getTitle();

        if (bean.isIsTop()) {
            title = DriverInteractionUtil.getTopTitle(getContext(), title);
        }

        if (bean.isIsHot()) {
            DriverInteractionUtil.setHostTitle(getContext(), viewHolder.mTextTitle, title);
        } else {
            viewHolder.mTextTitle.setText(title);
        }


        viewHolder.mTextDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickDeleteListener != null) {
                    mItemClickDeleteListener.onItemClickDelete(position);
                }
            }
        });

        viewHolder.mTextEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickEditListener != null) {
                    mItemClickEditListener.onItemClickEdit(position);
                }
            }
        });

        GlideUtil.loadPath(getContext(), viewHolder.mImage, bean.getPicture());

    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_my_release_driver_interaction;
    }

    public void setOnItemClickDeleteListener(OnItemClickDeleteListener listener) {
        mItemClickDeleteListener = listener;
    }

    public void setOnItemClickEditListener(OnItemClickEditListener listener) {
        mItemClickEditListener = listener;
    }


    static class ViewHolder {
        @BindView(R.id.item_my_release_driver_interaction_image)
        ImageView mImage;
        @BindView(R.id.item_my_release_driver_interaction_text_title)
        TextView mTextTitle;
        @BindView(R.id.item_my_release_driver_interaction_text_content)
        TextView mTextContent;
        @BindView(R.id.item_my_release_driver_interaction_text_like_number)
        TextView mTextLikeNumber;
        @BindView(R.id.item_my_release_driver_interaction_text_comment_number)
        TextView mCommentNumber;
        @BindView(R.id.item_my_release_driver_interaction_text_date)
        TextView mTextDate;
        @BindView(R.id.item_my_release_driver_interaction_text_edit)
        TextView mTextEdit;
        @BindView(R.id.item_my_release_driver_interaction_text_delete)
        TextView mTextDelete;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }
    }
}
