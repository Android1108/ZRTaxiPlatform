package org.wzeiri.zr.zrtaxiplatform.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.util.DensityUtil;

/**
 * @author klm on 2017/9/28.
 */

public class DrawTextTextView extends android.support.v7.widget.AppCompatTextView {

    private TextPaint mLeftPaint, mTopPaint, mRightPaint, mBottomPaint;

    private String mLeftString, mTopString, mRightString, mBottomString;

    public DrawTextTextView(Context context) {
        super(context);
    }

    public DrawTextTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public DrawTextTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        if (attrs == null) {
            return;
        }

        int paintColor = getPaint().getColor();
        float paintSize = getPaint().getTextSize();

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DrawTextTextView, defStyleAttr, 0);

        mLeftString = a.getString(R.styleable.DrawTextTextView_drawableLeftText);
        int leftColor = a.getColor(R.styleable.DrawTextTextView_drawableLeftTextColor, paintColor);
        float leftSize = a.getDimension(R.styleable.DrawTextTextView_drawableLeftTextSize, paintSize);


        mTopString = a.getString(R.styleable.DrawTextTextView_drawableTopText);
        int topColor = a.getColor(R.styleable.DrawTextTextView_drawableTopTextColor, paintColor);
        float topSize = a.getDimension(R.styleable.DrawTextTextView_drawableTopTextSize, paintSize);

        mRightString = a.getString(R.styleable.DrawTextTextView_drawableRightText);
        int rightColor = a.getColor(R.styleable.DrawTextTextView_drawableRightTextColor, paintColor);
        float rightSize = a.getDimension(R.styleable.DrawTextTextView_drawableRightTextSize, paintSize);

        mBottomString = a.getString(R.styleable.DrawTextTextView_drawableBottomText);
        int bottomColor = a.getColor(R.styleable.DrawTextTextView_drawableBottomTextColor, paintColor);
        float bottomSize = a.getDimension(R.styleable.DrawTextTextView_drawableBottomTextSize, paintSize);

        a.recycle();

        if (!TextUtils.isEmpty(mLeftString) || leftColor != paintColor || leftSize != paintSize) {
            setLeftDrawText(mLeftString, leftColor, leftSize);
        }

        if (!TextUtils.isEmpty(mTopString) || topColor != paintColor || topSize != paintSize) {
            setTopDrawText(mTopString, topColor, topSize);
        }

        if (!TextUtils.isEmpty(mRightString) || rightColor != paintColor || rightSize != paintSize) {
            setRightDrawText(mRightString, rightColor, rightSize);
        }

        if (!TextUtils.isEmpty(mBottomString) || bottomColor != paintColor || bottomSize != paintSize) {
            setBottomDrawText(mBottomString, bottomColor, bottomSize);
        }


    }


    public void setLeftDrawText(String text) {
        int color;
        if (mLeftPaint == null) {
            color = getPaint().getColor();
        } else {
            color = mLeftPaint.getColor();
        }
        setLeftDrawTextColor(text, color);
    }

    public void setRightDrawText(String text) {
        int color;
        if (mRightPaint == null) {
            color = getPaint().getColor();
        } else {
            color = mRightPaint.getColor();
        }
        setRightDrawTextColor(text, color);
    }

    public void setTopDrawText(String text) {
        int color;
        if (mTopPaint == null) {
            color = getPaint().getColor();
        } else {
            color = mTopPaint.getColor();
        }
        setTopDrawTextColor(text, color);
    }

    public void setBottomDrawText(String text) {
        int color;
        if (mBottomPaint == null) {
            color = getPaint().getColor();
        } else {
            color = mBottomPaint.getColor();
        }
        setBottomDrawTextColor(text, color);
    }


    public void setLeftDrawTextColor(String text, int color) {
        float size;
        if (mLeftPaint == null) {
            size = getPaint().getTextSize();
        } else {
            size = mLeftPaint.getTextSize();
        }
        setLeftDrawText(text, color, size);
    }

    public void setRightDrawTextColor(String text, int color) {
        float size;
        if (mRightPaint == null) {
            size = getPaint().getTextSize();
        } else {
            size = mRightPaint.getTextSize();
        }
        setRightDrawText(text, color, size);
    }

    public void setTopDrawTextColor(String text, int color) {
        float size;
        if (mTopPaint == null) {
            size = getPaint().getTextSize();
        } else {
            size = mTopPaint.getTextSize();
        }
        setTopDrawText(text, color, size);
    }

    public void setBottomDrawTextColor(String text, int color) {
        float size;
        if (mBottomPaint == null) {
            size = getPaint().getTextSize();
        } else {
            size = mBottomPaint.getTextSize();
        }
        setBottomDrawText(text, color, size);
    }


    public void setLeftDrawTextSize(String text, float size) {
        int color;
        if (mLeftPaint == null) {
            color = getPaint().getColor();
        } else {
            color = mLeftPaint.getColor();
        }
        setLeftDrawText(text, color, size);
    }

    public void setRightDrawTextSize(String text, float size) {
        int color;
        if (mRightPaint == null) {
            color = getPaint().getColor();
        } else {
            color = mRightPaint.getColor();
        }
        setRightDrawText(text, color, size);
    }

    public void setTopDrawTextSize(String text, float size) {
        int color;
        if (mTopPaint == null) {
            color = getPaint().getColor();
        } else {
            color = mTopPaint.getColor();
        }
        setTopDrawText(text, color, size);
    }

    public void setBottomDrawTextSize(String text, float size) {
        int color;
        if (mBottomPaint == null) {
            color = getPaint().getColor();
        } else {
            color = mBottomPaint.getColor();
        }
        setBottomDrawText(text, color, size);
    }


    public void setLeftDrawText(String text, int color, float size) {
        if (mLeftPaint == null) {
            mLeftPaint = new TextPaint(getPaint());
//            mLeftPaint.setTextAlign(Paint.Align.CENTER);
        }
        mLeftString = text;
        mLeftPaint.setColor(color);
        mLeftPaint.setTextSize(size);
        Drawable drawable = getTextDrawable(text, mLeftPaint);
        Drawable[] drawables = getCompoundDrawables();
        setCompoundDrawablesWithIntrinsicBounds(drawable, drawables[1], drawables[2], drawables[3]);
    }

    public void setRightDrawText(String text, int color, float size) {
        if (mRightPaint == null) {
            mRightPaint = new TextPaint(getPaint());
//            mRightPaint.setTextAlign(Paint.Align.CENTER);
        }
        mTopString = text;
        mRightPaint.setColor(color);
        mRightPaint.setTextSize(size);

        Drawable drawable = getTextDrawable(text, mRightPaint);
        Drawable[] drawables = getCompoundDrawables();
        setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], drawable, drawables[3]);
    }

    public void setTopDrawText(String text, int color, float size) {
        if (mTopPaint == null) {
            mTopPaint = new TextPaint(getPaint());
//            mTopPaint.setTextAlign(Paint.Align.CENTER);
        }
        mTopString = text;
        mTopPaint.setColor(color);
        mTopPaint.setTextSize(size);
        Drawable drawable = getTextDrawable(text, mTopPaint);
        Drawable[] drawables = getCompoundDrawables();
        setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawable, drawables[2], drawables[3]);
    }

    public void setBottomDrawText(String text, int color, float size) {
        if (mBottomPaint == null) {
            mBottomPaint = new TextPaint(getPaint());
//            mBottomPaint.setTextAlign(Paint.Align.CENTER);
        }
        mBottomString = text;
        mBottomPaint.setColor(color);
        mBottomPaint.setTextSize(size);
        Drawable drawable = getTextDrawable(text, mBottomPaint);
        Drawable[] drawables = getCompoundDrawables();
        setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], drawables[2], drawable);
    }

    private Drawable getTextDrawable(String text, TextPaint textPaint) {
        if (text == null || TextUtils.isEmpty(text.trim()) || textPaint == null) {
            return null;
        }

        TextPaint.FontMetrics fontMetrics = textPaint.getFontMetrics();

        int textWidth = getTextWidth(textPaint, text);
        float texHeight = Math.abs((fontMetrics.top - fontMetrics.bottom));


        int margin = +DensityUtil.dip2px(getContext(), 2);

        Bitmap bitmap = Bitmap.createBitmap(textWidth, (int) texHeight + margin * 2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawText(text, 0, texHeight + margin, textPaint);
        canvas.save();
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        return drawable;
    }


    private int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
