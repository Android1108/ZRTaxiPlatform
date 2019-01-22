package org.wzeiri.zr.zrtaxiplatform.ui.fragment.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.wzeiri.zr.zrtaxiplatform.util.UserInfoHelper;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author k-lm on 2017/11/14.
 */

public abstract class BaseFragment extends Fragment {
    /**
     * 加载进度条显示
     */
    ProgressDialog progressDialog;

    Unbinder unbinder;
    /**
     * 是否显示
     */
    private boolean isShowFragment = false;
    /**
     * 是否加在过数据
     */
    private boolean isLoadData = false;

    /**
     * 吐司提示
     */
    public void showToast(String context) {
        Toast.makeText(getContext(), context, Toast.LENGTH_SHORT).show();
    }

    /**
     * 吐司提示
     */
    public void showToast(@StringRes int context) {
        Toast.makeText(getContext(), context, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示加载对话框
     */
    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        if (progressDialog.isShowing()) {
            return;
        }

        progressDialog.show();
        progressDialog.setMessage("正在加载请稍候");
    }

    /**
     * 显示加载对话框
     *
     * @param msg 消息内容
     */
    public void showProgressDialog(String msg) {
        showProgressDialog();
        progressDialog.setMessage(msg);
    }


    /**
     * 关闭加载框
     */
    public void closeProgressDialog() {
        if (progressDialog == null || !progressDialog.isShowing()) {
            return;
        }
        progressDialog.dismiss();
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isShowFragment = isVisibleToUser;
        // 只有当前已显示且没有加载过数据时才会加载数据 切view不能为空
        if (isShowFragment && !isLoadData && getView() != null) {
            isLoadData = true;
            initData();
        }

        if (isVisibleToUser && getView() != null) {
            onLoadData();
        }
    }

    @Override
    public void onDestroy() {
        closeProgressDialog();
        super.onDestroy();
    }


    public abstract int getLayoutId();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext())
                .inflate(getLayoutId(), container, false);

        if (view == null) {
            view = new View(getContext());
        }
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        create();
        initView();
        init();
        // 只有当前已显示且没有加载过数据时才会加载数据
        if (isShowFragment && !isLoadData) {
            isLoadData = true;
            initData();
        }
        onLoadData();

    }

    void initView() {

    }

    public void create() {
        UserInfoHelper.getInstance()
                .addOnUserStateListener(this, new UserInfoHelper.OnLoginUserState(this));
    }


    public void init() {
    }

    /**
     * 只有第一次显示fragment才会执行
     */
    public void initData() {
    }

    /**
     * 每次显示当前fragmet会执行loadData
     */
    public void onLoadData() {

    }

    protected BaseFragment getThis() {
        return this;
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

    @Override
    public void onDestroyView() {
        UserInfoHelper.getInstance().removeOnUserStateListener(this);
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }

    }

    public void startActivity(Class<? extends Activity> clazz) {
        Intent intent = new Intent(getContext(), clazz);
        startActivity(intent);
    }

    public void startActivityForResult(Class<? extends Activity> clazz, int requestCode) {
        Intent intent = new Intent(getContext(), clazz);
        startActivityForResult(intent, requestCode);
    }

}
