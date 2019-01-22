package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseListAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.JobRecruitmentBean;
import org.wzeiri.zr.zrtaxiplatform.util.GlideUtil;
import org.wzeiri.zr.zrtaxiplatform.util.TimeUtil;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 求职
 *
 * @author k-lm on 2017/11/21.
 */

public class JobWantedAdapter extends BaseListAdapter<JobRecruitmentBean> {
    public JobWantedAdapter(List<JobRecruitmentBean> list) {
        super(list);
    }

    @Override
    public void getItemView(int position, View convertView, ViewGroup parent, int type) {
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolder(convertView);
        }
        JobRecruitmentBean bean = getData(position);

       /* viewHolder.mTextTitle.setText("标题 " + (position + 1));
        viewHolder.mTextContent.setText("内容 " + (position + 1));
        viewHolder.mVtvReleaseInfo.setText("管理员");
        viewHolder.mVtvReleaseInfo.setTextRight("2017-11-21");*/

        viewHolder.mTextTitle.setText(bean.getTitle());
        viewHolder.mTextContent.setText(bean.getContact());
        viewHolder.mVtvReleaseInfo.setText(bean.getCreatorUserName());
        viewHolder.mVtvReleaseInfo.setTextRight(TimeUtil.getServerDate(bean.getCreationTime()));
        GlideUtil.loadPath(getContext(), viewHolder.mImage, bean.getCoverPicture());


    }

    @Override
    public int getItemLayoutId(int type) {
        return R.layout.item_job_information;
    }

    static class ViewHolder {
        @BindView(R.id.item_job_information_image)
        ImageView mImage;
        @BindView(R.id.item_job_information_text_title)
        TextView mTextTitle;
        @BindView(R.id.item_job_information_text_content)
        TextView mTextContent;
        @BindView(R.id.item_job_information_vtv_release_info)
        ValueTextView mVtvReleaseInfo;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }
    }
}
