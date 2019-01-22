package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.LostFoundAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;
import org.wzeiri.zr.zrtaxiplatform.bean.LostFoundBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.ILostFound;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseListActivity;
import org.wzeiri.zr.zrtaxiplatform.util.UserInfoHelper;

import java.util.List;

import retrofit2.Call;

/**
 * 财务丢失
 *
 * @author k-lm on 2017/11/20.
 */

public class FinancialLossActivity extends BaseListActivity<LostFoundBean, LostFoundAdapter> {
    private ILostFound mILostFound;


    @Override
    public LostFoundAdapter getAdapter(List<LostFoundBean> list) {
        return new LostFoundAdapter(list);
    }

    @Override
    protected Call<BaseBean<BaseListBean<LostFoundBean>>> getNetCall(int pagerIndex, int pagerSize) {
        if (mILostFound == null) {
            mILostFound = RetrofitHelper.create(ILostFound.class);
        }
        return mILostFound.getLostFounds(pagerIndex, pagerSize);
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("财物丢失");
        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LostFoundBean bean = getAdapter().getData(position);
                FinancialLossDetailActivity.start(getThis(), bean.getId(), false);
            }
        });

        mListView.setDividerHeight(getResources().getDimensionPixelOffset(R.dimen.layout_margin));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_release_lost_found, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_release_lost_found:

                // 检测是否认证
                if (!UserInfoHelper.getInstance().isAuthentication(getThis())) {
                    return false;
                }
                // 检测是否切换地区
                if (UserInfoHelper.getInstance().isSwitchRegion(getThis())) {
                    return false;
                }

                startActivityForResult(ReleaseLostFoundActivity.class,
                        ReleaseLostFoundActivity.REQUEST_CODE);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initData() {
        super.initData();
        onRefresh();
    }

    @Override
    protected void onLoadSuccess(List<LostFoundBean> list) {
        super.onLoadSuccess(list);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == ReleaseLostFoundActivity.REQUEST_CODE) {
            setResult(RESULT_OK);
            finish();
        }

    }
}
