package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.graphics.Color;
import android.view.ViewGroup;
import android.webkit.WebSettings;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.ISundry;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseWebActivity;
import org.wzeiri.zr.zrtaxiplatform.util.HtmlUtil;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 每月之星
 *
 * @author k-lm on 2018/1/8.
 */

public class ArtyStarActivity extends BaseWebActivity {


    @Override
    protected void init() {
        super.init();
        setCenterTitle("每月之星");
        WebSettings settings = getWebView().getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        setContentBackgroundColor(Color.WHITE);

        int margin = getResources().getDimensionPixelOffset(R.dimen.layout_margin_tiny);
        ViewGroup.LayoutParams layoutParams = getWebView().getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) layoutParams).setMargins(margin,
                    margin,
                    margin,
                    0);
        }

    }

    @Override
    protected void initData() {
        super.initData();
        ISundry iSundry = RetrofitHelper.create(ISundry.class);

        iSundry.getArtyStar()
                .enqueue(new MsgCallBack<BaseBean<String>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                        String imageUrl = response.body().getResult();
                        getWebView().loadUrl(imageUrl);

                    }
                });
    }
}
