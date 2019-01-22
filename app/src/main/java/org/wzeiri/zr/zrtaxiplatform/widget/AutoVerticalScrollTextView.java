package org.wzeiri.zr.zrtaxiplatform.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Color;
import android.graphics.Matrix;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;


import org.wzeiri.zr.zrtaxiplatform.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 自动垂直滚动的TextView
 */
public class AutoVerticalScrollTextView extends TextSwitcher implements ViewSwitcher.ViewFactory {

    private Context mContext;

    //mInUp,mOutUp分别构成向下翻页的进出动画
    private Rotate3dAnimation mInUp;
    private Rotate3dAnimation mOutUp;

    private boolean mTurning;
    private long mAutoTurningTime = 2000;
    private AdTask mAdTask;

    private List<String> mList = new ArrayList<>();
    private int mCurrentIndex = 0;

    private OnItemTextClickListener mItemTextClickListener;

    public AutoVerticalScrollTextView(Context context) {
        this(context, null);
    }

    public AutoVerticalScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        init();

    }

    private void init() {

        setFactory(this);

        mInUp = createAnim(true, true);
        mOutUp = createAnim(false, true);

        setInAnimation(mInUp);//当View显示时动画资源ID
        setOutAnimation(mOutUp);//当View隐藏是动画资源ID。
        mAdTask = new AdTask(this);


        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemTextClickListener == null || mList == null || mList.size() == 0) {
                    return;
                }
                mItemTextClickListener.onItemTextClick(mCurrentIndex);
            }
        });
    }

    public void setTexts(List<String> list) {
        mList.clear();
        if (list == null || list.size() == 0) {
            return;
        }
        mList.addAll(list);
    }

    public void showNextText() {
        if (mList.size() == 0) {
            return;
        }
        if (mCurrentIndex + 1 >= mList.size()) {
            mCurrentIndex = 0;
        } else {
            mCurrentIndex++;
        }
        next();
        setText(mList.get(mCurrentIndex));
    }

    public AutoVerticalScrollTextView startTurning(long autoTurningTime) {
        //如果是正在翻页的话先停掉
        if (mTurning) {
            stopTurning();
        }
        if (mList.size() == 0) {
            return this;
        }
        this.mAutoTurningTime = autoTurningTime;
        mTurning = true;
        postDelayed(mAdTask, autoTurningTime);
        return this;
    }

    public void stopTurning() {
        mTurning = false;
        removeCallbacks(mAdTask);
    }

    private Rotate3dAnimation createAnim(boolean turnIn, boolean turnUp) {

        Rotate3dAnimation rotation = new Rotate3dAnimation(turnIn, turnUp);
        rotation.setDuration(500);//执行动画的时间
        rotation.setFillAfter(false);//是否保持动画完毕之后的状态
        rotation.setInterpolator(new AccelerateInterpolator());//设置加速模式

        return rotation;
    }


    //这里返回的TextView，就是我们看到的View,可以设置自己想要的效果
    @Override
    public View makeView() {

        TextView textView = new TextView(mContext);
        textView.setGravity(Gravity.LEFT);
        textView.setTextSize(14);
        textView.setSingleLine(true);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.black33));
        return textView;

    }

    //定义动作，向上滚动翻页
    public void next() {
        //显示动画
        if (getInAnimation() != mInUp) {
            setInAnimation(mInUp);
        }
        //隐藏动画
        if (getOutAnimation() != mOutUp) {
            setOutAnimation(mOutUp);
        }
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
    }

    public void setOnItemTextClickListener(OnItemTextClickListener listener) {
        mItemTextClickListener = listener;
    }

    class Rotate3dAnimation extends Animation {
        private float mCenterX;
        private float mCenterY;
        private final boolean mTurnIn;
        private final boolean mTurnUp;
        private Camera mCamera;

        public Rotate3dAnimation(boolean turnIn, boolean turnUp) {
            mTurnIn = turnIn;
            mTurnUp = turnUp;
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            mCamera = new Camera();
            mCenterY = getHeight();
            mCenterX = getWidth();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {

            final float centerX = mCenterX;
            final float centerY = mCenterY;
            final Camera camera = mCamera;
            final int derection = mTurnUp ? 1 : -1;

            final Matrix matrix = t.getMatrix();

            camera.save();
            if (mTurnIn) {
                camera.translate(0.0f, derection * mCenterY * (interpolatedTime - 1.0f), 0.0f);
            } else {
                camera.translate(0.0f, derection * mCenterY * (interpolatedTime), 0.0f);
            }
            camera.getMatrix(matrix);
            camera.restore();

            matrix.preTranslate(-centerX, -centerY);
            matrix.postTranslate(centerX, centerY);
        }
    }

    static class AdTask implements Runnable {

        private final WeakReference<AutoVerticalScrollTextView> reference;

        AdTask(AutoVerticalScrollTextView textView) {
            this.reference = new WeakReference<AutoVerticalScrollTextView>(textView);
        }

        @Override
        public void run() {
            AutoVerticalScrollTextView textView = reference.get();

            if (textView != null && textView.mTurning) {
                textView.showNextText();
                textView.postDelayed(textView.mAdTask, textView.mAutoTurningTime);
            }
        }
    }


    public interface OnItemTextClickListener {
        void onItemTextClick(int position);
    }

}
