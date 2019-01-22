package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import org.wzeiri.zr.zrtaxiplatform.bean.LegalAdviceTypeBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.ILegal;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.SelectListActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 选择咨询类型
 *
 * @author k-lm on 2017/12/26.
 */

public class SelectLegalAdviceTypeActivity extends SelectListActivity {

    public static final int REQUSET_CODE = 10041;


    private ILegal mILegal;

    private List<LegalAdviceTypeBean> mLegalAdviceTypeList = new ArrayList<>();

    private int mTypeId = -1;

    @Override
    protected void create() {
        super.create();
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        mTypeId = intent.getIntExtra("type", mTypeId);
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("法律咨询");

        setOnSelectItemListener(new OnSelectItemListener() {
            @Override
            public void onSelectItem(String data, int position) {
                Intent intent = new Intent();
                intent.putExtra("typeName", data);
                intent.putExtra("typeId", mLegalAdviceTypeList.get(position).getLegalType());
                setResult(RESULT_OK, intent);
                finish();
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

        if (mILegal == null) {
            mILegal = RetrofitHelper.create(ILegal.class);
        }

        mILegal.getLegalTypies()
                .enqueue(new MsgCallBack<BaseBean<List<LegalAdviceTypeBean>>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<List<LegalAdviceTypeBean>>> call, Response<BaseBean<List<LegalAdviceTypeBean>>> response) {
                        List<LegalAdviceTypeBean> beanList = response.body().getResult();
                        if (beanList == null || beanList.size() == 0) {
                            loadDateEnd();
                            return;
                        }
                        mLegalAdviceTypeList.clear();
                        mLegalAdviceTypeList.addAll(beanList);

                        int count = mLegalAdviceTypeList.size();
                        for (int i = 0; i < count; i++) {
                            LegalAdviceTypeBean bean = mLegalAdviceTypeList.get(i);
                            addData(bean.getDisplayValue());

                            if (bean.getLegalType() == mTypeId) {
                                getAdapter().setSelectPosition(i);
                            }
                        }

                        getAdapter().notifyDataSetChanged();
                        loadDateEnd();
                    }

                    @Override
                    public void onError(Call<BaseBean<List<LegalAdviceTypeBean>>> call, Throwable t) {
                        super.onError(call, t);
                        loadDateEnd();
                    }
                });

    }


    public static void start(Activity activity, int typeId) {
        Intent starter = new Intent(activity, SelectLegalAdviceTypeActivity.class);
        starter.putExtra("type", typeId);
        activity.startActivityForResult(starter, REQUSET_CODE);
    }

    @NonNull
    public static LegalAdviceTypeBean getSelectData(Intent data) {
        LegalAdviceTypeBean bean = new LegalAdviceTypeBean();
        if (data == null) {
            return bean;
        }

        String name = data.getStringExtra("typeName");
        int id = data.getIntExtra("typeId", -1);
        bean.setLegalType(id);
        bean.setDisplayValue(name);

        return bean;

    }
}
