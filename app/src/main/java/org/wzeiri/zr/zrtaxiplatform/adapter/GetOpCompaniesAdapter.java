package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseListAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.GetOnCompaniesBean;
import org.wzeiri.zr.zrtaxiplatform.bean.JobRecruitmentBean;
import org.wzeiri.zr.zrtaxiplatform.util.GlideUtil;
import org.wzeiri.zr.zrtaxiplatform.util.TimeUtil;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 获取出租车公司
 *
 * Created by zz on 2017-12-13.
 */

public class GetOpCompaniesAdapter extends BaseListAdapter<Object>{
    public GetOpCompaniesAdapter(List<Object> list) {
        super(list);
    }
    @Override
    public void getItemView(int position, View convertView, ViewGroup parent, int viewType) {
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolder(convertView);
        }
//        GetOnCompaniesBean bean = getData(position);
        viewHolder.mTextView.setText("城市名称 " + (position + 1));
//        viewHolder.mTextView.setText(bean.getId());

    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_taxi_companies;
    }

    static class ViewHolder {
        @BindView(R.id.item_tv_taxi_companies)
        TextView mTextView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }
    }
}

