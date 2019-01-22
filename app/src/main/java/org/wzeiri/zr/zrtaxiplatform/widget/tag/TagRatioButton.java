package org.wzeiri.zr.zrtaxiplatform.widget.tag;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;


/**
 * Created by Administrator on 2016/10/17.
 */

public class TagRatioButton extends LinearLayout {
    private ImageView mLeftIcon;
    private TextView mTopTextView;
    private TextView mBottomTextView;
    private RadioButton mContentRadio;

    private OnCheckedChangeWidgetListener mCheckedChangeWidgetListener = null;
    private OnCheckedChangeListener mCheckedChangeListener = null;

    public TagRatioButton(Context context) {
        this(context, null);
    }

    public TagRatioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initAttrs(attrs);
    }

    private void initView(Context context) {
        inflate(context, R.layout.view_tag_radio_button, this);
        setOrientation(HORIZONTAL);

        mLeftIcon = findViewById(R.id.view_tag_radio_button_image_icon);
        mTopTextView = findViewById(R.id.view_tag_radio_button_text_top_text);
        mBottomTextView = findViewById(R.id.view_tag_radio_button_text_bottom_text);
        mContentRadio = findViewById(R.id.view_tag_radio_button_rb_radio);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mContentRadio.setChecked(!mContentRadio.isChecked());
            }
        });

        mContentRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mCheckedChangeWidgetListener != null) {
                    mCheckedChangeWidgetListener.onCheckedChangeWidget(TagRatioButton.this, isChecked);
                }

                if (mCheckedChangeListener != null) {
                    mCheckedChangeListener.onCheckChangeListener(TagRatioButton.this, isChecked);
                }
            }
        });


    }

    private void initAttrs(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.TagRatioButton);

        String topText = ta.getString(R.styleable.TagRatioButton_tagRatioButton_topText);
        int topTextColor = ta.getColor(R.styleable.TagRatioButton_tagRatioButton_topTextColor, getResources().getColor(R.color.black70));
        float topTextSize = ta.getDimension(R.styleable.TagRatioButton_tagRatioButton_topTextSize, getResources().getDimension(R.dimen.text_size_medium));

        String bottomText = ta.getString(R.styleable.TagRatioButton_tagRatioButton_bottomText);
        int bottomTextColor = ta.getColor(R.styleable.TagRatioButton_tagRatioButton_bottomTextColor, getResources().getColor(R.color.gray60));
        float bottomTextSize = ta.getDimension(R.styleable.TagRatioButton_tagRatioButton_bottomTextSize, getResources().getDimension(R.dimen.text_size_small));


        setTopText(topText);
        setTopTextColor(topTextColor);
        setTopTextSize(topTextSize);

        setBottomText(bottomText);
        setBottomTextColor(bottomTextColor);
        setBottomTextSize(bottomTextSize);


        int icon = ta.getResourceId(R.styleable.TagRatioButton_tagRatioButton_leftIcon, 0);
        int iconSize = (int) ta.getDimension(R.styleable.TagRatioButton_tagRatioButton_leftIconSize, LayoutParams.WRAP_CONTENT);
        int iconMargin = ta.getDimensionPixelSize(R.styleable.TagRatioButton_tagRatioButton_leftIconMargin, 0);

        setLeftIconMargin(iconMargin);
        setLeftIconResource(icon);
        setLeftIconSize(iconSize);


        boolean check = ta.getBoolean(R.styleable.TagRatioButton_tagRatioButton_check, false);
        setChecked(check);

        ta.recycle();
    }

    /**
     * 设置图标
     *
     * @param drawable
     */
    public void setLeftIcon(Drawable drawable) {
        if (drawable != null) {
            setIsShowIcon(true);
        }
        mLeftIcon.setImageDrawable(drawable);
    }

    /**
     * 设置图标
     *
     * @param resId
     */
    public void setLeftIconResource(@DrawableRes int resId) {
        if (resId != 0) {
            setIsShowIcon(true);
        }
        mLeftIcon.setImageResource(resId);
    }

    /**
     * 是否显示图标
     *
     * @param isShow
     */
    public void setIsShowIcon(boolean isShow) {
        if (isShow) {
            mLeftIcon.setVisibility(VISIBLE);
        } else {
            mLeftIcon.setVisibility(GONE);
        }
    }

    /**
     * 设置图标大小
     *
     * @param size
     */
    public void setLeftIconSize(int size) {
        LayoutParams params = (LayoutParams) mLeftIcon.getLayoutParams();
        params.width = size;
        params.height = size;
    }

    /**
     * 设置图标和文字的间距
     *
     * @param margin
     */
    public void setLeftIconMargin(int margin) {
        LayoutParams params = (LayoutParams) mLeftIcon.getLayoutParams();
        params.rightMargin = margin;
    }

    /**
     * 设置输入文字
     *
     * @param text text
     */
    public void setTopText(CharSequence text) {
        mTopTextView.setText(text);
    }

    /**
     * 设置输入文字
     *
     * @param resId
     */
    public void setTopText(@StringRes int resId) {
        mTopTextView.setText(resId);

    }

    /**
     * 设置输入文字
     *
     * @param text text
     */
    public void setBottomText(CharSequence text) {
        mBottomTextView.setText(text);
    }

    /**
     * 设置输入文字
     *
     * @param resId
     */
    public void setBottomText(@StringRes int resId) {
        mBottomTextView.setText(resId);
    }


    /**
     * 设置文字大小
     *
     * @param size
     */
    public void setTopTextSize(float size) {
        mTopTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    /**
     * 设置文字大小
     *
     * @param size
     */
    public void setBottomTextSize(float size) {
        mBottomTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    /**
     * 设置文字颜色
     *
     * @param color
     */
    public void setTopTextColorResources(@ColorRes int color) {
        mTopTextView.setTextColor(getResources().getColor(color));
    }

    /**
     * 设置文字颜色
     *
     * @param color
     */
    public void setTopTextColor(int color) {
        mTopTextView.setTextColor(color);
    }

    /**
     * 设置文字颜色
     *
     * @param color
     */
    public void setBottomTextColorResources(@ColorRes int color) {
        mBottomTextView.setTextColor(getResources().getColor(color));
    }

    /**
     * 设置文字颜色
     *
     * @param color
     */
    public void setBottomTextColor(int color) {
        mBottomTextView.setTextColor(color);
    }


    /**
     * 设置是否选中
     *
     * @param isCheck
     */
    public void setChecked(boolean isCheck) {
        if (mContentRadio != null) {
            mContentRadio.setChecked(isCheck);
        }
    }

    /**
     * 设置选中改变事件回调
     *
     * @param listener
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mCheckedChangeListener = listener;
    }

    /**
     * 返回是否选中
     *
     * @return
     */
    public boolean isChecked() {
        if (mContentRadio != null) {
            return mContentRadio.isChecked();
        }
        return false;
    }

    /**
     * 设置选中状态改变事件回调<br>
     * 提供给TagRadioGroup调用
     *
     * @param listener
     */
    void setOnCheckedChangeWidgetListener(OnCheckedChangeWidgetListener listener) {
        mCheckedChangeWidgetListener = listener;
    }

    /**
     * 选中状态改变事件<br>
     * 提供给TagRadioGroup调用
     */
    public interface OnCheckedChangeWidgetListener {
        void onCheckedChangeWidget(TagRatioButton button, boolean isCheck);
    }

    /**
     * 选中状态改变事件
     */
    public interface OnCheckedChangeListener {
        void onCheckChangeListener(TagRatioButton button, boolean isCheck);
    }


}
