package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseListAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.GetRecordsBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zz on 2017-12-19.
 */

public class IntegralRecordsAdapter extends BaseListAdapter<GetRecordsBean>{
    public IntegralRecordsAdapter(List<GetRecordsBean> list) {
        super(list);
    }

    @Override
    public void getItemView(int position, View convertView, ViewGroup parent, int viewType) {
        IntegralRecordsAdapter.ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new IntegralRecordsAdapter.ViewHolder(convertView);
        }
        GetRecordsBean bean = getData(position);

//        viewHolder.mIntegralTextDate.setText("日期" + (position + 1));
//        viewHolder.mIntegralTextOperation.setText("操作" + (position + 1));
//        viewHolder.mIntegralTextDetail.setText("细节" + (position + 1));

        /*viewHolder.mIntegralTextDate.setText(bean.get);
        viewHolder.mIntegralTextOperation.setText(bean.getItems());
        viewHolder.mIntegralTextDetail.setText("细节" + (position + 1));*/
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
            view.setTag(this);
        }
    }
}
