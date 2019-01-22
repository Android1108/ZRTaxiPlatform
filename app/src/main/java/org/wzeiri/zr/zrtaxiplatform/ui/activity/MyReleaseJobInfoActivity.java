package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.view.View;
import android.widget.AdapterView;

import org.wzeiri.zr.zrtaxiplatform.adapter.MyReleaseJobInfoAdapter;
import org.wzeiri.zr.zrtaxiplatform.adapter.inter.OnItemClickDeleteListener;
import org.wzeiri.zr.zrtaxiplatform.bean.JobRecruitmentBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IJobRecruitment;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItem;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseListActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 我发布的求职信息
 *
 * @author k-lm on 2017/12/13.
 */

public class MyReleaseJobInfoActivity extends BaseListActivity<JobRecruitmentBean, MyReleaseJobInfoAdapter> {

    private IJobRecruitment mIJobRecruitment;


    @Override
    public MyReleaseJobInfoAdapter getAdapter(List<JobRecruitmentBean> list) {
        return new MyReleaseJobInfoAdapter(list);
    }

    @Override
    protected Call<BaseBean<BaseListBean<JobRecruitmentBean>>> getNetCall(int currentIndex, int pagerSize) {
        if (mIJobRecruitment == null) {
            mIJobRecruitment = RetrofitHelper.create(IJobRecruitment.class);
        }
        return mIJobRecruitment.getMyJobRecruitments(1, pagerSize, currentIndex);
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("求职信息");

        getAdapter().setOnItemClickDeleteListener(new OnItemClickDeleteListener() {
            @Override
            public void onItemClickDelete(int position) {
                deleteItem(position);
            }
        });

        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JobRecruitmentBean bean = getData(position);
//                MyReleaseCandidateDetailActivity.start(getThis(), bean.getId());
                JobInfoDetailActivity.start(getThis(), bean.getId(), false);
            }
        });
    }

    /**
     * 删除发布的求职信息
     *
     * @param position
     */
    private void deleteItem(int position) {
        JobRecruitmentBean bean = getData(position);
        mIJobRecruitment.deleteJobRecruitment(
                RetrofitHelper.getBody(
                        new JsonItem("id", bean.getId())))
                .enqueue(new MsgCallBack<BaseBean<String>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                        onRefresh();
                        showToast("删除成功");
                    }
                });
    }

    @Override
    protected void initData() {
        super.initData();
        onRefresh();
    }
}
