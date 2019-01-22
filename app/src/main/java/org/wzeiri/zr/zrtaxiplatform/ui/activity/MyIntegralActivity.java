package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.support.v4.view.ViewPager;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.MyIntegralPagerAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.GetRecordsBean;
import org.wzeiri.zr.zrtaxiplatform.bean.IntegralBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IRecords;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseTabLayoutActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 我的积分
 *
 * @author k-lm on 2017/11/27.
 */

public class MyIntegralActivity extends BaseTabLayoutActivity<MyIntegralPagerAdapter> {

    @BindView(R.id.aty_my_integral_text_integral)
    TextView mTextIntegral;
    @BindView(R.id.layout_tab_layout_pager)
    ViewPager mTablayoutPager;

    private IRecords mIRecords;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_integral;
    }

    @Override
    protected MyIntegralPagerAdapter getPagerAdapter() {
        return new MyIntegralPagerAdapter(getSupportFragmentManager());
    }


    @Override
    protected void init() {
        super.init();
        setCenterTitle("我的积分");
        loadData();
    }


    private void loadData() {
        if (mIRecords == null) {
            mIRecords = RetrofitHelper.create(IRecords.class);
        }
        mIRecords.getIntegral(1, 1)
                .enqueue(new MsgCallBack<BaseBean<IntegralBean>>(this, true) {
                             @Override
                             public void onSuccess(Call<BaseBean<IntegralBean>> call, Response<BaseBean<IntegralBean>> response) {
                                 IntegralBean bean = response.body().getResult();
                                 mTextIntegral.setText(bean.getIntegralCount() + "");
                             }
                         }
                );
    }


}