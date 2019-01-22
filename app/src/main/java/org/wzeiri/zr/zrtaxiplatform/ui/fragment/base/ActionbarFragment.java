package org.wzeiri.zr.zrtaxiplatform.ui.fragment.base;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author k-lm on 2017/11/14.
 */

public abstract class ActionbarFragment extends BaseFragment implements Toolbar.OnMenuItemClickListener {

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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.fragment_base_actionbar, container, false);

        mCenterTitle = view.findViewById(R.id.fragment_action_text_center_title);
        mCenterToolbar = view.findViewById(R.id.fragment_action_bar_center_toolbar);
        mContentLayout = view.findViewById(R.id.fragment_action_layout_content_layout);
        mNoticeBar = view.findViewById(R.id.fragment_action_view_notice_bar);

        onCreateOptionsMenu(mCenterToolbar.getMenu(), getActivity().getMenuInflater());
        mCenterToolbar.setOnMenuItemClickListener(this);
        initNoticeBar();
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        View contentView = LayoutInflater.from(getContext())
                .inflate(getLayoutId(), mContentLayout, false);

        if (contentView == null) {
            contentView = new View(getContext());
        }
        mContentLayout.addView(contentView);
        unbinder = ButterKnife.bind(this, mContentLayout);
        super.onViewCreated(view, savedInstanceState);

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
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        // hideNoticeBar();

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
    public void setNoticeBarColor(Drawable color) {
        mNoticeBar.setBackground(color);
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
    protected void setCenterTitleColor(int color) {
        mCenterTitle.setTextColor(color);
    }

    /**
     * 设置标题字体大小
     *
     * @param size 字体大小
     */
    protected void setCenterTitleSize(float size) {
        mCenterTitle.setTextSize(size);
    }

    /**
     * 设置标题字体大小
     *
     * @param size 字体大小
     */
    protected void setCenterTitleSize(@DimenRes int size) {
        mCenterTitle.setTextSize(size);
    }

    protected void setBarBackgroundColor(int color) {
        mCenterToolbar.setBackgroundColor(color);
    }
    protected void setBarBackgroundColor(Drawable color) {
        mCenterToolbar.setBackground(color);
    }
    protected void setContentBackgroundColor(int color) {
        mContentLayout.setBackgroundColor(color);
    }

    protected void setContentBackgroundResource(@DrawableRes int resId) {
        mContentLayout.setBackgroundResource(resId);
    }

    protected void setToolBarBackgroundColor(int color) {
        mCenterToolbar.setBackgroundColor(color);
    }

    protected void setToolBarBackgroundResource(@DrawableRes int resId) {
        mCenterToolbar.setBackgroundResource(resId);
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


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return onOptionsItemSelected(item);
    }


}
