package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.BingCarInfoBean;
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
import org.wzeiri.zr.zrtaxiplatform.util.EditTextFormatUtil;
import org.wzeiri.zr.zrtaxiplatform.util.GlideUtil;
import org.wzeiri.zr.zrtaxiplatform.util.PhotoHelper;
import org.wzeiri.zr.zrtaxiplatform.util.ThreadSwitch;
import org.wzeiri.zr.zrtaxiplatform.util.UploadPictureHelper;
import org.wzeiri.zr.zrtaxiplatform.util.UserInfoHelper;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;
import org.wzeiri.zr.zrtaxiplatform.widget.dialog.BottomDialog;
import org.wzeiri.zr.zrtaxiplatform.widget.flow.FlowLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 发布gps故障
 *
 * @author k-lm on 2017/11/23.
 */

public class ReleaseGpsFaultActivity extends ActionbarActivity implements PhotoHelper.OnPhotoUpdateListener {
    public static final int REQUEST_CODE = 30006;
    public static final String KEY_RELEASE_CAR_ID = ReleaseAdvertisementPhotoInfoActivity.KEY_RELEASE_CAR_ID;

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
    TextView mTextTaxiCompany;
    @BindView(R.id.layout_taxi_info_text_current_car)
    TextView mTextCurrentCar;
    @BindView(R.id.aty_release_gps_fault_info_fl_photo_layout)
    FlowLayout mFlowLayout;

    private PhotoHelper mPhotoHelper;

    private UploadPictureHelper mUploadHelper;
    private Bitmap mBitmap;

    private IEquipmentFault mIEquipmentFault;
    private BottomDialog mBottomDialog;
    /**
     * 错误类型
     */
    private int mTypeCode = -1;

    private List<String> mImageList = new ArrayList<>(3);

    @Override
    protected int getLayoutId() {
        return R.layout.activity_release_gps_fault_info;
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("发布故障信息");
        EditTextFormatUtil.formatRemoveEmoji(mEditDescribe);
        initHelper();
        if (!CarHelper.getInstance().isLoadCarInfo()) {
            loadCarInfo();
            return;
        }
        initCarInfo(CarHelper.getInstance().getCurrentCarInfo());
    }


    private void initHelper() {
        mPhotoHelper = new PhotoHelper(getThis(), mFlowLayout, 3, Config.STORAGE_PATH);
        mPhotoHelper.setOnPhotoUpdateListener(this);
        mPhotoHelper.setOnPhotoOperationListener(new PhotoHelper.OnPhotoOperationListener() {
            @Override
            public void onPhotoDelete(int position) {
                mImageList.remove(position);
            }

            @Override
            public void onPhotoClick(int position, Bitmap bitmap) {

            }
        });

        mUploadHelper = new UploadPictureHelper(getThis());
        mUploadHelper.setOnUploadListener(new UploadPictureHelper.OnUploadListener() {
            @Override
            public void onUploadSuccess(@NonNull List<UploadResultBean> beans) {
                UploadResultBean bean = beans.get(0);
                if (bean == null) {
                    return;
                }
                closeProgressDialog();
                mPhotoHelper.addPhoto(mBitmap);
                mImageList.add(bean.getFileName());
            }

            @Override
            public void onUploadError(String msg) {
                showToast("图片处理失败");
                closeProgressDialog();
                //mPhotoHelper.removeLastPhoto();
            }
        });
    }


    @Override
    protected void initData() {
        super.initData();


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


    @OnClick(R.id.aty_release_gps_fault_info_vtv_fault_type)
    public void onFaultTypelClicked() {
        if (mBottomDialog == null) {
            String[] typeCodeArray = new String[]{"GPS故障", "广告设备故障", "其他"};
            mBottomDialog = new BottomDialog(this, R.style.NoTitleDialog);
            mBottomDialog.addDataArray(typeCodeArray);
            mBottomDialog.setOnItemClickListener(new BottomDialog.OnItemClickListener() {
                @Override
                public void onItemClick(BottomDialog dialog, int position) {
                    switch (position) {
                        case 0:
                            mTypeCode = 1;
                            break;
                        case 1:
                            mTypeCode = 2;
                            break;
                        case 2:
                            mTypeCode = 4;
                            break;
                    }
                    mVtvFaultType.setText(dialog.getData(position));
                }
            });
        }

        mBottomDialog.show();
    }

    @OnClick(R.id.layout_release_info_text_release)
    public void onLayoutReleaseInfoTextReleaseClicked() {
        String faultStr = mVtvFaultType.getText().toString();
        String describe = mEditDescribe.getText().toString();
        if (TextUtils.isEmpty(faultStr)) {
            showToast("请选择故障类型");
            return;
        }

        if (TextUtils.isEmpty(describe)) {
            showToast("请输入描述信息");
            return;
        }


        if (mIEquipmentFault == null) {
            mIEquipmentFault = RetrofitHelper.create(IEquipmentFault.class);
        }

        mIEquipmentFault.createEquipmentFault(RetrofitHelper
                .getBody(new JsonItemArray("equipmentFaultPictures", mImageList)
                        , new JsonItem("carId", CarHelper.getInstance().getCurrentCarId()),
                        new JsonItem("equipmentFaultType", mTypeCode),
                        new JsonItem("faultDescribe", describe),
                        new JsonItem("createUserId", UserInfoHelper.getInstance().getUserId())))
                .enqueue(new MsgCallBack<BaseBean<String>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                        showToast("发布成功");
                        Intent intent = new Intent();
                        intent.getIntExtra(KEY_RELEASE_CAR_ID, CarHelper.getInstance().getCurrentCarId());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPhotoHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPhotoHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onStartCameraOrAlbum(boolean isSuccess) {

    }


    @Override
    public void onResult(boolean isSuccess) {
        if (isSuccess) {
            showProgressDialog("正在处理图片，请稍候");
        }
    }

    @Override
    public void onResultBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            closeProgressDialog();
            showToast("图片获取失败，请重试");
            return;
        }
        mBitmap = bitmap;
        // 保存文件
        ThreadSwitch.createTask(new ThreadSwitch.OnCreateListener<Boolean>() {
            @Override
            public Boolean onCreate(ThreadSwitch threadSwitch) {
                BitmapUtil.saveBitmap(Config.STORAGE_PATH, Config.UPLOAD_IMAGE_NAME, mBitmap, 100);
                return true;
            }
        })
                .switchLooper(ThreadSwitch.MAIN_THREAD)
                .submit(new ThreadSwitch.OnSubmitListener<Boolean>() {
                    @Override
                    public void onSubmit(ThreadSwitch threadSwitch, Boolean value) {
                        File file = new File(Config.STORAGE_PATH + Config.UPLOAD_IMAGE_NAME);
                        if (!file.exists()) {
                            showToast("图片获取失败，请重试");
                            closeProgressDialog();
                            return;
                        }
                        mUploadHelper.removeAllUploadFile();
                        mUploadHelper.addUploadFile(file);
                        mUploadHelper.upload();
                    }
                });
    }
}
