package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.LostFoundDetailBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.common.Config;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.ILostFound;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.util.CalculateUtil;
import org.wzeiri.zr.zrtaxiplatform.util.DetailInfoUtil;
import org.wzeiri.zr.zrtaxiplatform.util.GlideUtil;
import org.wzeiri.zr.zrtaxiplatform.util.StringUtil;
import org.wzeiri.zr.zrtaxiplatform.util.TimeUtil;
import org.wzeiri.zr.zrtaxiplatform.widget.CircleImageView;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 财务丢失
 *
 * @author k-lm on 2017/11/22.
 */

public class FinancialLossDetailActivity extends ActionbarActivity {


    @BindView(R.id.aty_financial_loss_detail_info_text_title)
    TextView mTextTitle;
    @BindView(R.id.aty_financial_loss_detail_info_vtv_state)
    ValueTextView mVtvState;
    @BindView(R.id.aty_financial_loss_detail_info_civ_avatar)
    CircleImageView mCivAvatar;
    @BindView(R.id.aty_financial_loss_detail_info_text_name)
    TextView mTextName;
    @BindView(R.id.aty_financial_loss_detail_info_vtv_money)
    ValueTextView mVtvMoney;
    @BindView(R.id.aty_financial_loss_detail_info_text_location)
    TextView mTextLocation;
    @BindView(R.id.aty_financial_loss_detail_info_text_by_car_date)
    TextView mTextByCarDate;
    @BindView(R.id.aty_financial_loss_detail_info_text_detail)
    TextView mTextDetail;
    @BindView(R.id.aty_financial_loss_detail_info_layout_image_layout)
    LinearLayout mLayoutImageLayout;
    @BindView(R.id.aty_financial_loss_detail_info_image_call_phone)
    ImageView mImagePhone;

    @BindView(R.id.aty_financial_loss_detail_info_text_by_plateNumber)
    TextView mPlateNumber;

    private String mPhone = "";

    private boolean mIsMyRelease = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_financial_loss_detail_info;
    }


    @OnClick(R.id.aty_financial_loss_detail_info_image_call_phone)
    void onCallPhone() {
        if (TextUtils.isEmpty(mPhone) || !StringUtil.isNumber(mPhone)) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mPhone));
        startActivity(intent);
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("详情");
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

        ILostFound lostFound = RetrofitHelper.create(ILostFound.class);
        lostFound.getLostFoundDetail(id)
                .enqueue(new MsgCallBack<BaseBean<LostFoundDetailBean>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<LostFoundDetailBean>> call, Response<BaseBean<LostFoundDetailBean>> response) {
                        LostFoundDetailBean bean = response.body().getResult();
                        if (bean == null) {
                            return;
                        }

                        mTextTitle.setText(bean.getTitle());
                        mVtvState.setTextLeft(bean.getLostFoundStatusName());
                        mVtvState.setText("发布时间" + TimeUtil.getServerDate(bean.getCreationTime()));
                        mTextName.setText(bean.getContactPerson());
                        mTextLocation.setText(getAddress(bean));
                        mTextByCarDate.setText(getTime(bean));
                        mTextDetail.setText(bean.getContent());
                        mPlateNumber.setText("车牌号" +bean.getPlateNumber());

                        String picture = bean.getPublishUserProfile();
                        if (TextUtils.isEmpty(picture)
                                || TextUtils.equals(picture, Config.NO_IMAGE_URL)) {
                            mCivAvatar.setImageResource(R.drawable.ic_default_avatar);
                        } else {
                            GlideUtil.loadPath(getThis(), mCivAvatar, picture);
                        }

                        mPhone = bean.getContact();

                        if (TextUtils.isEmpty(mPhone.trim())) {
                            mImagePhone.setVisibility(View.GONE);
                        } else {
                            mImagePhone.setVisibility(View.VISIBLE);
                        }
                        BigDecimal bigDecimal = bean.getTipAmount();
                        if (bigDecimal == null) {
                            mVtvMoney.setVisibility(View.GONE);
                        } else {
                            double money = bigDecimal.doubleValue();
                            if (money == 0 || mIsMyRelease) {
                                mVtvMoney.setVisibility(View.GONE);
                            } else if (money % 1 == 0) {
                                mVtvMoney.setVisibility(View.VISIBLE);
                                mVtvMoney.setTextLeft("赏金");
                                mVtvMoney.setText(((int) money) + "元");
                            } else {
                                mVtvMoney.setVisibility(View.VISIBLE);
                                mVtvMoney.setTextLeft("赏金");
                                mVtvMoney.setText(CalculateUtil.getFormatToString(money) + "元");
                            }
                        }


                        DetailInfoUtil.loadImages(mLayoutImageLayout, bean.getPictures());
                    }
                });

    }

    /**
     * 返回乘车时间
     *
     * @param bean
     * @return
     */
    private String getTime(LostFoundDetailBean bean) {
        if (bean == null) {
            return "";
        }
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm", Locale.US);
        String time = "";
        Date date = null;
        try {
            date = TimeUtil.mFormatServer.parse(bean.getBoardingTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        time += TimeUtil.getServerDate(date);
        time += "(大约" + formatTime.format(date) + ")";
        time += "→ ";

        try {
            date = TimeUtil.mFormatServer.parse(bean.getAlightingTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        time += "(" + formatTime.format(date) + "左右" + ")";

        return time;
    }

    /**
     * 返回地点信息
     *
     * @param bean
     * @return
     */
    private String getAddress(LostFoundDetailBean bean) {
        if (bean == null) {
            return "";
        }

        String address = "";

        address += bean.getBoardingPoint();
        address += "→ ";
        address += bean.getAlightingPoint();

        return address;

    }


    public static void start(Context context, int id, boolean isMyRelease) {
        Intent starter = new Intent(context, FinancialLossDetailActivity.class);
        starter.putExtra("id", id);
        starter.putExtra("isMyRelease", isMyRelease);
        context.startActivity(starter);
    }


}
