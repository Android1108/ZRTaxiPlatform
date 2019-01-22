package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alivc.player.AliVcMediaPlayer;
import com.alivc.player.MediaPlayer;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.SingleRequest;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.VideoDetailBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.common.Config;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.ILearn;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItem;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.WidgetActivity;
import org.wzeiri.zr.zrtaxiplatform.util.CountDownHelper;
import org.wzeiri.zr.zrtaxiplatform.util.DensityUtil;
import org.wzeiri.zr.zrtaxiplatform.util.GlideUtil;
import org.wzeiri.zr.zrtaxiplatform.util.LogUtil;
import org.wzeiri.zr.zrtaxiplatform.util.NetWorkHelp;
import org.wzeiri.zr.zrtaxiplatform.util.ThreadSwitch;
import org.wzeiri.zr.zrtaxiplatform.widget.dialog.BaseDialog;
import org.wzeiri.zr.zrtaxiplatform.widget.dialog.IOSAlertDialog;

import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 视频学习
 *
 * @author k-lm on 2017/12/8.
 */

public class VideoStudyActivity extends WidgetActivity {

    private static final String TAG = "VideoStudyActivity";

    @BindView(R.id.aty_video_study_image_back)
    ImageView mImageBack;

    @BindView(R.id.aty_video_study_sv_video)
    SurfaceView mSvVideo;

    @BindView(R.id.aty_video_study_layout_bottom_layout)
    LinearLayout mBottomLayout;

    @BindView(R.id.aty_video_study_image_play_stop)
    ImageView mImagePlayOrStop;

    @BindView(R.id.aty_video_study_pb_play_progress)
    ProgressBar mPlayProgress;

    @BindView(R.id.aty_video_study_text_play_time)
    TextView mPlayTime;

    @BindView(R.id.aty_video_study_text_duration)
    TextView mTextDuration;

    @BindView(R.id.aty_video_study_image_background)
    ImageView mImageBackground;

    /**
     * 控制 显示底部显示时间
     */
    private CountDownHelper mCountDownHelper;

    private CountDownHelper mShowPausePromptHelp;

    private BaseDialog mPausePromptDialog;

    /**
     * 视频播放器
     */
    AliVcMediaPlayer mPlayer;

    private int mVideoId = -1;

    private String mVideoUrl = "";
    /**
     * 封面
     */
    private String mCover = "";
    /**
     * 是否显示底部布局
     */
    private boolean mIsShowBottomLayout = true;
    /**
     * 双击和带单击事件
     */
    private GestureDetectorCompat mGestureDetector;

    ILearn mILearn;
    /**
     * 是否已学习
     */
    private boolean mIsLearned = false;

    @OnClick(R.id.aty_video_study_image_back)
    void onBackClick() {
        finish();
    }


    @OnClick(R.id.aty_video_study_image_play_stop)
    void onPlayOrPause() {
        if (mPlayer.isPlaying()) {
            pauseVideo();
        } else {
            playVideo();
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_study;
    }

    @Override
    protected void create() {
        super.create();
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        mVideoId = intent.getIntExtra("id", mVideoId);
        mCover = intent.getStringExtra("cover");
        mIsLearned = intent.getBooleanExtra("isLearned", mIsLearned);
    }

    @Override
    protected void init() {
        super.init();

        initPlayer();
        // 隐藏通知栏
        mSvVideo.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        mCountDownHelper = new CountDownHelper(CountDownHelper.TimeUnit.SECONDS, 5);

        mCountDownHelper.setOnCountDownListener(new CountDownHelper.OnCountDownListener() {
            @Override
            public void onCountDownFinish() {
                loadPlayProgress();
                hideBottomLayoutAndBack();
            }

            @Override
            public void onCountDownTick(long millisUntilFinished) {
                loadPlayProgress();
            }
        });

        if (!mIsLearned) {
            mShowPausePromptHelp = new CountDownHelper(CountDownHelper.TimeUnit.MINUTE, 5);
            mShowPausePromptHelp.setOnCountDownListener(new CountDownHelper.OnCountDownListener() {
                @Override
                public void onCountDownFinish() {
                    showVideoPausePromptDialog();
                }

                @Override
                public void onCountDownTick(long millisUntilFinished) {

                }
            });
        }

       /* NetWorkHelp help = NetWorkHelp.getInstance();
        help.addOnNetworkChangeListener(getThis(), new NetWorkHelp.OnNetworkChangeListener() {
            @Override
            public void onConnectWIFi() {
                LogUtil.d(TAG, "onConnectWIFi");
            }

            @Override
            public void onConnectMobileNetwork() {
                LogUtil.d(TAG, "onConnectMobileNetwork");
                if (mPlayer.isPlaying()) {
                    showToast("当前网络以切换成移动网络，如需观看视频请点击播放按钮");
                    mPlayer.pause();
                }
            }

            @Override
            public void onDisconnectNetwork() {
                LogUtil.d(TAG, "onDisconnectNetwork");
                //showToast("当前网络已断开");
            }
        });

        help.registerReceiver(getThis());*/
        // 隐藏底部虚拟按键
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        hideBottomLayoutAndBack();


    }


    /**
     * 初始化播放器
     */
    private void initPlayer() {
        mPlayer = new AliVcMediaPlayer(getThis(), mSvVideo);
        mPlayer.setVideoScalingMode(MediaPlayer.VideoScalingMode.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
        mPlayer.setPreparedListener(new MediaPlayer.MediaPlayerPreparedListener() {
            @Override
            public void onPrepared() {
                //准备完成时触发
                // 设置总时长
                LogUtil.d(TAG, "onPrepared");
                int duration = mPlayer.getDuration();
                mPlayProgress.setMax(duration);
                mTextDuration.setText(getTime(duration));
            }
        });
        mPlayer.setFrameInfoListener(new MediaPlayer.MediaPlayerFrameInfoListener() {
            @Override
            public void onFrameInfoListener() {
                //首帧显示时触发
                LogUtil.d(TAG, "onFrameInfoListener");
               /* mSvVideo.setZOrderMediaOverlay(true);
                mSvVideo.setBackground(null);*/
                mImageBackground.setVisibility(View.GONE);
                if (mShowPausePromptHelp != null) {
                    mShowPausePromptHelp.startTime();
                }

            }
        });

        mPlayer.setCompletedListener(new MediaPlayer.MediaPlayerCompletedListener()

        {
            @Override
            public void onCompleted() {
                //视频正常播放完成时触发
                LogUtil.d(TAG, "视频播放完成");
                mPlayer.reset();
                mPlayer.prepareToPlay(mVideoUrl);
                mImageBackground.setVisibility(View.VISIBLE);
                if (mCountDownHelper != null) {
                    mCountDownHelper.cancel();
                }
                if(!mIsLearned){
                    createLearnRecord();
                }

            }
        });
        mPlayer.setStoppedListener(new MediaPlayer.MediaPlayerStoppedListener()

        {
            @Override
            public void onStopped() {
                //使用stop接口时触发
                LogUtil.d(TAG, "onStopped");
            }
        });

        mPlayer.setErrorListener(new MediaPlayer.MediaPlayerErrorListener()

        {
            @Override
            public void onError(int i, String msg) {
                //错误发生时触发，错误码见接口文档
                LogUtil.d(TAG, i + ": " + msg);
            }
        });

        mPlayer.setBufferingUpdateListener(new MediaPlayer.MediaPlayerBufferingUpdateListener()

        {
            @Override
            public void onBufferingUpdateListener(int i) {
                //mPlayProgress.setSecondaryProgress(i);
            }
        });


        // 文档没有说明 ，需要监听surfaceView的状态，不然无法播放视频
        mSvVideo.getHolder().

                addCallback(new SurfaceHolder.Callback() {
                    public void surfaceCreated(SurfaceHolder holder) {
//                holder.setType(SurfaceHolder.SURFACE_TYPE_GPU);
                        holder.setKeepScreenOn(true);
                        LogUtil.d(TAG, "surfaceCreated");
                        // Important: surfaceView changed from background to front, we need reset surface to mediaplayer.
                        // 对于从后台切换到前台,需要重设surface;部分手机锁屏也会做前后台切换的处理
                        mPlayer.setVideoSurface(holder.getSurface());

                    }

                    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
                        LogUtil.d(TAG, "surfaceChanged");
                        mPlayer.setSurfaceChanged();
                    }

                    public void surfaceDestroyed(SurfaceHolder holder) {
                        LogUtil.d(TAG, "surfaceDestroyed");
                    }
                });


        // 设置媒体播放类型
        // 0硬件编码 ， 1 软件编码
        mPlayer.setDefaultDecoder(0);

        mGestureDetector = new GestureDetectorCompat(getThis(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (mIsShowBottomLayout) {
                    hideBottomLayoutAndBack();
                } else {
                    showBottomLayoutAndBack();
                }
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (mPlayer == null) {
                    return false;
                }
                //双击暂停或播放
                if (mPlayer.isPlaying()) {
                    pauseVideo();
                } else {
                    playVideo();
                }

                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return super.onScroll(e1, e2, distanceX, distanceY);
            }
        });

        // 版本小于19的，底部内容位置要在虚拟按键上方
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            if (hasNavBar(getThis())) {
                Resources res = getResources();
                int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    int result = res.getDimensionPixelSize(resourceId);
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mBottomLayout.getLayoutParams();
                    params.bottomMargin += result;
                }
            }
        }

    }


    /**
     * 检查是否存在虚拟按键栏
     *
     * @param context
     * @return
     */
    public boolean hasNavBar(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            // check override flag
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else { // fallback
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }

    /**
     * 判断虚拟按键栏是否重写
     *
     * @return
     */
    private String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
            }
        }
        return sNavBarOverride;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean isDouble = mGestureDetector.onTouchEvent(event);
        if (isDouble) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void createLearnRecord() {
        mILearn.createLearnRecord(RetrofitHelper
                .getBody(new JsonItem("learnVedioId", mVideoId)))
                .enqueue(new MsgCallBack<BaseBean<String>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                        showToast("已完成学习");
                        // finish();
                    }
                });
    }

    /**
     * 播放视频
     */
    private void playVideo() {
        /*if (NetWorkHelp.isWifi(getThis())) {
            mPlayer.play();
        } else {
            showToast("当前使用移动流量，如需观看视频请点击播放按钮");
        }*/
        if (mShowPausePromptHelp != null) {
            mShowPausePromptHelp.startTime();
        }
        mPlayer.play();
        mImagePlayOrStop.setImageResource(R.drawable.ic_video_pause);
    }

    /**
     * 暂停视频
     */
    private void pauseVideo() {
        if (mShowPausePromptHelp != null) {
            mShowPausePromptHelp.cancel();
        }
        mPlayer.pause();
        mImagePlayOrStop.setImageResource(R.drawable.ic_video_play);
    }


    /**
     * 加载播放进度
     */
    private void loadPlayProgress() {
        int currentPlayTime = mPlayer.getCurrentPosition();
        mPlayProgress.setProgress(currentPlayTime);
        mPlayTime.setText(getTime(currentPlayTime));
        int buffer = mPlayer.getBufferPosition();
        mPlayProgress.setSecondaryProgress(buffer);
    }

    /**
     * 返回时间字符串
     *
     * @param time
     * @return
     */
    private String getTime(int time) {
        time /= 1000;
        int minute = time / 60;
        int seconds;
        if (minute != 0) {
            seconds = time % 60;
        } else {
            seconds = time;
        }

        String minuteStr;
        String secondsStr;

        if (seconds < 10) {
            secondsStr = "0" + seconds;
        } else {
            secondsStr = "" + seconds;
        }

        if (minute < 10) {
            minuteStr = "0" + minute;
        } else {
            minuteStr = "" + minute;
        }

        return minuteStr + ":" + secondsStr;
    }

    /**
     * 显示底部布局和后退键
     */
    private void showBottomLayoutAndBack() {
        if (mIsShowBottomLayout) {
            return;
        }
        mIsShowBottomLayout = true;

        mBottomLayout.setVisibility(View.VISIBLE);
        mImageBack.setVisibility(View.VISIBLE);
        loadPlayProgress();
        if (mCountDownHelper != null) {
            mCountDownHelper.startTime();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mSvVideo.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else {
            mSvVideo.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }


    /**
     * 隐藏布局和后退键
     */
    private void hideBottomLayoutAndBack() {
        if (!mIsShowBottomLayout) {
            return;
        }
        if (mCountDownHelper != null) {
            mCountDownHelper.cancel();
        }
        mIsShowBottomLayout = false;

        mBottomLayout.setVisibility(View.GONE);
        mImageBack.setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_FULLSCREEN);
        }


    }

    @Override
    protected void initData() {
        super.initData();

        mILearn = RetrofitHelper.create(ILearn.class);
        mILearn.getLearnVideoDetail(mVideoId)
                .enqueue(new MsgCallBack<BaseBean<VideoDetailBean>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<VideoDetailBean>> call, Response<BaseBean<VideoDetailBean>> response) {
                        VideoDetailBean bean = response.body().getResult();
                        if (bean == null) {
                            return;
                        }
                        mVideoUrl = bean.getVedioUrl();
                        mPlayer.prepareToPlay(mVideoUrl);
                        // mSvVideo.setZOrderOnTop(true);
                        playVideo();
                        // mPlayer.play();
                    }
                });
        // 设置封面图
        mSvVideo.setZOrderOnTop(true);
        mSvVideo.setZOrderMediaOverlay(true);
        if (!TextUtils.isEmpty(mCover)) {
            Glide.with(getThis())
                    .asDrawable()
                    .load(mCover)
                    .into(new SimpleTarget<Drawable>(DensityUtil.WINDOW_WIDTH, DensityUtil.WINDOW_HEIGHT) {
                        @Override
                        public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                            ViewCompat.setBackground(mImageBackground, resource);
                        }
                    });

        }
   /* showProgressDialog();
       mImageBack.postDelayed(new Runnable() {
           @Override
           public void run() {
               closeProgressDialog();
               String url = "http://zunrong01.oss-cn-hangzhou.aliyuncs.com/2018/1/5a543202d4a0c62198004563.mp4?Expires=1515491214&OSSAccessKeyId=LTAIVSRAAzGw4VvC&Signature=frDg39o97tpokm3yI35CofYIEfI%3D";
               mPlayer.prepareToPlay(url);
               mPlayer.play();
           }
       },2000);*/
    }


    /**
     * 显示暂停提示框
     */
    private void showVideoPausePromptDialog() {
        if (mPausePromptDialog == null) {
            mPausePromptDialog = new BaseDialog(getThis(), R.style.NoTitleDialog) {
                @Override
                public int getLayoutId() {
                    return R.layout.dialog_vide_pause_prompt;
                }
            };

            mPausePromptDialog.setPositiveButton_(null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    playVideo();
                    dialog.dismiss();
                }
            });
        }
        mPausePromptDialog.show();
        pauseVideo();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.d(TAG, "onResume");
        playVideo();
    }

    @Override
    protected void onPause() {
        LogUtil.d(TAG, "onPause");
        super.onPause();
        mPlayer.pause();
    }

    @Override
    protected void onStop() {
        LogUtil.d(TAG, "onStop");
        super.onStop();
        mPlayer.pause();
    }


    @Override
    protected void onDestroy() {
        LogUtil.d(TAG, "onDestroy");
        mPlayer.stop();
        mPlayer.releaseVideoSurface();
        mPlayer.destroy();
        if (mShowPausePromptHelp != null) {
            mShowPausePromptHelp.cancel();
        }
       /* NetWorkHelp.getInstance().unregisterReceiver(getThis());
        NetWorkHelp.getInstance().removeOnNetworkChangeListener(getThis());*/
        super.onDestroy();

    }

    public static void start(Context context, int id, String cover, boolean isLearned) {
        Intent starter = new Intent(context, VideoStudyActivity.class);
        starter.putExtra("id", id);
        starter.putExtra("cover", cover);
        starter.putExtra("isLearned", isLearned);
        context.startActivity(starter);
    }


}
