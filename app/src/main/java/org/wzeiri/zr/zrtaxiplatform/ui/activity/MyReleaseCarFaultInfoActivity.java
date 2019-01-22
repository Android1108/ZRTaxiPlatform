package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.Context;
import android.content.Intent;

import org.wzeiri.zr.zrtaxiplatform.adapter.MyCarInfoPagerAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.BingCarInfoBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IDriver;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseTabLayoutActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.MyCarFaultInfoFragment;
import org.wzeiri.zr.zrtaxiplatform.util.CarHelper;
import org.wzeiri.zr.zrtaxiplatform.util.UserInfoHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 我发布的故障信息
 *
 * @author k-lm on 2017/12/14.
 */

public class MyReleaseCarFaultInfoActivity extends BaseTabLayoutActivity<MyCarInfoPagerAdapter> {
    private IDriver mIDriver;

    private List<MyCarFaultInfoFragment> mFragmentList = new ArrayList<>();
    private List<CarHelper.CarInfo> mCarInfoLst = new ArrayList<>();
    private List<Integer> mCarIdList = new ArrayList<>();

    @Override
    protected MyCarInfoPagerAdapter getPagerAdapter() {
        return new MyCarInfoPagerAdapter(getSupportFragmentManager(), mFragmentList, mCarInfoLst);
    }


    @Override
    protected void init() {
        super.init();
        setCenterTitle("故障信息");
    }


    @Override
    protected void initData() {
        super.initData();
        if(!UserInfoHelper.getInstance().isAuthentication()){
            showEmptyView();
            return;
        }

        CarHelper helper = CarHelper.getInstance();
        if (!helper.isLoadCarInfo()) {
            getData();
            return;
        }
        loadCarInfo();

    }

    /**
     * 获取数据
     */
    private void getData() {
        if (mIDriver == null) {
            mIDriver = RetrofitHelper.create(IDriver.class);
        }

        mIDriver.getBindingCars()
                .enqueue(new MsgCallBack<BaseBean<BingCarInfoBean>>(this, true) {
                    @Override
                    public void onSuccess(Call<BaseBean<BingCarInfoBean>> call, Response<BaseBean<BingCarInfoBean>> response) {
                        CarHelper helper = CarHelper.getInstance();
                        helper.save(response.body().getResult());
                        loadCarInfo();
                    }


                });

    }

    /**
     * 加载车辆信息
     */
    private void loadCarInfo() {
        CarHelper helper = CarHelper.getInstance();

        List<CarHelper.CarInfo> list = helper.getBindCarInfoList();
        if (list == null || list.size() == 0) {
            showEmptyView();
            return;
        }
        mCarInfoLst.addAll(list);
        //loadCarInfo();
        for (CarHelper.CarInfo carInfo : mCarInfoLst) {
            MyCarFaultInfoFragment fragment = MyCarFaultInfoFragment.newInstance(carInfo.getId());
            mFragmentList.add(fragment);
        }
        getAdapter().notifyDataSetChanged();
        showCarAdvertisement();
    }


    /**
     * 设置显示的位置
     */
    private void showCarAdvertisement() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        int id = intent.getIntExtra("showId", -1);
        if (id < 0 || mCarIdList.size() == 0) {
            return;
        }

        int position = mCarIdList.indexOf(id);
        mLayoutPager.setCurrentItem(position);
    }

    /**
     * @param id 需要展示的车辆id
     */
    public static void start(Context context, int id) {
        Intent starter = new Intent(context, MyReleaseCarFaultInfoActivity.class);
        starter.putExtra("showId", id);
        context.startActivity(starter);
    }
}
