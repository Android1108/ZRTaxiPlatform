package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseListAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.LegalBean;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author k-lm on 2017/12/6.
 */

public class LegalAdviceReplyAdapter extends BaseListAdapter<LegalBean> {


    public LegalAdviceReplyAdapter(List<LegalBean> list) {
        super(list);
    }


    @Override
    public void getItemView(int position, View convertView, ViewGroup parent, int viewType) {
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolder(convertView);
        }

        LegalBean bean = getData(position);

        viewHolder.mTextConsultTypeDate.setText("咨询类型：刑事辩护");
        viewHolder.mTextConsultTypeDate.setTextRight("2017-12-6");
        viewHolder.mTextConsultContent.setText("我就咨询一个问题");


        viewHolder.mLayoutReplay.setVisibility(View.VISIBLE);
        viewHolder.mTextReplayContent.setText("我就回答一个问题");
        viewHolder.mTextReplayDate.setTextRight("2017-12-6");

    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_legal_advice_reply;
    }

    static class ViewHolder {
        @BindView(R.id.aty_legal_advice_reply_vtv_consult_type_date)
        ValueTextView mTextConsultTypeDate;
        @BindView(R.id.aty_legal_advice_reply_text_consult_content)
        TextView mTextConsultContent;
        @BindView(R.id.aty_legal_advice_reply_vtv_replay_date)
        ValueTextView mTextReplayDate;
        @BindView(R.id.aty_legal_advice_reply_text_replay_content)
        TextView mTextReplayContent;
        @BindView(R.id.aty_legal_advice_reply_layout_replay)
        LinearLayout mLayoutReplay;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }
    }
}
