package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.AuthenticationDataBean;
import org.wzeiri.zr.zrtaxiplatform.bean.UploadResultBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.common.Config;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IDriver;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItem;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.util.BitmapUtil;
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
 * 车辆认证
 *
 * @author k-lm on 2017/11/17.
 */

public class VehicleAuthenticationActivity extends ActionbarActivity {

    @BindView(R.id.ty_vehicle_authentication_ved_car_number)
    ValueEditText mVedCarNumber;
    @BindView(R.id.aty_vehicle_authentication_vtv_brand)
    ValueTextView mVtyBrand;
    @BindView(R.id.ty_vehicle_authentication_vet_car_models)
    ValueEditText mVtyCarMobile;
    @BindView(R.id.ty_vehicle_authentication_vtv_color)
    ValueTextView mVtyColor;
    @BindView(R.id.aty_driver_authentication_vet_taxi_license_number)
    ValueEditText mVetTaxiLicenseNumber;
    @BindView(R.id.aty_driver_authentication_image_taxi_license)
    ImageView mImageTaxiLicense;


    private BottomDialog mBottomDialog;
    /**
     * 图片uri
     */
    private Uri mPhotoUri;

    private Bitmap mPhotoBitmap;

    private IDriver mIDriver;

    private int mColorId;

    private int mBrandId;

    private boolean mIsToMain = true;

    private AuthenticationDataBean mDataBean;

    private String mUploadName;

    private UploadPictureHelper mUploadPictureHelper;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_vehicle_authentication;
    }

    @Override
    protected void create() {
        super.create();
        // 如果没有设置intent 说明程序出现异常，则关闭页面
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }

        mIsToMain = intent.getBooleanExtra("isToMain", mIsToMain);
        mDataBean = intent.getParcelableExtra("data");
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("车辆认证");
        setNavigationIcon(null);
        setContentBackgroundResource(R.color.white);
        initView();

        mUploadPictureHelper = new UploadPictureHelper(this);
        mUploadPictureHelper.setOnUploadListener(new UploadPictureHelper.OnUploadListener() {
            @Override
            public void onUploadSuccess(@NonNull List<UploadResultBean> bean) {
                closeProgressDialog();
                mUploadName = bean.get(0).getFileName();
                mImageTaxiLicense.setImageBitmap(mPhotoBitmap);
            }

            @Override
            public void onUploadError(String msg) {
                closeProgressDialog();
            }
        });
    }


    private void initView() {
        if (mDataBean == null) {
            //mDataBean = new AuthenticationDataBean();
            return;
        }


        mVedCarNumber.setText(mDataBean.getPlateNumber());
        mVtyBrand.setText(mDataBean.getBrand());
        mVtyCarMobile.setText(mDataBean.getCarModel());
        mVtyColor.setText(mDataBean.getColor());
    }


    @OnClick(R.id.ty_vehicle_authentication_vtv_color)
    void onColorClick() {
        SelectCarColorActivity.start(getThis(), mColorId);
    }

    @OnClick(R.id.aty_vehicle_authentication_vtv_brand)
    void onBrandClick() {
        SelectCarBrandActivity.start(getThis(), mBrandId);
    }

    @OnClick(R.id.aty_driver_authentication_image_taxi_license)
    void onLicenseClick() {
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
                        mPhotoUri = PhotographUtil.createImageUri("license", getThis());
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


    @OnClick(R.id.aty_vehicle_authentication_text_submit)
    public void onmTextSubmitClicked() {
        final String plateNumber = mVedCarNumber.getText().toString().trim();
        String brand = mVtyBrand.getText().toString().trim();
        final String carModel = mVtyCarMobile.getText().toString().trim();
        String color = mVtyColor.getText().toString().trim();
        final String carLinsenceNumber = mVetTaxiLicenseNumber.getText().toString().trim();


        if (TextUtils.isEmpty(plateNumber.trim())) {
            showToast("请输入车牌号");
            return;
        }

        if (TextUtils.isEmpty(carModel)) {
            showToast("请选择车型");
            return;
        }

        if (TextUtils.isEmpty(brand)) {
            showToast("请选择车辆品牌");
            return;
        }

        if (TextUtils.isEmpty(color)) {
            showToast("请选择车辆颜色");
            return;
        }

        if (TextUtils.isEmpty(carLinsenceNumber)) {
            showToast("请输入运营证号");
            return;
        }

        if (mPhotoBitmap == null) {
            showToast("请设置运营证照片");
            return;
        }

        Intent intent = this.getIntent();
        if (intent == null) {
            showToast("认证失败，请重试");
            return;
        }


        final String idCardNum = mDataBean.getIdCardNumber();
        final String name = mDataBean.getName();
        final int tenantId = mDataBean.getTenantId();
        final String qualificationCertificate = mDataBean.getLisenceNumber();
        final String taxiCompany = mDataBean.getCompany();
        final String lisencePicture = mDataBean.getLisencePicture();

        showProgressDialog();

        RequestBody body = RetrofitHelper
                .getBody(new JsonItem("idNumber", idCardNum),
                        new JsonItem("name", name),
                        new JsonItem("tenentId", tenantId),
                        new JsonItem("lisenceNumber", qualificationCertificate),
                        new JsonItem("lisencePicture", lisencePicture),
                        new JsonItem("companyId", taxiCompany),
                        new JsonItem("plateNumber", plateNumber),
                        new JsonItem("brandId", mBrandId),
                        new JsonItem("carModel", carModel),
                        new JsonItem("colorId", mColorId),
                        new JsonItem("carLisenceNumber", carLinsenceNumber),
                        new JsonItem("carLisencePicture", mUploadName));

        if (mIDriver == null) {
            mIDriver = RetrofitHelper.create(IDriver.class);
        }

       /* mIDriver.driverAuth(body)
                .enqueue(new MsgCallBack<BaseBean<String>>(getThis(), false) {
                    @Override
                    public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                        VehicleAuthenticationActivity.this.closeProgressDialog();
                        setResult(RESULT_OK);
                        finish();

                    }
                });*/


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_skip, menu);

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
            setResult(RESULT_OK);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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

        }
        if (data == null) {
            return;
        }

        if (requestCode == SelectCarColorActivity.REQUEST_CODE) {
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
                        mPhotoUri = null;
                        if (value == null) {
                            closeProgressDialog();
                            showToast("获取图片失败，请重试");
                            return;
                        }
                        mUploadPictureHelper.addUploadFile(value);
                        mUploadPictureHelper.upload();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isSuccess = PhotographUtil.onRequestPermissionsResult(getThis(),
                requestCode,
                permissions,
                grantResults,
                mPhotoUri);

        if (!isSuccess) {
            showToast("无法获取拍摄权限");
        }
    }



    public static void start(Activity activity, AuthenticationDataBean dataBean, boolean isToMain) {
        Intent starter = new Intent(activity, VehicleAuthenticationActivity.class);
        starter.putExtra("data", dataBean);
        starter.putExtra("isToMain", isToMain);
        activity.startActivityForResult(starter, 100);
    }




}
