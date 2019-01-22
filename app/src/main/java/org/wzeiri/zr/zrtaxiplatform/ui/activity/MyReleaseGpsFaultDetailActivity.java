package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.BingCarInfoBean;
import org.wzeiri.zr.zrtaxiplatform.bean.EquipmentFaultDetailBean;
import org.wzeiri.zr.zrtaxiplatform.bean.UploadResultBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.common.Config;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IDriver;
import org.wzeiri.zr.zrtaxiplatform.network.api.IEquipmentFault;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItem;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItemArray;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.util.BitmapUtil;
import org.wzeiri.zr.zrtaxiplatform.util.CarHelper;
import org.wzeiri.zr.zrtaxiplatform.util.DensityUtil;
import org.wzeiri.zr.zrtaxiplatform.util.EditTextFormatUtil;
import org.wzeiri.zr.zrtaxiplatform.util.GlideUtil;
import org.wzeiri.zr.zrtaxiplatform.util.PhotoHelper;
import org.wzeiri.zr.zrtaxiplatform.util.ThreadSwitch;
import org.wzeiri.zr.zrtaxiplatform.util.UploadPictureHelper;
import org.wzeiri.zr.zrtaxiplatform.util.UserInfoHelper;
import org.wzeiri.zr.zrtaxiplatform.widget.PhotoView;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;
import org.wzeiri.zr.zrtaxiplatform.widget.dialog.BottomDialog;
import org.wzeiri.zr.zrtaxiplatform.widget.flow.FlowLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 我发布的gps故障详情
 *
 * @author k-lm on 2017/11/23.
 */

public class MyReleaseGpsFaultDetailActivity extends ActionbarActivity {

    @BindView(R.id.aty_release_gps_fault_info_vtv_fault_type)
    ValueTextView mVtvFaultType;
    @BindView(R.id.aty_release_gps_fault_info_edit_describe)
    EditText mEditDescribe;
    @BindView(R.id.layout_taxi_info_image_car_logo)
    ImageView mImageCarLogo;
    @BindView(R.id.layout_taxi_info_text_license_plate_number)
    TextView mTextLicensePlateNumber;
    @BindView(R.id.layout_taxi_info_text_car_model)
    TextView mTextCarModel;
    @BindView(R.id.layout_taxi_info_text_taxi_audit_status)
    TextView mTextTaxiAuditStatus;
    @BindView(R.id.layout_taxi_info_vtv_taxi_company)
    TextView mTaxiCompany;
    @BindView(R.id.layout_taxi_info_text_current_car)
    TextView mTextCurrentCar;
    @BindView(R.id.aty_release_gps_fault_info_fl_photo_layout)
    FlowLayout mFlowLayout;
    @BindView(R.id.aty_release_gps_fault_info_view_submit)
    View mViewSubmit;
    private int mId = -1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_release_gps_fault_info;
    }

    @Override
    protected void create() {
        super.create();
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        mId = intent.getIntExtra("id", mId);
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("故障信息详情");
        EditTextFormatUtil.formatRemoveEmoji(mEditDescribe);
        mEditDescribe.setEnabled(false);
        mEditDescribe.setHint("");

        mViewSubmit.setVisibility(View.GONE);
    }


    @Override
    protected void initData() {
        super.initData();
        IEquipmentFault iEquipmentFault = RetrofitHelper.create(IEquipmentFault.class);
        iEquipmentFault.getEquipmentFaultDetail(mId)
                .enqueue(new MsgCallBack<BaseBean<EquipmentFaultDetailBean>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<EquipmentFaultDetailBean>> call, Response<BaseBean<EquipmentFaultDetailBean>> response) {
                        EquipmentFaultDetailBean bean = response.body().getResult();
                        if (bean == null) {
                            return;
                        }
                        mEditDescribe.setText(bean.getFaultDescribe());
                        mVtvFaultType.setText(bean.getEquipmentFaultTypeName());
                        int carId = bean.getCarId();
                        initCarInfo(CarHelper.getInstance().getBindCarInfo(carId));
                        addImages(bean.getPictures());
                    }
                });
    }


    /**
     * 初始化车辆信息
     *
     * @param carInfo
     */
    private void initCarInfo(CarHelper.CarInfo carInfo) {
        if (carInfo == null) {
            return;
        }
        GlideUtil.loadPath(getThis(), mImageCarLogo, carInfo.getBrandLogoPicture());
        mTextCarModel.setText(carInfo.getCarModel());
        mTextLicensePlateNumber.setText(carInfo.getPlateNumber());
        mTaxiCompany.setText(carInfo.getCompnay());
    }

    /**
     * 添加图片
     *
     * @param imageUrlList
     */
    private void addImages(List<String> imageUrlList) {
        if (imageUrlList == null || imageUrlList.size() == 0) {
            return;
        }
        int size = imageUrlList.size();
        int viewSize = DensityUtil.dip2px(getThis(), 80);
        for (int i = 0; i < size; i++) {
            String url = imageUrlList.get(i);
            PhotoView photoView = new PhotoView(getThis());
            ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(viewSize, viewSize);
            layoutParams.rightMargin = getResources().getDimensionPixelOffset(R.dimen.layout_margin_mini);
            mFlowLayout.addView(photoView, layoutParams);
            photoView.setPhotoUrl(url);
            photoView.setShowClose(false);
            photoView.setTag(url);
            photoView.setOnPhotoClickListener(new PhotoView.OnPhotoClickListener() {
                @Override
                public void onClickPhoto(PhotoView photoView, Bitmap bitmap) {
                    ImageZoomActivity.startUrl(getThis(), photoView, (String) photoView.getTag());
                }

                @Override
                public void onClickClose(PhotoView photoView) {

                }
            });

        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public static void start(Context context, int id) {
        Intent starter = new Intent(context, MyReleaseGpsFaultDetailActivity.class);
        starter.putExtra("id", id);
        context.startActivity(starter);
    }


}
