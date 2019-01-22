package org.wzeiri.zr.zrtaxiplatform.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.TaxiAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.BingCarInfoBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IDriver;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItem;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.BindTaxiActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.IdCardAuthenticationActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.MainActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.base.ActionbarFragment;
import org.wzeiri.zr.zrtaxiplatform.util.CarHelper;
import org.wzeiri.zr.zrtaxiplatform.util.UserInfoHelper;
import org.wzeiri.zr.zrtaxiplatform.widget.dialog.IOSAlertDialog;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author k-lm on 2017/11/15.
 */

public class TaxiFragment extends ActionbarFragment implements SwipeRefreshLayout.OnRefreshListener {


    private IDriver mIDriver;

    @BindView(R.id.fragment_taxi_layout_bind_srl_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.fragment_taxi_text_bind_new_taxi)
    TextView mNewTaxi;
    @BindView(R.id.fragment_taxi_layout_bind_list)
    ListView mListView;

    private TaxiAdapter mAdapter;

    private List<CarHelper.CarInfo> mCarInfoList = new ArrayList<>();

    private IOSAlertDialog mSwitchCarDialog;

    private int mSelectCarId = -1;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_taxi;
    }


    @OnClick(R.id.fragment_taxi_layout_bind_new_taxi)
    void onBindTaxi() {
        if (UserInfoHelper.getInstance().getAuthenticationState() == UserInfoHelper.UN_AUTHENTICATION) {
            IdCardAuthenticationActivity.start(getThis(), false);
            return;
        }

//        if (!UserInfoHelper.getInstance().isAuthentication(getThis())) {
//            return;
//        }
//
//        if (UserInfoHelper.getInstance().isSwitchRegion(getThis())) {
//            return;
//        }

        startActivityForResult(BindTaxiActivity.class, 10024);
    }


    @Override
    public void init() {
        setCenterTitle("出租车");
        mRefreshLayout.setOnRefreshListener(this);
        mAdapter = new TaxiAdapter(mCarInfoList);
        mListView.setAdapter(mAdapter);

        UserInfoHelper.getInstance().addOnAuthenticationListener(getThis(),
                new UserInfoHelper.OnAuthenticationChangeListener() {

                    @Override
                    public void onAuthenticationChangeListener() {
                        mRefreshLayout.setEnabled(UserInfoHelper.getInstance().isAuthentication());
                        getData(false);
                    }
                });


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CarHelper.CarInfo carInfo = mCarInfoList.get(position);
                CarHelper helper = CarHelper.getInstance();
                if (helper.isCurrentCar(carInfo) || carInfo.getBindingCarRequestStatus() != -1) {
                    return;
                }

                if (!UserInfoHelper.getInstance().isAuthentication(getThis())) {
                    return;
                }

                if (UserInfoHelper.getInstance().isSwitchRegion(getThis())) {
                    return;
                }

                mSelectCarId = carInfo.getId();
                showSwitchCarDialog();
            }
        });
        if (!UserInfoHelper.getInstance().isAuthentication()) {
            mRefreshLayout.setEnabled(false);
        }


    }


    @Override
    public void initData() {
        super.initData();
        CarHelper helper = CarHelper.getInstance();

        if (!UserInfoHelper.getInstance().isAuthentication()) {
            return;
        }

        if (!helper.isLoadCarInfo()) {
            onRefresh();
            return;
        }
        mCarInfoList.addAll(helper.getAllCarInfoList());
        mAdapter.notifyDataSetChanged();

    }


    @Override
    public void onDestroy() {
        UserInfoHelper.getInstance().removeOnAuthenticationListener(getThis());
        super.onDestroy();

    }

    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(true);
        getData(true);
    }

    /**
     * 获取数据
     */
    private void getData(boolean isShowDialog) {
        if (mIDriver == null) {
            mIDriver = RetrofitHelper.create(IDriver.class);
        }

        mIDriver.getBindingCars()
                .enqueue(new MsgCallBack<BaseBean<BingCarInfoBean>>(this, isShowDialog) {
                    @Override
                    public void onSuccess(Call<BaseBean<BingCarInfoBean>> call, Response<BaseBean<BingCarInfoBean>> response) {
                        CarHelper helper = CarHelper.getInstance();
                        helper.save(response.body().getResult());
                        mCarInfoList.clear();
                        mCarInfoList.addAll(helper.getAllCarInfoList());
                        mAdapter.notifyDataSetChanged();
                        mRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Call<BaseBean<BingCarInfoBean>> call, Throwable t) {
                        super.onError(call, t);
                        mRefreshLayout.setRefreshing(false);
                    }
                });
    }

    /**
     * 显示对话框
     */
    private void showSwitchCarDialog() {
        if (mSwitchCarDialog == null) {
            mSwitchCarDialog = new IOSAlertDialog(getContext());
            mSwitchCarDialog.builder()
                    .setMsg("是否切换为当前车辆")
                    .setNegativeButton("", null)
                    .setPositiveButton("切换", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switchCurrentCar();
                        }
                    });
        }
        mSwitchCarDialog.show();

    }

    /**
     * 切换当前车辆
     */
    private void switchCurrentCar() {
        if (mIDriver == null) {
            mIDriver = RetrofitHelper.create(IDriver.class);
        }
        mIDriver.switchCurrentCar(RetrofitHelper.getBody(new JsonItem("id", mSelectCarId)))
                .enqueue(new MsgCallBack<BaseBean<String>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                        showToast("成功切换当前车辆");
                        CarHelper.getInstance().setCurrentCarId(mSelectCarId);
                        mAdapter.notifyDataSetChanged();
                        if(getActivity() != null){
                            ((MainActivity)getActivity()).checkBottomButton(0);
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == 10024) {
            onRefresh();
        } else if (requestCode == IdCardAuthenticationActivity.REQUEST_CODE) {
            onRefresh();
        }

    }
}
