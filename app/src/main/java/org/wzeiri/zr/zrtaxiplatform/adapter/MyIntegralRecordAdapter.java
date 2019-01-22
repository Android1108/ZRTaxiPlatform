package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseListAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.GetRecordsBean;
import org.wzeiri.zr.zrtaxiplatform.util.TimeUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 积分记录
 *
 * @author k-lm on 2017/11/27.
 */

public class MyIntegralRecordAdapter extends BaseListAdapter<GetRecordsBean> {
    public MyIntegralRecordAdapter(List<GetRecordsBean> list) {
        super(list);
    }

    @Override
    public void getItemView(int position, View convertView, ViewGroup parent, int type) {
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolder(convertView);
        }
        GetRecordsBean bean = getData(position);

//        viewHolder.mTextDate.setText("2017-11-27");
//        viewHolder.mTextOperation.setText("推广");
//        viewHolder.mTextDetail.setText("+5分");

        viewHolder.mTextDate.setText(TimeUtil.getServerDateAndTime(bean.getCreationTime()));
        viewHolder.mTextOperation.setText(bean.getIntegralChangeTypeName());

        String quantity = bean.getQuantity() + "分";
        if (bean.getIntegralCreaseType() == 1) {
            quantity = "+" + quantity;
        } else {
            quantity = "-" + quantity;
        }
        viewHolder.mTextDetail.setText(quantity);

    }

    @Override
    public int getItemLayoutId(int type) {
        return R.layout.item_my_integral;
    }

    static class ViewHolder {
        @BindView(R.id.item_my_integral_text_date)
        TextView mTextDate;
        @BindView(R.id.item_my_integral_text_operation)
        TextView mTextOperation;
        @BindView(R.id.item_my_integral_text_detail)
        TextView mTextDetail;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }
    }
}
