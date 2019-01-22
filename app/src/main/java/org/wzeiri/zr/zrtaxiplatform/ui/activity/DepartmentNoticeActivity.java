package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.Context;
import android.content.Intent;

import org.wzeiri.zr.zrtaxiplatform.adapter.DepartmentNoticePagerAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.ArticleTypeBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IArticle;
import org.wzeiri.zr.zrtaxiplatform.network.api.IRegion;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseTabLayoutActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 部门公告 如
 * 公司专区公告
 * 运管专区公告
 * 交警专区公告
 *
 * @author k-lm on 2017/11/21.
 */

public class DepartmentNoticeActivity extends BaseTabLayoutActivity<DepartmentNoticePagerAdapter> {
    private int mType = -1;

    @Override
    protected void create() {
        super.create();
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        mType = intent.getIntExtra("type", mType);
    }

    @Override
    protected DepartmentNoticePagerAdapter getPagerAdapter() {
        return new DepartmentNoticePagerAdapter(getSupportFragmentManager());
    }

    @Override
    protected void init() {
        super.init();
        switch (mType) {
            case 1:
                setCenterTitle("运管专区");
                break;
            case 2:
                setCenterTitle("交警专区");
                break;
            case 4:
                setCenterTitle("公司专区");
                break;
        }

    }

    @Override
    protected void initData() {
        super.initData();
        IArticle article = RetrofitHelper.create(IArticle.class);
        article.getArticleCategories(mType)
                .enqueue(new MsgCallBack<BaseBean<List<ArticleTypeBean>>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<List<ArticleTypeBean>>> call, Response<BaseBean<List<ArticleTypeBean>>> response) {
                        List<ArticleTypeBean> beans = response.body().getResult();
                        getAdapter().addDate(beans,mType);
                        getAdapter().notifyDataSetChanged();
                    }
                });
    }

    /**
     * @param context
     * @param type    类型,1-运管,2-交警,4-公司
     */
    public static void start(Context context, int type) {
        Intent starter = new Intent(context, DepartmentNoticeActivity.class);
        starter.putExtra("type", type);
        context.startActivity(starter);
    }
}
