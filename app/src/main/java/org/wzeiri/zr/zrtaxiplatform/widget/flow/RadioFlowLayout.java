package org.wzeiri.zr.zrtaxiplatform.widget.flow;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author k-lm on 2017/12/4.
 */

public class RadioFlowLayout extends FlowLayout implements View.OnClickListener {

    private View mSelectView;

    private OnSelectItemViewListener mSelectItemViewListener;

    public RadioFlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RadioFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadioFlowLayout(Context context) {
        this(context, null);
    }


    private void init() {
        setOnHierarchyChangeListener(new OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                child.setOnClickListener(RadioFlowLayout.this);


            }

            @Override
            public void onChildViewRemoved(View parent, View child) {
                if (child.isSelected() && mSelectItemViewListener != null) {
                    selectView(null);
                }
            }
        });
    }

    /**
     * 选择view
     *
     * @param view
     */
    public void selectView(View view) {
        if (mSelectView != null) {
            mSelectView.setSelected(false);
            if (mSelectItemViewListener != null) {
                mSelectItemViewListener.onSelectItemView(mSelectView, false);
            }
        }

        mSelectView = view;
        if (view == null) {
            return;
        }
        mSelectView.setSelected(true);
        if (mSelectItemViewListener != null) {
            mSelectItemViewListener.onSelectItemView(mSelectView, true);
        }
    }

    /**
     * 选择view
     *
     * @param index 位置
     */
    public void selectView(int index) {
        selectView(getChildAt(index));
    }


    @Override
    public void onClick(View v) {
        selectView(v);
    }

    public void setOnSelectItemViewListener(OnSelectItemViewListener listener) {
        mSelectItemViewListener = listener;
    }

    /**
     * 选择item
     */
    public interface OnSelectItemViewListener {
        void onSelectItemView(View view, boolean isSelect);
    }

}
