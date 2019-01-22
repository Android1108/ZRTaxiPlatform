package org.wzeiri.zr.zrtaxiplatform.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import org.wzeiri.zr.zrtaxiplatform.adapter.ArticleAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.CivilizationArticleBean;
import org.wzeiri.zr.zrtaxiplatform.bean.TenantBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IArticle;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.ArticleDetailActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.base.BaseListFragment;
import org.wzeiri.zr.zrtaxiplatform.util.UserInfoHelper;

import java.util.List;

import retrofit2.Call;

/**
 * 发现文章动态
 *
 * @author k-lm on 2017/11/21.
 */

public class FindArticleFragment extends BaseListFragment<CivilizationArticleBean, ArticleAdapter> {
    private int mTypeId = -1;

    private IArticle mIArticle;

    /**
     * 是否切换城市
     */
    private boolean mIsChangeCity = false;

    @Override
    public ArticleAdapter getAdapter(List<CivilizationArticleBean> list) {
        return new ArticleAdapter(list);
    }

    @Override
    protected Call<BaseBean<BaseListBean<CivilizationArticleBean>>> getNetCall(int pagerIndex, int pagerSize) {
        if (mIArticle == null) {
            mIArticle = RetrofitHelper.create(IArticle.class);
        }

        return mIArticle.getDiscoveryArticles(mTypeId, pagerSize, pagerIndex);
    }

    @Override
    public void create() {
        super.create();
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }

        mTypeId = bundle.getInt("typeId", mTypeId);
    }


    @Override
    public void init() {
        super.init();
        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CivilizationArticleBean bean = getData(position);
                ArticleDetailActivity.start(getContext(),
                        bean.getTitle(),
                        bean.getCreationTime(),
                        bean.getSource(),
                        bean.getId());
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        // 切换城市刷新数据
        onRefresh();
    }

    /**
     * 切换城市刷新数据
     */
    public void changeCityRefresh() {
        mIsChangeCity = true;
        if (getView() != null ) {
            setShowProgressDialog(false);
            onRefresh();
            mIsChangeCity = false;
            setShowProgressDialog(true);
        }
    }

    @Override
    public void onLoadData() {
        super.onLoadData();
        if (mIsChangeCity && getView() != null) {
            setShowProgressDialog(false);
            onRefresh();
            mIsChangeCity = false;
            setShowProgressDialog(true);
        }
    }

    /**
     * @param typeId 分类id ,1-运管,2-交警,4-公司
     * @return
     */
    public static FindArticleFragment newInstance(int typeId) {

        Bundle args = new Bundle();
        args.putInt("typeId", typeId);
        FindArticleFragment fragment = new FindArticleFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
