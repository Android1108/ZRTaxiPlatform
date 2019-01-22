package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseWebActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 展示web数据的activity
 *
 * @author k-lm on 2018/1/18.
 */

public class WebActivity extends BaseWebActivity {

    @BindView(R.id.aty_web_layout_load_error)
    LinearLayout mLoadError;


    private String mUrl;


    @OnClick(R.id.aty_web_layout_load_error)
    void onLoadUrl() {
        if (TextUtils.isEmpty(mUrl)) {
            return;
        }
        showProgressDialog("正在重新加载网页，请稍候");
        getWebView().loadUrl(mUrl);
        /*getWebView().setVisibility(View.VISIBLE);
        mLoadError.setVisibility(View.GONE);*/
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("详情");
        WebSettings webSettings = getWebView().getSettings();
        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);


        getWebView().setWebViewClient(new BaseWebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                mLoadError.setVisibility(View.VISIBLE);
                getWebView().setVisibility(View.GONE);
            }
        });


        getWebView().setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    closeProgressDialog();
                    view.setVisibility(View.VISIBLE);
                    mLoadError.setVisibility(View.GONE);
                }
            }
        });
    }


    @Override
    protected void initData() {
        super.initData();

        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        String url = intent.getStringExtra("url");
        mUrl = url;
        if (!TextUtils.isEmpty(url)) {
            getWebView().loadUrl(url);
            return;
        }

        String content = intent.getStringExtra("content");
        loadData(content);

    }

    public static void startUrl(Context context, String url) {
        Intent starter = new Intent(context, WebActivity.class);
        starter.putExtra("url", url);
        context.startActivity(starter);
    }

    public static void startContent(Context context, String content) {
        Intent starter = new Intent(context, WebActivity.class);
        starter.putExtra("content", content);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
