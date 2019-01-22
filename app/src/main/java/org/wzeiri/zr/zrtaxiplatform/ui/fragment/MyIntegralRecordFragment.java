package org.wzeiri.zr.zrtaxiplatform.ui.fragment;

import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.MyIntegralRecordAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.GetRecordsBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IRecords;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.base.BaseListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;

/**
 * 积分记录
 *
 * @author k-lm on 2017/11/27.
 */

public class MyIntegralRecordFragment extends BaseListFragment<GetRecordsBean, MyIntegralRecordAdapter> {

    @BindView(R.id.aty_my_integral_record_text_date)
    TextView mIntegralRecordTextDate;
    @BindView(R.id.aty_my_integral_record_text_operation)
    TextView mIntegralRecordTextOperation;
    @BindView(R.id.aty_my_integral_record_text_detail)
    TextView mIntegralRecordTextDetail;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my_integral_record;
    }

    private IRecords mIRecords;


    @Override
    public MyIntegralRecordAdapter getAdapter(List<GetRecordsBean> list) {
        return new MyIntegralRecordAdapter(list);
    }


    @Override
    protected Call<BaseBean<BaseListBean<GetRecordsBean>>> getNetCall(int pagerIndex, int pagerSize) {
        if (mIRecords == null) {
            mIRecords = RetrofitHelper.create(IRecords.class);
        }

        return mIRecords.getIntegralRecords(pagerSize, pagerIndex);
    }

    @Override
    public void initData() {
        super.initData();
        onRefresh();
    }

    @Override
    protected void onLoadError(String msg) {
        super.onLoadError(msg);
    }

    @Override
    protected void onLoadSuccess(List<GetRecordsBean> list) {
        super.onLoadSuccess(list);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
