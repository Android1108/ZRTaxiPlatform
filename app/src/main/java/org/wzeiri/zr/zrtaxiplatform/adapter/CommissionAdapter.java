package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseListAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.CommissionBean;
import org.wzeiri.zr.zrtaxiplatform.util.CalculateUtil;
import org.wzeiri.zr.zrtaxiplatform.util.GlideUtil;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author k-lm on 2018/1/25.
 */

public class CommissionAdapter extends BaseListAdapter<CommissionBean> {

    public CommissionAdapter(List<CommissionBean> list) {
        super(list);
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_commission;
    }

    @Override
    public void getItemView(int position, View convertView, ViewGroup parent, int viewType) {
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        if (viewHolder == null) {
            viewHolder = new ViewHolder(convertView);
        }

        CommissionBean bean = getData(position);

        viewHolder.mTextName.setText(bean.getName());
        String number = "剩余数量：" + bean.getStockQuantity() + "张";
        viewHolder.mTextSurplusNumber.setText(number);
        BigDecimal bigDecimal = bean.getAmountOfTheCommission();
        String symbol = "￥";
        String moneyInteger;
        String moneyDecimal;


        if (bigDecimal == null) {
            moneyInteger = "0";
            moneyDecimal = ".00";
        } else {
            moneyInteger = CalculateUtil.getFormatToString(bigDecimal.intValue());
            moneyDecimal = bigDecimal.doubleValue() * 100 % 100 + "";
            moneyInteger = moneyInteger.substring(0, moneyInteger.indexOf(".") + 1);
        }
        viewHolder.mCommission.setTextLeft(symbol);
        viewHolder.mCommission.setText(moneyInteger);
        viewHolder.mCommissionDecimal.setText(moneyDecimal);

        GlideUtil.loadPath(getContext(), viewHolder.mImageCover, bean.getCover());


    }


    static class ViewHolder {
        @BindView(R.id.item_commission_image_cover)
        ImageView mImageCover;
        @BindView(R.id.item_commission_text_name)
        TextView mTextName;
        @BindView(R.id.item_commission_text_surplus_number)
        TextView mTextSurplusNumber;
        @BindView(R.id.item_commission_vtv_commission)
        ValueTextView mCommission;
        @BindView(R.id.item_commission_text_commission_decimal)
        TextView mCommissionDecimal;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }
    }
}
