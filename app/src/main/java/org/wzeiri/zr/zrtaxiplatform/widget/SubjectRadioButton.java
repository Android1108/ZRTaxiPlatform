package org.wzeiri.zr.zrtaxiplatform.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.RadioButton;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.util.DensityUtil;

import java.lang.reflect.Field;

/**
 * @author k-lm on 2017/12/8.
 */

public class SubjectRadioButton extends android.support.v7.widget.AppCompatRadioButton {

    private TextPaint mTextPaint;
    private Paint mPaint;
    private String mLeftString;

    public SubjectRadioButton(Context context) {
        super(context);
        init();
    }

    public SubjectRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SubjectRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.gray50));
        mTextPaint.setTextSize(getResources().getDimension(R.dimen.text_size_medium));

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.gray50));

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            //4.4 以下版本 设置button为null
            // 4.4 以下版本不能直接设置为null，只能通过反射的方式设置
            try {
                Field field = getClass()
                        .getSuperclass()
                        .getSuperclass()
                        .getSuperclass()
                        .getDeclaredField("mButtonDrawable");
                field.setAccessible(true);
                field.set(this, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            setButtonDrawable(null);
        }


    }


    public void setLeftDrawText(String text) {
        mLeftString = text;
        Drawable drawable = getTextDrawable(text, mTextPaint);
        Drawable[] drawables = getCompoundDrawables();
        setCompoundDrawablesWithIntrinsicBounds(drawable, drawables[1], drawables[2], drawables[3]);
    }


    private Drawable getTextDrawable(String text, TextPaint textPaint) {
        if (text == null || TextUtils.isEmpty(text.trim()) || textPaint == null) {
            return null;
        }

        TextPaint.FontMetrics fontMetrics = textPaint.getFontMetrics();

        Rect bounds = new Rect();
        int textWidth = getTextWidth(textPaint, text);

        textPaint.getTextBounds(text, 0, text.length(), bounds);

        int margin = +DensityUtil.dip2px(getContext(), 6);
        int size = Math.max(textWidth, bounds.height()) + margin * 2;


        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        RectF rectF = new RectF(0, 0, size, size);
        canvas.drawRoundRect(rectF, size / 2, size / 2, mPaint);


        canvas.drawText(text, (size - textWidth) / 2, (size + bounds.height()) / 2, textPaint);
        canvas.save();
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        drawable.setBounds(margin, margin, drawable.getIntrinsicWidth(),
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

}
