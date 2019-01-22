package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseListAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.WalletNoteBean;
import org.wzeiri.zr.zrtaxiplatform.util.CalculateUtil;
import org.wzeiri.zr.zrtaxiplatform.util.TimeUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author k-lm on 2017/11/23.
 */

public class BalanceInfoAdapter extends BaseListAdapter<WalletNoteBean> {
    public BalanceInfoAdapter(List<WalletNoteBean> list) {
        super(list);
    }

    @Override
    public void getItemView(int position, View convertView, ViewGroup parent, int type) {
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolder(convertView);
        }
        WalletNoteBean bean = getData(position);

        viewHolder.mTextTitle.setText(bean.getNote());
        viewHolder.mTextDate.setText(TimeUtil.getServerDate(bean.getCreationTime()));
        String moneyStr = "￥" + CalculateUtil.getFormatToString(bean.getAmount().doubleValue());

        if (bean.getWalletNoteType() == 0) {
            moneyStr = "+" + moneyStr;
            viewHolder.mTextMoney.setTextColor(ContextCompat.getColor(convertView.getContext(), R.color.orange1));
        } else {
            moneyStr = "-" + moneyStr;
            viewHolder.mTextMoney.setTextColor(ContextCompat.getColor(convertView.getContext(), R.color.black70));
        }

        viewHolder.mTextMoney.setText(moneyStr);


    }

    @Override
    public int getItemLayoutId(int type) {
        return R.layout.item_balance_info;
    }

    static class ViewHolder {
        @BindView(R.id.item_balance_info_text_title)
        TextView mTextTitle;
        @BindView(R.id.item_balance_info_text_date)
        TextView mTextDate;
        @BindView(R.id.item_balance_info_text_money)
        TextView mTextMoney;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }
    }
}
