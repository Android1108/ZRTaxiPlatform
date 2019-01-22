package org.wzeiri.zr.zrtaxiplatform.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;


import org.wzeiri.zr.zrtaxiplatform.R;

/**
 * @author k-lm on 2017/12/7.
 */

public class ClearEditText extends android.support.v7.widget.AppCompatEditText {

    private Drawable mClearDrawable;


    public ClearEditText(Context context) {
        super(context);
        init(null, 0);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }


    private void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ClearEditText, defStyleAttr, 0);
        mClearDrawable = typedArray.getDrawable(R.styleable.ClearEditText_clearDrawable);
        setClearDrawable(mClearDrawable);
    }


    public void setClearDrawableResources(@DrawableRes int resId) {
        mClearDrawable =   new ColorDrawable(ContextCompat.getColor(getContext(),R.color.gray10));
        Drawable[] drawables = getCompoundDrawables();
        setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], mClearDrawable, drawables[3]);
    }

    public void setClearDrawable(Drawable drawable) {
        mClearDrawable = drawable;
        Drawable[] drawables = getCompoundDrawables();
        setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], mClearDrawable, drawables[3]);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        setShowClearDrawable(getText().length() != 0);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                Drawable drawable = getCompoundDrawables()[2];
                if (drawable != null && event.getX() <= (getWidth() - getPaddingRight())
                        && event.getX() >= (getWidth() - getPaddingRight() - drawable.getBounds().width())) {
                    setText("");
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    /**
     * 是否显示清除按钮
     *
     * @param isShow
     */
    private void setShowClearDrawable(boolean isShow) {
        if (mClearDrawable == null) {
            return;
        }

        Drawable[] drawables = getCompoundDrawables();
        setCompoundDrawablesWithIntrinsicBounds(drawables[0],
                drawables[1],
                isShow ? mClearDrawable : null,
                drawables[3]);


    }
}
