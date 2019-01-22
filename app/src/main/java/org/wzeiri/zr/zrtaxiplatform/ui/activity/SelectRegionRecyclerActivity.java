package org.wzeiri.zr.zrtaxiplatform.ui.activity;

/**
 * @author k-lm on 2017/12/28.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.Gravity;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.LocationRegionBean;
import org.wzeiri.zr.zrtaxiplatform.bean.ProvinceBean;
import org.wzeiri.zr.zrtaxiplatform.bean.TenantBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IRegion;
import org.wzeiri.zr.zrtaxiplatform.network.api.IUser;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItem;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.recycler.decoration.FloatingBarItemDecoration;
import org.wzeiri.zr.zrtaxiplatform.service.BaiduLocationService;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.SelectRecyclerActivity;
import org.wzeiri.zr.zrtaxiplatform.util.LogUtil;
import org.wzeiri.zr.zrtaxiplatform.widget.IndexBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 选择地址 - 字母索引 + recycler
 */
public class SelectRegionRecyclerActivity extends SelectRecyclerActivity {

    @BindView(R.id.aty_select_region_recycler_index_bar)
    IndexBar mIndexBar;

    private IUser mIUser;
    private List<TenantBean> mDataList = new ArrayList<>();


    public static final int REQUEST_CODE = 10151;

    public static final String KEY_DATE = "cityDate";

    private Map<Integer, String> mFloatingMap = new HashMap<>();
    /**
     * 记录每个首字母的第一个位置
     */
    private SparseArray<String> mFloatingIndexArray = new SparseArray<>();

    private List<String> mCityLetterList = new ArrayList<>();

    private TenantBean mLocationBean;

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getThis(),
                LinearLayoutManager.VERTICAL,
                false);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_region_recycler;
    }

    @Override
    protected void create() {
        super.create();
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }


    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("选择城市");
        FloatingBarItemDecoration mDecoration = new FloatingBarItemDecoration(getThis(), mFloatingMap);
        getRecyclerView().addItemDecoration(mDecoration);
        setOnSelectItemListener(new OnSelectItemListener() {
            @Override
            public void onSelectItem(String data, int position) {
                TenantBean bean = mDataList.get(position);
                Intent intent = new Intent();
                intent.putExtra(KEY_DATE, bean);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        mIndexBar.setOnTouchingLetterChangedListener(new IndexBar.OnTouchingLetterChangeListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                LogUtil.d("onTouchingLetterChanged: " + s);
            }

            @Override
            public void onTouchingStart(String s) {
                LogUtil.d("onTouchingStart: " + s);
            }

            @Override
            public void onTouchingEnd(String s) {
                LogUtil.d("onTouchingStart: " + s);

                int index = mFloatingIndexArray.indexOfValue(s);
                getRecyclerView().getLayoutManager().scrollToPosition(index);
            }
        });

        getAdapter().setTextmGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        getAdapter().notifyDataSetChanged();
    }

    @Override
    protected void initData() {
        super.initData();
        mIUser = RetrofitHelper.create(IUser.class);
        BaiduLocationService.start(getThis(), new BaiduLocationService.OnLocationListener() {
            @Override
            public void onReceiveLocation() {
                loadLocationData();
            }

            @Override
            public void onError() {

            }
        });
        onRefresh();
    }

    /**
     * 获取定位信息
     */
    private void loadLocationData() {
        String provinceName = BaiduLocationService.getProvince();
        String cityName = BaiduLocationService.getCity();
        mIUser.getCurrentTenantByRegionName(
                RetrofitHelper.getBody(
                        new JsonItem("provinceName", provinceName),
                        new JsonItem("cityName", cityName)))
                .enqueue(new MsgCallBack<BaseBean<TenantBean>>(getThis()) {
                    @Override
                    public void onSuccess(Call<BaseBean<TenantBean>> call, Response<BaseBean<TenantBean>> response) {
                        mLocationBean = response.body().getResult();
                        if (mLocationBean != null) {
                            mFloatingMap.put(0, "定位城市");
                            mCityLetterList.add(0, "定位");
                            mDataList.add(0, mLocationBean);
                            mFloatingIndexArray.put(0, "定位");
                            transformationDate();
                        }

                    }
                });


    }

    @Override
    protected void getData() {
        super.getData();

        mIUser.getTenants()
                .enqueue(new MsgCallBack<BaseBean<List<TenantBean>>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<List<TenantBean>>> call, Response<BaseBean<List<TenantBean>>> response) {
                        List<TenantBean> beanList = response.body().getResult();
                        if (beanList == null || beanList.size() == 0) {
                            return;
                        }
                        mFloatingMap.clear();
                        mCityLetterList.clear();
                        mFloatingMap.clear();
                        mDataList.clear();

                        loadData(beanList);
                        loadDateEnd();
                    }

                    @Override
                    public void onError(Call<BaseBean<List<TenantBean>>> call, Throwable t) {
                        super.onError(call, t);
                        loadDateEnd();
                    }
                });

    }

    /**
     * 加载数据
     *
     * @param beanList
     */
    private void loadData(List<TenantBean> beanList) {
        Map<String, List<TenantBean>> map = new HashMap<>();
        // 添加定位信息

        int count = beanList.size();
        // 按照首字母 将数据进行分组
        for (int i = 0; i < count; i++) {
            TenantBean bean = beanList.get(i);
            if (bean == null) {
                continue;
            }
            String cityLetter = bean.getCityLetter();
            List<TenantBean> beans;
            // 判断是否有首字母
            if (mCityLetterList.contains(cityLetter)) {
                beans = map.get(cityLetter);
                if(beans == null){
                    continue;
                }
                beans.add(bean);
            } else {
                beans = new ArrayList<>();
                beans.add(bean);
                map.put(cityLetter, beans);
                mCityLetterList.add(cityLetter);
                mFloatingIndexArray.put(i, cityLetter);
            }
        }
        // 按照首字母进行降序
        Collections.sort(mCityLetterList);


        if (mLocationBean != null) {
            mFloatingMap.put(0, "定位城市");
            mDataList.add(mLocationBean);
            mCityLetterList.add(0, "定位");
            mFloatingIndexArray.put(0, "定位");
        }
        int index = 1;

        // 根据首字母获取数据

        for (String cityLetter : mCityLetterList) {
            if (!map.containsKey(cityLetter)) {
                continue;
            }
            List<TenantBean> list = map.get(cityLetter);
            if (list == null || list.size() == 0) {
                continue;
            }
            mDataList.addAll(list);
            mFloatingMap.put(index, cityLetter);

            index += list.size();
        }
        transformationDate();

    }

    @Override
    protected void onDestroy() {
        BaiduLocationService.stop(getThis());
        super.onDestroy();
    }

    /**
     * 将当前数据转换为字符串 ，并刷新
     */
    private void transformationDate() {
        removeAllDate();

        List<String> dateList = new ArrayList<>();

        for (TenantBean bean : mDataList) {
            dateList.add(bean.getCityName());
        }
        addDatas(dateList);
        mIndexBar.setNavigators(mCityLetterList);
        mIndexBar.requestLayout();
        mIndexBar.invalidate();
        getAdapter().notifyDataSetChanged();
    }

    /**
     * @param activity
     */
    public static void start(Activity activity) {
        Intent starter = new Intent(activity, SelectRegionRecyclerActivity.class);
        activity.startActivityForResult(starter, REQUEST_CODE);
    }

    /**
     * @param fragment
     */
    public static void start(Fragment fragment) {
        Intent starter = new Intent(fragment.getContext(), SelectRegionRecyclerActivity.class);
        fragment.startActivityForResult(starter, REQUEST_CODE);
    }

   /* *//**
     * @param activity
     * @param bean     定位得到的信息
     *//*
    public static void start(Activity activity, TenantBean bean) {
        Intent starter = new Intent(activity, SelectRegionRecyclerActivity.class);
        starter.putExtra("locationInfo", bean);
        activity.startActivityForResult(starter, REQUEST_CODE);
    }

    *//**
     * @param fragment
     * @param bean     定位得到的信息
     *//*
    public static void start(Fragment fragment, TenantBean bean) {
        Intent starter = new Intent(fragment.getContext(), SelectRegionRecyclerActivity.class);
        starter.putExtra("locationInfo", bean);
        fragment.startActivityForResult(starter, REQUEST_CODE);
    }*/

    /**
     * 返回地区信息
     *
     * @param intent
     * @return
     */
    @NonNull
    public static TenantBean loadRegionInfo(Intent intent) {
        TenantBean bean = new TenantBean();
        if (intent == null) {
            return bean;
        }
        bean = intent.getParcelableExtra(KEY_DATE);
        return bean;
    }


}
