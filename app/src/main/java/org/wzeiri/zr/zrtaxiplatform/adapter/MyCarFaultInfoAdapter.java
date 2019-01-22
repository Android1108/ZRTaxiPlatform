package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseListAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.FaultInfoBean;
import org.wzeiri.zr.zrtaxiplatform.util.TimeUtil;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我发布的故障信息
 *
 * @author k-lm on 2017/12/13.
 */

public class MyCarFaultInfoAdapter extends BaseListAdapter<FaultInfoBean> {

    public MyCarFaultInfoAdapter(List<FaultInfoBean> list) {
        super(list);
    }

    @Override
    public void getItemView(int position, View convertView, ViewGroup parent, int viewType) {
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        if (viewHolder == null) {
            viewHolder = new ViewHolder(convertView);
        }

        FaultInfoBean bean = getData(position);
        viewHolder.mTextTitle.setText(bean.getEquipmentFaultTypeName());
        viewHolder.mTextContent.setText(bean.getFaultDescribe());
        viewHolder.mVrvTypeDate.setTextRight(getState(bean.getEquipmentFaultStatus()));
        viewHolder.mVrvTypeDate.setText(TimeUtil.getServerDate(bean.getCreationTime()));
    }

    /**
     * 返回故障类型
     *
     * @param type
     * @return
     */
    private String getType(int type) {
        String typeStr = "";
        switch (type) {
            case 1:
                typeStr = "Gps故障";
                break;
            case 2:
                typeStr = "广告设备故障";
                break;
            case 3:
                typeStr = "其他";
                break;
        }
        return typeStr;
    }

    /**
     * 返回状态
     *
     * @param state
     * @return
     */
    private String getState(int state) {
        String stateStr = "";
        switch (state) {
            case 1:
                stateStr = "待维修";
                break;
            case 2:
                stateStr = "已维修";
                break;
        }
        return stateStr;
    }


    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_my_car_fault_info;
    }

    static class ViewHolder {
        @BindView(R.id.item_my_car_fault_info_text_title)
        TextView mTextTitle;
        @BindView(R.id.item_my_car_fault_info_text_content)
        TextView mTextContent;
        @BindView(R.id.item_my_car_fault_info_vrv_type_date)
        ValueTextView mVrvTypeDate;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }
    }
}
