package org.wzeiri.zr.zrtaxiplatform;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.alivc.player.AliVcMediaPlayer;
import com.baidu.mapapi.SDKInitializer;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.iflytek.cloud.SpeechUtility;

import org.wzeiri.zr.zrtaxiplatform.common.Config;
import org.wzeiri.zr.zrtaxiplatform.util.DensityUtil;
import org.wzeiri.zr.zrtaxiplatform.util.LogUtil;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends StackApplication {
    private static MyApplication mApplication;


    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        //计算屏幕宽度
        DensityUtil.WINDOW_WIDTH = wm.getDefaultDisplay().getWidth();
        //计算屏幕高度
        DensityUtil.WINDOW_HEIGHT = wm.getDefaultDisplay().getHeight();

        // 设置日志
        new LogUtil.Builder(this)
                .setLogSwitch(Config.DEBUG);

        // 初始化百度地图
        SDKInitializer.initialize(getApplicationContext());

        AliVcMediaPlayer.init(getApplicationContext());
        JPushInterface.setDebugMode(Config.DEBUG);
        JPushInterface.init(getApplication());

        MultiDex.install(this);


        SpeechUtility.createUtility(this, "appid=" +Config.XUNFEI_APPID);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }

    public static MyApplication getApplication() {
        return mApplication;
    }

}
