package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.app.Activity;
import android.content.Context;
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
 * 选择出租车公司
 *
 * @author k-lm on 2017/12/19.
 */

public class SelectTaxiCompanyActivity extends SelectListActivity {

    private IDriver mIDriver;

    private List<DriverBean> mCompanyList = new ArrayList<>();
    /**
     * 地区id
     */
    private int mTenantId = -1;

    public static final int REQUEST_CODE = 10061;

    @Override
    protected void create() {
        super.create();

        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        mSelectId = intent.getIntExtra(KEY_SELECT_ID, mSelectId);
        mTenantId = intent.getIntExtra("tenantId", mTenantId);
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("出租车公司");
        onRefresh();

        setOnSelectItemListener(new OnSelectItemListener() {
            @Override
            public void onSelectItem(String data, int position) {
                int id = mCompanyList.get(position).getId();
                Intent intent = new Intent();
                intent.putExtra(KEY_SELECT_NAME, data);
                intent.putExtra(KEY_SELECT_ID, id);
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

        mIDriver.getOpCompanies(mTenantId)
                .enqueue(new MsgCallBack<BaseBean<List<DriverBean>>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<List<DriverBean>>> call, Response<BaseBean<List<DriverBean>>> response) {
                        List<DriverBean> driverBeanList = response.body().getResult();

                        if (driverBeanList == null || driverBeanList.size() == 0) {
                            loadDateEnd();
                            return;
                        }
                        removeAll();
                        mCompanyList.clear();
                        mCompanyList.addAll(driverBeanList);
                        int count = mCompanyList.size();
                        List<String> dataList = new ArrayList<>(count);
                        for (int i = 0; i < count; i++) {
                            DriverBean bean = mCompanyList.get(i);
                            dataList.add(bean.getName());

                            if (mSelectId == bean.getId()) {
                                getAdapter().setSelectPosition(i);
                            }
                        }
                        addDatas(dataList);
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


    /**
     * 选择公司名称
     *
     * @param activity
     * @param selectId 已选择的id
     * @param tenantId 地区id
     */
    public static void start(Activity activity, int selectId, int tenantId) {
        Intent starter = new Intent(activity, SelectTaxiCompanyActivity.class);
        starter.putExtra(KEY_SELECT_ID, selectId);
        starter.putExtra("tenantId", tenantId);
        activity.startActivityForResult(starter, REQUEST_CODE);
    }


}
