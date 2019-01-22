package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.ALiPayBean;
import org.wzeiri.zr.zrtaxiplatform.bean.OilCardBean;
import org.wzeiri.zr.zrtaxiplatform.bean.OilRechargeTempBean;
import org.wzeiri.zr.zrtaxiplatform.bean.RechargeSelectBean;
import org.wzeiri.zr.zrtaxiplatform.bean.WeChatPayBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;
import org.wzeiri.zr.zrtaxiplatform.common.Config;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IOilCard;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItem;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.util.CalculateUtil;
import org.wzeiri.zr.zrtaxiplatform.util.PayUtil;
import org.wzeiri.zr.zrtaxiplatform.util.RefuelCardHelper;
import org.wzeiri.zr.zrtaxiplatform.widget.DrawTextTextView;
import org.wzeiri.zr.zrtaxiplatform.widget.flow.RadioFlowLayout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 加油卡充值
 *
 * @author k-lm on 2017/11/27.
 */

public class RefuelingCardRechargeActivity extends ActionbarActivity {

    @BindView(R.id.aty_refueling_card_recharge_fl_recharge)
    RadioFlowLayout mRadioFlowLayout;

    @BindView(R.id.aty_refueling_card_recharge_text_type)
    TextView mCardType;

    @BindView(R.id.aty_refueling_card_recharge_text_card_number)
    TextView mCardNumber;

    @BindView(R.id.aty_refueling_card_recharge_rg_pay)
    RadioGroup mPayLayout;

    /**
     * 加油卡类型id
     */
    private int mTypeId = -1;
    /**
     * 加油卡id
     */
    private int mOilCardId = -1;

    /**
     * 加油卡分类
     */
    List<OilRechargeTempBean> mLegalAdviceTypeBeanList = new ArrayList<>();


    private IOilCard mIOilCard;
    /**
     * 选择的油卡id
     */
    private int mSelectId = -1;
    /**
     * 支付类型
     */
    private int mPayTypeId = -1;

    private IWXAPI mIwxapi;

    private OilCardBean mOilCardBean;

    @OnClick(R.id.aty_refueling_card_recharge_layout_refueling_card)
    void selectCar() {
        startActivityForResult(SelectRefuelingCard.class, SelectRefuelingCard.REQUEST_CODE);
    }


    @OnClick(R.id.aty_refueling_card_recharge_text_play)
    void onPlay() {
        if (mOilCardId < 0) {
            showToast("请选择需要充值的加油卡");
            return;
        }
        if (mSelectId < 0) {
            showToast("请选择加油卡充值金额");
            return;
        }
        if (mPayTypeId < 0) {
            showToast("请选择支付类型");
            return;
        }

        if (mPayTypeId == 1) {
            weChatPay();
        } else if (mPayTypeId == 2) {
            aLiPay();
        }


    }

    /**
     * 支付宝支付
     */
    private void aLiPay() {
        if (PayUtil.payHandler == null) {
            PayUtil.payHandler = getPayHandler();
        }

        showProgressDialog("正在进行支付，请稍候");
        mIOilCard.createOilCardRechargeALiPay(
                RetrofitHelper.getBody(new JsonItem("paymentType", mPayTypeId),
                        new JsonItem("oilCardId", mOilCardId),
                        new JsonItem("oilRechargeTempId", mSelectId)))
                .enqueue(new MsgCallBack<BaseBean<String>>(getThis(), false) {
                    @Override
                    public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                        String result = response.body().getResult();
                        if (TextUtils.isEmpty(result)) {
                            showToast("支付失败，请重试");
                            closeProgressDialog();
                            return;
                        }
                        showProgressDialog("正在进行支付，请稍候");
                        PayUtil.alpay(getThis(), result);

                    }
                });

    }


    private void weChatPay() {
        if (mIwxapi == null) {
            mIwxapi = WXAPIFactory.createWXAPI(getThis(), Config.WECHAT_APPID);
        }


        if (PayUtil.payHandler == null) {
            PayUtil.payHandler = getPayHandler();
        }

        showProgressDialog("正在进行支付，请稍候");
        mIOilCard.createOilCardRechargeWeChatPay(
                RetrofitHelper.getBody(new JsonItem("paymentType", mPayTypeId),
                        new JsonItem("oilCardId", mOilCardId),
                        new JsonItem("oilRechargeTempId", mSelectId)))
                .enqueue(new MsgCallBack<BaseBean<WeChatPayBean>>(getThis(), false) {
                    @Override
                    public void onSuccess(Call<BaseBean<WeChatPayBean>> call, Response<BaseBean<WeChatPayBean>> response) {
                        showProgressDialog("正在进行支付，请稍候");
                        WeChatPayBean bean = response.body().getResult();
                        if (bean == null) {
                            showToast("支付失败");
                            closeProgressDialog();
                            return;
                        }

                        boolean isPay = PayUtil.wechatPay(mIwxapi,
                                bean.getAppId(),
                                bean.getPartnerId(),
                                bean.getPrepayId(),
                                bean.getPackageX(),
                                bean.getNonceStr(),
                                bean.getTimestamp(),
                                bean.getSign());


                        if (!isPay) {
                            showToast("未安装微信或微信版本过低无法进行支付");
                            closeProgressDialog();
                        }

                    }
                });
    }

    /**
     * 返回支付handler
     *
     * @return 返回支付handler
     */
    private Handler getPayHandler() {

        return new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case PayUtil.SUCCESS:
                        showToast("支付成功");
                        startActivity(OilCardRechargeActivity.class);
                        // finish();
                        break;
                    case PayUtil.DENIED:
                        showToast("支付失败");
                        break;
                    case PayUtil.CANCEL:
                        break;
                }
                closeProgressDialog();
            }
        };
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_refueling_card_recharge;
    }

    @Override
    protected void create() {
        super.create();
        Intent intent = getIntent();
        OilCardBean oilCardBean = null;
        if (intent != null) {
            oilCardBean = intent.getParcelableExtra("oilCard");
        }

        if (oilCardBean != null) {
            mTypeId = oilCardBean.getOilCardType();
            mOilCardBean = oilCardBean;
        }

        if (mOilCardBean == null) {
            RefuelCardHelper helper = RefuelCardHelper.getInstance();
            if (helper.getCardDateSize() > 0) {
                mOilCardBean = helper.getCardDate(0);
            }
        }


    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("加油卡充值");


        mRadioFlowLayout.setOnSelectItemViewListener(new RadioFlowLayout.OnSelectItemViewListener() {
            @Override
            public void onSelectItemView(View view, boolean isSelect) {
                if (!(view instanceof DrawTextTextView) || !isSelect) {
                    return;
                }
                mSelectId = view.getId();
            }
        });

        mPayLayout.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.aty_refueling_card_recharge_rb_we_chat:
                        mPayTypeId = 1;
                        break;
                    case R.id.aty_refueling_card_recharge_rb_ali_pay:
                        mPayTypeId = 2;
                        break;
                    default:
                        mPayTypeId = -1;
                        break;

                }
            }
        });

        // 加载油卡数据
        if (mOilCardBean != null) {
            mTypeId = mOilCardBean.getOilCardType();
            mOilCardId = mOilCardBean.getId();

            mCardType.setText(mOilCardBean.getOilCardTypeName());
            mCardNumber.setText(mOilCardBean.getOilCardNumber());
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recharge_record, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_recharge_record) {
            startActivity(OilCardRechargeActivity.class);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initData() {
        super.initData();
        mIOilCard = RetrofitHelper.create(IOilCard.class);

        if (mOilCardBean != null || mTypeId > 0) {
            loadOilCardAndRechargeInfo();
        } else {
            // 通过网络加载数据
            loadRefuelingCardInfo();
        }
    }

    /**
     * 获取用户第一张加油卡信息
     */
    private void loadRefuelingCardInfo() {
        mIOilCard.getOilCards(1, 1)
                .enqueue(new MsgCallBack<BaseBean<BaseListBean<OilCardBean>>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<BaseListBean<OilCardBean>>> call, Response<BaseBean<BaseListBean<OilCardBean>>> response) {
                        BaseListBean<OilCardBean> listBean = response.body().getResult();
                        if (listBean == null || listBean.size() == 0) {
                            startActivityForResult(AddRefuelingCardActivity.class, AddRefuelingCardActivity.REQUEST_CODE);
                            return;
                        }
                        OilCardBean bean = listBean.getItems().get(0);
                        RefuelCardHelper helper = RefuelCardHelper.getInstance();
                        // 如果当前油卡大小为0 ，则将当前获取到的油卡添加到helper里面
                        if (helper.getCardDateSize() == 0) {
                            helper.addCardDate(bean);
                        }

                        mOilCardBean = bean;
                        mTypeId = mOilCardBean.getOilCardType();
                        mOilCardId = mOilCardBean.getId();

                        mCardType.setText(mOilCardBean.getOilCardTypeName());
                        mCardNumber.setText(mOilCardBean.getOilCardNumber());


                    }
                });
    }

    /**
     * 加载加油卡信息及充值信息
     */
    private void loadOilCardAndRechargeInfo() {
        Call<BaseBean<RechargeSelectBean>> oilCardRechargeSelectCall;

        if (mTypeId > 0) {
            oilCardRechargeSelectCall = mIOilCard.getOilCardRechargeSelect(mTypeId);
        } else {
            oilCardRechargeSelectCall = mIOilCard.getOilCardRechargeSelect();
        }

        oilCardRechargeSelectCall.enqueue(new MsgCallBack<BaseBean<RechargeSelectBean>>(getThis(), true) {
            @Override
            public void onSuccess(Call<BaseBean<RechargeSelectBean>> call, Response<BaseBean<RechargeSelectBean>> response) {
                RechargeSelectBean selectBean = response.body().getResult();
                if (selectBean == null) {
                    return;
                }

                mTypeId = selectBean.getOilCardType();
                // 加载加油卡信息
                List<RechargeSelectBean.OilCardsBean> oilCardsBeanList = selectBean.getOilCards();
                if (oilCardsBeanList != null && oilCardsBeanList.size() > 0) {
                    RechargeSelectBean.OilCardsBean oilCardsBean = oilCardsBeanList.get(0);
                    if (oilCardsBean != null) {
                        if (mOilCardBean == null) {
                            mOilCardBean = new OilCardBean();
                            mOilCardBean.setId(oilCardsBean.getId());
                            mOilCardBean.setOilCardType(mTypeId);
                            mOilCardBean.setOilCardNumber(oilCardsBean.getOilCardNumber());
                            if (mTypeId == 1) {
                                mOilCardBean.setOilCardTypeName("中国石油");
                            } else if (mTypeId == 2) {
                                mOilCardBean.setOilCardTypeName("中国石化");
                            } else if (mTypeId == 4) {
                                mOilCardBean.setOilCardTypeName("交运");
                            }
                        }

                        RefuelCardHelper helper = RefuelCardHelper.getInstance();
                        // 如果当前油卡大小为0 ，则将当前获取到的油卡添加到helper里面
                        if (helper.getCardDateSize() == 0) {
                            helper.addCardDate(mOilCardBean);
                        }

                        mCardType.setText(mOilCardBean.getOilCardTypeName());
                        mCardNumber.setText(mOilCardBean.getOilCardNumber());
                        mOilCardId = oilCardsBean.getId();


                    }

                }

                // 加载充值信息
                loadRechargeInfo(selectBean.getOilRechargeTemps());

            }
        });
    }


    /**
     * 加载充值选项信息
     *
     * @param list
     */
    private void loadRechargeInfo(List<OilRechargeTempBean> list) {
        mLegalAdviceTypeBeanList.clear();
        mRadioFlowLayout.removeAllViews();
        if (list == null && list.size() == 0) {
            return;
        }
        mLegalAdviceTypeBeanList.addAll(list);

        for (OilRechargeTempBean bean : mLegalAdviceTypeBeanList) {

            BigDecimal moneyBigDecimal = bean.getAmount();
            BigDecimal discountMoneyBigDecimal = bean.getRealPrice();
            String money = "0";
            String discountMoney = "0";

            if (moneyBigDecimal != null) {
                money = CalculateUtil.getFormatToString(moneyBigDecimal.doubleValue());
            }

            if (discountMoneyBigDecimal != null) {
                discountMoney = CalculateUtil.getFormatToString(discountMoneyBigDecimal.doubleValue());
            }


            addRadioItem(bean.getId(), money, discountMoney);
        }
    }


    /**
     * 添加充值选项
     *
     * @param money         原价
     * @param discountMoney 优惠价
     */
    private void addRadioItem(int id, String money, String discountMoney) {

        DrawTextTextView drawTextTextView = (DrawTextTextView) LayoutInflater.from(this)
                .inflate(R.layout.layout_draw_text, mRadioFlowLayout, false);
        drawTextTextView.setId(id);
        mRadioFlowLayout.addView(drawTextTextView);

        drawTextTextView.setBackgroundResource(R.drawable.bg_select_fuel_card);
        int parentWidth = mRadioFlowLayout.getWidth() - mRadioFlowLayout.getPaddingLeft() - mRadioFlowLayout.getPaddingRight();
        int margin = getResources().getDimensionPixelOffset(R.dimen.layout_margin);
        int viewWidth = parentWidth / 2 - margin * 2;
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) drawTextTextView.getLayoutParams();
        layoutParams.width = viewWidth;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.leftMargin = margin;
        layoutParams.rightMargin = margin;
        layoutParams.bottomMargin = margin;


        drawTextTextView.setTopDrawText(money + "元");
        String discountMoneyStr = "优惠价 " + discountMoney + "元";
        drawTextTextView.setText(discountMoneyStr);
    }

    @Override
    protected void onDestroy() {
        PayUtil.payHandler = null;
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            if (requestCode == AddRefuelingCardActivity.REQUEST_CODE && mOilCardId < 0) {
                finish();
                return;
            }
            return;
        }

        if (requestCode == SelectRefuelingCard.REQUEST_CODE) {
            mOilCardBean = SelectRefuelingCard.getResultData(data);
            mTypeId = mOilCardBean.getOilCardType();
            mOilCardId = mOilCardBean.getId();

            mCardType.setText(mOilCardBean.getOilCardTypeName());
            mCardNumber.setText(mOilCardBean.getOilCardNumber());
            loadOilCardAndRechargeInfo();
        } else if (requestCode == AddRefuelingCardActivity.REQUEST_CODE) {
            loadRefuelingCardInfo();
        }
    }

    /**
     * @param oilCardBean 加油卡
     */
    public static void start(Context context, OilCardBean oilCardBean) {
        Intent starter = new Intent(context, RefuelingCardRechargeActivity.class);
        starter.putExtra("oilCard", oilCardBean);
        context.startActivity(starter);
    }

}
