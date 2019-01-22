package org.wzeiri.zr.zrtaxiplatform.amin;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import org.wzeiri.zr.zrtaxiplatform.R;

/**
 * @author k-lm on 2017/11/24.
 */

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class MainVisibilityIntoAnimation extends Visibility {

    private ImageView mSrcImageView;
    private ImageView mFromImageView;

    private View[] mAlphaViews;


    private float mPivotX = 0;
    private float mPivotY = 0;


    public MainVisibilityIntoAnimation(ImageView stcImageView, ImageView fromImageView, View... alphaViews) {
        mSrcImageView = stcImageView;
        mFromImageView = fromImageView;
        mAlphaViews = alphaViews;

    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        super.captureStartValues(transitionValues);
        mFromImageView.setVisibility(View.GONE);
        mSrcImageView.setVisibility(View.VISIBLE);
        int[] locations = new int[2];
        mSrcImageView.getLocationOnScreen(locations);


        mPivotX = locations[0];
        mPivotY = locations[1];


    }

    @Override
    public Animator onAppear(ViewGroup sceneRoot, final View view, TransitionValues startValues, TransitionValues endValues) {
        view.setPivotX(mPivotX);
        view.setPivotY(mPivotY);
        ValueAnimator anim = ValueAnimator.ofFloat(1f, 0f);
        anim.setDuration(getDuration());

        anim.setInterpolator(new LinearInterpolator());

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();


                float rotation = 135 * value - 135;
                mSrcImageView.setRotation(rotation);

                float saturation = 1 * value;

                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(saturation);//饱和度 0灰色 100过度彩色，50正常
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                mSrcImageView.setColorFilter(filter);

                if (mAlphaViews != null && mAlphaViews.length > 0) {
                    for (View view : mAlphaViews) {
                        view.setAlpha(1 - value);
                    }
                }

                // view.setAlpha(1 - value);
            }
        });

        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mFromImageView.setVisibility(View.VISIBLE);
                mSrcImageView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


        return anim;
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        super.captureEndValues(transitionValues);
        mFromImageView.setVisibility(View.GONE);
        mSrcImageView.setVisibility(View.VISIBLE);

    }


    @Override
    public Animator onDisappear(final ViewGroup sceneRoot, TransitionValues startValues, int startVisibility, TransitionValues endValues, int endVisibility) {
        ValueAnimator anim = ValueAnimator.ofFloat(0f, 1f);
        anim.setDuration(getDuration());

        anim.setInterpolator(new LinearInterpolator());

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();

                float rotation = 135 * value - 135;
                mSrcImageView.setRotation(rotation);

                float saturation = 1 * value;

                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(saturation);//饱和度 0灰色 100过度彩色，50正常
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                mSrcImageView.setColorFilter(filter);


                if (value > 0.5) {
                    sceneRoot.setAlpha(1 - value);
                }


//                mSrcImageView.setAlpha(1F);


            }
        });

        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mSrcImageView.setVisibility(View.VISIBLE);
                mFromImageView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mFromImageView.setVisibility(View.GONE);
                mSrcImageView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


        return anim;
    }


}
