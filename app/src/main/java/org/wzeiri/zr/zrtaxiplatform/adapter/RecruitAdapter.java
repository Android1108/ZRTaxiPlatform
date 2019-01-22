package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseListAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.JobRecruitmentBean;
import org.wzeiri.zr.zrtaxiplatform.util.GlideUtil;
import org.wzeiri.zr.zrtaxiplatform.util.TimeUtil;
import org.wzeiri.zr.zrtaxiplatform.widget.CircleImageView;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 招聘
 *
 * @author k-lm on 2017/11/21.
 */

public class RecruitAdapter extends BaseListAdapter<JobRecruitmentBean> {
    public RecruitAdapter(List<JobRecruitmentBean> list) {
        super(list);
    }

    @Override
    public void getItemView(int position, View convertView, ViewGroup parent, int type) {
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolder(convertView);
        }
        JobRecruitmentBean bean = getData(position);

       /* viewHolder.mTextTitle.setText("招聘出租车" + (position + 1));
        viewHolder.mTextWages.setText("100-200/月");
        viewHolder.mTextRequirement.setText("无休");
        viewHolder.mTvtPublisherInfo.setText("管理员");
        viewHolder.mTvtPublisherInfo.setTextRight("2017-11-21");*/

        viewHolder.mTextTitle.setText(bean.getTitle());
        String address = bean.getCityName() + " " + bean.getAreaName();
        viewHolder.mTextWages.setText(address);
        viewHolder.mTvtPublisherInfo.setText(bean.getContactPerson());
        viewHolder.mTvtPublisherInfo.setTextRight(TimeUtil.getServerDate(bean.getCreationTime()));
        GlideUtil.loadPath(getContext(), viewHolder.mAvatar, bean.getCoverPicture());

        viewHolder.mTextRequirement.setText(Html.fromHtml(bean.getContent()));

    }

    @Override
    public int getItemLayoutId(int type) {
        return R.layout.item_recruit_info;
    }

    static class ViewHolder {
        @BindView(R.id.item_recruit_info_text_title)
        TextView mTextTitle;
        @BindView(R.id.item_recruit_info_text_wages)
        TextView mTextWages;
        @BindView(R.id.item_recruit_info_text_requirement)
        TextView mTextRequirement;
        @BindView(R.id.item_recruit_info_tvt_publisher_info)
        ValueTextView mTvtPublisherInfo;
        @BindView(R.id.item_recruit_info_civ_publisher_avatar)
        CircleImageView mAvatar;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }
    }
}
