package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.MerchantDetailBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.common.Config;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IMerchant;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.WidgetActivity;
import org.wzeiri.zr.zrtaxiplatform.util.DensityUtil;
import org.wzeiri.zr.zrtaxiplatform.util.GlideUtil;
import org.wzeiri.zr.zrtaxiplatform.util.HtmlUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 司机特惠详情
 *
 * @author k-lm on 2017/11/22.
 */

public class DriverDiscountDetailActivity extends WidgetActivity {
    @BindView(R.id.aty_driver_discount_detail_image)
    ImageView mImage;
    @BindView(R.id.aty_driver_discount_detail_text_store_name)
    TextView mTextStoreName;
    @BindView(R.id.aty_driver_discount_detail_text_date)
    TextView mTextDate;
    @BindView(R.id.aty_driver_discount_detail_text_store_location)
    TextView mTextStoreLocation;
    @BindView(R.id.aty_driver_discount_detail_text_phone)
    TextView mTextPhone;
    @BindView(R.id.aty_driver_discount_detail_web_detail)
    WebView mWebDetail;
    @BindView(R.id.aty_driver_discount_detail_image_back)
    ImageView mImageBack;

    private String mPhone;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_driver_discount_detail;
    }


    @OnClick(R.id.aty_driver_discount_detail_image_back)
    void onBack() {
        onBackPressed();
    }

    @OnClick(R.id.aty_driver_discount_detail_text_phone)
    void onCallPhone() {
        if (TextUtils.isEmpty(mPhone)) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + mPhone));
        startActivity(intent);
    }

    @Override
    protected void init() {
        super.init();
        ViewGroup.LayoutParams params = mImage.getLayoutParams();
        params.width = DensityUtil.WINDOW_WIDTH;
        params.height = (int) (DensityUtil.WINDOW_WIDTH * Config.DRIVER_DISCOUNT_DETAIL_HEIGHT_WIDTH);


        FrameLayout.LayoutParams backParams = (FrameLayout.LayoutParams) mImageBack.getLayoutParams();
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            backParams.topMargin = getResources().getDimensionPixelSize(resourceId);
        }

    }


    @Override
    protected void initData() {
        super.initData();
        Intent intent = getIntent();
        if (intent == null) {
            showToast("没有找到商家信息");
            finish();
            return;
        }

        long id = intent.getLongExtra("id", -1);
        IMerchant merchant = RetrofitHelper.create(IMerchant.class);

        merchant.getMerchantDetail(id)
                .enqueue(new MsgCallBack<BaseBean<MerchantDetailBean>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<MerchantDetailBean>> call, Response<BaseBean<MerchantDetailBean>> response) {
                        MerchantDetailBean bean = response.body().getResult();

                        if (bean == null) {
                            showToast("无法获取服务器数据");
                            return;
                        }
                        mPhone = bean.getContact();
                        mTextStoreName.setText(bean.getName());
                        mTextDate.setText(bean.getMerchantFeature() + "|" + bean.getBusinessHours());
                        mTextStoreLocation.setText(bean.getAddress());
                        mTextPhone.setText(mPhone);
                        String data = bean.getDetail();
                        data = HtmlUtil.getImageStyle(data);
                        data = HtmlUtil.getBodyAdaptableScreenStyle(data);
                        mWebDetail.loadDataWithBaseURL(null, data, "text/html", "UTF-8", null);
                        GlideUtil.loadPath(getThis(), mImage, bean.getConverPicture());
                    }

                });

    }

    public static void start(Context context, long id) {
        Intent starter = new Intent(context, DriverDiscountDetailActivity.class);
        starter.putExtra("id", id);
        context.startActivity(starter);
    }

}
