package org.wzeiri.zr.zrtaxiplatform.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;

import org.wzeiri.zr.zrtaxiplatform.R;


/**
 * 支持左右文本TextView 效果如下
 * Created by Lcsunm on 2017/2/21.
 */

public class ValueTextView extends android.support.v7.widget.AppCompatTextView {

    private String mTextRight = null;
    private int mTextRightColor;
    private float mTextRightSize;

    private String mHintRight = null;
    private int mHintRightColor;

    private String mTextLeft = null;
    private int mTextLeftColor;
    private float mTextLeftSize;
    private float mTextLeftWidth;
    private float mTextLeftMinWidth;

    private Paint mPaint;
    // 实际paddingLeft
    private int mPaddingLeft = 0;
    // 是否设置View的padding
    private boolean isSetViewPadding = true;


    public ValueTextView(Context context) {
        super(context);
        init(null, 0);

    }

    public ValueTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ValueTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ValueTextView, defStyleAttr, 0);
            mTextRight = a.getString(R.styleable.ValueTextView_rightText);
            mTextRightColor = a.getColor(R.styleable.ValueTextView_rightTextColor, 0xff666666);
            mTextRightSize = a.getDimensionPixelSize(R.styleable.ValueTextView_rightTextSize, getResources().getDimensionPixelSize(R.dimen.text_size_small));
            mHintRight = a.getString(R.styleable.ValueTextView_rightHint);
            mHintRightColor = a.getColor(R.styleable.ValueTextView_rightHintColor, 0x33000000);
            mTextLeft = a.getString(R.styleable.ValueTextView_leftText);
            mTextLeftColor = a.getColor(R.styleable.ValueTextView_leftTextColor, 0xff666666);
            mTextLeftSize = a.getDimensionPixelSize(R.styleable.ValueTextView_leftTextSize, getResources().getDimensionPixelSize(R.dimen.text_size_small));
            mTextLeftMinWidth = a.getDimensionPixelSize(R.styleable.ValueTextView_leftMinWidth, -1);
            a.recycle();
        }

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);

        mPaddingLeft = getPaddingLeft();

        if (!TextUtils.isEmpty(mTextLeft)) {
            mPaint.setTextSize(mTextLeftSize);
            mTextLeftWidth = getFontLength(mPaint, mTextLeft);
            if (mTextLeftMinWidth != -1 && mTextLeftWidth < mTextLeftMinWidth) {
                mTextLeftWidth = mTextLeftMinWidth;
            }

            setPaddingLeft((int) (mPaddingLeft + mTextLeftWidth));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!TextUtils.isEmpty(mTextLeft)) {
            mPaint.setTextSize(mTextLeftSize);
            mPaint.setColor(mTextLeftColor);
            drawTextLeft(canvas, mTextLeft);
        }
        if (!TextUtils.isEmpty(mTextRight)) {
            mPaint.setTextSize(mTextRightSize);
            mPaint.setColor(mTextRightColor);
            drawTextRight(canvas, mTextRight);
        } else if (!TextUtils.isEmpty(mHintRight)) {
            mPaint.setTextSize(mTextRightSize);
            mPaint.setColor(mHintRightColor);
            drawTextRight(canvas, mHintRight);
        }
    }

    private void drawTextRight(Canvas canvas, String text) {
        float drawRightWidth = getCompoundDrawables()[2] == null ? 0 : (getCompoundDrawables()[2].getBounds().width() + getCompoundDrawablePadding());
        float textLeft = getWidth() - getPaddingRight() - getFontLength(mPaint, text) - drawRightWidth;
        float textTop = (getHeight() - getFontHeight(mPaint)) / 2 + getFontLeading(mPaint);

        canvas.drawText(text, textLeft, textTop, mPaint);
    }

    private void drawTextLeft(Canvas canvas, String text) {
        float drawLeftWidth = getCompoundDrawables()[0] == null ? 0 : (getCompoundDrawables()[0].getBounds().width() + getCompoundDrawablePadding());
        float textLeft = mPaddingLeft + drawLeftWidth;
        float textTop = (getHeight() - getFontHeight(mPaint)) / 2 + getFontLeading(mPaint);
        float textWidth = getFontLength(mPaint, text);
        canvas.drawText(text, textLeft, textTop, mPaint);


        int padding = (int) Math.max(mTextLeftMinWidth, textLeft + textWidth);
        if (padding == getPaddingLeft()) {
            return;
        }
        setPaddingLeft(padding);
    }

    public void setTextRight(String textRight) {
        this.mTextRight = textRight;
        invalidate();
    }

    public void setTextLeft(String textLeft) {
        this.mTextLeft = textLeft;
        invalidate();
    }

    public void setHintRight(String hintRight) {
        this.mHintRight = hintRight;
        invalidate();
    }

    public String getTextRight() {
        return getTextRight(false);
    }

    public String getTextRight(boolean NotNull) {
        if (mTextRight == null && NotNull) {
            return "";
        }
        return mTextRight;
    }


    public String getHintRight() {
        return this.mHintRight;
    }

    public String getTextLeft() {
        return getTextLeft(false);
    }

    public String getTextLeft(boolean NotNull) {
        if (mTextLeft == null && NotNull) {
            return "";
        }
        return mTextLeft;
    }


    /**
     * 根据屏幕系数比例获取文字大小
     *
     * @return
     */
    private static float scalaFonts(int size) {
        //暂未实现
        return size;
    }

    /**
     * @return 返回指定笔和指定字符串的长度
     */
    public static float getFontLength(Paint paint, String str) {
        return paint.measureText(str);
    }

    /**
     * @return 返回指定笔的文字高度
     */
    public static float getFontHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.descent - fm.ascent;
    }

    /**
     * @return 返回指定笔离文字顶部的基准距离
     */
    public static float getFontLeading(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.leading - fm.ascent;
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        if (isSetViewPadding) {
            mPaddingLeft = left;
        }
        super.setPadding(left, top, right, bottom);
        isSetViewPadding = true;
    }

    private void setPaddingLeft(int paddingLeft) {
        isSetViewPadding = false;
        setPadding(paddingLeft, getPaddingTop(), getPaddingRight(), getPaddingBottom());

    }

}
