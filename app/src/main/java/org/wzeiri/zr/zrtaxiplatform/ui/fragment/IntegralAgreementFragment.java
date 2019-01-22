package org.wzeiri.zr.zrtaxiplatform.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.wzeiri.zr.zrtaxiplatform.R;

import butterknife.BindView;

/**
 * 积分规则
 *
 * Created by zz on 2017-12-18.
 */

public class IntegralAgreementFragment extends Fragment {

    @BindView(R.id.fragment_integral_agreement_webview)
    WebView mWebview;

    public int getLayoutId() {
        return  R.layout.fragment_integral_agreement;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (mWebview.canGoBack()) {
//                mWebview.goBack();//返回上一页面
//            } else {
//                System.exit(0);//退出程序
//            }
//        }
//        return super.onKeyDown(keyCode, event);
//    }


    private void init() {
        mWebview.getSettings().setJavaScriptEnabled(false);
        mWebview.getSettings().setSupportZoom(false);
        mWebview.getSettings().setBuiltInZoomControls(false);
        mWebview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebview.getSettings().setDefaultFontSize(18);
        mWebview.loadUrl("http://www.baidu.com");
        mWebview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String request) {
//                return super.shouldOverrideUrlLoading(view, request);
                mWebview.loadUrl(request);
                return true;
            }
        });
    }

}
