package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.provider.Telephony;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseListAdapter;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import java.util.List;

/**
 * Created by zz on 2017-12-14.
 */

public class DriverAuthenticationAdapter extends BaseListAdapter<Object>{
    public DriverAuthenticationAdapter(List<Object> list) {
        super(list);
    }

    @Override
    public void getItemView(int position, View convertView, ViewGroup parent, int viewType) {

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        if (viewHolder == null) {
            viewHolder = new ViewHolder(convertView);
        }

        viewHolder.mItemTvTaxiCompanies.setText("出租车公司");
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_taxi_companies;
    }

    static class ViewHolder {
        @BindView(R.id.item_tv_taxi_companies)
        TextView mItemTvTaxiCompanies;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }

    }

}
