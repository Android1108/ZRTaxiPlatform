package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.view.View;
import android.widget.AdapterView;

import org.wzeiri.zr.zrtaxiplatform.adapter.CivilizedSocietyAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.CivilizationArticleBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IArticle;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseListActivity;

import java.util.List;

import retrofit2.Call;

/**
 * 文明创建
 *
 * @author k-lm on 2017/11/21.
 */

public class CivilizedSocietyActivity extends BaseListActivity<CivilizationArticleBean, CivilizedSocietyAdapter> {

    private IArticle mIArticle;

    @Override
    public CivilizedSocietyAdapter getAdapter(List<CivilizationArticleBean> list) {
        return new CivilizedSocietyAdapter(list);
    }

    @Override
    protected Call<BaseBean<BaseListBean<CivilizationArticleBean>>> getNetCall(int currentIndex, int pagerSize) {
        if (mIArticle == null) {
            mIArticle = RetrofitHelper.create(IArticle.class);
        }

        return mIArticle.getCivilizationArticles(pagerSize, currentIndex);
    }


    @Override
    protected void init() {
        super.init();
        setCenterTitle("文明创建");

        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CivilizationArticleBean bean = getData(position);
                ArticleDetailActivity.start(getThis(),
                        bean.getTitle(),
                        bean.getCreationTime(),
                        bean.getSource(),
                        bean.getId());
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        onRefresh();
      /*  for (int i = 0; i < 10; i++) {
            addData("");
        }
        getAdapter().notifyDataSetChanged();*/
    }
}
