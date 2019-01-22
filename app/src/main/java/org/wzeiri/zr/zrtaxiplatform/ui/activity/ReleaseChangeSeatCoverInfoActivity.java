package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.BingCarInfoBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IChangeSeat;
import org.wzeiri.zr.zrtaxiplatform.network.api.IDriver;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItem;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.util.CarHelper;
import org.wzeiri.zr.zrtaxiplatform.util.EditTextFormatUtil;
import org.wzeiri.zr.zrtaxiplatform.util.GlideUtil;
import org.wzeiri.zr.zrtaxiplatform.util.TimeUtil;
import org.wzeiri.zr.zrtaxiplatform.util.UploadPictureHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 发布车换座套
 *
 * @author k-lm on 2017/11/23.
 */

public class ReleaseChangeSeatCoverInfoActivity extends ActionbarActivity {
    public static final int REQUEST_CODE = 30004;
    public static final String KEY_RELEASE_CAR_ID = ReleaseAdvertisementPhotoInfoActivity.KEY_RELEASE_CAR_ID;
    @BindView(R.id.aty_release_change_seat_cover_info_edit_describe)
    EditText mDescribe;
    @BindView(R.id.layout_taxi_info_image_car_logo)
    ImageView mImageCarLogo;
    @BindView(R.id.layout_taxi_info_text_license_plate_number)
    TextView mTextLicensePlateNumber;
    @BindView(R.id.layout_taxi_info_text_car_model)
    TextView mTextCarModel;
    @BindView(R.id.layout_taxi_info_vtv_taxi_company)
    TextView mTextTaxiCompany;
    @BindView(R.id.layout_taxi_info_text_current_car)
    TextView mTextCurrentCar;
    @BindView(R.id.layout_release_info_text_release)
    TextView mTextRelease;

    private IChangeSeat mIChangeSeat;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_release_change_seat_cover_info;
    }


    @Override
    protected void init() {
        super.init();
        setCenterTitle("发布换座套请求");
        EditTextFormatUtil.formatRemoveEmoji(mDescribe);

        if (!CarHelper.getInstance().isLoadCarInfo()) {
            loadCarInfo();
            return;
        }
        initCarInfo(CarHelper.getInstance().getCurrentCarInfo());

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
                        initCarInfo(helper.getCurrentCarInfo());
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
        mTextTaxiCompany.setText(carInfo.getCompnay());
        mTextCurrentCar.setVisibility(View.VISIBLE);
    }


    @OnClick(R.id.layout_release_info_text_release)
    void releaseClick() {
        String describe = mDescribe.getText().toString();
        /*if (TextUtils.isEmpty(describe)) {
            showToast("请填写备注");
            return;
        }*/

        if (mIChangeSeat == null) {
            mIChangeSeat = RetrofitHelper.create(IChangeSeat.class);
        }

        int carId = CarHelper.getInstance().getCurrentCarId();
        mIChangeSeat.createChangeSeat(RetrofitHelper
                .getBody(new JsonItem("carId", carId),
                        new JsonItem("describe", describe)))
                .enqueue(new MsgCallBack<BaseBean<String>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                        showToast("提交成功");
                        Intent intent = new Intent();
                        intent.getIntExtra(KEY_RELEASE_CAR_ID, CarHelper.getInstance().getCurrentCarId());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });


    }


}