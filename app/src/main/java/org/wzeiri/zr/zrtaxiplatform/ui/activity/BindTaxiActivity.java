package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.UploadResultBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.common.Config;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IDriver;
import org.wzeiri.zr.zrtaxiplatform.network.api.ISundry;
import org.wzeiri.zr.zrtaxiplatform.network.api.ITransport;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItem;
import org.wzeiri.zr.zrtaxiplatform.network.callback.BaseCallBack;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.util.BitmapUtil;
import org.wzeiri.zr.zrtaxiplatform.util.EditTextFormatUtil;
import org.wzeiri.zr.zrtaxiplatform.util.PhotographUtil;
import org.wzeiri.zr.zrtaxiplatform.util.ThreadSwitch;
import org.wzeiri.zr.zrtaxiplatform.util.UploadPictureHelper;
import org.wzeiri.zr.zrtaxiplatform.util.UserInfoHelper;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueEditText;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;
import org.wzeiri.zr.zrtaxiplatform.widget.dialog.BottomDialog;
import org.wzeiri.zr.zrtaxiplatform.widget.dialog.SendMessageCodeDialog;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author k-lm on 2017/11/24.
 */

public class BindTaxiActivity extends ActionbarActivity {
    @BindView(R.id.aty_vehicle_authentication_ved_car_number)
    ValueEditText mVedCarNumber;
    @BindView(R.id.aty_vehicle_authentication_vtv_brand)
    ValueTextView mVtvBrand;
    @BindView(R.id.aty_vehicle_authentication_vet_car_models)
    ValueEditText mVtvCarModels;
    @BindView(R.id.aty_vehicle_authentication_vtv_color)
    ValueTextView mVtvColor;
    @BindView(R.id.aty_bind_taxi_text_submit)
    TextView mTextSubmit;

    @BindView(R.id.aty_vehicle_authentication_vet_taxi_license_number)
    ValueEditText mVetTaxiLicenseNumber;
    @BindView(R.id.aty_vehicle_authentication_image_taxi_license)
    ImageView mImageTaxiLicense;

    @BindView(R.id.aty_vehicle_authentication_vtv_taxi_company)
    ValueTextView mVtvTaxiCompany;

    private int mSelectColorId = -1;
    private int mSelectBrandId = -1;

    private IDriver mIDriver=RetrofitHelper.create(IDriver.class);

    private String mImagePicture = "";

    private Bitmap mBitmap;

    private UploadPictureHelper mUploadPictureHelper;

    private BottomDialog mPhotoDialog;

    private Uri mUri;

    private String mPhotoName = "license.jpg";

    private int mTaxiCompanyId = -1;


    /**
     * 车型
     */
    private List<String> mCarModelList;

    /**
     * 选择车型对话框
     */
    private BottomDialog mSelectCarModelDialog;


    /**
     * 发送验证码对话框
     */
    private SendMessageCodeDialog mSendMessageCodeDialog;

    private ITransport mTransport;

    private boolean isSendCode = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bind_taxi;
    }


    @Override
    protected void init() {
        super.init();
        setCenterTitle("绑定新出租车");
        EditTextFormatUtil.formatNumberCapitalLetter(mVedCarNumber);
        String plateNumberPrefix = UserInfoHelper.getInstance().getPlateNumberPrefix() + " ";
        mVedCarNumber.setTextLeft(plateNumberPrefix);
        mUploadPictureHelper = new UploadPictureHelper(getThis());
        mUploadPictureHelper.setOnUploadListener(new UploadPictureHelper.OnUploadListener() {
            @Override
            public void onUploadSuccess(@NonNull List<UploadResultBean> beans) {
                closeProgressDialog();
                UploadResultBean bean = beans.get(0);
                if (bean == null) {
                    return;
                }
                mImagePicture = bean.getFileName();
                mImageTaxiLicense.setImageBitmap(mBitmap);

            }

            @Override
            public void onUploadError(String msg) {
                closeProgressDialog();
            }
        });
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


    @OnClick(R.id.aty_vehicle_authentication_vtv_taxi_company)
    void onSelectCompany() {
        SelectTaxiCompanyActivity.start(getThis(),
                mTaxiCompanyId,
                UserInfoHelper.getInstance().getTenantId());
    }

    @OnClick(R.id.aty_vehicle_authentication_image_taxi_license)
    void selectImage() {
        showBottomDialog();
    }

    @OnClick(R.id.aty_vehicle_authentication_vtv_brand)
    public void onmVtvBrandClicked() {
        SelectCarBrandActivity.start(getThis(), mSelectBrandId);
    }


    @OnClick(R.id.aty_vehicle_authentication_vtv_color)
    public void onmVtvColorClicked() {
        SelectCarColorActivity.start(getThis(), mSelectColorId);
    }

    @OnClick(R.id.aty_bind_taxi_text_submit)
    public void onAtyBindTaxiTextSubmitClicked() {
        String carNumber = mVedCarNumber.getText().toString().trim();
        String carModels = mVtvCarModels.getText().toString().trim();
        String carBrand = mVtvBrand.getText().toString().trim();
        String carColor = mVtvColor.getText().toString().trim();
        String licenseNumber = mVetTaxiLicenseNumber.getText().toString().trim();

        if (TextUtils.isEmpty(carNumber)) {
            showToast("请输入车牌号");
            return;
        }

        if (carNumber.length() < 5) {
            showToast("请输入正确的车牌号");
            return;
        }

        if (TextUtils.isEmpty(carBrand) || mSelectBrandId == -1) {
            showToast("请选择车辆品牌");
            return;
        }

        if (TextUtils.isEmpty(carModels.trim())) {
            showToast("请输入车型");
            return;
        }

        if (TextUtils.isEmpty(carColor) || mSelectColorId == -1) {
            showToast("请选择车辆颜色");
            return;
        }

      /*  if (TextUtils.isEmpty(licenseNumber)) {
            showToast("请输入营运证号");
            return;
        }*/

//        if (mBitmap == null || TextUtils.isEmpty(mImagePicture)) {
//            showToast("请设置运营证照片");
//            return;
//        }

        if (!isSendCode) {
            sendMessageCode();
        } else {
            showSendMessageDialog();
        }


    }

    /**
     * 提交数据
     */
    private void submitData(String code) {
        String carNumber = mVedCarNumber.getText().toString().trim();
        String carModels = mVtvCarModels.getText().toString().trim();
        carNumber = mVedCarNumber.getTextLeft().trim() + carNumber;
        String licenseNumber = mVetTaxiLicenseNumber.getText().toString().trim();
        String verificationCode=code;

        mIDriver.bindingCar(RetrofitHelper
                .getBody(new JsonItem("plateNumber", carNumber),
                        new JsonItem("brandId", mSelectBrandId),
                        new JsonItem("carModel", carModels),
                        new JsonItem("colorId", mSelectColorId),
                        new JsonItem("lisenceNumber", licenseNumber),
                        new JsonItem("lisencePicture", mImagePicture),
                        new JsonItem("verificationCode",verificationCode)))
                .enqueue(new MsgCallBack<BaseBean<String>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                        showToast("绑定成功");
                        setResult(RESULT_OK, getIntent());
                        finish();
                    }
                });
    }

    @OnClick(R.id.aty_vehicle_authentication_text_select_car_models)
    void OnSelectCarModels() {
        if (mCarModelList == null || mCarModelList.size() == 0) {
            showToast("当前没有可以选择的车型");
            return;
        }
        showSelectCarModelsDialog();
    }

    private void showBottomDialog() {
        if (mPhotoDialog == null) {
            mPhotoDialog = new BottomDialog(getThis(), R.style.NoTitleDialog);
            mPhotoDialog.addDataArray("相册", "相机");

            mPhotoDialog.setOnItemClickListener(new BottomDialog.OnItemClickListener() {
                @Override
                public void onItemClick(BottomDialog dialog, int position) {
                    if (position == 0) {
                        PhotographUtil.choosePicture(getThis());
                    } else {
                        mUri = PhotographUtil.createImageUri(mPhotoName, getThis());
                        // 进入相机
                        boolean isSuccess = PhotographUtil.takeCamera(getThis(), mUri);

                        if (!isSuccess) {
                            showToast("没有sd卡目录");
                        }
                    }
                    dialog.dismiss();
                }
            });
        }
        mPhotoDialog.show();
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
                    mVtvCarModels.setText(mCarModelList.get(position));
                }
            });
            mSelectCarModelDialog.addDataList(mCarModelList);
        }
        mSelectCarModelDialog.show();
    }

    /**
     * 显示发送验证码对话框
     */
    private void showSendMessageDialog() {
        if (mSendMessageCodeDialog == null) {
            mSendMessageCodeDialog = new SendMessageCodeDialog(getThis(), R.style.NoTitleDialog);

            mSendMessageCodeDialog.setPositiveButton_(null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String code=mSendMessageCodeDialog.getMessageCode();
                    if ("".equals(code)||code==null)
                    {
                        showToast("验证码不能为空");
                    }
                    else
                    {
                        queryCard7Info(code);
                        dialog.dismiss();
                    }
                }
            });
            mSendMessageCodeDialog.setPositiveButton_1(null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    queryCard7Info(" ");
                    dialogInterface.dismiss();
                }
            });
            mSendMessageCodeDialog.setOnClickSendMessageCodeListener(new SendMessageCodeDialog.OnClickSendMessageCodeListener() {
                @Override
                public void onSendMessage() {
                    sendMessageCode();
                }
            });

            mSendMessageCodeDialog.setCanceledOnTouchOutside(false);
        }

        mSendMessageCodeDialog.show();

    }

    /**
     * 查询司机卡信息
     *
     * @param code
     */
    private void queryCard7Info(String code) {
        if (mTransport == null) {
            mTransport = RetrofitHelper.create(ITransport.class);
        }
        showProgressDialog();
            mTransport.queryCard7Info(code)
                    .enqueue(new BaseCallBack<BaseBean<String>>() {


                        @Override
                        public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                            closeProgressDialog();
                            Log.d("A","------>"+response.body().getResult());
                            submitData(response.body().getResult());
                        }

                        @Override
                        public void onError(Call<BaseBean<String>> call, Throwable t) {
                            // 没有获取到运管数据
                            closeProgressDialog();
                            submitData("");
                        }
                    });

    }

    /**
     * 发送短信验证码
     */
    public void sendMessageCode() {
        if (mTransport == null) {
            mTransport = RetrofitHelper.create(ITransport.class);
        }

        showProgressDialog();
        mTransport.sendCard7InfoSendSms()
                .enqueue(new BaseCallBack<BaseBean<String>>() {
                    @Override
                    public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                        closeProgressDialog();
                        if (isSendCode) {
                            showToast("消息发送成功");
                        }
                        String result = response.body().getResult().toString();
                        if (null == result || "".equals(result)) {
                            submitData("");
                        } else{
                            isSendCode = true;
                            showSendMessageDialog();
                            mSendMessageCodeDialog.startTime();
                        }
                    }

                    @Override
                    public void onError(Call<BaseBean<String>> call, Throwable t) {
                        // 无法发送验证码
                        closeProgressDialog();
                        submitData("");
                    }
                });
    }

    /**
     * 将uri转换成bitmap
     */
    private void uriToBitmap() {
        showProgressDialog("正在处理图片，请稍候");
        ThreadSwitch.createTask(new ThreadSwitch.OnCreateListener<Bitmap>() {
            @Override
            public Bitmap onCreate(ThreadSwitch threadSwitch) {
                try {
                    return BitmapUtil.getUriBitmap(getThis(),
                            mUri,
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
                        mUri = null;
                        mBitmap = value;
                        //BitmapUtil.saveBitmap(Config.STORAGE_PATH, Config.UPLOAD_IMAGE_NAME, mPhotoBitmap, 100);

                        return new File(Config.STORAGE_PATH, Config.UPLOAD_IMAGE_NAME);
                    }
                })
                .switchLooper(ThreadSwitch.MAIN_THREAD)
                .submit(new ThreadSwitch.OnSubmitListener<File>() {
                    @Override
                    public void onSubmit(ThreadSwitch threadSwitch, File value) {
                        mUri = null;
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
                mUri);

        if (!isSuccess) {
            showToast("无法获取拍摄权限");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case SelectCarColorActivity.REQUEST_CODE:
                mSelectColorId = data.getIntExtra(SelectCarColorActivity.KEY_SELECT_ID, mSelectColorId);
                String color = data.getStringExtra(SelectCarColorActivity.KEY_SELECT_NAME);
                mVtvColor.setText(color);
                break;
            case SelectCarBrandActivity.REQUEST_CODE:
                mSelectBrandId = data.getIntExtra(SelectCarBrandActivity.KEY_SELECT_ID, mSelectColorId);
                String brand = data.getStringExtra(SelectCarColorActivity.KEY_SELECT_NAME);
                mVtvBrand.setText(brand);
                break;
            case PhotographUtil.ALBUM_REQUEST_CODE:
                mUri = data.getData();
            case PhotographUtil.CAMERA_REQUEST_CODE:
                if (mUri == null) {
                    showToast("获取图片失败，请重试");
                    break;
                }
                uriToBitmap();
                break;
            case SelectTaxiCompanyActivity.REQUEST_CODE:
                if (data == null) {
                    break;
                }
                mTaxiCompanyId = data.getIntExtra(SelectTaxiCompanyActivity.KEY_SELECT_ID, mTaxiCompanyId);
                String companyName = data.getStringExtra(SelectTaxiCompanyActivity.KEY_SELECT_NAME);
                mVtvTaxiCompany.setText(companyName);

                break;
        }

    }
}
