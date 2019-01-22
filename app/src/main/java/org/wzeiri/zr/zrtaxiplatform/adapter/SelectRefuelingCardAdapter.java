package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseListAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.OilCardBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author k-lm on 2018/1/5.
 */

public class SelectRefuelingCardAdapter extends BaseListAdapter<OilCardBean> {
    public SelectRefuelingCardAdapter(List<OilCardBean> list) {
        super(list);
    }

    @Override
    public void getItemView(int position, View convertView, ViewGroup parent, int viewType) {
        ViewHolder holder = (ViewHolder) convertView.getTag();

        if (holder == null) {
            holder = new ViewHolder(convertView);
        }
        OilCardBean bean = getData(position);

        holder.mCardNumber.setText(bean.getOilCardNumber());
        holder.mType.setText(bean.getOilCardTypeName());

    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_select_select_refueling_card;
    }

    static class ViewHolder {
        @BindView(R.id.item_select_select_refueling_card_text_type)
        TextView mType;
        @BindView(R.id.item_select_select_refueling_card_text_card_number)
        TextView mCardNumber;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }
    }
}
