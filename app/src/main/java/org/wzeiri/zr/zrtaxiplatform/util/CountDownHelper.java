package org.wzeiri.zr.zrtaxiplatform.util;

import android.os.CountDownTimer;

/**
 * @author k-lm on 2017/11/17.
 */

public class CountDownHelper {

    /**
     * 倒计时时间
     */
    private int mCountDownTime = 60_000;


    /**
     * 是否进入倒计时
     */
    private boolean isCountDown = false;

    /**
     * 计数器
     */
    private TimeCount mTime;


    private OnCountDownListener mCountDownListener;

    public CountDownHelper() {
    }

    public CountDownHelper(int time) {
        setCountDownTime(TimeUnit.MILLISECOND, time);
    }

    public CountDownHelper(TimeUnit timeUnit, int time) {
        setCountDownTime(timeUnit, time);
    }


    /**
     * 设置倒计时时间
     *
     * @param time 时间，单位为毫秒
     */
    public void setCountDownTime(int time) {
        setCountDownTime(TimeUnit.MILLISECOND, time);
    }

    /**
     * 设置倒计时时间
     *
     * @param timeUnit 时间单位
     * @param time     时间
     */
    public void setCountDownTime(TimeUnit timeUnit, int time) {
        if (timeUnit == null) {
            timeUnit = TimeUnit.MILLISECOND;
        }
        mCountDownTime = time * timeUnit.getMultiple() + 400;
    }

    /**
     * 是否正在倒计时
     *
     * @return
     */
    public boolean isCountDown() {
        return isCountDown;
    }

    public void cancel() {
        if (mTime != null && isCountDown) {
            mTime.cancel();
            isCountDown = false;
        }

    }

    /**
     * 开始计时
     */
    public void startTime() {
        if (mTime == null) {
            mTime = new TimeCount(mCountDownTime + 300, 1000);
        }


        if (isCountDown) {
            mTime.cancel();
        }

        mTime.start();
    }

    /**
     * 时间单位
     */
    public static enum TimeUnit {
        /**
         * 毫秒
         */
        MILLISECOND

                {
                    public int getMultiple() {
                        return 1;
                    }

                },
        /**
         * 秒
         */
        SECONDS

                {
                    public int getMultiple() {
                        return 1000;
                    }

                },
        /**
         * 分钟
         */
        MINUTE

                {
                    public int getMultiple() {
                        return 60_000;
                    }

                };

        /**
         * 返回当前时间单位与毫秒的倍数
         *
         * @return
         */
        public abstract int getMultiple();
    }

    //计数器
    private class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            isCountDown = false;
            if (mCountDownListener != null) {
                mCountDownListener.onCountDownFinish();
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            isCountDown = true;
            if (mCountDownListener != null) {
                mCountDownListener.onCountDownTick(millisUntilFinished);
            }
        }
    }


    public void setOnCountDownListener(OnCountDownListener listener) {
        mCountDownListener = listener;
    }

    public interface OnCountDownListener {
        void onCountDownFinish();

        void onCountDownTick(long millisUntilFinished);

    }
}
