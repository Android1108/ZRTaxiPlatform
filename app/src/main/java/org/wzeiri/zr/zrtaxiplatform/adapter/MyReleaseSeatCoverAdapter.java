package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseListAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.SeatCoverBean;
import org.wzeiri.zr.zrtaxiplatform.util.TimeUtil;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我发布车换座套
 *
 * @author k-lm on 2017/12/13.
 */

public class MyReleaseSeatCoverAdapter extends BaseListAdapter<SeatCoverBean> {

    public MyReleaseSeatCoverAdapter(List<SeatCoverBean> list) {
        super(list);
    }

    @Override
    public void getItemView(int position, View convertView, ViewGroup parent, int viewType) {
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        if (viewHolder == null) {
            viewHolder = new ViewHolder(convertView);
        }

        SeatCoverBean bean = getData(position);

        viewHolder.mTextTitle.setText(bean.getDescribe());
        viewHolder.mVtvTypeDate.setText(TimeUtil.getServerDate(bean.getCreationTime()));
        viewHolder.mVtvTypeDate.setTextRight(bean.getChangeSeatStatusName());
    }


    private String getType(int type) {
        String stateStr = "";
        switch (type) {
            case 1:
                stateStr = "待更换";
                break;
            case 2:
                stateStr = "已更换";
                break;
        }

        return stateStr;
    }


    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_my_car_seat_cover;
    }

    static class ViewHolder {
        @BindView(R.id.item_my_car_seat_cover_text_title)
        TextView mTextTitle;
        @BindView(R.id.item_my_car_seat_cover_vtv_type_date)
        ValueTextView mVtvTypeDate;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            view.setTag(this);
        }
    }
}
