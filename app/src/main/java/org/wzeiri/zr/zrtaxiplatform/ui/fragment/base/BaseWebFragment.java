package org.wzeiri.zr.zrtaxiplatform.ui.fragment.base;


import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;

import butterknife.BindView;

/**
 * @author k-lm on 2017/12/19.
 */

public abstract class BaseWebFragment extends BaseFragment {
    @BindView(R.id.layout_web_view)
    protected WebView mWebView;

    @Override
    public int getLayoutId() {
        return R.layout.layout_web_view;
    }


    @Override
    void initView() {
        super.initView();
        WebSettings settings = mWebView.getSettings();
        settings.setBlockNetworkImage(false);
        settings.setDomStorageEnabled(true);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.setWebViewClient(new BaseWebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());
    }


    protected static class BaseWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    protected void loadData(String content) {
        mWebView.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null);
    }

}
