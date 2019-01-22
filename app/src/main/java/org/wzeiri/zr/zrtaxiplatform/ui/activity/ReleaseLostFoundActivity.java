package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.HomeIndexBean;
import org.wzeiri.zr.zrtaxiplatform.bean.UploadResultBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.common.Config;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.ILostFound;
import org.wzeiri.zr.zrtaxiplatform.network.api.IUser;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItem;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItemArray;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.util.BitmapUtil;
import org.wzeiri.zr.zrtaxiplatform.util.EditTextFormatUtil;
import org.wzeiri.zr.zrtaxiplatform.util.PhotoHelper;
import org.wzeiri.zr.zrtaxiplatform.util.ThreadSwitch;
import org.wzeiri.zr.zrtaxiplatform.util.TimeUtil;
import org.wzeiri.zr.zrtaxiplatform.util.UploadPictureHelper;
import org.wzeiri.zr.zrtaxiplatform.util.UserInfoHelper;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueEditText;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;
import org.wzeiri.zr.zrtaxiplatform.widget.dialog.SelectDateDialog;
import org.wzeiri.zr.zrtaxiplatform.widget.dialog.SelectTimeDialog;
import org.wzeiri.zr.zrtaxiplatform.widget.flow.FlowLayout;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 发布失物招领
 *
 * @author k-lm on 2017/11/20.
 */

public class ReleaseLostFoundActivity extends ActionbarActivity implements PhotoHelper.OnPhotoUpdateListener {
    /**
     * 发布失物招领
     */
    public static final int REQUEST_CODE = 30003;

    @BindView(R.id.layout_release_info_edit_title)
    EditText mEditTitle;
    @BindView(R.id.layout_release_info_edit_content)
    EditText mEditContent;
    @BindView(R.id.layout_release_info_fl_layout)
    FlowLayout mFlowLayout;
    @BindView(R.id.aty_release_lost_found_info_vtv_find_date)
    ValueTextView mVtvFindDate;
    @BindView(R.id.aty_release_lost_found_info_vet_by_car_address)
    ValueEditText mVetByCarAddress;
    @BindView(R.id.aty_release_lost_found_info_vet_by_car_time)
    ValueTextView mVetByCarTime;
    @BindView(R.id.aty_release_lost_found_info_vet_get_out_car_address)
    ValueEditText mVetGetOutCarAddress;
    @BindView(R.id.aty_release_lost_found_info_vet_get_out_car_time)
    ValueTextView mVetGetOutCarTime;
    @BindView(R.id.aty_release_lost_found_info_vet_get_out_car_plateNumber)
    ValueTextView mPlateNumber;


    private PhotoHelper mPhotoHelper;

    private SelectDateDialog mSelectDateDialog;

    private Date mFindDate, mByCardDate, mAlightingDate;

    private ILostFound mILostFound;

    private Bitmap mBitmap;
    private UploadPictureHelper mUploadHelper;

    private SelectTimeDialog mByCarTimeDialog;

    private SelectTimeDialog mAlightingTimeDialog;


    private List<String> mImages = new ArrayList<>(3);

    @OnClick(R.id.aty_release_lost_found_info_vet_by_car_time)
    void selectByCarTime() {
        showByCarTimeDialog();
    }


    @OnClick(R.id.aty_release_lost_found_info_vet_get_out_car_time)
    void selectAlightingTime() {
        showAlightingTimeDialog();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_release_lost_found_info;
    }


    @Override
    protected void init() {
        super.init();
        setCenterTitle("发布失物招领");
        mEditTitle.setHint("标题 失物的名称");
        mEditContent.setHint("描述一下失物");
        mPhotoHelper = new PhotoHelper(this, mFlowLayout, 3, Config.STORAGE_PATH);
        mPhotoHelper.setOnPhotoUpdateListener(this);
        EditTextFormatUtil.formatRemoveEmoji(mEditTitle);
        EditTextFormatUtil.formatRemoveEmoji(mEditContent);
        EditTextFormatUtil.formatRemoveEmoji(mVetByCarAddress);
        //EditTextFormatUtil.formatRemoveEmoji(mVetByCarTime);
        EditTextFormatUtil.formatRemoveEmoji(mVetGetOutCarAddress);
        //EditTextFormatUtil.formatRemoveEmoji(mVetGetOutCarTime);
        setDate(new Date());
        mPlateNumber.setText(UserInfoHelper.getInstance().getCarLisenceNumber().toString());
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
    }

    /**
     * 设置时间
     *
     * @param date
     */
    private void setDate(Date date) {
        if (date == null) {
            return;
        }

        if (mAlightingTimeDialog != null) {
            mAlightingTimeDialog.setDate(date);
        }

        if (mByCarTimeDialog != null) {
            mByCarTimeDialog.setDate(date);
        }

        mFindDate = date;
        String dateStr = TimeUtil.getServerDate(mFindDate);
        mVtvFindDate.setText(dateStr);
    }

    @OnClick(R.id.aty_release_lost_found_info_vtv_find_date)
    void onSelectDate() {
        showDialog();
    }

    @OnClick(R.id.layout_release_info_text_release)
    public void onViewClicked() {
        String title = mEditTitle.getText().toString();
        String content = mEditContent.getText().toString();
        int count = mPhotoHelper.getImageSize();
        String findDate = mVtvFindDate.getText().toString();
        String boardingPoint = mVetByCarAddress.getText().toString();
        String boardingTime = mVetByCarTime.getText().toString();
        String alightingPoint = mVetGetOutCarAddress.getText().toString();
        String alightingTimeStr = mVetGetOutCarTime.getText().toString();
        Log.d("T","------->"+UserInfoHelper.getInstance().getCarLisenceNumber());
        if (TextUtils.isEmpty(title)) {
            showToast("请输入标题");
            return;
        }
        if (TextUtils.isEmpty(content)) {
            showToast("请输入内容");
            return;
        }

       /* if (count == 0) {

            return;
        }*/

        if (TextUtils.isEmpty(findDate)) {
            showToast("请选择发现日期");
            return;
        }
        if (TextUtils.isEmpty(boardingPoint)) {
            showToast("请填写上车地点");
            return;
        }

        if (TextUtils.isEmpty(boardingTime)) {
            showToast("请填写上车时间");
            return;
        }

        if (TextUtils.isEmpty(alightingPoint)) {
            showToast("请填写下车地点");
            return;
        }

        if (TextUtils.isEmpty(alightingTimeStr)) {
            showToast("请填写下车时间");
            return;
        }

        long byCardTime = mByCardDate.getTime();
        long alightingTime = mAlightingDate.getTime();

        if (byCardTime > alightingTime) {
            showToast("下车时间不能小于乘车时间");
            return;
        }

        send();
    }

    /**
     * 图片base64
     */
    private void send() {

        showProgressDialog("正在提交数据，请稍候");
        String title = mEditTitle.getText().toString();
        String content = mEditContent.getText().toString();

        String boardingPoint = mVetByCarAddress.getText().toString();

        String alightingPoint = mVetGetOutCarAddress.getText().toString();

        if (mILostFound == null) {
            mILostFound = RetrofitHelper.create(ILostFound.class);
        }

        String alightingTime = TimeUtil.getServiceTDate(mAlightingDate);
        String boardingTime = TimeUtil.getServiceTDate(mByCardDate);
        String findDate = TimeUtil.getServiceTDate(mFindDate);
         String PlateNumber=mPlateNumber.getText().toString();

        JsonItemArray jsonItemArray = new JsonItemArray("pictures", mImages);

        mILostFound.sendCreateLostFound(RetrofitHelper.getBody(jsonItemArray,
                new JsonItem("lostFoundType", 2),
                new JsonItem("title", title),
                new JsonItem("content", content),
                new JsonItem("contact", UserInfoHelper.getInstance().getPhone()),
                new JsonItem("ridingTime", findDate),
                new JsonItem("boardingPoint", boardingPoint),
                new JsonItem("boardingTime", boardingTime),
                new JsonItem("alightingPoint", alightingPoint),
                new JsonItem("alightingTime", alightingTime),
                new JsonItem("contactPerson", UserInfoHelper.getInstance().getName()),
                new JsonItem("plateNumber",PlateNumber)
                ))
                .enqueue(new MsgCallBack<BaseBean<String>>(this, false) {
                    @Override
                    public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                        showToast("发布成功");
                        closeProgressDialog();
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onError(Call<BaseBean<String>> call, Throwable t) {
                        super.onError(call, t);
                        closeProgressDialog();
                    }
                });
    }

    /**
     * 显示选择时间对话框
     */
    private void showDialog() {
        if (mSelectDateDialog == null) {
            mSelectDateDialog = new SelectDateDialog(this);
            mSelectDateDialog.setPositiveButton_(null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    setDate(mSelectDateDialog.getSelectDate());
                    dialog.dismiss();

                }
            })
                    .setNegativeButton_(null, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        }


        mSelectDateDialog.show();
    }

    /**
     * 显示上车时间对话框
     */
    public void showByCarTimeDialog() {
        if (mByCarTimeDialog == null) {
            mByCarTimeDialog = new SelectTimeDialog(getThis());
            mByCarTimeDialog.setDate(mFindDate);

            mByCarTimeDialog.setPositiveButton_(null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String time = mByCarTimeDialog.getSelectHourToString() + ":" + mByCarTimeDialog.getSelectMinuteToString();
                    mVetByCarTime.setText(time);
                    mByCardDate = mByCarTimeDialog.getSelectDate();
                    dialog.dismiss();

                }
            })
                    .setNegativeButton_(null, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        }
        mByCarTimeDialog.show();


    }

    /**
     * 显示下车时间对话框
     */
    public void showAlightingTimeDialog() {
        if (mAlightingTimeDialog == null) {
            mAlightingTimeDialog = new SelectTimeDialog(getThis());
            mAlightingTimeDialog.setDate(mFindDate);

            mAlightingTimeDialog.setPositiveButton_(null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String time = mAlightingTimeDialog.getSelectHourToString() + ":" + mAlightingTimeDialog.getSelectMinuteToString();
                    mVetGetOutCarTime.setText(time);
                    mAlightingDate = mAlightingTimeDialog.getSelectDate();
                    dialog.dismiss();
                }
            })
                    .setNegativeButton_(null, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        }
        mAlightingTimeDialog.show();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPhotoHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPhotoHelper.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        mPhotoHelper.onDestroy();
        super.onDestroy();

    }


}
