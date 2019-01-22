package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.SelectRefuelingCardAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.OilCardBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IOilCard;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseListActivity;
import org.wzeiri.zr.zrtaxiplatform.util.RefuelCardHelper;

import java.util.List;

import butterknife.OnClick;
import retrofit2.Call;

/**
 * 选择加油卡
 *
 * @author k-lm on 2018/1/5.
 */

public class SelectRefuelingCard extends BaseListActivity<OilCardBean, SelectRefuelingCardAdapter> {

    public static final int REQUEST_CODE = 10020;

    private IOilCard mIOilCard;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_refueling_card;
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("选择加油卡");

        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OilCardBean bean = getData(position);
                Intent intent = new Intent();
                intent.putExtra("carInfo", bean);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public SelectRefuelingCardAdapter getAdapter(List<OilCardBean> list) {
        return new SelectRefuelingCardAdapter(list);
    }

    @Override
    protected Call<BaseBean<BaseListBean<OilCardBean>>> getNetCall(int currentIndex, int pagerSize) {
        if (mIOilCard == null) {
            mIOilCard = RetrofitHelper.create(IOilCard.class);
        }
        return mIOilCard.getOilCards(pagerSize, currentIndex);
    }


    @Override
    protected void initData() {
        super.initData();
//        onRefresh();
        setCurrentPagerIndex(2);
        addDatas(RefuelCardHelper.getInstance().getCardDateList());
        getAdapter().notifyDataSetChanged();

    }

    @OnClick(R.id.aty_select_refueling_card_layout_add_card)
    void addCard() {
        startActivityForResult(AddRefuelingCardActivity.class, AddRefuelingCardActivity.REQUEST_CODE);
    }

    @NonNull
    public static OilCardBean getResultData(Intent intent) {
        if (intent == null) {
            return new OilCardBean();
        }
        OilCardBean bean = intent.getParcelableExtra("carInfo");
        if (bean == null) {
            bean = new OilCardBean();
        }
        return bean;

    }


    @Override
    protected void onLoadSuccess(List<OilCardBean> list) {
        int size = getSize();
        if (size == 0) {
            RefuelCardHelper.getInstance().clearCardDate();
            RefuelCardHelper.getInstance().addCardDateList(list);
        }
        super.onLoadSuccess(list);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == AddRefuelingCardActivity.REQUEST_CODE) {
            onRefresh();
        }
    }
}
