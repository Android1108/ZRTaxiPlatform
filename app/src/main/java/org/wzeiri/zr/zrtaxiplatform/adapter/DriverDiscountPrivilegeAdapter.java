package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseListAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.MerchantBean;
import org.wzeiri.zr.zrtaxiplatform.util.CalculateUtil;
import org.wzeiri.zr.zrtaxiplatform.util.GlideUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author k-lm on 2017/11/21.
 */

public class DriverDiscountPrivilegeAdapter extends BaseListAdapter<MerchantBean> {
    public DriverDiscountPrivilegeAdapter(List<MerchantBean> list) {
        super(list);
    }

    @Override
    public void getItemView(int position, View convertView, ViewGroup parent, int type) {
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        if (viewHolder == null) {
            viewHolder = new ViewHolder(convertView);
        }

        MerchantBean bean = getData(position);

        viewHolder.mTextStoreName.setText(bean.getName());
        viewHolder.mTextDate.setText(bean.getMerchantFeature() + "|" + bean.getBusinessHours());
        viewHolder.mTextLocation.setText(bean.getAddress());
        String distanceStr;
        double distance = bean.getDistance();

        if (distance < 1) {
            distanceStr = distance * 1000 + "M";
        } else if (distance < 10) {
            distanceStr = CalculateUtil.getFormatToString(distance) + "KM";
        } else {
            distanceStr = ">10KM";
        }

       /* if (distance < 1000) {
            distanceStr = CalculateUtil.getFormatToString(distance) + "M";
        } else {
            distanceStr = CalculateUtil.getFormatToString(distance / 1000) + "KM";
        }*/
        //distanceStr = CalculateUtil.getFormatToString(distance) + "KM";

        viewHolder.mTextDistance.setText(distanceStr);
        GlideUtil.loadPath(getContext(), viewHolder.mImage, bean.getConverPicture());


    }

    @Override
    public int getItemLayoutId(int type) {
        return R.layout.item_driver_discount;
    }


    class ViewHolder {
        @BindView(R.id.item_driver_discount_image)
        ImageView mImage;
        @BindView(R.id.item_driver_discount_text_store_name)
        TextView mTextStoreName;
        @BindView(R.id.item_driver_discount_text_date)
        TextView mTextDate;
        @BindView(R.id.item_driver_discount_text_location)
        TextView mTextLocation;
        @BindView(R.id.item_driver_discount_text_distance)
        TextView mTextDistance;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
