package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import org.wzeiri.zr.zrtaxiplatform.bean.LocationRegionBean;
import org.wzeiri.zr.zrtaxiplatform.bean.ProvinceBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IRegion;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.SelectListActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 选择地区 - listView
 *
 * @author k-lm on 2017/12/22.
 */

public class SelectRegionActivity extends SelectListActivity {

    private IRegion mIRegion;
    private List<ProvinceBean> mDataList = new ArrayList<>();

    /**
     * 省
     */
    public static final int PROVINCE = 103;
    /**
     * 城市
     */
    public static final int CITY = 102;
    /**
     * 区
     */
    public static final int REGION = 101;

    /**
     * 省
     */
    public static final String KEY_PROVINCE = "province";
    /**
     * 城市
     */
    public static final String KEY_CITY = "city";
    /**
     * 区
     */
    public static final String KEY_REGION = "region";

    /**
     * 省
     */
    public static final String KEY_PROVINCE_CODE = "provinceCode";
    /**
     * 城市
     */
    public static final String KEY_CITY_CODE = "cityCode";
    /**
     * 区
     */
    public static final String KEY_REGION_CODE = "regionCode";

    public static final int REQUEST_CODE = 10150;


    private String mCode = "";
    /**
     * 当前类型
     */
    private int mType = PROVINCE;

    private int mFromType = REGION;
    private int mToType = PROVINCE;


    @Override
    protected void create() {
        super.create();
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            showToast("无法加载数据");
            return;
        }


        mType = intent.getIntExtra("type", mType);
        mFromType = intent.getIntExtra("formType", mFromType);
        mToType = intent.getIntExtra("toType", mToType);

        switch (mType) {
            case PROVINCE:
                break;
            case CITY:
                mCode = intent.getStringExtra(KEY_PROVINCE_CODE);
                break;
            case REGION:
                mCode = intent.getStringExtra(KEY_CITY_CODE);
                break;
        }
    }

    @Override
    protected void init() {
        super.init();
        switch (mType) {
            case PROVINCE:
                setCenterTitle("选择省份");
                break;
            case CITY:
                setCenterTitle("选择城市");
                break;
            case REGION:
                setCenterTitle("选择地区");
                break;
        }


        // 选择
        setOnSelectItemListener(new OnSelectItemListener() {
            @Override
            public void onSelectItem(String data, int position) {
                Intent intent = new Intent(getIntent());
                String code = mDataList.get(position).getCode();
                switch (mType) {
                    case PROVINCE:
                        intent.putExtra(KEY_PROVINCE, data);
                        intent.putExtra(KEY_PROVINCE_CODE, code);
                        break;
                    case CITY:
                        intent.putExtra(KEY_CITY_CODE, code);
                        intent.putExtra(KEY_CITY, data);
                        break;
                    case REGION:
                        intent.putExtra(KEY_REGION_CODE, code);
                        intent.putExtra(KEY_REGION, data);
                        break;
                }
                if (mType == mToType) {
                    setResult(RESULT_OK, intent);
                    finish();
                    return;
                }

                int type = mType - 1;
                intent.setClass(getThis(), SelectRegionActivity.class);
                intent.putExtra("type", type);
                startActivityForResult(intent, REQUEST_CODE);

            }
        });
    }


    @Override
    protected void initData() {
        super.initData();
        onRefresh();
    }

    @Override
    protected void getData() {
        super.getData();
        if (mIRegion == null) {
            mIRegion = RetrofitHelper.create(IRegion.class);
        }

        retrofit2.Call<BaseBean<List<ProvinceBean>>> call = null;

        switch (mType) {
            case PROVINCE:
                call = mIRegion.getProvinces();
                break;
            case CITY:
                call = mIRegion.getCities(mCode);
                break;
            case REGION:
                call = mIRegion.getRegionAreas(mCode);
                break;
            default:
                return;
        }

        call.enqueue(new MsgCallBack<BaseBean<List<ProvinceBean>>>(getThis(), true) {
            @Override
            public void onSuccess(retrofit2.Call<BaseBean<List<ProvinceBean>>> call, Response<BaseBean<List<ProvinceBean>>> response) {
                List<ProvinceBean> beans = response.body().getResult();
                if (beans == null || beans.size() == 0) {
                    loadDateEnd();
                    return;
                }

                mDataList.addAll(beans);

                List<String> dataList = new ArrayList<>();
                for (ProvinceBean bean : mDataList) {
                    dataList.add(bean.getName());
                }
                addDatas(dataList);
                getAdapter().notifyDataSetChanged();
                loadDateEnd();
            }

            @Override
            public void onError(Call<BaseBean<List<ProvinceBean>>> call, Throwable t) {
                super.onError(call, t);
                loadDateEnd();
            }
        });
    }


    /**
     * @param activity
     * @param formType 开始选择的类型
     * @param toType   结束选择的类型
     */
    public static void start(Activity activity, int formType, int toType) {
        if (formType > PROVINCE) {
            formType = PROVINCE;
        } else if (formType < REGION) {
            formType = REGION;
        }

        if (toType > PROVINCE) {
            toType = PROVINCE;
        } else if (toType < REGION) {
            toType = REGION;
        }

        if (formType < toType) {
            formType = toType;
        }

        Intent starter = new Intent(activity, SelectRegionActivity.class);
        starter.putExtra("formType", formType);
        starter.putExtra("type", formType);
        starter.putExtra("toType", toType);
        activity.startActivityForResult(starter, REQUEST_CODE);
    }

    /**
     * @param fragment
     * @param formType 开始选择的类型
     * @param toType   结束选择的类型
     */
    public static void start(Fragment fragment, int formType, int toType) {
        if (formType > PROVINCE) {
            formType = PROVINCE;
        } else if (formType < REGION) {
            formType = REGION;
        }

        if (toType > PROVINCE) {
            toType = PROVINCE;
        } else if (toType < REGION) {
            toType = REGION;
        }

        if (formType > toType) {
            formType = toType;
        }

        Intent starter = new Intent(fragment.getContext(), SelectRegionActivity.class);
        starter.putExtra("formType", formType);
        starter.putExtra("type", formType);
        starter.putExtra("toType", toType);
        fragment.startActivityForResult(starter, REQUEST_CODE);
    }

    /**
     * 返回地区信息
     *
     * @param intent
     * @return
     */
    @NonNull
    public static LocationRegionBean loadRegionInfo(Intent intent) {
        LocationRegionBean bean = new LocationRegionBean();
        if (intent == null) {
            return bean;
        }

        bean.setAreaCode(intent.getStringExtra(KEY_REGION_CODE));
        bean.setAreaName(intent.getStringExtra(KEY_REGION));
        bean.setCityCode(intent.getStringExtra(KEY_CITY_CODE));
        bean.setCityName(intent.getStringExtra(KEY_CITY));
        bean.setProvinceCode(intent.getStringExtra(KEY_PROVINCE_CODE));
        bean.setProvinceName(intent.getStringExtra(KEY_PROVINCE));


        return bean;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && requestCode == REQUEST_CODE) {
            setResult(RESULT_OK, data);
            finish();
            return;
        }


    }
}
