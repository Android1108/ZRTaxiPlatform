package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseListAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.LegalBean;
import org.wzeiri.zr.zrtaxiplatform.util.TimeUtil;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author k-lm on 2017/12/6.
 */

public class MyLegalAdviceReplyAdapter extends BaseListAdapter<LegalBean> {
    private boolean mIsReply = false;
    private boolean mIsMyConsultation = false;

    public MyLegalAdviceReplyAdapter(List<LegalBean> list, boolean isReply, boolean isMyConsultation) {
        super(list);
        mIsReply = isReply;
        mIsMyConsultation = isMyConsultation;
    }


    @Override
    public void getItemView(int position, View convertView, ViewGroup parent, int viewType) {
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolder(convertView);
        }
        LegalBean bean = getData(position);
        String leftstr;
        if (mIsMyConsultation) {
            String typeName = bean.getLegalTypeName() == null ? "" : bean.getLegalTypeName();
            leftstr = "咨询类型：" + typeName;
        } else {
            leftstr = "用户" + bean.getCreationUserName();
        }

        viewHolder.mTextConsultTypeDate.setText(leftstr);
        viewHolder.mTextConsultTypeDate.setTextRight(TimeUtil.getServerDateAndTime(bean.getCreationTime()));
        viewHolder.mTextConsultContent.setText(bean.getContent());
        viewHolder.mLayoutReplay.setVisibility(View.GONE);

        if (!mIsReply) {
            return;
        }
        List<LegalBean.RepliesBean> repliesBeanList = bean.getReplies();
        if (repliesBeanList == null || repliesBeanList.size() < 1) {
            return;
        }
        LegalBean.RepliesBean repliesBean = bean.getReplies().get(0);
        viewHolder.mLayoutReplay.setVisibility(View.VISIBLE);
        viewHolder.mTextReplayContent.setText(repliesBean.getReplyContent());
        viewHolder.mTextReplayDate.setTextRight(TimeUtil.getServerDateAndTime(repliesBean.getCreationTime()));

    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_my_legal_advice_reply;
    }

    static class ViewHolder {
        @BindView(R.id.aty_my_legal_advice_reply_vtv_consult_type_date)
        ValueTextView mTextConsultTypeDate;
        @BindView(R.id.aty_my_legal_advice_reply_text_consult_content)
        TextView mTextConsultContent;
        @BindView(R.id.aty_my_legal_advice_reply_vtv_replay_date)
        ValueTextView mTextReplayDate;
        @BindView(R.id.aty_my_legal_advice_reply_text_replay_content)
        TextView mTextReplayContent;
        @BindView(R.id.aty_my_legal_advice_reply_layout_replay)
        LinearLayout mLayoutReplay;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }
    }
}
