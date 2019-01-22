package org.wzeiri.zr.zrtaxiplatform.ui.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
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
import org.wzeiri.zr.zrtaxiplatform.network.api.IAdverPostApply;
import org.wzeiri.zr.zrtaxiplatform.network.api.IDriver;
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
import org.wzeiri.zr.zrtaxiplatform.util.TimeUtil;
import org.wzeiri.zr.zrtaxiplatform.util.UploadPictureHelper;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;
import org.wzeiri.zr.zrtaxiplatform.widget.dialog.SelectDateDialog;
import org.wzeiri.zr.zrtaxiplatform.widget.flow.FlowLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTouch;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 发布张贴广告照片
 *
 * @author k-lm on 2017/11/23.
 */

public class ReleaseAdvertisementPhotoInfoActivity extends ActionbarActivity implements PhotoHelper.OnPhotoUpdateListener{
    /**
     * 发布的车辆id
     */
    public static final String KEY_RELEASE_CAR_ID = "carId";

    public static final int REQUEST_CODE = 30005;


    @BindView(R.id.aty_release_advertisement_photo_info_edit_describe)
    EditText mEditDescribe;
    @BindView(R.id.aty_release_advertisement_photo_info_fl_layout)
    FlowLayout mFlowLayout;
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
    @BindView(R.id.aty_release_advertisement_photo_info_vtv_date)
    ValueTextView mVtvDate;

    @BindView(R.id.bankCardOwner)
    TextView mBankCardOwner;

    @BindView(R.id.bankCardNo)
    TextView mBankCardNo;

    @BindView(R.id.bankName)
    TextView mBankName;

    @BindView(R.id.bankCardBindPhoneNumber)
    TextView mBankCardBindPhoneNumber;

    @BindView(R.id.Reference_resources)
    ImageView mReference_resources;

    private SelectDateDialog mDateDialog;

    private IAdverPostApply mIAdverPostApply;

    private PhotoHelper mPhotoHelper;

    private int mCurrentCarId = -1;

    private UploadPictureHelper mUploadHelper;

    private Bitmap mBitmap;

    private List<String> mImages = new ArrayList<>(3);

    private Date mSelectDate;
    Bitmap bp=null;
    float scaleWidth;
    float scaleHeight;
    boolean num=false;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_release_advertisement_photo_info;
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("上传广告张贴照片");
        Intent intent=getIntent();
        mBankCardOwner.setText(intent.getStringExtra("bankCardOwner"));
        mBankCardNo.setText(intent.getStringExtra("bankCardNo"));
        mBankName.setText(intent.getStringExtra("bankName"));
        mBankCardBindPhoneNumber.setText(intent.getStringExtra("bankCardBindPhoneNumber"));

        EditTextFormatUtil.formatRemoveEmoji(mEditDescribe);

        mPhotoHelper = new PhotoHelper(getThis(), mFlowLayout, 3, Config.STORAGE_PATH);
        mPhotoHelper.setOnPhotoUpdateListener(this);


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
                mImages.add(bean.getFileName());
            }

            @Override
            public void onUploadError(String msg) {
                showToast("图片处理失败");
                closeProgressDialog();
                //mPhotoHelper.removeLastPhoto();
            }
        });

        mPhotoHelper.setOnPhotoOperationListener(new PhotoHelper.OnPhotoOperationListener() {
            @Override
            public void onPhotoDelete(int position) {
                mImages.remove(position);
            }

            @Override
            public void onPhotoClick(int position, Bitmap bitmap) {

            }
        });

        if (!CarHelper.getInstance().isLoadCarInfo()) {
            loadCarInfo();
            return;
        }
        initCarInfo(CarHelper.getInstance().getCurrentCarInfo());
    }

    @Override
    protected void initData() {
        super.initData();
        mSelectDate = new Date();
        mVtvDate.setText(TimeUtil.getServerDate(mSelectDate));
        DisplayMetrics dm=new DisplayMetrics();//创建矩阵
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        bp= BitmapFactory.decodeResource(getResources(),R.drawable.picref);
        int width=bp.getWidth();
        int height=bp.getHeight();
        int w=dm.widthPixels; //得到屏幕的宽度
        int h=dm.heightPixels; //得到屏幕的高度
        scaleWidth=((float)w)/width;
        scaleHeight=((float)h)/height;
        mReference_resources.setImageBitmap(bp);

    }

  @OnClick(R.id.Reference_resources)
    void Image(){
               startActivity(ImageViewAcitvity.class);

//                if(num==true)        {
//                    Matrix matrix=new Matrix();
//                    matrix.postScale(scaleWidth,scaleHeight);
//
//                    Bitmap newBitmap=Bitmap.createBitmap(bp, 0, 0, bp.getWidth(), bp.getHeight(), matrix, true);
//                    mReference_resources.setImageBitmap(newBitmap);
//                    num=false;
//                }
//                else{
//                    Matrix matrix=new Matrix();
//                    matrix.postScale(1.0f,1.0f);
//                    Bitmap newBitmap=Bitmap.createBitmap(bp, 0, 0, bp.getWidth(), bp.getHeight(), matrix, true);
//                    mReference_resources.setImageBitmap(newBitmap);
//                    num=true;
//                }
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
        mCurrentCarId = carInfo.getId();
        GlideUtil.loadPath(getThis(), mImageCarLogo, carInfo.getBrandLogoPicture());
        mTextCarModel.setText(carInfo.getCarModel());
        mTextLicensePlateNumber.setText(carInfo.getPlateNumber());
        mTextTaxiCompany.setText(carInfo.getCompnay());
        mTextCurrentCar.setVisibility(View.VISIBLE);
    }

    private void showSelectDateDialog() {
        if (mDateDialog == null) {
            mDateDialog = new SelectDateDialog(getThis(), R.style.NoTitleDialog);
            mDateDialog.setPositiveButton_(null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mSelectDate = mDateDialog.getSelectDate();
                    mVtvDate.setText(TimeUtil.getServerDate(mSelectDate));
                    dialog.dismiss();
                }
            });
            mDateDialog.setNegativeButton_(null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        mDateDialog.show();
    }

    @OnClick(R.id.aty_release_advertisement_photo_info_vtv_date)
    void onSelectData() {
        showSelectDateDialog();
    }

    @OnClick(R.id.layout_release_info_text_release)
    void onReleaseClick() {
        final String describe = mEditDescribe.getText().toString();

        if (TextUtils.isEmpty(describe.trim())) {
            showToast("请输入描述信息");
            return;
        }


        if (mIAdverPostApply == null) {
            mIAdverPostApply = RetrofitHelper.create(IAdverPostApply.class);
        }
        sendAdverPostApply(describe);

    }

    /**
     * 提交张贴信息
     */
    private void sendAdverPostApply(String describe) {

        if (mIAdverPostApply == null) {
            mIAdverPostApply = RetrofitHelper.create(IAdverPostApply.class);
        }


        mIAdverPostApply.createAdverPostApply(RetrofitHelper
                .getBody(new JsonItemArray("adverPostApplyPictures", mImages),
                        new JsonItem("postTime", TimeUtil.getServiceTDate(mSelectDate)),
                        new JsonItem("carId", mCurrentCarId),
                        new JsonItem("describe", describe)))
                .enqueue(new MsgCallBack<BaseBean<String>>(getThis(), false) {
                    @Override
                    public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                        showToast("提交成功");
                        Intent intent = new Intent();
                        intent.putExtra(KEY_RELEASE_CAR_ID, mCurrentCarId);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });

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
    protected void onDestroy() {
        mPhotoHelper.onDestroy();
        super.onDestroy();
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
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch(event.getAction()){

            case MotionEvent.ACTION_DOWN:  //当屏幕检测到第一个触点按下之后就会触发到这个事件。
                if(num==true)        {
                    Matrix matrix=new Matrix();
                    matrix.postScale(scaleWidth,scaleHeight);

                    Bitmap newBitmap=Bitmap.createBitmap(bp, 0, 0, bp.getWidth(), bp.getHeight(), matrix, true);
                    mReference_resources.setImageBitmap(newBitmap);
                    num=false;
                }
                else{
                    Matrix matrix=new Matrix();
                    matrix.postScale(1.0f,1.0f);
                    Bitmap newBitmap=Bitmap.createBitmap(bp, 0, 0, bp.getWidth(), bp.getHeight(), matrix, true);
                    mReference_resources.setImageBitmap(newBitmap);
                    num=true;
                }
                break;
        }

        return super.onTouchEvent(event);
    }
}

