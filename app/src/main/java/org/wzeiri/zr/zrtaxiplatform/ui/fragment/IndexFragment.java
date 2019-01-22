package org.wzeiri.zr.zrtaxiplatform.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.BuildConfig;
import org.wzeiri.zr.zrtaxiplatform.MyApplication;
import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.FragmentFunctionMenuAdapter;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseRecyclerAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.FunctionMenuBean;
import org.wzeiri.zr.zrtaxiplatform.bean.HomeIndexBean;
import org.wzeiri.zr.zrtaxiplatform.bean.TenantBean;
import org.wzeiri.zr.zrtaxiplatform.bean.VersionBean;
import org.wzeiri.zr.zrtaxiplatform.bean.WalletDetailBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.common.Config;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IDriver;
import org.wzeiri.zr.zrtaxiplatform.network.api.IDriverClock;
import org.wzeiri.zr.zrtaxiplatform.network.api.ISundry;
import org.wzeiri.zr.zrtaxiplatform.network.api.IUser;
import org.wzeiri.zr.zrtaxiplatform.network.api.IWallet;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItem;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.network.update.DownloadHelper;
import org.wzeiri.zr.zrtaxiplatform.recycler.decoration.DividerGridItemDecoration;
import org.wzeiri.zr.zrtaxiplatform.service.BaiduLocationService;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.ArtyStarActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.ConvenientServiceActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.DepartmentChannelActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.DriverServiceActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.FinancialLossActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.LegalAdviceActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.MainActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.MyLegalAdviceReplyActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.MyReleaseDriverInteractionActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.MyReleaseJobInfoActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.MyReleaseLostFoundActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.NotificationActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.PaymentQRCodeActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.QueryIllegalActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.ReleaseCandidateInfoActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.ReleaseDriverInteractionActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.ReleaseLostFoundActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.SafetyLearnActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.SelectRegionRecyclerActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.TrainDetailActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.WealthActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.WebActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.WebActivity1;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.base.ActionbarFragment;
import org.wzeiri.zr.zrtaxiplatform.util.CarHelper;
import org.wzeiri.zr.zrtaxiplatform.util.DensityUtil;
import org.wzeiri.zr.zrtaxiplatform.util.GlideUtil;
import org.wzeiri.zr.zrtaxiplatform.util.NetWorkHelp;
import org.wzeiri.zr.zrtaxiplatform.util.UriUtil;
import org.wzeiri.zr.zrtaxiplatform.util.UserInfoHelper;
import org.wzeiri.zr.zrtaxiplatform.widget.AutoVerticalScrollTextView;
import org.wzeiri.zr.zrtaxiplatform.widget.CircleImageView;
import org.wzeiri.zr.zrtaxiplatform.widget.dialog.IOSAlertDialog;
import org.wzeiri.zr.zrtaxiplatform.widget.dialog.UpdateDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author k-lm on 2017/11/14.
 */

public class IndexFragment extends ActionbarFragment implements SwipeRefreshLayout.OnRefreshListener {

    private MenuItem mMessageMenu;
    private Dialog cus_dialog ;

    @BindView(R.id.fragment_index_rv_menu_list)
    RecyclerView mRecyclerView;

    @BindView(R.id.fragment_index_avst_message)
    AutoVerticalScrollTextView mAutoVerticalScrollTextView;

    @BindView(R.id.fragment_index_text_license_plate)
    TextView mTextlicensePlate;

    @BindView(R.id.fragment_index_carousel_pager)
    ImageView mImageAdvertisement;


    @BindView(R.id.fragment_index_swl_refresh)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.fragment_index_text_load_data_error)
    TextView mNotNetWorkTextView;
     @BindView(R.id.fragment_index_text_punch_the_clock)
    TextView mClock;
     @BindView(R.id.fragment_index_text_state)
     TextView mState;
    private IUser mIUser;
    private IWallet mIWallet;
    private IDriver iDriver;
    private IDriverClock iDriverClock= RetrofitHelper.create(IDriverClock.class);
    /**
     * 城市
     */
    private TextView mTextCity;

    private List<FunctionMenuBean> mMenuBeanList = new ArrayList<>();

    private FragmentFunctionMenuAdapter mAdapter;
    /**
     * 定位信息
     */
    private TenantBean mTenantBean;


    private IOSAlertDialog mCityDialog;
    /**
     * 轮播文章
     */
    private List<HomeIndexBean.AnnouncementsBean> mAnnouncementsBeanList = new ArrayList<>();

    private HomeIndexBean.AdverBean mAdverBean;

    private int mCarId = -1;

    private boolean isFirstLoad = true;

    private boolean online=true;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_index;
    }


    @Override
    public void init() {
        super.init();

        initActionBar();
        initMenu();


        mAutoVerticalScrollTextView.setOnItemTextClickListener(new AutoVerticalScrollTextView.OnItemTextClickListener() {
            @Override
            public void onItemTextClick(int position) {
                int id = mAnnouncementsBeanList.get(position).getId();
                TrainDetailActivity.start(getContext(), id);
            }
        });

        // 监听认证状态
        UserInfoHelper.getInstance().addOnAuthenticationListener(getThis(),
                new UserInfoHelper.OnAuthenticationChangeListener() {

                    @Override
                    public void onAuthenticationChangeListener() {
                        onRefresh();
                    }
                });

        mRefreshLayout.setOnRefreshListener(this);


        // 设置广告图片高度
        mImageAdvertisement.getLayoutParams().height =
                (int) (DensityUtil.WINDOW_WIDTH / Config.INDEX_ADVERTISEMENT_COVER_HEIGHT);

        initNetWork();
        // 检查更新
        checkVersion();
    }

    /**
     * 初始化网络
     */
    private void initNetWork() {
        if (!NetWorkHelp.getInstance().isNetworkConnected(getContext())) {
            mNotNetWorkTextView.setVisibility(View.VISIBLE);
        } else {
            mNotNetWorkTextView.setVisibility(View.GONE);
        }
        NetWorkHelp.getInstance().addOnNetworkChangeListener(getThis(), new NetWorkHelp.OnNetworkChangeListener() {
            @Override
            public void onConnectWIFi() {
                mNotNetWorkTextView.setVisibility(View.GONE);
            }

            @Override
            public void onConnectMobileNetwork() {
                mNotNetWorkTextView.setVisibility(View.GONE);
            }

            @Override
            public void onDisconnectNetwork() {
                mNotNetWorkTextView.setVisibility(View.VISIBLE);
            }
        });

        NetWorkHelp.getInstance().registerReceiver(getContext());
    }

    @Override
    public void onLoadData() {
        super.onLoadData();
        CarHelper carHelper = CarHelper.getInstance();
        CarHelper.CarInfo carInfo = carHelper.getCurrentCarInfo();
        // 获取当前车辆信息

        if (carInfo == null || carInfo.getCarModel() == "")
        {
          showToast("未找到车辆信息");
            mTextlicensePlate.setText("");
        }
        else if (carInfo != null && mCarId != carInfo.getId()) {
                mTextlicensePlate.setText(carInfo.getPlateNumber());
                mCarId = carInfo.getId();
            }

    }

    private void initActionBar() {
        setCenterTitle(R.string.app_name);
        setNoticeBarColor(ContextCompat.getDrawable(getContext(), R.drawable.toolbar_bg));
        setCenterTitleColor(ContextCompat.getColor(getContext(), R.color.black90));
        setBarBackgroundColor(ContextCompat.getDrawable(getContext(), R.drawable.toolbar_bg));
        mTextCity = new TextView(getContext());
        mTextCity.setText(BaiduLocationService.getCity());
        mTextCity.setTextColor(ContextCompat.getColor(getContext(), R.color.black90));
        mTextCity.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_medium));
        mTextCity.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.layout_margin));
        mTextCity.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_down, 0);
        mTextCity.setVisibility(View.INVISIBLE);
        addToolBarLeftView(mTextCity);


        mTextCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectRegionRecyclerActivity.start(getThis());
            }
        });

    }

    /**
     * 初始化功能菜单
     */
    private void initMenu() {


        int count = 3;
        GridLayoutManager manager = new GridLayoutManager(getContext(),
                count,
                GridLayoutManager.VERTICAL,
                false);


        mRecyclerView.setLayoutManager(manager);

        mAdapter = new FragmentFunctionMenuAdapter(mMenuBeanList);
        mRecyclerView.setAdapter(mAdapter);

        DividerGridItemDecoration decoration = new DividerGridItemDecoration(DividerGridItemDecoration.HORIZONTAL);
        decoration.setHorizontalmDividerHeight(getResources().getDimensionPixelOffset(R.dimen.layout_margin_tiny));

        mRecyclerView.addItemDecoration(decoration);

        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
//                    case 0:
//                        //司机特惠
//                        startActivity(DriverDiscountPrivilegeActivity.class);
//                        break;

                    case 0:
                        //安全学习

                        if(mTextCity.getText().toString().equals("杭州市"))
                        {
                            showToast("尚未开通");
                        }
                        else if (!UserInfoHelper.getInstance().isAuthentication(getThis())) {
                            return;
                        }
                        else  if (UserInfoHelper.getInstance().isSwitchRegion(getThis())) {
                            return;
                        }
                        else
                        //违章查询
                        startActivity(QueryIllegalActivity.class);
                        break;
                    case 1:
                        //失物招领
                        startActivityForResult(FinancialLossActivity.class, ReleaseLostFoundActivity.REQUEST_CODE);

                        break;
                    case 2:
                        //安全学习
                        if (!UserInfoHelper.getInstance().isAuthentication(getThis())) {
                            return;
                        }
                        if (UserInfoHelper.getInstance().isSwitchRegion(getThis())) {
                            return;
                        }
                        else {
                            startActivity(SafetyLearnActivity.class);
                        }
                        break;
                    case 3:
                        Intent intent=new Intent(getActivity(),WebActivity1.class);
                        intent.putExtra("url",UserInfoHelper.getInstance().getmUrl().toString());
                        startActivity(intent);
                        break;
                    case 4:
                        //每月之星
                        startActivity(ArtyStarActivity.class);
                        break;
                    case 5:
                        //一键导航
                        startActivity(ConvenientServiceActivity.class);
                        break;
//                    case 5:
//                        //文明创建
//                        startActivity(CivilizedSocietyActivity.class);
//                        break;


                    case 6:
                        //司机服务
                        startActivityForResult(DriverServiceActivity.class, DriverServiceActivity.REQUEST_CODE);
                        break;
                    case 7:
                        //部门专区
                        startActivity(DepartmentChannelActivity.class);
                        break;

                }
            }
        });


        String[] titles = new String[]{
                  "违章查询","失物招领","继续教育",
                "T币商城", "每月之星","一键导航",
                 "司机服务", "部门专区"};
        int[] imageIds = new int[]{
                 R.drawable.ic_violation_inquiry, R.drawable.ic_lost_found,
                R.drawable.ic_safety_learning, R.drawable.shoppingmall,
                R.drawable.ic_star_of_the_month, R.drawable.icon_guide, R.drawable.ic_driver_channel, R.drawable.ic_department_channel
        };

        for (int i = 0; i < titles.length; i++) {
            FunctionMenuBean bean = new FunctionMenuBean();
            bean.setImageId(imageIds[i]);
            bean.setTitle(titles[i]);
            mMenuBeanList.add(bean);
        }
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void initData() {
        super.initData();
        mIUser = RetrofitHelper.create(IUser.class);
       mIWallet= RetrofitHelper.create(IWallet.class);
       iDriver = RetrofitHelper.create(IDriver.class);
        if (NetWorkHelp.getInstance().isNetworkConnected(getContext())) {
            onRefresh();
        }

    }

    /**
     * 获取定位信息
     */
    private void location() {
        // 启动定位服务
        BaiduLocationService.start(getActivity(), new BaiduLocationService.OnLocationListener() {
            @Override
            public void onReceiveLocation() {
                String city = BaiduLocationService.getCity();
                loadLocationRegion(BaiduLocationService.getProvince(),
                        BaiduLocationService.getCity());
            }

            @Override
            public void onError() {
                showCityDialog();
            }
        });
    }

    /**
     * 获取首页数据
     */
    private void loadHomeDate() {
        mIUser.getHomeIndex()
                .enqueue(new MsgCallBack<BaseBean<HomeIndexBean>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<HomeIndexBean>> call, Response<BaseBean<HomeIndexBean>> response) {
                        mRefreshLayout.setRefreshing(false);
                        HomeIndexBean bean = response.body().getResult();

                        if (bean == null) {
                            return;
                        }
                        UserInfoHelper.getInstance().setDriverStatus(bean.getDriverStatus());
                        // 获取车牌号
                        HomeIndexBean.CarBean carBean = bean.getCar();
                        if (carBean != null) {
                            String plateNumber = carBean.getPlateNumber();
                            if (!TextUtils.isEmpty(plateNumber) &&
                                    UserInfoHelper.getInstance().isAuthentication()) {
                                mTextlicensePlate.setText(plateNumber);
                                UserInfoHelper.getInstance().setCarLisenceNumber(carBean.getPlateNumber().toString());
                                UserInfoHelper.getInstance().setPlateNumberPrefix(carBean.getPlateNumber().substring(0,2));
                                if (carBean.getQrCodeUrl()!=null&&carBean.getQrCodeUrl()!=null) {
                                    UserInfoHelper.getInstance().setBindCode(carBean.getQrCodeUrl());
                                }
                                else
                                {
                                    UserInfoHelper.getInstance().setBindCode("");
                                }
                                mCarId = carBean.getId();
                                online=carBean.getOnline();
                                if (online==true){
                                    mState.setText("当班中");
                                    mClock.setBackgroundResource(R.drawable.btn_enable);
                                    mState.setTextColor(Color.parseColor("#000000"));
//                         mClock.setEnabled(false);
                                }
                                if (online==false){
                                    mState.setText("休息中");
                                    mClock.setBackgroundResource(R.drawable.btn_nor);
                                    mState.setTextColor(Color.parseColor("#ffffff"));
                                }
                            } else {
                                mTextlicensePlate.setText("尚未绑定");
                            }

                            if (UserInfoHelper.getInstance().isAuthentication() &&
                                    isFirstLoad &&
                                    carBean != null) {
                                showSwitchCarDialog();
                            }
                            isFirstLoad = false;
                        } else {
                            mTextlicensePlate.setText("尚未绑定");
                        }


                        mTenantBean = bean.getTenant();
                        if (mTenantBean == null || mTenantBean.getId() <= 0) {
                            location();
                        } else {
                            UserInfoHelper.getInstance().setCurrentTenantId(mTenantBean.getId());
                            mTextCity.setVisibility(View.VISIBLE);
                            mTextCity.setText(mTenantBean.getCityName());

                        }


                        // 获取滚动文章
                        List<HomeIndexBean.AnnouncementsBean> announcementsBeans = bean.getAnnouncements();
                        loadAnnouncement(announcementsBeans);

                        mAdverBean = bean.getAdver();
                        if (mAdverBean != null) {
                            // 设置广告图片
                            String coverPicture = mAdverBean.getCoverPicture();
                            if (mAdverBean != null && !TextUtils.isEmpty(mAdverBean.getCoverPicture())) {
                                GlideUtil.loadPath(getContext(), mImageAdvertisement, coverPicture);
                            } else {
                                // 设置无广告状态
                                mImageAdvertisement.setImageBitmap(null);
                            }
                            UserInfoHelper.getInstance().setUrl(mAdverBean.getLinkUrl());
                        } else {
                            mImageAdvertisement.setImageBitmap(null);
                        }

                    }
                    @Override
                    public void onError(Call<BaseBean<HomeIndexBean>> call, Throwable t) {
                        super.onError(call, t);
                        mRefreshLayout.setRefreshing(false);
                    }
                });
            mIWallet.getWalletDetails()
                .enqueue(new MsgCallBack<BaseBean<WalletDetailBean>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<WalletDetailBean>> call, Response<BaseBean<WalletDetailBean>> response) {
                        WalletDetailBean mWalletDetailBean=response.body().getResult();
                        if (mWalletDetailBean.getBankCardNo()!=null)
                        {
                            UserInfoHelper.getInstance().setBankCardNo(mWalletDetailBean.getBankCardNo().toString());
                        }
                    }
                });

    }

    /**
     * 加载滚动文章
     *
     * @param beanList
     */
    private void loadAnnouncement(List<HomeIndexBean.AnnouncementsBean> beanList) {
        if (beanList == null || beanList.size() == 0) {
            mAutoVerticalScrollTextView.setTexts(null);
            return;
        }
        mAnnouncementsBeanList.addAll(beanList);
        List<String> dateList = new ArrayList<>();
        for (HomeIndexBean.AnnouncementsBean bean : beanList) {
            String str = bean.getTitle();
            dateList.add(str);
        }

        mAutoVerticalScrollTextView.setTexts(dateList);
        mAutoVerticalScrollTextView.startTurning(2000);
        mAutoVerticalScrollTextView.showNextText();
    }

    /**
     * 显示切换车辆按钮
     */
    private void showSwitchCarDialog() {
        IOSAlertDialog dialog = new IOSAlertDialog(getContext());
        if (online==false)
        {
            dialog.builder()
                    .setMsg("是否需要打卡")
                    .setPositiveButton("", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DriverClock("1");
                        }
                    })
                    .setNegativeButton("", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
        }
    }


    /**
     * 根据定位信息获取服务器相应的code
     *
     * @param provinceName
     * @param cityName
     */
    private void loadLocationRegion(@Nullable String provinceName,
                                    @Nullable String cityName) {

        if (mIUser == null) {
            mIUser = RetrofitHelper.create(IUser.class);
        }

        mIUser.changeCurrentTenantByRegionName(RetrofitHelper
                .getBody(new JsonItem("provinceName", provinceName),
                        new JsonItem("cityName", cityName)))
                .enqueue(new MsgCallBack<BaseBean<TenantBean>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<TenantBean>> call, Response<BaseBean<TenantBean>> response) {
                        TenantBean bean = response.body().getResult();
                        if (bean == null) {
                            // 弹出对话框
                            showCityDialog();
                            return;
                        }
                        mTenantBean = bean;
                        mTextCity.setVisibility(View.VISIBLE);
                        mTextCity.setText(bean.getCityName());
                    }

                    @Override
                    public void onError(Call<BaseBean<TenantBean>> call, Throwable t) {
                        super.onError(call, t);
                        // 弹出对话框
                        showCityDialog();
                    }
                });
    }

    @OnClick(R.id.fragment_index_text_load_data_error)
    void onSettingNetWork() {
        // 进入设置页面
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        startActivity(intent);
    }


    @OnClick(R.id.fragment_index_text_collection_payment)
    void collectionPaymentClick() {
        if (!UserInfoHelper.getInstance().isAuthentication(getThis())) {
            return;
        }
        startActivity(PaymentQRCodeActivity.class);
    }


    @OnClick(R.id.fragment_index_carousel_pager)
    void onAdvertisementClick() {
        if (mAdverBean == null) {
            return;
        }

        if (mAdverBean.getAdverType() == 1) {
            WebActivity.startUrl(getContext(), mAdverBean.getLinkUrl());
            return;
        }

        if (mAdverBean.getAdverType() == 2) {
            WebActivity.startUrl(getContext(), mAdverBean.getContent());
            return;
        }
    }

    @OnClick(R.id.fragment_index_text_wealth)
    void onWealthClick() {
        if (!UserInfoHelper.getInstance().isAuthentication(getThis())) {
            return;
        }
        startActivity(WealthActivity.class);
    }



//    @OnClick(R.id.fragment_index_text_collection_bill)
//    void onClickBill() {
//        if (!UserInfoHelper.getInstance().isAuthentication(getThis())) {
//            return;
//        }
//        startActivity(BillActivity.class);
//    }
    @OnClick(R.id.fragment_index_text_punch_the_clock)
    void onClock(){ShowDialog();};
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_message, menu);
        mMessageMenu = menu.findItem(R.id.menu_message_message);
        mMessageMenu.setIcon(R.drawable.ic_message);
    }

    /**
     * 切换城市
     *
     * @param bean
     */
    private void changeCity(TenantBean bean) {
       /* mTextCity.setText(bean.getCityName());
        mTextCity.setVisibility(View.VISIBLE);*/

        if (mIUser == null) {
            mIUser = RetrofitHelper.create(IUser.class);
        }

        mIUser.changeCurrentTenant(RetrofitHelper
                .getBody(new JsonItem("Id", bean.getId())))
                .enqueue(new MsgCallBack<BaseBean<TenantBean>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<TenantBean>> call, Response<BaseBean<TenantBean>> response) {
                        if (mCityDialog != null) {
                            mCityDialog.dismiss();
                            mCityDialog = null;
                        }

                        mTenantBean = response.body().getResult();
                        UserInfoHelper.getInstance().changeCity(mTenantBean);
                        onRefresh();
                    }

                    @Override
                    public void onError(Call<BaseBean<TenantBean>> call, Throwable t) {
                        super.onError(call, t);
                    }
                });
    }

    /**
     * 显示城市信息
     */
    private void showCityDialog() {
        if (mCityDialog == null) {
            mCityDialog = new IOSAlertDialog(getContext());
            mCityDialog.builder()
                    .setMsg("无法获取当前城市信息，请手动选择您当前的城市")
                    .setPositiveButton("", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SelectRegionRecyclerActivity.start(getThis());
                        }
                    })
                    .setNegativeButton("", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mCityDialog.dismiss();
                            ((MyApplication) getActivity().getApplication()).finishAllActivity();
                        }
                    });
            mCityDialog.setCancelable(false);
        }
        if (mCityDialog.isShowing()) {
            return;
        }
        mCityDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //showToast("点击消息");
        if (item.getItemId() == R.id.menu_message_message) {
            startActivity(NotificationActivity.class);
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        UserInfoHelper.getInstance().removeOnAuthenticationListener(getThis());
        NetWorkHelp.getInstance().registerReceiver(getContext());
        NetWorkHelp.getInstance().removeOnNetworkChangeListener(getThis());
        BaiduLocationService.stop(getActivity());
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if (resultCode != Activity.RESULT_OK && data == null) {
            return;
        }*/
        releaseSuccess(requestCode, resultCode, data);
        if (requestCode == SelectRegionRecyclerActivity.REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK && data == null) {
                if (mTenantBean == null) {
                    showCityDialog();
                }
                return;
            }

            if (mCityDialog != null) {
                mCityDialog.dismiss();
                mCityDialog = null;
            }
            TenantBean bean = SelectRegionRecyclerActivity.loadRegionInfo(data);
            changeCity(bean);
        }
    }

    /**
     * 发布成功
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private void releaseSuccess(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == DriverServiceActivity.REQUEST_CODE) {
            requestCode = DriverServiceActivity.getRequestCode(data);

            if (requestCode == LegalAdviceActivity.REQUEST_CODE) {
                ((MainActivity) getActivity()).checkBottomButton(3);
                MyLegalAdviceReplyActivity.start(getContext(), false);
            } else if (requestCode == ReleaseLostFoundActivity.REQUEST_CODE) {
                ((MainActivity) getActivity()).checkBottomButton(3);
                startActivity(MyReleaseLostFoundActivity.class);
            } else if (requestCode == ReleaseCandidateInfoActivity.REQUEST_CODE) {
                ((MainActivity) getActivity()).checkBottomButton(3);
                startActivity(MyReleaseJobInfoActivity.class);
            } else if (requestCode == ReleaseDriverInteractionActivity.REQUEST_CODE) {
                ((MainActivity) getActivity()).checkBottomButton(3);
                startActivity(MyReleaseDriverInteractionActivity.class);
            }

        }


    }


    /**
     * 检测版本
     */
    private void checkVersion() {
        ISundry iSundry = RetrofitHelper.create(ISundry.class);

        iSundry.versionCheck(
                RetrofitHelper.getBody(new JsonItem("clientVersionType", 20),
                        new JsonItem("versionCode", BuildConfig.VERSION_NAME))
        ).enqueue(new MsgCallBack<BaseBean<VersionBean>>(getThis()) {
            @Override
            public void onSuccess(Call<BaseBean<VersionBean>> call, Response<BaseBean<VersionBean>> response) {
                VersionBean bean = response.body().getResult();
                if (bean == null || !bean.isHasUpdate()) {
                    return;
                }
                showUpdateDialog(bean);

            }
        });
    }

    /**
     * 显示版本更新对话框
     *
     * @param versionBean
     */
    private void showUpdateDialog(final VersionBean versionBean) {
        if (versionBean == null || !versionBean.isHasUpdate()) {
            return;
        }

        UpdateDialog mUpdateDialog = new UpdateDialog(getContext(), R.style.NoTitleDialog);
        mUpdateDialog.setTitle_("发现新版本V" + versionBean.getVersionCode());
        mUpdateDialog.setMessage_(versionBean.getDescribe());
        mUpdateDialog.setNegativeButton_(null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mUpdateDialog.setPositiveButton_(null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downloadApk(versionBean.getResourceUrl());
                dialog.dismiss();
            }
        });

        mUpdateDialog.show();
    }


    /**
     * 下载apk
     *
     * @param url apk路径
     */
    private void downloadApk(String url) {
        DownloadHelper downloadHelper = new DownloadHelper(getContext());
        downloadHelper.setOnDownloadListener(new DownloadHelper.OnDownloadListener() {
            @Override
            public void onStartDownload() {

            }

            @Override
            public void onEndDownload(String path) {
                openApk(path);
            }

            @Override
            public void onError(String msg) {
                showToast(msg);
            }
        });
        String apkPath = Config.STORAGE_PATH + File.separator + "apk" + File.separator
                + getString(R.string.app_name) + ".apk";
        downloadHelper.downloadFile(url, apkPath);
    }
    /**
     * 打开apk
     *
     * @param path apk 路径
     */
    private void openApk(String path) {
        File file = new File(path);
        Uri uri = UriUtil.getUri(getContext(), getContext().getPackageName() + ".fileprovider", file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        startActivity(intent);
    }
    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(true);
        mAnnouncementsBeanList.clear();
        mAutoVerticalScrollTextView.stopTurning();
        mAutoVerticalScrollTextView.setText("");
        loadHomeDate();
    }
    public void ShowDialog()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.dialog_punch_the_clock,null);
        builder.setView(view);
        final TextView change=view.findViewById(R.id.change);
        final TextView ok=view.findViewById(R.id.Ok);
        final TextView license_plate=view.findViewById(R.id.license_plate);
        final TextView DriverClock=view.findViewById(R.id.DriverClock);
        final CircleImageView dismiss=view.findViewById(R.id.dismiss);
        if (mState.getText().equals("当班中"))
        {
            DriverClock.setText("是否下班");
        }
        else
        {
            DriverClock.setText("是否打卡");
        }
        license_plate.setText("当前车牌:"+mTextlicensePlate.getText().toString());
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).checkBottomButton(2);
                cus_dialog.dismiss();
            }
        });
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cus_dialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mState.getText().equals("当班中"))
                {
                    DriverClock("0");
                    cus_dialog.dismiss();
                }
                else
                {
                    DriverClock("1");
                    cus_dialog.dismiss();
                }
            }
        });
        cus_dialog = builder.create();
        cus_dialog.show();
    }
    public void DriverClock(final String Value){
            iDriverClock.getClock(Value).enqueue(new MsgCallBack<BaseBean<String>>(IndexFragment.this,false) {
                @Override
                public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                   if (Value=="1")
                   {
                       showToast("打卡成功");
                       mState.setText("当班中");
                       mClock.setBackgroundResource(R.drawable.btn_enable);
                       mState.setTextColor(Color.parseColor("#000000"));
                   }
                   else
                   {
                       showToast("下班成功");
                       mState.setText("休息中");
                       mClock.setBackgroundResource(R.drawable.btn_nor);
                       mState.setTextColor(Color.parseColor("#ffffff"));
                   }
                }
            });
    }
}
