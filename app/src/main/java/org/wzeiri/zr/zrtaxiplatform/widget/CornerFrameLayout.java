package org.wzeiri.zr.zrtaxiplatform.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import org.wzeiri.zr.zrtaxiplatform.R;


/**
 * Created by Lcsunm on 2017/6/1.
 * 修改版
 * 圆角FrameLayout
 *
 * 参考 ybao
 * https://github.com/Y-bao/RoundAngleFrameLayout/blob/master/app/src/main/java/com/ybao/rf/RoundAngleFrameLayout.java
 */

public class CornerFrameLayout extends FrameLayout {

    private float topLeftRadius;
    private float topRightRadius;
    private float bottomLeftRadius;
    private float bottomRightRadius;

    private float paddingTop;
    private float paddingLeft;
    private float paddingRight;
    private float paddingBottom;

    private boolean radiusPadding = false;

    private float arrowWidth;
    private float arrowHeight;

    private Paint roundPaint;
    //private Paint shadowPaint;

    private boolean isCircle = false;
    private Paint circlePaint;
    private PorterDuffXfermode circleXfermode;

    public CornerFrameLayout(Context context) {
        this(context, null);
    }

    public CornerFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CornerFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setLayerType(LAYER_TYPE_SOFTWARE, null);

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CornerFrameLayout);
            float radius = ta.getDimension(R.styleable.CornerFrameLayout_radius, 0);
            topLeftRadius = ta.getDimension(R.styleable.CornerFrameLayout_topLeftRadius, radius);
            topRightRadius = ta.getDimension(R.styleable.CornerFrameLayout_topRightRadius, radius);
            bottomLeftRadius = ta.getDimension(R.styleable.CornerFrameLayout_bottomLeftRadius, radius);
            bottomRightRadius = ta.getDimension(R.styleable.CornerFrameLayout_bottomRightRadius, radius);
            radiusPadding = ta.getBoolean(R.styleable.CornerFrameLayout_radiusPadding, radiusPadding);
            if (radiusPadding) {
                paddingTop = getPaddingTop();
                paddingLeft = getPaddingLeft();
                paddingRight = getPaddingRight();
                paddingBottom = getPaddingBottom();
            }
            isCircle = ta.getBoolean(R.styleable.CornerFrameLayout_isCircle, isCircle);
            ta.recycle();
        }

        if (!isCircle) {
            roundPaint = new Paint();
            roundPaint.setColor(Color.WHITE);
            roundPaint.setAntiAlias(true);
            roundPaint.setStyle(Paint.Style.FILL);
            roundPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        } else {
            circlePaint = new Paint();
            circlePaint.setColor(Color.WHITE);
            circlePaint.setAntiAlias(true);
            circlePaint.setStyle(Paint.Style.FILL);
            circleXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        }
    }

    //实现4
    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.saveLayer(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), null, Canvas.ALL_SAVE_FLAG);
        if (!isCircle) {
            super.dispatchDraw(canvas);
        }

        if (isCircle) {
            int width = canvas.getWidth();
            int height = canvas.getHeight();
            float circleSize;
            float circleX;
            float circleY;
            if (!radiusPadding) {
                circleSize = width > height ? height : width;
                circleX = width / 2;
                circleY = height / 2;
            } else {
                float widthHasPadding = width - paddingLeft - paddingRight;
                float heightHasPadding = height - paddingTop - paddingBottom;
                circleSize = widthHasPadding > heightHasPadding ? heightHasPadding : widthHasPadding;
                circleX = widthHasPadding / 2 + paddingLeft;
                circleY = heightHasPadding / 2 + paddingTop;
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas1 = new Canvas(bitmap);
            super.dispatchDraw(canvas1);
            circlePaint.setXfermode(null);
            canvas.drawCircle(circleX, circleY, circleSize / 2, circlePaint);
            circlePaint.setXfermode(circleXfermode);
            canvas.drawBitmap(bitmap, 0, 0, circlePaint);

        } else if (paddingTop == 0 && paddingBottom == 0 && paddingLeft == 0 && paddingRight == 0) {
            drawTopLeft(canvas);
            drawTopRight(canvas);
            drawBottomLeft(canvas);
            drawBottomRight(canvas);
        } else {
            int width = canvas.getWidth();
            int height = canvas.getHeight();
            Path path = new Path();
            path.moveTo(paddingLeft, paddingTop + topLeftRadius);
            path.arcTo(new RectF(paddingLeft, paddingTop, paddingLeft + topLeftRadius, paddingTop + topLeftRadius), 180, 90);
            path.lineTo(width - paddingRight - topRightRadius, paddingTop);
            path.arcTo(new RectF(
                    width - paddingRight - topRightRadius,
                    paddingTop,
                    width - paddingRight,
                    paddingTop + topLeftRadius
            ), -90, 90);
            path.lineTo(width - paddingRight, height - paddingBottom - bottomRightRadius - arrowHeight);
            path.arcTo(new RectF(
                    width - paddingRight - topRightRadius,
                    height - paddingBottom - bottomRightRadius - arrowHeight,
                    width - paddingRight,
                    height - paddingBottom - arrowHeight
            ), 0, 90);
            /*path.lineTo(mArrowLocationX + arrowWidth / 2, height - paddingBottom - arrowHeight);
            path.lineTo(mArrowLocationX, height - paddingBottom - arrowHeight + arrowHeight);
            path.lineTo(mArrowLocationX - arrowWidth / 2, height - paddingBottom - arrowHeight);*/
            path.lineTo(paddingLeft + bottomLeftRadius, height - paddingBottom - arrowHeight);
            path.arcTo(new RectF(
                    paddingLeft,
                    height - paddingBottom - bottomLeftRadius - arrowHeight,
                    paddingLeft + bottomLeftRadius,
                    height - paddingBottom - arrowHeight
            ), 90, 90);
            path.lineTo(paddingLeft, paddingTop + topLeftRadius);

            path.lineTo(0, 0);
            path.lineTo(0, height);
            path.lineTo(width, height);
            path.lineTo(width, 0);
            path.lineTo(0, 0);
            path.lineTo(paddingLeft, paddingTop + topLeftRadius);

            path.close();
            canvas.drawPath(path, roundPaint);
        }

        canvas.restore();
    }

    private void drawTopLeft(Canvas canvas) {
        if (topLeftRadius > 0) {
            Path path = new Path();
            path.moveTo(0, topLeftRadius);
            path.lineTo(0, 0);
            path.lineTo(topLeftRadius, 0);
            path.arcTo(new RectF(0, 0, topLeftRadius * 2, topLeftRadius * 2),
                    -90, -90);
            path.close();
            canvas.drawPath(path, roundPaint);
        }
    }

    private void drawTopRight(Canvas canvas) {
        if (topRightRadius > 0) {
            int width = getWidth();
            Path path = new Path();
            path.moveTo(width - topRightRadius, 0);
            path.lineTo(width, 0);
            path.lineTo(width, topRightRadius);
            path.arcTo(new RectF(width - 2 * topRightRadius, 0, width,
                    topRightRadius * 2), 0, -90);
            path.close();
            canvas.drawPath(path, roundPaint);
        }
    }

    private void drawBottomLeft(Canvas canvas) {
        if (bottomLeftRadius > 0) {
            int height = getHeight();
            Path path = new Path();
            path.moveTo(0, height - bottomLeftRadius);
            path.lineTo(0, height);
            path.lineTo(bottomLeftRadius, height);
            path.arcTo(new RectF(0, height - 2 * bottomLeftRadius,
                    bottomLeftRadius * 2, height), 90, 90);
            path.close();
            canvas.drawPath(path, roundPaint);
        }
    }

    private void drawBottomRight(Canvas canvas) {
        if (bottomRightRadius > 0) {
            int height = getHeight();
            int width = getWidth();
            Path path = new Path();
            path.moveTo(width - bottomRightRadius, height);
            path.lineTo(width, height);
            path.lineTo(width, height - bottomRightRadius);
            path.arcTo(new RectF(width - 2 * bottomRightRadius, height - 2
                    * bottomRightRadius, width, height), 0, 90);
            path.close();
            canvas.drawPath(path, roundPaint);
        }
    }

}