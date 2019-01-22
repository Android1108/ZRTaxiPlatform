package org.wzeiri.zr.zrtaxiplatform.ui.fragment;

import android.os.Bundle;

import org.wzeiri.zr.zrtaxiplatform.adapter.MyLegalAdviceReplyAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.LegalBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.ILegal;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.base.BaseListFragment;

import java.util.List;

import retrofit2.Call;

/**
 * 咨询回复
 *
 * @author k-lm on 2017/12/6.
 */

public class LegalAdviceReplyFragment extends BaseListFragment<LegalBean, MyLegalAdviceReplyAdapter> {
    private int mTypeKey = -1;
    private ILegal mILegal;

    @Override
    public MyLegalAdviceReplyAdapter getAdapter(List<LegalBean> list) {
        return new MyLegalAdviceReplyAdapter(list, true,false);
    }

    @Override
    protected Call<BaseBean<BaseListBean<LegalBean>>> getNetCall(int pagerIndex, int pagerSize) {
        if (mILegal == null) {
            mILegal = RetrofitHelper.create(ILegal.class);
        }


        return mILegal.getLegals(true,
                mTypeKey,
                pagerSize,
                pagerIndex);
    }

    @Override
    public void create() {
        super.create();
        Bundle bundle = getArguments();

        if (bundle == null) {
            return;
        }

        mTypeKey = bundle.getInt("typeKey", mTypeKey);


    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void initData() {
        super.initData();
        onRefresh();

    }


    public static LegalAdviceReplyFragment newInstance(int typeKey) {

        Bundle args = new Bundle();
        args.putInt("typeKey", typeKey);

        LegalAdviceReplyFragment fragment = new LegalAdviceReplyFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
