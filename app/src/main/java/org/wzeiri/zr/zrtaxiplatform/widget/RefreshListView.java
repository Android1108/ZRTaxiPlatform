package org.wzeiri.zr.zrtaxiplatform.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.wzeiri.zr.zrtaxiplatform.R;

/**
 * @author k-lm on 2017/11/17.
 */

public class RefreshListView extends ListView {
    /**
     * 底部提示文字
     */
    private String mButtonHintText;

    private TextPaint mTextPaint;


    public RefreshListView(Context context) {
        super(context);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    public void setAdapter(ListAdapter adapter) {
        //4.4以下想要添加footerView需要在setAdapter之前加入footerView
        View view = new View(getContext());
        addFooterView(view);
        super.setAdapter(adapter);
        removeFooterView(view);
    }


    /**
     * 显示底部文字
     *
     * @param text
     */
    public void setHintText(String text) {
        if (TextUtils.equals(text, mButtonHintText)) {
            return;
        }

        if (mTextPaint == null) {
            mTextPaint = new TextPaint();
            mTextPaint.setTextSize(getResources().getDimension(R.dimen.text_size_small));
            mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.gray40));
            mTextPaint.setTextAlign(Paint.Align.CENTER);
            mTextPaint.setStyle(Paint.Style.FILL);
        }
        mButtonHintText = text;

        invalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawBottomHint(canvas);
    }

    private void drawBottomHint(Canvas canvas) {
        if (TextUtils.isEmpty(mButtonHintText)) {
            return;
        }

        int height = getHeight() - getPaddingBottom();
        int width = getWidth() - getPaddingLeft() - getPaddingRight();
       /* StaticLayout staticLayout = new StaticLayout(mButtonHintText,
                mTextPaint,
                width,
                Layout.Alignment.ALIGN_CENTER,
                1,
                0,
                false);
        staticLayout.draw(canvas);*/
       
        height -= getResources().getDimensionPixelOffset(R.dimen.layout_margin_tiny);
        canvas.drawText(mButtonHintText, width / 2, height, mTextPaint);

    }
}
