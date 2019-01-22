package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.AdverPostApplyDetailBean;
import org.wzeiri.zr.zrtaxiplatform.bean.BingCarInfoBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IAdverPostApply;
import org.wzeiri.zr.zrtaxiplatform.network.api.IDriver;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.util.CarHelper;
import org.wzeiri.zr.zrtaxiplatform.util.DensityUtil;
import org.wzeiri.zr.zrtaxiplatform.util.EditTextFormatUtil;
import org.wzeiri.zr.zrtaxiplatform.util.GlideUtil;
import org.wzeiri.zr.zrtaxiplatform.util.TimeUtil;
import org.wzeiri.zr.zrtaxiplatform.widget.PhotoView;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;
import org.wzeiri.zr.zrtaxiplatform.widget.flow.FlowLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 我发布张贴广告照片详情
 *
 * @author k-lm on 2017/11/23.
 */

public class MyReleaseAdvertisementPhotoDetailActivity extends ActionbarActivity {
    public static final int REQUEST_CODE = 30005;
    @BindView(R.id.aty_my_release_advertisement_photo_detail_text_describe)
    TextView mTextDescribe;
    @BindView(R.id.aty_my_release_advertisement_photo_detail_fl_layout)
    FlowLayout mFlowLayout;
    @BindView(R.id.aty_my_release_advertisement_photo_detail_vtv_date)
    ValueTextView mVtvDate;
    @BindView(R.id.layout_taxi_info_image_car_logo)
    ImageView mImageCarLogo;
    @BindView(R.id.layout_taxi_info_text_license_plate_number)
    TextView mTextLicensePlateNumber;
    @BindView(R.id.layout_taxi_info_text_car_model)
    TextView mTextCarModel;
    @BindView(R.id.layout_taxi_info_text_taxi_audit_status)
    TextView mTextTaxiAuditStatus;
    @BindView(R.id.layout_taxi_info_vtv_taxi_company)
    ValueTextView mTaxiCompany;
    @BindView(R.id.layout_taxi_info_text_current_car)
    TextView mTextCurrentCar;

    private int mId = -1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_release_advertisement_photo_detail;
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
        setCenterTitle("广告张贴详情");

    }

    @Override
    protected void initData() {
        super.initData();

        IAdverPostApply mIAdverPostApply = RetrofitHelper.create(IAdverPostApply.class);
        mIAdverPostApply.getAdverPostApplyDetail(mId)
                .enqueue(new MsgCallBack<BaseBean<AdverPostApplyDetailBean>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<AdverPostApplyDetailBean>> call, Response<BaseBean<AdverPostApplyDetailBean>> response) {
                        AdverPostApplyDetailBean bean = response.body().getResult();
                        if (bean == null) {
                            return;
                        }
                        mTextDescribe.setText(bean.getDescribe());
                        mVtvDate.setText(TimeUtil.getServerDate(bean.getPostTime()));
                        addImages(bean.getAdverPostApplyPictures());
                        initCarInfo(CarHelper.getInstance().getBindCarInfo(bean.getCarId()));
                    }
                });
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

    /**
     * 加载车辆信息
     */
    private void loadCarInfo() {
        IDriver driver = RetrofitHelper.create(IDriver.class);
        driver.getBindingCars()
                .enqueue(new MsgCallBack<BaseBean<BingCarInfoBean>>(this, true) {
                    @Override
                    public void onSuccess(Call<BaseBean<BingCarInfoBean>> call, Response<BaseBean<BingCarInfoBean>> response) {
                        CarHelper helper = CarHelper.getInstance();
                        helper.save(response.body().getResult());

//                        initCarInfo(helper.getBindCarInfo(mCar));
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public static void start(Context context, int id) {
        Intent starter = new Intent(context, MyReleaseAdvertisementPhotoDetailActivity.class);
        starter.putExtra("id", id);
        context.startActivity(starter);
    }


}
