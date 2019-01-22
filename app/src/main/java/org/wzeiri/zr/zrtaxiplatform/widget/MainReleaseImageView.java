package org.wzeiri.zr.zrtaxiplatform.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageView;

import org.wzeiri.zr.zrtaxiplatform.R;

/**
 * @author k-lm on 2017/12/4.
 */

public class MainReleaseImageView extends android.support.v7.widget.AppCompatImageView {

    private RectF mRectF;
    private Paint mPaint;
    private Path mPath;

    public MainReleaseImageView(Context context) {
        super(context);
        init();
    }

    public MainReleaseImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MainReleaseImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mPath = new Path();
        mRectF = new RectF();
        mPaint = new Paint();
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.gray20));
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mRectF.set(0, 0, getWidth(), getHeight());
        canvas.drawArc(mRectF, -136, 92, false, mPaint);


    }
}
