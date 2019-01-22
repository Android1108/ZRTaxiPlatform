package org.wzeiri.zr.zrtaxiplatform.ui.activity.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import org.wzeiri.zr.zrtaxiplatform.util.UserInfoHelper;

import butterknife.ButterKnife;

/**
 * @author k-lm on 2017/11/14.
 */

public abstract class WidgetActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        create();
        initLayout();
        initView();
        init();
        initData();

    }

    /**
     * 返回layoutId
     *
     * @return layoutId
     */
    protected abstract int getLayoutId();

    /**
     * 用于初始化布局
     */
    void initLayout() {
        setContentView(getLayoutId());
        ButterKnife.bind(this);
    }

    /**
     * 用于初始化view ，仅用于base类
     */
    void initView() {

    }

    /**
     * 该方法是在设置layout之前调用
     */
    protected void create() {
        UserInfoHelper.getInstance()
                .addOnUserStateListener(this, new UserInfoHelper.OnLoginUserState(this));
    }

    protected void init() {

    }

    protected void initData() {
    }


    public void hideNoticeBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }


    public void showNoticeBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }
    /**
     * 返回通知栏高度
     *
     * @return 返回 返回通知栏高度, 返回-1为未获取到
     */
    public int getNoticeBarHeight() {
        int statusBarHeight = -1;
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);

        }
        return statusBarHeight;
    }


}
