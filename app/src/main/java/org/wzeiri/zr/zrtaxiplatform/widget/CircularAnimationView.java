package org.wzeiri.zr.zrtaxiplatform.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import org.wzeiri.zr.zrtaxiplatform.R;

/**
 * @author k-lm on 2017/11/23.
 */

public class CircularAnimationView extends View {

    private Paint mCircularPaint;

    private Paint mLinePaint;
    /**
     * 外圆半径
     */
    private int mOuterCircleRadius = 0;
    /**
     * 内圆半径
     */
    private int mInnerCircleRadius = 0;
    /**
     * 线高度
     */
    private int mLineHeight = 0;
    /**
     * 线宽度
     */
    private int mLineWidth = 4;
    /**
     * 外圆
     */
    private RectF mOuterCircleRectF;
    /**
     * 内圆
     */
    private RectF mInnerCircleRectF;

    /**
     * 外圆颜色
     */
    private int mOuterCircleColor = 0;
    /**
     * 内圆颜色
     */
    private int mInnerCircleColor = 0;
    /**
     * 是否画外圆
     */
    private boolean isDrawOuterCircle = true;
    /**
     * 旋转角度
     */
    private int mDegrees = 0;

    public CircularAnimationView(Context context) {
        this(context, null);
    }

    public CircularAnimationView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularAnimationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initAttr(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CircularAnimationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    private void init() {
        mCircularPaint = new Paint();
        mCircularPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mCircularPaint.setAntiAlias(true);

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(Color.WHITE);
        mLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mLinePaint.setStrokeWidth(mLineWidth);

    }


    private void initAttr(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CircularAnimationView, 0, 0);
        isDrawOuterCircle = a.getBoolean(R.styleable.CircularAnimationView_circularDrawOuterCircle, isDrawOuterCircle);
        mOuterCircleColor = a.getColor(R.styleable.CircularAnimationView_circularOuterCircleColor, mOuterCircleColor);
        mInnerCircleColor = a.getColor(R.styleable.CircularAnimationView_circularInnerCircleColor, mInnerCircleColor);
        mDegrees = a.getInt(R.styleable.CircularAnimationView_circularDegrees, mDegrees);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int viewWidth = getMeasuredWidth();
        if (isDrawOuterCircle) {
            // 设置半径
            mInnerCircleRadius = (int) (viewWidth / 2 * 0.8);
            mOuterCircleRadius = viewWidth / 2;
        } else {
            mInnerCircleRadius = viewWidth / 2;
        }


        // 线高度
        mLineHeight = (int) (mInnerCircleRadius * 0.8);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int viewWidth = getMeasuredWidth();
        int viewHeight = getMeasuredHeight();
        // 中心点
        int centerX = viewWidth / 2;
        int centerY = viewHeight / 2;
        // 选择角度
        canvas.rotate(mDegrees, centerX, centerY);

        if (isDrawOuterCircle) {
            // 设置外圆
            if (mOuterCircleRectF == null) {
                mOuterCircleRectF = new RectF(0,
                        0,
                        mOuterCircleRadius * 2,
                        mOuterCircleRadius * 2);
            } else {
                mOuterCircleRectF.set(0,
                        0,
                        mOuterCircleRadius * 2,
                        mOuterCircleRadius * 2);
            }
        }


        // 设置内圆
        if (mInnerCircleRectF == null) {
            mInnerCircleRectF = new RectF(centerX - mInnerCircleRadius,
                    centerY - mInnerCircleRadius,
                    centerX + mInnerCircleRadius,
                    centerY + mInnerCircleRadius);
        } else {
            mInnerCircleRectF.set(centerX - mInnerCircleRadius,
                    centerY - mInnerCircleRadius,
                    centerX + mInnerCircleRadius,
                    centerY + mInnerCircleRadius);
        }

        if (isDrawOuterCircle) {
            // 画外圆
            mCircularPaint.setColor(mOuterCircleColor);
            canvas.drawRoundRect(mOuterCircleRectF, centerX, centerY, mCircularPaint);
        }
        // 画内圆
        mCircularPaint.setColor(mInnerCircleColor);
        canvas.drawRoundRect(mInnerCircleRectF, centerX, centerY, mCircularPaint);

        // 画线
        canvas.drawLine(centerX,
                centerY - mLineHeight / 2.0F,
                centerX,
                centerY + mLineHeight / 2.0F,
                mLinePaint);

        canvas.drawLine(centerX - mLineHeight / 2.0F,
                centerY,
                centerX + mLineHeight / 2.0F,
                centerY,
                mLinePaint);

    }


    public void setOuterCircleColor(int color) {
        mOuterCircleColor = color;
    }

    public void setInnerCircleColor(int color) {
        mInnerCircleColor = color;
    }

    public void setDrawOuterCircle(boolean isDraw) {
        isDrawOuterCircle = isDraw;
    }

    public void setDegrees(int degrees) {
        mDegrees = degrees;
    }

    public void addDegrees(int degrees) {
        mDegrees += degrees;
    }


}
