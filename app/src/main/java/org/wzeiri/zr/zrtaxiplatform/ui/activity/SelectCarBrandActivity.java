package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.app.Activity;
import android.content.Intent;

import org.wzeiri.zr.zrtaxiplatform.bean.DriverBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IDriver;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.SelectListActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 选择品牌
 *
 * @author k-lm on 2017/12/18.
 */

public class SelectCarBrandActivity extends SelectListActivity {

    private IDriver mIDriver;

    private List<DriverBean> mBrandBeanList = new ArrayList<>();


    public static final int REQUEST_CODE = 10901;


    @Override
    protected void create() {
        super.create();

        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        mSelectId = intent.getIntExtra(KEY_SELECT_ID, mSelectId);

    }

    @Override
    protected void initData() {
        super.initData();
        getData();
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("品牌");
        setOnSelectItemListener(new OnSelectItemListener() {
            @Override
            public void onSelectItem(String data, int position) {
                Intent intent = new Intent();
                mSelectId = mBrandBeanList.get(position).getId();
                intent.putExtra(KEY_SELECT_ID, mSelectId);
                intent.putExtra(KEY_SELECT_NAME, data);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void getData() {
        super.getData();

        if (mIDriver == null) {
            mIDriver = RetrofitHelper.create(IDriver.class);
        }

        mIDriver.getBrands()
                .enqueue(new MsgCallBack<BaseBean<List<DriverBean>>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<List<DriverBean>>> call, Response<BaseBean<List<DriverBean>>> response) {
                        List<DriverBean> brandBeanList = response.body().getResult();
                        if (brandBeanList == null || brandBeanList.size() == 0) {
                            loadDateEnd();
                            return;
                        }
                        mBrandBeanList.clear();
                        mBrandBeanList.addAll(brandBeanList);

                        List<String> dateList = new ArrayList<>();
                        int count = mBrandBeanList.size();
                        int selectIndex = -1;
                        for (int i = 0; i < count; i++) {
                            DriverBean bean = mBrandBeanList.get(i);
                            dateList.add(bean.getName());

                            if (bean.getId() == mSelectId) {
                                selectIndex = i;
                            }
                        }
                        removeAll();
                        addDatas(dateList);
                        getAdapter().setSelectPosition(selectIndex);
                        getAdapter().notifyDataSetChanged();
                        loadDateEnd();
                    }

                    @Override
                    public void onError(Call<BaseBean<List<DriverBean>>> call, Throwable t) {
                        super.onError(call, t);
                        loadDateEnd();
                    }
                });
    }


    public static void start(Activity activity, int id) {
        Intent starter = new Intent(activity, SelectCarBrandActivity.class);
        starter.putExtra(KEY_SELECT_ID, id);
        activity.startActivityForResult(starter, REQUEST_CODE);
    }


}
