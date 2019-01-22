package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseActivity;
import org.wzeiri.zr.zrtaxiplatform.util.CountDownHelper;

/**
 * @author k-lm on 2018/1/23.
 */

public class StartActivity extends BaseActivity {

    private CountDownHelper mCountDownHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            finish();
            return;
        }

        mCountDownHelper = new CountDownHelper(CountDownHelper.TimeUnit.SECONDS, 3);
        mCountDownHelper.setOnCountDownListener(new CountDownHelper.OnCountDownListener() {
            @Override
            public void onCountDownFinish() {
               /* if (Config.DEBUG) {
                    startActivity(SpeechRecognitionActivity.class);
                    finish();
                    return;
                }*/
                startActivity(LoginActivity.class);
                finish();
            }

            @Override
            public void onCountDownTick(long millisUntilFinished) {

            }
        });
        mCountDownHelper.startTime();

    }


    @Override
    protected void onDestroy() {
        if (mCountDownHelper != null) {
            mCountDownHelper.cancel();
        }
        super.onDestroy();
    }
}
