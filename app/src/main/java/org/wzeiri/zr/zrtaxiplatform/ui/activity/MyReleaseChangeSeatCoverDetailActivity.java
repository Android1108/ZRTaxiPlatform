package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.Context;
import android.content.Intent;
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

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 发布车换座套
 *
 * @author k-lm on 2017/11/23.
 */

public class MyReleaseChangeSeatCoverDetailActivity extends ActionbarActivity {
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

    @BindView(R.id.aty_release_change_seat_cover_info_view_submit)
    View mViewSubmit;


    private int mCarId = -1;
    /**
     * 备注
     */
    private String mDescribeStr;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_release_change_seat_cover_info;
    }


    @Override
    protected void create() {
        super.create();
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        mCarId = intent.getIntExtra("carId", mCarId);
        mDescribeStr = intent.getStringExtra("describe");
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("换座套请求详情");
        mDescribe.setHint("");
        mDescribe.setEnabled(false);
        EditTextFormatUtil.formatRemoveEmoji(mDescribe);
        mViewSubmit.setVisibility(View.GONE);
      /*  if (!CarHelper.getInstance().isLoadCarInfo()) {
            loadCarInfo();
            return;
        }
        initCarInfo(CarHelper.getInstance().getCurrentCarInfo());*/
    }


    @Override
    protected void initData() {
        super.initData();
        mDescribe.setText(mDescribeStr);
        initCarInfo(CarHelper.getInstance().getBindCarInfo(mCarId));

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
//        mTextCurrentCar.setVisibility(View.VISIBLE);
    }

    /**
     * @param carId    车辆id
     * @param describe 备注
     */
    public static void start(Context context, int carId, String describe) {
        Intent starter = new Intent(context, MyReleaseChangeSeatCoverDetailActivity.class);
        starter.putExtra("carId", carId);
        starter.putExtra("describe", describe);
        context.startActivity(starter);
    }

}
