package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseWebActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 发现详情
 *
 * @author k-lm on 2017/11/25.
 */

public class FindNewsDetailActivity extends ActionbarActivity {
    @BindView(R.id.aty_find_news_detail_text_title)
    TextView mTextTitle;
    @BindView(R.id.aty_find_news_detail_text_date)
    TextView mTextDate;
    @BindView(R.id.aty_find_news_detail_text_source)
    TextView mTextSource;
    @BindView(R.id.aty_find_news_detail_web_content)
    WebView mWebContent;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_find_news_detail;
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("详情");

        mWebContent.setWebViewClient(new BaseWebActivity.BaseWebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.loadUrl("javascript:document.body.style.paddingBottom=\"" +
                        getResources().getDimensionPixelOffset(R.dimen.layout_margin)
                        + "px\"; void 0");
            }
        });
    }
}
