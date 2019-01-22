package org.wzeiri.zr.zrtaxiplatform.ui.activity.base;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.MenuRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;

import butterknife.ButterKnife;

/**
 * @author k-lm on 2017/11/14.
 */

public abstract class ActionbarActivity extends WidgetActivity {
    /**
     * 标题
     */
    private TextView mCenterTitle;
    /**
     * 标题栏
     */
    private Toolbar mCenterToolbar;
    /**
     * 内容栏
     */
    private FrameLayout mContentLayout;

    private View mNoticeBar;

    private View mContentView;

    @Override
    void initLayout() {
        setContentView(R.layout.activity_base_actionbar);

        mContentLayout = (FrameLayout) findViewById(R.id.aty_action_layout_content_layout);
        mCenterTitle = (TextView) findViewById(R.id.aty_action_text_center_title);
        mCenterToolbar = (Toolbar) findViewById(R.id.aty_action_bar_center_toolbar);
        mNoticeBar = findViewById(R.id.aty_action_view_notice_bar);


        mContentView = LayoutInflater
                .from(this)
                .inflate(getLayoutId(), mContentLayout, false);

        if (mContentView == null) {
            mContentView = mContentLayout;
        } else {
            mContentLayout.addView(mContentView);
        }

        ButterKnife.bind(this, mContentView);


    }

    @Override
    void initView() {
        super.initView();
        setSupportActionBar(mCenterToolbar);
        mCenterToolbar.setNavigationIcon(R.drawable.ic_gray_back);
        mCenterToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onToolBarBackPressed();
            }
        });
        //初始化标题
        mCenterToolbar.setTitle("");
        mCenterToolbar.setSubtitle("");
        setTitle("");
        initNoticeBar();
    }

    /**
     * 初始化通知栏
     */
    private void initNoticeBar() {
        // 版本小于4.4无法设置为沉浸式
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            mNoticeBar.setVisibility(View.GONE);
            return;
        }
        // 4.4 设置沉浸式
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        //hideNoticeBar();

        int noticeBarHeight = getNoticeBarHeight();
        if (noticeBarHeight < 0) {
            return;
        }


        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mNoticeBar.getLayoutParams();
        layoutParams.height = noticeBarHeight;
        mNoticeBar.setVisibility(View.VISIBLE);
    }


    /**
     * 设置通知栏颜色
     *
     * @param color
     */

    public void setNoticeBarColor(int color) {
        mNoticeBar.setBackgroundColor(color);
    }

    /**
     * 设置右侧图标
     *
     * @param resId
     */
    protected void setNavigationIcon(@DrawableRes int resId) {
        mCenterToolbar.setNavigationIcon(resId);
    }

    /**
     * 设置右侧图标
     *
     * @param drawable
     */
    protected void setNavigationIcon(Drawable drawable) {
        mCenterToolbar.setNavigationIcon(drawable);
    }

    /**
     * 返回内容View
     *
     * @return
     */
    protected View getContentView() {
        return mContentView;
    }

    /**
     * 设置居中标题内容
     *
     * @param msg 内容
     */
    protected void setCenterTitle(CharSequence msg) {
        mCenterTitle.setText(msg);
    }

    /**
     * 设置居中标题内容
     *
     * @param resId 内容id
     */
    protected void setCenterTitle(@StringRes int resId) {
        mCenterTitle.setText(resId);
    }

    /**
     * 设置居中标题颜色
     *
     * @param color 颜色
     */
    protected void setCenTerTitleColor(int color) {
        mCenterTitle.setTextColor(color);
    }

    /**
     * 设置标题字体大小
     *
     * @param size 字体大小
     */
    protected void setCenTerTitleSize(float size) {
        mCenterTitle.setTextSize(size);
    }

    /**
     * 设置标题字体大小
     *
     * @param size 字体大小
     */
    protected void setCenTerTitleSize(@DimenRes int size) {
        mCenterTitle.setTextSize(size);
    }

    /**
     * 设置内容区域的背景
     *
     * @param color
     */
    protected void setContentBackgroundColor(int color) {
        mContentLayout.setBackgroundColor(color);
    }

    /**
     * 设置内容区域的背景
     *
     * @param resId
     */
    protected void setContentBackgroundResource(@DrawableRes int resId) {
        mContentLayout.setBackgroundResource(resId);
    }

    /**
     * 设置标题栏背景颜色
     *
     * @param color
     */
    protected void setToolBarBackgroundColor(int color) {
        mCenterToolbar.setBackgroundColor(color);
    }

    /**
     * 设置标题栏背景
     *
     * @param resId
     */
    protected void setToolBarBackgroundResource(@DrawableRes int resId) {
        mCenterToolbar.setBackgroundResource(resId);
    }

    protected void inflateMenu(@MenuRes int resId) {
        mCenterToolbar.inflateMenu(resId);
    }

    /**
     * 将view添加到menuitem中，并且加入了view的点击事件
     *
     * @param menuItem menuItem
     * @param view     view
     */
    protected void setActionView(final MenuItem menuItem, View view) {
        menuItem.setActionView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
    }


    /**
     * 在标题栏左侧加入view
     *
     * @param view
     */
    protected void addToolBarLeftView(View view) {
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        mCenterToolbar.addView(view, layoutParams);
    }

    /**
     * 在标题栏中间加入view
     *
     * @param view
     */
    protected void addToolBarCenterView(View view) {
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        mCenterToolbar.addView(view, layoutParams);
    }

    /**
     * toolbar后退按钮事件
     */
    protected void onToolBarBackPressed() {
        finish();
    }

    protected int getContentLayoutId() {
        return mContentLayout.getId();
    }

    protected Toolbar getToolbar() {
        return mCenterToolbar;
    }
}
