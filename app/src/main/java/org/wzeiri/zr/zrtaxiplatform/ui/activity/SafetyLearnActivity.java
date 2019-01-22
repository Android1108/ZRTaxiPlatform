package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.view.View;
import android.widget.AdapterView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.SafetyLearnAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.LearnVideoBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.ILearn;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseListActivity;

import java.util.List;

import butterknife.OnClick;
import retrofit2.Call;

/**
 * 安全学习
 *
 * @author k-lm on 2017/11/21.
 */

public class SafetyLearnActivity extends BaseListActivity<LearnVideoBean, SafetyLearnAdapter> {
    private ILearn mILearn;

    @Override
    public SafetyLearnAdapter getAdapter(List<LearnVideoBean> list) {
        return new SafetyLearnAdapter(list);
    }

    @Override
    protected Call<BaseBean<BaseListBean<LearnVideoBean>>> getNetCall(int currentIndex, int pagerSize) {
        if (mILearn == null) {
            mILearn = RetrofitHelper.create(ILearn.class);
        }
        return mILearn.getLearnVideos(pagerSize, currentIndex);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_safety_learn;
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("安全学习");
    }

    @Override
    protected void initData() {
        super.initData();
        onRefresh();
        //addData(new LearnVideoBean());
        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LearnVideoBean bean = getData(position);
                VideoStudyActivity.start(getThis(), bean.getId(), bean.getCoverPicture(), bean.isIsLearned());
//                VideoViewActivity.start(getThis(),bean.getVedioUrl());
            }
        });

    }


    @OnClick(R.id.aty_safety_learn_layout_practice)
    public void onViewClicked() {
        AnswerActivity.startExercises(this);
    }

    @OnClick(R.id.aty_safety_learn_layout_simulation_test)
    void onSimulationTestClick() {
        AnswerActivity.startSimulation(this);
    }

}
