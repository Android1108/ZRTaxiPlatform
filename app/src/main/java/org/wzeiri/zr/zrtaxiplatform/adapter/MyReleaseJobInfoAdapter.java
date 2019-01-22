package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseListAdapter;
import org.wzeiri.zr.zrtaxiplatform.adapter.inter.OnItemClickDeleteListener;
import org.wzeiri.zr.zrtaxiplatform.bean.JobRecruitmentBean;
import org.wzeiri.zr.zrtaxiplatform.util.TimeUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的求职信息
 *
 * @author k-lm on 2017/12/13.
 */

public class MyReleaseJobInfoAdapter extends BaseListAdapter<JobRecruitmentBean> {
    public MyReleaseJobInfoAdapter(List<JobRecruitmentBean> list) {
        super(list);
    }

    private OnItemClickDeleteListener mItemClickDeliteListener;

    @Override
    public void getItemView(final int position, View convertView, ViewGroup parent, int viewType) {
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        if (viewHolder == null) {
            viewHolder = new ViewHolder(convertView);
        }
        JobRecruitmentBean bean = getData(position);

        viewHolder.mTextTitle.setText(bean.getTitle());
        String address = bean.getCityName() + " " + bean.getAreaName();
        viewHolder.mTextSalary.setText(address);
        viewHolder.mTextContent.setText(bean.getContent());
        viewHolder.mTextDate.setText(TimeUtil
                .getServerDate(bean.getCreationTime()));

        viewHolder.mTextDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickDeliteListener != null) {
                    mItemClickDeliteListener.onItemClickDelete(position);
                }
            }
        });

    }


    public void setOnItemClickDeleteListener(OnItemClickDeleteListener listener) {
        mItemClickDeliteListener = listener;
    }


    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_my_release_job_wanted;
    }

    static class ViewHolder {
        @BindView(R.id.item_my_release_job_wanted_text_title)
        TextView mTextTitle;
        @BindView(R.id.item_my_release_job_wanted_text_salary)
        TextView mTextSalary;
        @BindView(R.id.item_my_release_job_wanted_text_content)
        TextView mTextContent;
        @BindView(R.id.item_my_release_job_wanted_text_date)
        TextView mTextDate;
        @BindView(R.id.item_my_release_job_wanted_text_close)
        TextView mTextclose;
        @BindView(R.id.item_my_release_job_wanted_text_delete)
        TextView mTextDelete;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }
    }


}
