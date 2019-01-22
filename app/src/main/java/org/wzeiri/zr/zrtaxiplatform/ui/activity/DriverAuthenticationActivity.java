package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.DriverAuthenticationAdapter;
import org.wzeiri.zr.zrtaxiplatform.adapter.DriverInteractionAdapter;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseListAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.AuthenticationDataBean;
import org.wzeiri.zr.zrtaxiplatform.bean.DeviceVehicleBean;
import org.wzeiri.zr.zrtaxiplatform.bean.LocationRegionBean;
import org.wzeiri.zr.zrtaxiplatform.bean.LoginBean;
import org.wzeiri.zr.zrtaxiplatform.bean.TenantBean;
import org.wzeiri.zr.zrtaxiplatform.bean.UploadResultBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.common.Config;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IDriver;
import org.wzeiri.zr.zrtaxiplatform.network.api.IRegion;
import org.wzeiri.zr.zrtaxiplatform.network.api.ISundry;
import org.wzeiri.zr.zrtaxiplatform.network.api.ITokenAuth;
import org.wzeiri.zr.zrtaxiplatform.network.api.IUser;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItem;
import org.wzeiri.zr.zrtaxiplatform.network.callback.BaseCallBack;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.service.BaiduLocationService;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseListActivity;
import org.wzeiri.zr.zrtaxiplatform.util.BitmapUtil;
import org.wzeiri.zr.zrtaxiplatform.util.EditTextFormatUtil;
import org.wzeiri.zr.zrtaxiplatform.util.PhotographUtil;
import org.wzeiri.zr.zrtaxiplatform.util.ThreadSwitch;
import org.wzeiri.zr.zrtaxiplatform.util.UploadPictureHelper;
import org.wzeiri.zr.zrtaxiplatform.util.UserInfoHelper;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueEditText;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;
import org.wzeiri.zr.zrtaxiplatform.widget.dialog.BottomDialog;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 司机认证
 *
 * @author k-lm on 2017/11/17.
 */

public class DriverAuthenticationActivity extends ActionbarActivity {


    @BindView(R.id.aty_driver_authentication_vet_full_name)
    ValueEditText mVetFullName;
    @BindView(R.id.aty_driver_authentication_vet_qualification_certificate)
    ValueEditText mVetQualificationCertificate;
    @BindView(R.id.aty_driver_authentication_image_qualification_certificate)
    ImageView mImageCertificate;
    @BindView(R.id.aty_driver_authentication_vtv_taxi_company)
    ValueTextView mVtvTaxiCompany;

    @BindView(R.id.aty_vehicle_authentication_ved_car_number)
    ValueEditText mVedCarNumber;
    @BindView(R.id.aty_vehicle_authentication_vtv_brand)
    ValueTextView mVtyBrand;
    @BindView(R.id.aty_vehicle_authentication_vet_car_models)
    ValueEditText mVtyCarMobile;
    @BindView(R.id.aty_vehicle_authentication_vtv_color)
    ValueTextView mVtyColor;
    @BindView(R.id.aty_driver_authentication_vet_taxi_license_number)
    ValueEditText mVetTaxiLicenseNumber;
    @BindView(R.id.aty_driver_authentication_image_taxi_license)
    ImageView mImageTaxiLicense;


    private IDriver mIDriver;
    private BottomDialog mBottomDialog;


    private boolean mIsToMain = true;

    /**
     * 图片uri
     */
    private Uri mPhotoUri;

    private Bitmap mPhotoBitmap;
    /**
     * 出租车公司id
     */
    private int mCompanyId = -1;
    /**
     * 地区id
     */
    private int mTenantId = -1;
    /**
     * 颜色id
     */
    private int mColorId;
    /**
     * 品牌id
     */
    private int mBrandId;
    /**
     * 车牌号前缀
     */
    private String mPlateNumberPrefix = "";

    private AuthenticationDataBean mDataBean;

    private UploadPictureHelper mUploadPictureHelper;
    /**
     * 上传运营证图片名称
     */
    private String mQualificationUploadName = "";
    /**
     * 上传的从业资格证图片名称
     */
    private String mOperationUploadName = "";


    public static final int REQUEST_CODE = 10060;

    /**
     * 营运证
     */
    private final int QUALIFICATION_CERTIFICATE = 1;
    /**
     * 从业资格证
     */
    private final int OPERATION_CERTIFICATE = 2;
    /**
     * 司机车辆信息
     */
    private DeviceVehicleBean mDeviceVehicleBean;

    private int mImageType = -1;

    /**
     * 车型
     */
    private List<String> mCarModelList;

    /**
     * 选择车型对话框
     */
    private BottomDialog mSelectCarModelDialog;

    @Override
    protected void create() {
        super.create();
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        mDataBean = intent.getParcelableExtra("data");
        mTenantId = mDataBean.getTenantId();
        mIsToMain = intent.getBooleanExtra("isToMain", mIsToMain);
        mPlateNumberPrefix = intent.getStringExtra("plateNumberPrefix");
        mDeviceVehicleBean = intent.getParcelableExtra("deviceVehicleData");
        mPlateNumberPrefix = mPlateNumberPrefix.trim();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_driver_authentication;
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("司机认证");
        setNavigationIcon(null);
        setContentBackgroundResource(R.color.white);
        EditTextFormatUtil.formatNumberCapitalLetter(mVedCarNumber);
        mVedCarNumber.setTextLeft(mPlateNumberPrefix + " ");

        mUploadPictureHelper = new UploadPictureHelper(this);
        // 上传回调
        mUploadPictureHelper.setOnUploadListener(new UploadPictureHelper.OnUploadListener() {
            @Override
            public void onUploadSuccess(@NonNull List<UploadResultBean> bean) {
                closeProgressDialog();

                if (mImageType == OPERATION_CERTIFICATE) {
                    mOperationUploadName = bean.get(0).getFileName();
                    mImageCertificate.setImageBitmap(mPhotoBitmap);
                } else if (mImageType == QUALIFICATION_CERTIFICATE) {
                    mQualificationUploadName = bean.get(0).getFileName();
                    mImageTaxiLicense.setImageBitmap(mPhotoBitmap);
                }


            }

            @Override
            public void onUploadError(String msg) {
                closeProgressDialog();
            }
        });


        if (mDeviceVehicleBean != null) {
            // 填充车辆数据
            mVetFullName.setText(mDeviceVehicleBean.getName());
            mVtvTaxiCompany.setText(mDeviceVehicleBean.getCompany());
            mVtyCarMobile.setText(mDeviceVehicleBean.getCarModel());
            mVedCarNumber.setText(mDeviceVehicleBean.getPlateNumber());
            mVetQualificationCertificate.setText(mDeviceVehicleBean.getLisenceNumber());
            mVtyBrand.setText(mDeviceVehicleBean.getBrand());

            mBrandId = mDeviceVehicleBean.getBrandId();
            mCompanyId = mDeviceVehicleBean.getCompanyId();
        }

    }


    @Override
    protected void initData() {
        super.initData();
        loadCarModeData();
    }

    /**
     * 加载车型数据
     */
    private void loadCarModeData() {
        ISundry sundry = RetrofitHelper.create(ISundry.class);
        sundry.getCarModels()
                .enqueue(new MsgCallBack<BaseBean<List<String>>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<List<String>>> call, Response<BaseBean<List<String>>> response) {
                        mCarModelList = response.body().getResult();
                    }
                });
    }

    @OnClick(R.id.aty_driver_authentication_vtv_taxi_company)
    public void onmVtvTaxiCompanyClicked() {
        SelectTaxiCompanyActivity.start(getThis(), mCompanyId, mTenantId);
    }


    @OnClick(R.id.aty_driver_authentication_image_qualification_certificate)
    void onClickQualificationCertificate() {
        mImageType = OPERATION_CERTIFICATE;
        showPhotoDialog();
    }

    @OnClick(R.id.aty_driver_authentication_image_taxi_license)
    void onClickTaxiLicense() {
        mImageType = QUALIFICATION_CERTIFICATE;
        showPhotoDialog();
    }

    @OnClick(R.id.aty_vehicle_authentication_vtv_color)
    void onColorClick() {
        SelectCarColorActivity.start(getThis(), mColorId);
    }

    @OnClick(R.id.aty_vehicle_authentication_vtv_brand)
    void onBrandClick() {
        SelectCarBrandActivity.start(getThis(), mBrandId);
    }

    @OnClick(R.id.aty_driver_authentication_text_submit)
    public void onmTextSubmitClicked() {
        final String name = mVetFullName.getText().toString().trim();
        final String qualificationCertificate = mVetQualificationCertificate.getText().toString().trim();
        final String taxiCompany = mVtvTaxiCompany.getText().toString().trim();
        String plateNumber = mVedCarNumber.getText().toString().trim();
        String brand = mVtyBrand.getText().toString().trim();
        final String carModel = mVtyCarMobile.getText().toString().trim();
        String color = mVtyColor.getText().toString().trim();
        final String carLinsenceNumber = mVetTaxiLicenseNumber.getText().toString().trim();

        //final String imageIdCardBack = mImageIdCardBack.getDrawable().toString().trim();

        if (TextUtils.isEmpty(name)) {
            showToast("请输入姓名");
            return;
        } else if (TextUtils.isEmpty(taxiCompany)) {
            showToast("请选择出租车公司");
            return;
        } else if (TextUtils.isEmpty(qualificationCertificate)) {
            showToast("请输入从业资格证");
            return;
        } else if (TextUtils.isEmpty(mOperationUploadName)) {
            showToast("请设置从业资格证图片");
            return;
        } else if (TextUtils.isEmpty(plateNumber)) {
            showToast("请输入车牌号");
            return;
        } else if (plateNumber.length() < 5) {
            showToast("请输入正确的车牌号");
            return;
        } else if (TextUtils.isEmpty(carModel)) {
            showToast("请选择车型");
            return;
        } else if (TextUtils.isEmpty(brand)) {
            showToast("请选择车辆品牌");
            return;
        } else if (TextUtils.isEmpty(color)) {
            showToast("请选择车辆颜色");
            return;
        } /*else if (TextUtils.isEmpty(carLinsenceNumber)) {
            showToast("请输入运营证号");
            return;
        }*/ else if (TextUtils.isEmpty(mQualificationUploadName)) {
            showToast("请设置运营证照片");
            return;
        }

        if (mDataBean == null) {
            showToast("认证失败，请重试");
            return;
        }
        plateNumber = mPlateNumberPrefix + plateNumber;

        String idCardNum = mDataBean.getIdCardNumber();


        showProgressDialog();

        RequestBody body = RetrofitHelper
                .getBody(new JsonItem("idNumber", idCardNum),
                        new JsonItem("name", name),
                        new JsonItem("tenentId", mTenantId),
                        new JsonItem("lisenceNumber", qualificationCertificate),
                        new JsonItem("lisencePicture", mOperationUploadName),
                        new JsonItem("companyId", mCompanyId),
                        new JsonItem("plateNumber", plateNumber),
                        new JsonItem("brandId", mBrandId),
                        new JsonItem("carModel", carModel),
                        new JsonItem("colorId", mColorId),
                        new JsonItem("carLisenceNumber", carLinsenceNumber),
                        new JsonItem("carLisencePicture", mQualificationUploadName));

        if (mIDriver == null) {
            mIDriver = RetrofitHelper.create(IDriver.class);
        }

        mIDriver.driverAuth(body)
                .enqueue(new MsgCallBack<BaseBean<LoginBean>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<LoginBean>> call, Response<BaseBean<LoginBean>> response) {
                        LoginBean bean = response.body().getResult();
                        if (bean == null) {
                            return;
                        }
                        showToast("提交成功");
                        bean.setTenantId(mTenantId);
                        UserInfoHelper.getInstance().save(bean);
                        setResult(100, getIntent());
                        UserInfoHelper.getInstance().setDriverStatus(bean.getDriverStatus());
                        finish();
                    }
                });

        //VehicleAuthenticationActivity.start(getThis(), mDataBean, mIsToMain);

    }

    @OnClick(R.id.aty_vehicle_authentication_text_select_car_models)
    void OnSelectCarModels() {
        if (mCarModelList == null || mCarModelList.size() == 0) {
            showToast("当前没有可以选择的车型");
            return;
        }
        showSelectCarModelsDialog();
    }

    /**
     * 显示选择照片对话框
     */
    private void showPhotoDialog() {
        if (mBottomDialog == null) {
            mBottomDialog = new BottomDialog(getThis(), R.style.NoTitleDialog);
            mBottomDialog.addDataArray("相册", "相机");
            mBottomDialog.setOnItemClickListener(new BottomDialog.OnItemClickListener() {
                @Override
                public void onItemClick(BottomDialog dialog, int position) {
                    if (position == 0) {
                        // 进入相册
                        PhotographUtil.choosePicture(getThis());
                    } else if (position == 1) {
                        // 生成指定uri
                        mPhotoUri = PhotographUtil.createImageUri("certificate", getThis());
                        // 进入相机
                        boolean isSuccess = PhotographUtil.takeCamera(getThis(), mPhotoUri);
                        if (!isSuccess) {
                            showToast("没有sd卡目录");
                        }
                    }
                    dialog.dismiss();
                }
            });
        }
        mBottomDialog.show();
    }

    /**
     * 显示选择车型对话框
     */
    private void showSelectCarModelsDialog() {
        if (mSelectCarModelDialog == null) {
            mSelectCarModelDialog = new BottomDialog(getThis(), R.style.NoTitleDialog);
            mSelectCarModelDialog.setOnItemClickListener(new BottomDialog.OnItemClickListener() {
                @Override
                public void onItemClick(BottomDialog dialog, int position) {
                    mVtyCarMobile.setText(mCarModelList.get(position));
                }
            });
            mSelectCarModelDialog.addDataList(mCarModelList);
        }
        mSelectCarModelDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_skip, menu);

//        inflateMenu(R.menu.menu_skip);

       /* TextView textView = new TextView(this);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_medium));
        textView.setTextColor(ContextCompat.getColor(this, R.color.orange1));
        textView.setText("跳过");
        setActionView(menu.findItem(R.id.menu_skip), textView);*/

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_skip) {
            // 如果登录成功进入首页，否则回到登录页
            setResult(100, getIntent());
            finish();
            return true;
        }
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == PhotographUtil.CAMERA_REQUEST_CODE) {
            // 解析uri
            uriBitmap();
            // 在子线程中转换图片的操作
        } else if (requestCode == PhotographUtil.ALBUM_REQUEST_CODE) {
            mPhotoUri = data.getData();
            if (mPhotoUri == null) {
                showToast("获取图片失败，请重试");
                return;
            }
            uriBitmap();

        } else if (requestCode == SelectTaxiCompanyActivity.REQUEST_CODE) {
            String name = data.getStringExtra(SelectTaxiCompanyActivity.KEY_SELECT_NAME);
            mCompanyId = data.getIntExtra(SelectTaxiCompanyActivity.KEY_SELECT_ID, mCompanyId);
            mVtvTaxiCompany.setText(name);
        } else if (requestCode == SelectCarColorActivity.REQUEST_CODE) {
            String colorStr = data.getStringExtra(SelectCarColorActivity.KEY_SELECT_NAME);
            mVtyColor.setText(colorStr);
            mColorId = data.getIntExtra(SelectCarColorActivity.KEY_SELECT_ID, mColorId);
        } else if (requestCode == SelectCarBrandActivity.REQUEST_CODE) {
            String brandStr = data.getStringExtra(SelectCarBrandActivity.KEY_SELECT_NAME);
            mVtyBrand.setText(brandStr);
            mBrandId = data.getIntExtra(SelectCarBrandActivity.KEY_SELECT_ID, mBrandId);
        }
    }

    /**
     * 将uri转换成bitmap
     */
    private void uriBitmap() {
        showProgressDialog("正在处理图片，请稍候");
        ThreadSwitch.createTask(new ThreadSwitch.OnCreateListener<Bitmap>() {
            @Override
            public Bitmap onCreate(ThreadSwitch threadSwitch) {
                try {
                    return BitmapUtil.getUriBitmap(getThis(),
                            mPhotoUri,
                            Config.STORAGE_PATH,
                            Config.UPLOAD_IMAGE_NAME,
                            768,
                            432);
                } catch (IOException e) {
                    return null;
                }
            }
        })
                .addTaskListener(new ThreadSwitch.OnTaskListener<Bitmap, File>() {
                    @Override
                    public File onNext(ThreadSwitch threadSwitch, Bitmap value) {
                        mPhotoUri = null;
                        mPhotoBitmap = value;
                        //BitmapUtil.saveBitmap(Config.STORAGE_PATH, Config.UPLOAD_IMAGE_NAME, mPhotoBitmap, 100);

                        return new File(Config.STORAGE_PATH, Config.UPLOAD_IMAGE_NAME);
                    }
                })
                .switchLooper(ThreadSwitch.MAIN_THREAD)
                .submit(new ThreadSwitch.OnSubmitListener<File>() {
                    @Override
                    public void onSubmit(ThreadSwitch threadSwitch, File value) {
                        if (value == null || !value.exists()) {
                            closeProgressDialog();
                            showToast("获取图片失败，请重试");
                            return;
                        }
                        mUploadPictureHelper.removeAllUploadFile();
                        mUploadPictureHelper.addUploadFile(value);
                        mUploadPictureHelper.upload();

                    }
                });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BaiduLocationService.onRequestPermissionsResult(getThis(), requestCode, permissions, grantResults);
        boolean isSuccess = PhotographUtil.onRequestPermissionsResult(getThis(),
                requestCode,
                permissions,
                grantResults,
                mPhotoUri);

        if (!isSuccess) {
            showToast("无法获取拍摄权限");
        }
    }


    @Override
    protected void onDestroy() {
        if (mPhotoBitmap != null) {
            mPhotoBitmap.recycle();
        }

        BaiduLocationService.stop(getThis());
        super.onDestroy();
    }


    public static void start(Activity activity, String requestId) {
        start(activity, requestId, true);
    }


    /**
     * @param isToMain 返回的时候是否返回首页
     */
    public static void start(Activity activity, String requestId, boolean isToMain) {
        Intent starter = new Intent(activity, DriverAuthenticationActivity.class);
        starter.putExtra("requestId", requestId);
        starter.putExtra("isToMain", isToMain);
        activity.startActivityForResult(starter, REQUEST_CODE);
    }

    /**
     * @param fragment
     * @param isToMain 返回的时候是否返回首页
     */
    /*public static void start(Fragment fragment, String requestId, boolean isToMain) {
        Intent starter = new Intent(fragment.getContext(), DriverAuthenticationActivity.class);
        starter.putExtra("requestId", requestId);
        starter.putExtra("isToMain", isToMain);
        fragment.startActivityForResult(starter, REQUEST_CODE);
    }*/

    /**
     * @param dataBean          认证数据 包含 身份证、认证地区
     * @param plateNumberPrefix 车牌号前缀
     * @param isToMain          返回的时候是否返回首页
     */
    public static void start(Activity activity,
                             AuthenticationDataBean dataBean,
                             String plateNumberPrefix,
                             boolean isToMain) {
        Intent starter = new Intent(activity, DriverAuthenticationActivity.class);
        starter.putExtra("data", dataBean);
        starter.putExtra("isToMain", isToMain);
        starter.putExtra("plateNumberPrefix", plateNumberPrefix);
        activity.startActivityForResult(starter, REQUEST_CODE);
    }

    /**
     * @param dataBean          认证数据 包含 身份证、认证地区
     * @param deviceVehicleBean 车辆信息
     * @param plateNumberPrefix 车牌号前缀
     * @param isToMain          返回的时候是否返回首页
     */
    public static void start(Activity activity,
                             AuthenticationDataBean dataBean,
                             DeviceVehicleBean deviceVehicleBean,
                             String plateNumberPrefix,
                             boolean isToMain) {
        Intent starter = new Intent(activity, DriverAuthenticationActivity.class);
        starter.putExtra("data", dataBean);
        starter.putExtra("deviceVehicleData", deviceVehicleBean);
        starter.putExtra("isToMain", isToMain);
        starter.putExtra("plateNumberPrefix", plateNumberPrefix);
        activity.startActivityForResult(starter, REQUEST_CODE);

    }
}
