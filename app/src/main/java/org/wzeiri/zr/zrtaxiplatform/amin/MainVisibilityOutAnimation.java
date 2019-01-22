package org.wzeiri.zr.zrtaxiplatform.amin;


import android.animation.Animator;
import android.animation.ValueAnimator;
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

/**
 * @author k-lm on 2017/11/24.
 */

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class MainVisibilityOutAnimation extends Visibility {

    private ImageView mSrcImageView;

    private ColorFilter mColorFilter;

    public MainVisibilityOutAnimation(ImageView stcImageView) {
        mSrcImageView = stcImageView;
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        super.captureStartValues(transitionValues);
        mColorFilter = mSrcImageView.getColorFilter();
        mSrcImageView.setVisibility(View.VISIBLE);

    }

    @Override
    public Animator onAppear(ViewGroup sceneRoot, final View view, TransitionValues startValues, TransitionValues endValues) {


        ValueAnimator anim = ValueAnimator.ofFloat(0, 1f);
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

                view.setAlpha(value);

            }
        });

        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mSrcImageView.setVisibility(View.VISIBLE);
                mSrcImageView.setColorFilter(mColorFilter);
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

                sceneRoot.setAlpha(value);
//                mSrcImageView.setAlpha(1F);


            }
        });

        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mSrcImageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
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
