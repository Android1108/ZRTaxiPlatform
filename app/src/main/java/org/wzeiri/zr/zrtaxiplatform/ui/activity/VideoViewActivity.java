package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * VideoView视频播放器
 *
 * @author k-lm on 2018/2/2.
 */

public class VideoViewActivity extends ActionbarActivity {
    @BindView(R.id.aty_video_view)
    VideoView mVideoView;

    private String mUrl;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_view;
    }


    @Override
    protected void create() {
        super.create();
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        mUrl = intent.getStringExtra("url");
    }

    @Override
    protected void init() {
        super.init();
        mVideoView.setVideoURI(Uri.parse(mUrl));
        //设置视频控制器
        mVideoView.setMediaController(new MediaController(this));
        mVideoView.start();
    }

    public static void start(Context context, String url) {
        Intent starter = new Intent(context, VideoViewActivity.class);
        starter.putExtra("url", url);
        context.startActivity(starter);
    }

}
