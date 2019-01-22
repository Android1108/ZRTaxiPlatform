package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.view.View;
import android.widget.AdapterView;

import org.wzeiri.zr.zrtaxiplatform.adapter.LearningRecordAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.LearnRecordBean;
import org.wzeiri.zr.zrtaxiplatform.bean.LearnRecordBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.ILearn;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseListActivity;

import java.util.List;

import retrofit2.Call;

/**
 * 学习记录
 *
 * @author k-lm on 2017/11/27.
 */

public class LearningRecordActivity extends BaseListActivity<LearnRecordBean, LearningRecordAdapter> {
    @Override
    public LearningRecordAdapter getAdapter(List<LearnRecordBean> list) {
        /*return new LearningRecordAdapter(list);*/
        return new LearningRecordAdapter(list);
    }

    private ILearn mILearn;

    @Override
    protected Call<BaseBean<BaseListBean<LearnRecordBean>>> getNetCall(int currentIndex, int pagerSize) {
        if (mILearn == null) {
            mILearn = RetrofitHelper.create(ILearn.class);
        }
        return mILearn.getLearnRecords(pagerSize, currentIndex);
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("学习记录");
        getAdapter().notifyDataSetChanged();

        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // LearnRecordBean bean = getAdapter().getData(position);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        onRefresh();
    }


}
