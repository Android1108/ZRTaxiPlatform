package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseListAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.OilCardRechargeBean;
import org.wzeiri.zr.zrtaxiplatform.util.CalculateUtil;
import org.wzeiri.zr.zrtaxiplatform.util.TimeUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author k-lm on 2018/1/9.
 */

public class OilCardRechargeAdapter extends BaseListAdapter<OilCardRechargeBean> {
    public OilCardRechargeAdapter(List<OilCardRechargeBean> list) {
        super(list);
    }

    @Override
    public void getItemView(int position, View convertView, ViewGroup parent, int viewType) {
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }


        OilCardRechargeBean bean = getData(position);
        viewHolder.mTextDate.setText(TimeUtil.getServerDateAndTime(bean.getCreationTime()));

        String title = "充值" + CalculateUtil.getFormatToString(bean.getRechargeAmount().doubleValue()) + "元";
        viewHolder.mTextTitle.setText(title);

        String consumption = "-￥" + CalculateUtil.getFormatToString(bean.getPayAmount().doubleValue());
        viewHolder.mTextMoney.setText(consumption);

        int state = bean.getStatus();

        if (state == 2 || state == 4) {
            viewHolder.mTextState.setVisibility(View.GONE);
        } else if (state == 1) {
            int color = ContextCompat.getColor(getContext(), R.color.orange1);
            viewHolder.mTextState.setTextColor(color);
            viewHolder.mTextState.setText("等待付款");
            viewHolder.mTextState.setVisibility(View.VISIBLE);
        } else if (state == 8) {
            int color = ContextCompat.getColor(getContext(), R.color.gray40);
            viewHolder.mTextState.setTextColor(color);
            viewHolder.mTextState.setText("交易关闭");
            viewHolder.mTextState.setVisibility(View.VISIBLE);
        } else if (state == 16) {
            int color = ContextCompat.getColor(getContext(), R.color.gray40);
            viewHolder.mTextState.setTextColor(color);
            viewHolder.mTextState.setText("交易失效");
            viewHolder.mTextState.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_oil_card_recharge;
    }

    static class ViewHolder {
        @BindView(R.id.item_oil_card_recharge_text_title)
        TextView mTextTitle;
        @BindView(R.id.item_oil_card_recharge_text_date)
        TextView mTextDate;
        @BindView(R.id.item_oil_card_recharge_text_money)
        TextView mTextMoney;
        @BindView(R.id.item_oil_card_recharge_text_state)
        TextView mTextState;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
