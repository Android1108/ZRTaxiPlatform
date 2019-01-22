package org.wzeiri.zr.zrtaxiplatform.widget.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.util.DensityUtil;

import butterknife.ButterKnife;

/**
 * Created by LC on 2017/5/20.
 */

public abstract class BaseDialog extends AppCompatDialog {

    private View mView;
    private TextView mTitleView;
    private TextView mMessageView;
    private View mPositiveView;
    private View mPositiveView1;
    private View mNeutralView;
    private View mNegativeView;

    public BaseDialog(@NonNull Context context) {
        this(context, R.style.NoTitleDialog);
    }

    public BaseDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init();
    }

    private void init() {
        mView = LayoutInflater.from(getContext()).inflate(getLayoutId(), null);
        mTitleView = (TextView) mView.findViewById(R.id.dialog_title);
        mMessageView = (TextView) mView.findViewById(R.id.dialog_message);
        mPositiveView = mView.findViewById(R.id.dialog_positive);
        mPositiveView1 = mView.findViewById(R.id.dialog_positive1);
        mNeutralView = mView.findViewById(R.id.dialog_neutral);
        mNegativeView = mView.findViewById(R.id.dialog_negative);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(mView);
        initView(mView);

        //设置dialog大小
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams params = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        dialogWindow.setGravity(Gravity.CENTER);
        params.width = DensityUtil.getDialogWidth(); // 宽度设置为屏幕的0.65，根据实际情况调整
        dialogWindow.setAttributes(params);

    }

    public BaseDialog getThis() {
        return this;
    }

    public BaseDialog setTitle_(@Nullable CharSequence title) {
        if (mTitleView != null) {
            mTitleView.setText(title);
        }
        return this;
    }

    public BaseDialog setMessage_(@Nullable CharSequence message) {
        if (mMessageView != null) {
            mMessageView.setText(message);
        }
        return this;
    }

    public BaseDialog setPositiveButton_(CharSequence text, final OnClickListener listener) {
        if (mPositiveView != null) {
            if (text != null) {
                if (mPositiveView instanceof TextView) {
                    ((TextView) mPositiveView).setText(text);
                }
            }
            mPositiveView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClick(getThis(), DialogInterface.BUTTON_POSITIVE);
                    }
                }
            });

        }
        return this;
    }public BaseDialog setPositiveButton_1(CharSequence text, final OnClickListener listener) {
        if (mPositiveView1 != null) {
            if (text != null) {
                if (mPositiveView instanceof TextView) {
                    ((TextView) mPositiveView).setText(text);
                }
            }
            mPositiveView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClick(getThis(), DialogInterface.BUTTON_POSITIVE);
                    }
                }
            });
        }
        return this;
    }

    public BaseDialog setNeutralButton_(CharSequence text, final OnClickListener listener) {
        if (mNeutralView != null) {
            if (text != null) {
                if (mNeutralView instanceof TextView) {
                    ((TextView) mNeutralView).setText(text);
                }
            }
            mNeutralView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClick(getThis(), DialogInterface.BUTTON_NEUTRAL);
                    }
                }
            });
        }
        return this;
    }

    public BaseDialog setNegativeButton_(CharSequence text, final OnClickListener listener) {
        if (mNegativeView != null) {
            if (text != null) {
                if (mNegativeView instanceof TextView) {
                    ((TextView) mNegativeView).setText(text);
                }
            }
            mNegativeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClick(getThis(), DialogInterface.BUTTON_NEGATIVE);
                        dismiss();
                    } else {
                        dismiss();
                    }
                }
            });
        }
        return this;
    }

    public BaseDialog setCanceledOnTouchOutside_(boolean cancel) {
        this.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public BaseDialog setCancelable_(boolean flag) {
        this.setCancelable(flag);
        return this;
    }

    public BaseDialog setOnCancelListener_(@Nullable OnCancelListener listener) {
        this.setOnCancelListener(listener);
        return this;
    }

    public BaseDialog setOnDismissListener_(@Nullable OnDismissListener listener) {
        this.setOnDismissListener(listener);
        return this;
    }

    public void initView(View view) {
        ButterKnife.bind(this, view);
    }

    public abstract int getLayoutId();

    //public abstract int getTitleViewId();
    //public abstract int getMessageViewId();
    //public abstract int getPositiveViewId();
    //public abstract int getNegativeViewId();
    //public abstract int getNeutralViewId();

}
