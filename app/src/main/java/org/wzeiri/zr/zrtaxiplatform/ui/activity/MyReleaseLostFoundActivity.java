package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.view.View;
import android.widget.AdapterView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.MyReleaseLostFoundAdapter;
import org.wzeiri.zr.zrtaxiplatform.adapter.inter.OnItemClickDeleteListener;
import org.wzeiri.zr.zrtaxiplatform.bean.LostFoundBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.ILostFound;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItem;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseListActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 我发布的失物招领
 *
 * @author k-lm on 2017/12/13.
 */

public class MyReleaseLostFoundActivity extends BaseListActivity<LostFoundBean, MyReleaseLostFoundAdapter> {
    private ILostFound mILostFound;

    @Override
    public MyReleaseLostFoundAdapter getAdapter(List<LostFoundBean> list) {
        return new MyReleaseLostFoundAdapter(list);
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("失物招领");
        int dividerHeight = getResources().getDimensionPixelOffset(R.dimen.layout_margin);
        getListView().setDividerHeight(dividerHeight);

        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LostFoundBean bean = getData(position);
                FinancialLossDetailActivity.start(getThis(), bean.getId(), true);
            }
        });


        getAdapter().setOnItemClickDeleteListener(new OnItemClickDeleteListener() {
            @Override
            public void onItemClickDelete(int position) {
                deleteItem(position);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        onRefresh();
    }

    private void deleteItem(int position) {
        LostFoundBean bean = getData(position);
        mILostFound.deleteLostFound(
                RetrofitHelper.getBody(
                        new JsonItem("id", bean.getId())
                )
        ).enqueue(new MsgCallBack<BaseBean<String>>(getThis(), true) {
            @Override
            public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                onRefresh();
                showToast("删除成功");
            }
        });
    }

    @Override
    protected Call<BaseBean<BaseListBean<LostFoundBean>>> getNetCall(int currentIndex, int pagerSize) {
        if (mILostFound == null) {
            mILostFound = RetrofitHelper.create(ILostFound.class);
        }
        return mILostFound.getMyLostFounds(2, pagerSize, currentIndex);
    }
}
