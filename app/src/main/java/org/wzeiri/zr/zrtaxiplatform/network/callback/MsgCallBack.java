package org.wzeiri.zr.zrtaxiplatform.network.callback;

import android.text.TextUtils;

import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.base.BaseFragment;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @author k-lm on 2017/12/11.
 */

public abstract class MsgCallBack<T extends BaseBean> extends BaseCallBack<T> {

    private BaseActivity mActivity;
    private BaseFragment mFragment;
    private boolean mIsShowDialog;

    public MsgCallBack(BaseActivity activity) {
        this(activity, false);
    }

    public MsgCallBack(BaseFragment fragment) {
        this(fragment, false);
    }

    public MsgCallBack(BaseActivity activity, boolean isShowDialog) {
        mActivity = activity;
        mIsShowDialog = isShowDialog;
        showProgressDialog();
    }

    public MsgCallBack(BaseFragment fragment, boolean isShowDialog) {
        mFragment = fragment;
        mIsShowDialog = isShowDialog;
        showProgressDialog();
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {

        super.onResponse(call, response);
        closeProgressDialog();

    }

    @Override
    public void onError(Call<T> call, Throwable t) {
        closeProgressDialog();
        toast(t.getMessage());
    }

    private void showProgressDialog() {
        if (!mIsShowDialog) {
            return;
        }
        if (mActivity != null) {
            mActivity.showProgressDialog();
            return;
        }

        if (mFragment != null) {
            mFragment.showProgressDialog();
            return;
        }
    }

    private void closeProgressDialog() {
        /*if (!mIsShowDialog) {
            return;
        }*/
        if (mActivity != null) {
            mActivity.closeProgressDialog();
            return;
        }

        if (mFragment != null) {
            mFragment.closeProgressDialog();
            return;
        }
    }


    /**
     * 土司
     *
     * @param message
     */
    private void toast(String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        if (mActivity != null) {
            mActivity.showToast(message);
            return;
        }

        if (mFragment != null) {
            mFragment.showToast(message);
            return;
        }

    }
}
