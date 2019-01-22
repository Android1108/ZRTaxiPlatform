package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseListAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.BillBean;
import org.wzeiri.zr.zrtaxiplatform.util.CalculateUtil;
import org.wzeiri.zr.zrtaxiplatform.util.GlideUtil;
import org.wzeiri.zr.zrtaxiplatform.util.TimeUtil;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author k-lm on 2017/11/25.
 */

public class BillAdapter extends BaseListAdapter<BillBean> {
    public BillAdapter(List<BillBean> list) {
        super(list);
    }

    @Override
    public void getItemView(int position, View convertView, ViewGroup parent, int type) {

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        if (viewHolder == null) {
            viewHolder = new ViewHolder(convertView);
        }

       /* viewHolder.mVtvDateState.setText("2017-11-25");
        //viewHolder.mVtvDateState.setTextRight("待付款");
        viewHolder.mTextCommodityName.setText("中石化充值卡200元");
        viewHolder.mVtvCommodityPriceNumber.setText("￥200.00");
        viewHolder.mVtvCommodityPriceNumber.setTextRight("X1");
        viewHolder.mVtvDiscountMoney.setTextLeft("赚");
        viewHolder.mVtvDiscountMoney.setText("￥10.00");
        viewHolder.mTextCommoditySum.setText("共1件商品，实付款：");
        viewHolder.mVtvCommodityTotal.setTextLeft("￥200");
        viewHolder.mVtvCommodityTotal.setText(".00");*/

        BillBean billBean = getData(position);

        viewHolder.mVtvDateState.setText(TimeUtil.getServerDateAndTime(billBean.getCreationTime()));
        viewHolder.mTextCommodityName.setText(billBean.getCardName());
        // 单价
        BigDecimal unitPrice = billBean.getUnitPrice();
        if (unitPrice != null) {
            String price = "￥" + CalculateUtil.getFormatToString(unitPrice.doubleValue());
            viewHolder.mVtvCommodityPriceNumber.setText(price);
        }
        // 数量
        int count = billBean.getQuantity();
        viewHolder.mVtvCommodityPriceNumber.setTextRight("X" + count);

        // 总佣金
        BigDecimal totalAmountOfTheCommission = billBean.getTotalAmountOfTheCommission();
        if (totalAmountOfTheCommission != null) {
            String discount = "￥" + CalculateUtil.getFormatToString(totalAmountOfTheCommission.doubleValue());
            viewHolder.mVtvDiscountMoney.setTextLeft("赚");
            viewHolder.mVtvDiscountMoney.setText(discount);
        }

        BigDecimal orderTotal = billBean.getOrderTotal();
        if (orderTotal != null) {
            String total = "￥" + CalculateUtil.getFormatToString(orderTotal.doubleValue());
            viewHolder.mVtvCommodityTotal.setText(total);
        }

        viewHolder.mTextCommoditySum.setText("共" + count + "件商品，合计：");

        GlideUtil.loadPath(getContext(), viewHolder.mImageCommodityImage, billBean.getCardCover());

    }

    @Override
    public int getItemLayoutId(int type) {
        return R.layout.item_bill;
    }

    static class ViewHolder {
        @BindView(R.id.item_bill_vtv_date_state)
        ValueTextView mVtvDateState;
        @BindView(R.id.item_bill_image_commodity_image)
        ImageView mImageCommodityImage;
        @BindView(R.id.item_bill_text_commodity_name)
        TextView mTextCommodityName;
        @BindView(R.id.item_bill_vtv_commodity_price_number)
        ValueTextView mVtvCommodityPriceNumber;
        @BindView(R.id.item_bill_vtv_discount_money)
        ValueTextView mVtvDiscountMoney;
        @BindView(R.id.item_bill_text_commodity_sum)
        TextView mTextCommoditySum;
        @BindView(R.id.item_bill_vtv_commodity_total)
        ValueTextView mVtvCommodityTotal;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }
    }
}
