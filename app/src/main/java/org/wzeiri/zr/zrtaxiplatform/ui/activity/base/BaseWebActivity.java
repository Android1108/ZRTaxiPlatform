package org.wzeiri.zr.zrtaxiplatform.ui.activity.base;


import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.util.DensityUtil;
import org.wzeiri.zr.zrtaxiplatform.util.HtmlUtil;

import butterknife.BindView;
// JobInfoDetailActivity、CarTransactionDetailActivity 有用到webView，如果对webView有改动，
// 需要对这两个activity的webView也进行相应的改动

/**
 * @author k-lm on 2017/12/19.
 */

public abstract class BaseWebActivity extends ActionbarActivity {
    @BindView(R.id.layout_web_view)
    WebView mWebView;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_web_view;
    }


    @Override
    void initView() {
        super.initView();
        WebSettings settings = mWebView.getSettings();
        settings.setBlockNetworkImage(false);
        settings.setDomStorageEnabled(true);
        settings.setDefaultTextEncodingName("utf-8");
        // 缩放webView整体的比例，使内容适配屏幕
       /* settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);*/

        mWebView.setWebViewClient(new BaseWebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());
    }

    protected void loadData(String data) {
        data = HtmlUtil.getImageStyle(data);
        data = HtmlUtil.getBodyAdaptableScreenStyle(data);
        mWebView.loadDataWithBaseURL(null, data, "text/html", "UTF-8", null);
    }

    protected WebView getWebView() {
        return mWebView;
    }

    public static class BaseWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
