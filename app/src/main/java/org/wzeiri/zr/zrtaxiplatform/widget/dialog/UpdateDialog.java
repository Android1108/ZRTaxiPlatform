package org.wzeiri.zr.zrtaxiplatform.widget.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.util.LogUtil;

/**
 * @author k-lm on 2018/1/30.
 */

public class UpdateDialog extends BaseDialog {

    private View mMessageLayout;

    public UpdateDialog(@NonNull Context context) {
        super(context);
    }

    public UpdateDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMessageLayout = findViewById(R.id.dialog_version_update_layout_message);

        mMessageLayout.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mMessageLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        int height = mMessageLayout.getHeight();
                        LogUtil.d(height);
                        if (height <= 400) {
                            return;
                        }
                        height = 400;


                        ViewGroup.LayoutParams layoutParams = mMessageLayout.getLayoutParams();
                        layoutParams.height = height;
                        mMessageLayout.setLayoutParams(layoutParams);

                    }
                });

        // 点击透明区域，关闭对话框
        View view = findViewById(R.id.dialog_version_update_layout_content);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_version_update;
    }
}
