package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseListAdapter;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.recycler.BaseAdapter;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.recycler.LoadMoreAdapter;
import org.wzeiri.zr.zrtaxiplatform.util.CarHelper;
import org.wzeiri.zr.zrtaxiplatform.util.GlideUtil;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 出租车
 *
 * @author k-lm on 2017/11/20.
 */

public class TaxiAdapter extends BaseListAdapter<CarHelper.CarInfo> {


    public TaxiAdapter(List<CarHelper.CarInfo> list) {
        super(list);
    }

    @Override
    public void getItemView(int position, View convertView, ViewGroup parent, int viewType) {

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        if (viewHolder == null) {
            viewHolder = new ViewHolder(convertView);
        }

        CarHelper.CarInfo carInfo = getData(position);


        GlideUtil.loadPath(getContext(), viewHolder.mImageCarLogo, carInfo.getBrandLogoPicture());

        viewHolder.mTextLicensePlateNumber.setText(carInfo.getPlateNumber());
        viewHolder.mTextCarModel.setText(carInfo.getCarModel());
        viewHolder.mTextTaxiCompany.setText(carInfo.getCompnay());

        if (CarHelper.getInstance().isCurrentCar(carInfo)) {
            viewHolder.mTextCurrentCar.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mTextCurrentCar.setVisibility(View.GONE);
        }


        if (carInfo.getBindingCarRequestStatus() == 0) {
            viewHolder.mTextTaxiCompany.setTextRight("审核中");
        }else   if (carInfo.getBindingCarRequestStatus() == 2) {
            viewHolder.mTextTaxiCompany.setTextRight("审核失败");
        } else {
            viewHolder.mTextTaxiCompany.setTextRight("");
        }

    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.layout_taxi_info;
    }

    public static class ViewHolder {
        @BindView(R.id.layout_taxi_info_image_car_logo)
        ImageView mImageCarLogo;
        @BindView(R.id.layout_taxi_info_text_license_plate_number)
        TextView mTextLicensePlateNumber;
        @BindView(R.id.layout_taxi_info_text_car_model)
        TextView mTextCarModel;
        @BindView(R.id.layout_taxi_info_vtv_taxi_company)
        ValueTextView mTextTaxiCompany;
        @BindView(R.id.layout_taxi_info_text_current_car)
        TextView mTextCurrentCar;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
