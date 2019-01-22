package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;

import butterknife.BindView;

/**
 * Created by wxy on 2018/5/26.
 */

public class ImageViewAcitvity extends ActionbarActivity {
    @BindView(R.id.iv)
    ImageView iv;
    @Override
    protected int getLayoutId() {
        return R.layout.view_image;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
