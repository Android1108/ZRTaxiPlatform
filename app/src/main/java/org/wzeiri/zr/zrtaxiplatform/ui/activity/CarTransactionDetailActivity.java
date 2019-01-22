package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.VehicleTransactionDetailBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.ILostFound;
import org.wzeiri.zr.zrtaxiplatform.network.api.IVehicleTransaction;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.util.DetailInfoUtil;
import org.wzeiri.zr.zrtaxiplatform.util.GlideUtil;
import org.wzeiri.zr.zrtaxiplatform.util.HtmlUtil;
import org.wzeiri.zr.zrtaxiplatform.util.TimeUtil;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 车辆交易详情
 *
 * @author k-lm on 2017/11/22.
 */

public class CarTransactionDetailActivity extends ActionbarActivity {


    @BindView(R.id.aty_car_transaction_detail_info_text_title)
    TextView mTextTitle;
    @BindView(R.id.aty_car_transaction_detail_info_vtv_time)
    ValueTextView mVtvTime;
    @BindView(R.id.aty_car_transaction_detail_info_text_name)
    TextView mTextName;
    @BindView(R.id.aty_car_transaction_detail_info_text_location)
    TextView mTextLocation;
    /* @BindView(R.id.aty_car_transaction_detail_info_text_detail)
     TextView mTextDetail;*/
   /* @BindView(R.id.aty_car_transaction_detail_info_vp_image_pager)
    ViewPager mVpImagePager;*/
/*@BindView(R.id.aty_car_transaction_detail_info_text_pager)
    TextView mTextPager;*/
    @BindView(R.id.aty_car_transaction_detail_info_web_detail)
    WebView mWebView;

    @BindView(R.id.aty_car_transaction_detail_info_image_phone)
    ImageView mImageCallPhone;


    private long mUserId = -1;
    private String mName = "";

    private String mPhone;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_car_transaction_detail_info;
    }


    @Override
    protected void init() {
        super.init();
        setCenterTitle("详情");

        WebSettings settings = mWebView.getSettings();
        settings.setBlockNetworkImage(false);
        settings.setDomStorageEnabled(true);
        settings.setDefaultTextEncodingName("utf-8");
    }

    @Override
    protected void initData() {
        super.initData();

        Intent intent = getIntent();
        if (intent == null) {
            finish();
            showToast("获取数据失败，请重试");
            return;
        }

        int id = intent.getIntExtra("id", -1);

        IVehicleTransaction vehicleTransaction = RetrofitHelper.create(IVehicleTransaction.class);
        vehicleTransaction.getVehicleTransactionDetail(id)
                .enqueue(new MsgCallBack<BaseBean<VehicleTransactionDetailBean>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<VehicleTransactionDetailBean>> call, Response<BaseBean<VehicleTransactionDetailBean>> response) {
                        VehicleTransactionDetailBean bean = response.body().getResult();
                        if (bean == null) {
                            return;
                        }
                        mUserId = bean.getPublishUserId();
                        mName = bean.getContactUserName();
                        mTextTitle.setText(bean.getTitle());
                        mVtvTime.setTextLeft("发布时间 ");
                        mVtvTime.setText(TimeUtil.getServerDate(bean.getCreationTime()));
                        mTextName.setText(mName);

                        String htmlStr = bean.getContent();
                        htmlStr = HtmlUtil.getImageStyle(htmlStr);
                        mWebView.loadDataWithBaseURL(null,
                                htmlStr,
                                "text/html",
                                "utf-8",
                                null);

                        mTextLocation.setText(bean.getAddress());
                        mPhone = bean.getContactPhone();

                        if (TextUtils.isEmpty(mPhone)) {
                            mImageCallPhone.setVisibility(ViewPager.GONE);
                        } else {
                            mImageCallPhone.setVisibility(View.VISIBLE);
                        }


                    }
                });

    }


    public static void start(Context context, int id) {
        Intent starter = new Intent(context, CarTransactionDetailActivity.class);
        starter.putExtra("id", id);
        context.startActivity(starter);
    }


    @OnClick(R.id.aty_car_transaction_detail_info_image_phone)
    void onCallPhone() {
        if (TextUtils.isEmpty(mPhone)) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + mPhone));
        startActivity(intent);
    }

    @OnClick(R.id.aty_car_transaction_detail_info_text_name)
    public void onViewClicked() {
//        UserInfoDetailActivity.start(getThis(), mUserId, mName);
    }
}
