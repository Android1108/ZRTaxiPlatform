package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.ArticleDetailBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IArticle;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseWebActivity;
import org.wzeiri.zr.zrtaxiplatform.util.DensityUtil;
import org.wzeiri.zr.zrtaxiplatform.util.TimeUtil;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 文章详情
 *
 * @author k-lm on 2017/12/29.
 */

public class ArticleDetailActivity extends BaseWebActivity {

    @BindView(R.id.aty_article_detail_text_title)
    TextView mTextTitle;
    @BindView(R.id.aty_article_detail_text_info)
    TextView mTextInfo;
    private String mTitle;
    private int mId;
    private String mSource;
    private String mTime;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_article_detail;
    }

    @Override
    protected void create() {
        super.create();
        Intent intent = getIntent();
        if (intent == null) {
            showToast("未找到相关公告");
            return;
        }
        mId = intent.getIntExtra("id", mId);
        mTitle = intent.getStringExtra("title");
        mTime = intent.getStringExtra("time");
        mSource = intent.getStringExtra("source");



    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("详情");
        mTextTitle.setText(mTitle);
        String info = "发布时间" + mTime + "　" + "来源 " + mSource;
        mTextInfo.setText(info);
    }

    @Override
    protected void initData() {
        super.initData();

        IArticle iArticle = RetrofitHelper.create(IArticle.class);

        iArticle.getArticleDetail(mId)
                .enqueue(new MsgCallBack<BaseBean<ArticleDetailBean>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<ArticleDetailBean>> call, Response<BaseBean<ArticleDetailBean>> response) {
                        ArticleDetailBean bean = response.body().getResult();
                        if (bean == null) {
                            return;
                        }
                        loadData(bean.getContent());
                    }
                });

    }


    public static void start(Context context, String title, Date time, String source, int id) {
        Intent starter = new Intent(context, ArticleDetailActivity.class);
        starter.putExtra("title", title);
        starter.putExtra("id", id);
        starter.putExtra("time", TimeUtil.getServerDate(time));
        starter.putExtra("source", source);
        context.startActivity(starter);
    }

}
