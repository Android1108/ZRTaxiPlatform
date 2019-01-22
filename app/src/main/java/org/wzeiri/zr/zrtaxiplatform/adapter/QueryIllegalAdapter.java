package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseListAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.QueryIllegalInfoBean;
import org.wzeiri.zr.zrtaxiplatform.util.TimeUtil;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author k-lm on 2018/3/8.
 */

public class QueryIllegalAdapter extends BaseListAdapter<QueryIllegalInfoBean> {

    public QueryIllegalAdapter(List<QueryIllegalInfoBean> list) {
        super(list);
    }

    @Override
    public void getItemView(int position, View convertView, ViewGroup parent, int viewType) {
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolder(convertView);
        }


        QueryIllegalInfoBean bean = getData(position);

        viewHolder.mCarNumber.setText(bean.getCph());
        viewHolder.mReason.setText(bean.getAy());

        viewHolder.mCompany.setText(bean.getZfjg().toString());
        String time = bean.getJcrq();

        if (!TextUtils.isEmpty(time) && time.length() > 3) {
            viewHolder.mTime.setText(TimeUtil.timet(time.substring(0, time.length() - 3)));
        }


    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_query_illegal;
    }


    static class ViewHolder {
        @BindView(R.id.item_query_illegal_text_car_number)
        TextView mCarNumber;
        @BindView(R.id.item_query_illegal_text_reason)
        TextView mReason;
        @BindView(R.id.item_query_illegal_vtv_time)
        ValueTextView mTime;
        @BindView(R.id.item_query_illegal_text_company)
        TextView mCompany;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }
    }
}
