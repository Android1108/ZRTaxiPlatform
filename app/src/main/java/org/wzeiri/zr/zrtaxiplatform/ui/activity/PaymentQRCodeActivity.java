package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.GetUserPaypalPictureBean;
import org.wzeiri.zr.zrtaxiplatform.bean.UploadResultBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.common.Config;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IUser;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItem;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.AlipayFragment;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.MyFragment;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.WxpayFragment;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.base.TabFragmentPagerAdapter;
import org.wzeiri.zr.zrtaxiplatform.util.BitmapUtil;
import org.wzeiri.zr.zrtaxiplatform.util.GlideUtil;
import org.wzeiri.zr.zrtaxiplatform.util.LogUtil;
import org.wzeiri.zr.zrtaxiplatform.util.PhotographUtil;
import org.wzeiri.zr.zrtaxiplatform.util.ThreadSwitch;
import org.wzeiri.zr.zrtaxiplatform.util.UploadPictureHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 收付款
 *
 * @author k-lm on 2017/12/29.
 */

public class PaymentQRCodeActivity extends ActionbarActivity {

    private String mCodeUrl;

    private IUser mIUser;

    private UploadPictureHelper mUploadHelper;

//    @BindView(R.id.aty_payment_qr_code_image_code)
//    ImageView mImageQRCode;
//    @BindView(R.id.aty_payment_qr_code_text_hint)
//    TextView mTextQrCodeHint;

    @BindView(R.id.aty_payment_qr_code_rg_pay_fee_code)
    RadioGroup mRadioGroupPayFeeCode;
    @BindView(R.id.activity_payment_qr_code_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.activity_payment_qr_code_wx)
    TextView mWxPay;
    @BindView(R.id.activity_payment_qr_code_ali)
    TextView mAliPay;

    private List<Fragment> list;
    private TabFragmentPagerAdapter adapter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_payment_qr_code;
    }

//    @OnClick(R.id.aty_payment_qr_code_text_submit)
//    void onSelectQrCode() {
//        PhotographUtil.choosePicture(getThis());
//    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("收付款");
        list=new ArrayList<>();
        list.add(new WxpayFragment());
        list.add(new AlipayFragment());
        adapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), list);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0);
        showToast("左右滑动点击切换收款码");
    }
    @OnClick(R.id.activity_payment_qr_code_wx)
    void wxpay(){
        mViewPager.setCurrentItem(0);

    }
    @OnClick(R.id.activity_payment_qr_code_ali)
    void alipay(){
        mViewPager.setCurrentItem(1);
    }
//    @Override
//    protected void initData() {
//        super.initData();
//        mIUser = RetrofitHelper.create(IUser.class);
//
//        mIUser.getUserPaypalPicture().enqueue(new MsgCallBack<BaseBean<GetUserPaypalPictureBean>>(this,true) {
//            @Override
//            public void onSuccess(Call<BaseBean<GetUserPaypalPictureBean>> call, Response<BaseBean<GetUserPaypalPictureBean>> response) {
//                GetUserPaypalPictureBean  url = response.body().getResult();
//                    if (TextUtils.isEmpty(url.getAliPayPictureUrl())) {
//
//                    } else {
//                        GlideUtil.loadPath(getThis(), mImageQRCode, url.getAliPayPictureUrl());
//                    }
//            }
//        });
//    }
//
//    /**
//     * 将uri转换成bitmap
//     */
//    private void saveUri(final Uri uri) {
//        showProgressDialog("正在处理图片，请稍候");
//        ThreadSwitch.createTask(new ThreadSwitch.OnCreateListener<Bitmap>() {
//            @Override
//            public Bitmap onCreate(ThreadSwitch threadSwitch) {
//                try {
//                    return BitmapUtil.getUriBitmap(getThis(),
//                            uri,
//                            Config.STORAGE_PATH,
//                            Config.UPLOAD_IMAGE_NAME,
//                            768,
//                            432);
//                } catch (IOException e) {
//                    return null;
//                }
//            }
//        })
//                .addTaskListener(new ThreadSwitch.OnTaskListener<Bitmap, File>() {
//                    @Override
//                    public File onNext(ThreadSwitch threadSwitch, Bitmap value) {
//                        //BitmapUtil.saveBitmap(Config.STORAGE_PATH, Config.UPLOAD_IMAGE_NAME, mPhotoBitmap, 100);
//
//                        return new File(Config.STORAGE_PATH, Config.UPLOAD_IMAGE_NAME);
//                    }
//                })
//                .switchLooper(ThreadSwitch.MAIN_THREAD)
//                .submit(new ThreadSwitch.OnSubmitListener<File>() {
//                    @Override
//                    public void onSubmit(ThreadSwitch threadSwitch, File value) {
//                        if (value == null) {
//                            closeProgressDialog();
//                            showToast("获取图片失败，请重试");
//                            return;
//                        }
//                        uploadImage(value);
//
//                    }
//                });
//    }
//
//    /**
//     * 上传图片
//     *
//     * @param file
//     */
//    private void uploadImage(File file) {
//        if (mUploadHelper == null) {
//            mUploadHelper = new UploadPictureHelper(getThis());
//
//            mUploadHelper.setOnUploadListener(new UploadPictureHelper.OnUploadListener() {
//                @Override
//                public void onUploadSuccess(@NonNull List<UploadResultBean> beans) {
//                    UploadResultBean bean = beans.get(0);
//                    mCodeUrl = bean.getUrl();
//                    updateUserPicture(bean.getFileName());
//                }
//
//                @Override
//                public void onUploadError(String msg) {
//                    closeProgressDialog();
//                    showToast("处理图片失败，请重试");
//                }
//            });
//        }
//
//        mUploadHelper.uploadSingleFile(file);
//
//    }
//
//    /**
//     * 提交收付款图片
//     *
//     * @param fileName
//     */
//    private void updateUserPicture(String fileName) {
//        mIUser.updateUserPaypalPicture(RetrofitHelper
//                .getBody(new JsonItem("picture", fileName)))
//                .enqueue(new MsgCallBack<BaseBean<String>>(getThis(), true) {
//                    @Override
//                    public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
//                        PaymentQRCodeActivity.this.closeProgressDialog();
//                        showToast("修改收付款图片成功");
//                        GlideUtil.loadPath(getThis(), mImageQRCode, mCodeUrl);
//                    }
//                });
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode != RESULT_OK && data == null) {
//            return;
//        }
//
//        switch (requestCode) {
//            case PhotographUtil.ALBUM_REQUEST_CODE:
//                Uri codeUri = data.getData();
//                if (codeUri == null) {
//                    showToast("获取图片失败，请重试");
//                    break;
//                }
//                saveUri(codeUri);
//                break;
//        }
//    }
}
