package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseListAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.GetRecordsBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 积分记录
 * Created by zz on 2017-12-20.
 */

public class MyIntegralRecordsAdapter extends BaseListAdapter<GetRecordsBean> {

    public MyIntegralRecordsAdapter(List<GetRecordsBean> list) {
        super(list);
    }

    @Override
    public void getItemView(int position, View convertView, ViewGroup parent, int viewType) {
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolder(convertView);
        }
        GetRecordsBean bean = getData(position);

        bean.getCreationTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sDate;
        Date date = new Date();
        sDate = sdf.format(date);
        viewHolder.mIntegralTextDate.setText(sDate);
        viewHolder.mIntegralTextDate.setText("2017-12-20");
        viewHolder.mIntegralTextOperation.setText("操作");
        viewHolder.mIntegralTextDetail.setText("明细");
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_my_integral;
    }

    static class ViewHolder {
        @BindView(R.id.item_my_integral_text_date)
        TextView mIntegralTextDate;
        @BindView(R.id.item_my_integral_text_operation)
        TextView mIntegralTextOperation;
        @BindView(R.id.item_my_integral_text_detail)
        TextView mIntegralTextDetail;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(view);
        }
    }
}
