package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseListAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.LearnVideoBean;
import org.wzeiri.zr.zrtaxiplatform.common.Config;
import org.wzeiri.zr.zrtaxiplatform.util.GlideUtil;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author k-lm on 2017/11/21.
 */

public class SafetyLearnAdapter extends BaseListAdapter<LearnVideoBean> {
    public SafetyLearnAdapter(List<LearnVideoBean> list) {
        super(list);
    }

    @Override
    public void getItemView(int position, View convertView, ViewGroup parent, int type) {
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        if (viewHolder == null) {
            viewHolder = new ViewHolder(convertView);
        }


        LearnVideoBean bean = getData(position);

        viewHolder.mStudyTextTitle.setText(bean.getTitle());
        GlideUtil.loadPath(getContext(),
                viewHolder.mStudyImage,
                bean.getCoverPicture());


        if (bean.isIsLearned()) {
            viewHolder.mStudyTextStudy.setVisibility(View.GONE);
            viewHolder.mStudyVtvStudyState.setTextLeft("已学习");
            viewHolder.mStudyVtvStudyState.setText("2017-11-21 10:10:10");

        } else {
            viewHolder.mStudyTextStudy.setVisibility(View.VISIBLE);
            viewHolder.mStudyVtvStudyState.setTextLeft("");
            viewHolder.mStudyVtvStudyState.setText("未学习");
        }


    }

    @Override
    public int getItemLayoutId(int type) {
        return R.layout.item_security_study;
    }

    static class ViewHolder {
        @BindView(R.id.item_security_study_image)
        ImageView mStudyImage;
        @BindView(R.id.item_security_study_text_title)
        TextView mStudyTextTitle;
        @BindView(R.id.item_security_study_vtv_study_state)
        ValueTextView mStudyVtvStudyState;
        @BindView(R.id.item_security_study_text_study)
        TextView mStudyTextStudy;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }
    }
}
