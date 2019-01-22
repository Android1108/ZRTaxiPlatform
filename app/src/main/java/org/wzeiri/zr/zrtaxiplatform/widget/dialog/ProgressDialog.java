package org.wzeiri.zr.zrtaxiplatform.widget.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author k-lm on 2018/1/26.
 */

public class ProgressDialog extends AlertDialog {
    @BindView(R.id.dialog_progress_text_progress)
    TextView mProgress;
    @BindView(R.id.dialog_progress_pb_progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.dialog_progress_vtv_size)
    ValueTextView mSize;

    public ProgressDialog(@NonNull Context context) {
        super(context);
    }

    public ProgressDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public ProgressDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_progress);
        ButterKnife.bind(this, this);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    /**
     * 设置大小
     *
     * @param currentSize 当前的大小(进度)
     * @param totalSize   总大小(进度)
     */
    public void setSize(String currentSize, String totalSize) {
        if (TextUtils.isEmpty(currentSize) || TextUtils.isEmpty(totalSize)) {
            return;
        }
        mSize.setTextLeft(currentSize);
        mSize.setText("\\");
        mSize.setTextRight(totalSize);
    }

    /**
     * 设置进度的百分比
     *
     * @param progress 进度百分比
     */
    public void setProgress(String progress) {
        mProgress.setText(progress);

    }

    /**
     * 设置进度的百分比
     *
     * @param progress 进度百分比
     */
    public void setProgress(int progress) {
        mProgressBar.setProgress(progress);
    }

    /**
     * 设置最大进度
     *
     * @param max 最大进度
     */
    public void setMaxProgress(int max) {
        mProgressBar.setMax(max);
    }

}
