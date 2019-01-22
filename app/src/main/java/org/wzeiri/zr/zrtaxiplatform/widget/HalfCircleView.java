package org.wzeiri.zr.zrtaxiplatform.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import org.wzeiri.zr.zrtaxiplatform.R;

import java.sql.Ref;

/**
 * @author k-lm on 2017/12/11.
 */

public class HalfCircleView extends View {
    Path mPath;
    private Paint mPaint;

    public HalfCircleView(Context context) {
        super(context);
        init();
    }

    public HalfCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HalfCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.orange4));
        mPaint.setAntiAlias(true);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        mPath.moveTo(0, 0);
        mPath.lineTo(0, height / 10 * 7);
        mPath.quadTo(width / 2, height / 10 * 13, width, height / 10 * 7);
        mPath.lineTo(width, 0);
        mPath.close();
        canvas.drawPath(mPath, mPaint);

    }
}
