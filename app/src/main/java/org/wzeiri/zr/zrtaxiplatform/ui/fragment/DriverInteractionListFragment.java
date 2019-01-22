package org.wzeiri.zr.zrtaxiplatform.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import org.wzeiri.zr.zrtaxiplatform.adapter.DriverInteractionAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.DriverInteractionPostBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IPost;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.DriverInteractionDetailActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.DriverInteractionDetailActivity1;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.base.BaseListFragment;

import java.util.List;

import retrofit2.Call;

/**
 * 司机互动
 *
 * @author k-lm on 2017/11/29.
 */

public class DriverInteractionListFragment extends BaseListFragment<DriverInteractionPostBean, DriverInteractionAdapter> {
    /**
     * 板块id
     */
    private int mId = -1;

    private IPost mIPost;


    @Override
    public DriverInteractionAdapter getAdapter(List<DriverInteractionPostBean> list) {
        return new DriverInteractionAdapter(list);
    }

    @Override
    protected Call<BaseBean<BaseListBean<DriverInteractionPostBean>>> getNetCall(int currentIndex, int pagerSize) {

        if (mIPost == null) {
            mIPost = RetrofitHelper.create(IPost.class);
        }
        return mIPost.getPosts(mId, pagerSize, currentIndex);
    }


    @Override
    public void create() {
        super.create();
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        mId = bundle.getInt("id", mId);
    }

    @Override
    public void init() {
        super.init();
        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DriverInteractionPostBean bean = getData(position);
                DriverInteractionDetailActivity.start(getContext(),bean.getId());
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
         onRefresh();
    }


    public static DriverInteractionListFragment newInstance(int id) {

        Bundle args = new Bundle();
        args.putInt("id", id);
        DriverInteractionListFragment fragment = new DriverInteractionListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public int getSelectId(){
        return mId;
    }

}
