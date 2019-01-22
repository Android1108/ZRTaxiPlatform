package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;


import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.BillAdapter;
import org.wzeiri.zr.zrtaxiplatform.adapter.GetOpCompaniesAdapter;
import org.wzeiri.zr.zrtaxiplatform.adapter.JobWantedAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.GetOnCompaniesBean;
import org.wzeiri.zr.zrtaxiplatform.bean.JobRecruitmentBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IDriver;
import org.wzeiri.zr.zrtaxiplatform.network.api.IJobRecruitment;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseListActivity;

import java.util.List;

import retrofit2.Call;

/**
 * 获取出租车公司
 *
 * Created by zz on 2017-12-13.
 */

public class GetOpCompaniesActivity extends BaseListActivity <Object , GetOpCompaniesAdapter> {
    private IDriver mIDriver;

    @Override
    public GetOpCompaniesAdapter getAdapter(List<Object> list) {
        return new GetOpCompaniesAdapter(list);
    }

    @Override
    protected Call<BaseBean<BaseListBean<Object>>> getNetCall(int currentIndex, int pagerSize) {
        return null;
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("出租车公司");
        getListView().setDividerHeight(getResources().getDimensionPixelOffset(R.dimen.layout_margin));
        getListView().setDivider(new ColorDrawable(ContextCompat.getColor(getThis(),R.color.gray10)));
    }

    @Override
    protected void initData() {
        super.initData();
        for (int i = 0; i < 10; i++) {
            String str = "";
            addData(str);
        }
        getAdapter().notifyDataSetChanged();
    }
}

