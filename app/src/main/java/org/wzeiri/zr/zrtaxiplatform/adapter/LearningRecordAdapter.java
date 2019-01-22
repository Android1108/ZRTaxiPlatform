package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.graphics.Bitmap;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseListAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.LearnRecordBean;
import org.wzeiri.zr.zrtaxiplatform.bean.LearnRecordBean;
import org.wzeiri.zr.zrtaxiplatform.util.GlideUtil;
import org.wzeiri.zr.zrtaxiplatform.util.TimeUtil;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author k-lm on 2017/11/27.
 */

public class LearningRecordAdapter extends BaseListAdapter<LearnRecordBean> {
    public LearningRecordAdapter(List<LearnRecordBean> list) {
        super(list);
    }

    @Override
    public void getItemView(int position, View convertView, ViewGroup parent, int type) {
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        if (viewHolder == null) {
            viewHolder = new ViewHolder(convertView);
        }

        LearnRecordBean bean = getData(position);


        GlideUtil.loadPath(getContext(), viewHolder.itemSecurityStudyImage, bean.getCoverPicture());
        viewHolder.itemSecurityStudyTextTitle.setText(bean.getTitle());
        viewHolder.itemSecurityStudyVtvStudyState.setText(TimeUtil.getServerDateAndTime(bean.getCreationTime()));

    }


    @Override
    public int getItemLayoutId(int type) {
        return R.layout.item_security_study;
    }

    static class ViewHolder {
        @BindView(R.id.item_security_study_image)
        ImageView itemSecurityStudyImage;
        @BindView(R.id.item_security_study_text_title)
        TextView itemSecurityStudyTextTitle;
        @BindView(R.id.item_security_study_vtv_study_state)
        ValueTextView itemSecurityStudyVtvStudyState;
        @BindView(R.id.item_security_study_text_study)
        TextView itemSecurityStudyTextStudy;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }
    }
}
