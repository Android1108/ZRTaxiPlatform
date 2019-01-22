package org.wzeiri.zr.zrtaxiplatform.widget.tag;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/17.
 */

public class TagRadioGroup extends LinearLayoutCompat {
    /**
     * 已勾选的TagRatioButton
     */
    private TagRatioButton mChildCheckTagRatioButton = null;

    private List<TagRatioButton> mButtonList = new ArrayList<>();

    private OnCheckChangeListener mOnCheckedChangeListener = null;

    private ChildCheckedStateTracker mChildOnCheckedChangeListener = null;
    private PassThroughHierarchyChangeListener mPassThroughHierarchyChangeListener = null;

    private final int TAG_RADIO_BUTTON_ID = 13200;

    private int IdPosition = 1;


    public TagRadioGroup(Context context) {
        this(context, null);
    }

    public TagRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        init();
    }

    private void init() {
        mChildOnCheckedChangeListener = new ChildCheckedStateTracker();
        mPassThroughHierarchyChangeListener = new PassThroughHierarchyChangeListener();
        super.setOnHierarchyChangeListener(mPassThroughHierarchyChangeListener);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    private void setCheck(TagRatioButton childButton, boolean isCheck) {
        if (childButton == null) {
            return;
        }

        if (mChildCheckTagRatioButton != null && mChildCheckTagRatioButton.getId() == childButton.getId()) {
            return;
        }

        if (isCheck) {
            childButton.setChecked(true);

            if (mChildCheckTagRatioButton != null) {
                mChildCheckTagRatioButton.setChecked(false);
            }

            mChildCheckTagRatioButton = childButton;
        }
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckChange(this, childButton, isCheck);
        }
    }

    /**
     * 勾选子button
     *
     * @param isCheck
     * @param index
     */
    public void setCheckAt(boolean isCheck, int index) {
        if (index < 0 || index > getChildCount()) {
            return;
        }

        View view = mButtonList.get(index);

        TagRatioButton tagRatioButton = mButtonList.get(index);
        if (tagRatioButton == null) {
            return;
        }

        if (tagRatioButton.isChecked() == isCheck) {
            return;
        }
        //判断当前是否有已选中的单选
        if (mChildCheckTagRatioButton != null) {
            //判断当前以选中的按钮是否是需要设置的单选
            if (mChildCheckTagRatioButton.getId() == tagRatioButton.getId()) {
                mChildCheckTagRatioButton.setChecked(isCheck);

                if (mOnCheckedChangeListener != null) {
                    mOnCheckedChangeListener.onCheckChange(this, mChildCheckTagRatioButton, isCheck);
                }

                if (!isCheck) {
                    mChildCheckTagRatioButton = null;
                }

            } else {
                if (!isCheck) {
                    return;
                }
                tagRatioButton.setChecked(isCheck);
                if (mOnCheckedChangeListener != null) {
                    mOnCheckedChangeListener.onCheckChange(this, tagRatioButton, isCheck);
                }

                mChildCheckTagRatioButton.setChecked(!isCheck);
                if (mOnCheckedChangeListener != null) {
                    mOnCheckedChangeListener.onCheckChange(this, mChildCheckTagRatioButton, isCheck);
                }
                mChildCheckTagRatioButton = tagRatioButton;
            }

        } else {
            if (!isCheck) {
                return;
            }
            tagRatioButton.setChecked(true);
            tagRatioButton.setChecked(isCheck);
            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckChange(this, tagRatioButton, isCheck);
            }
            mChildCheckTagRatioButton = tagRatioButton;

        }
    }

    /**
     * 勾选子button
     *
     * @param isCheck
     * @param id
     */
    public void setCheck(boolean isCheck, @IdRes int id) {
        View view = findViewById(id);

        if (view == null || !(view instanceof TagRatioButton)) {
            return;
        }


        TagRatioButton tagRatioButton = (TagRatioButton) view;
        if (tagRatioButton.isChecked() == isCheck) {
            return;
        }

        //判断当前是否有已选中的单选
        if (mChildCheckTagRatioButton != null) {
            //判断当前以选中的按钮是否是需要设置的单选
            if (mChildCheckTagRatioButton.getId() == tagRatioButton.getId()) {
                mChildCheckTagRatioButton.setChecked(isCheck);

                if (mOnCheckedChangeListener != null) {
                    mOnCheckedChangeListener.onCheckChange(this, mChildCheckTagRatioButton, isCheck);
                }

                if (!isCheck) {
                    mChildCheckTagRatioButton = null;
                }

            } else {
                if (!isCheck) {
                    return;
                }
                tagRatioButton.setChecked(isCheck);
                if (mOnCheckedChangeListener != null) {
                    mOnCheckedChangeListener.onCheckChange(this, tagRatioButton, isCheck);
                }

                mChildCheckTagRatioButton.setChecked(!isCheck);
                if (mOnCheckedChangeListener != null) {
                    mOnCheckedChangeListener.onCheckChange(this, mChildCheckTagRatioButton, isCheck);
                }
                mChildCheckTagRatioButton = tagRatioButton;
            }

        } else {
            if (!isCheck) {
                return;
            }
            tagRatioButton.setChecked(true);
            tagRatioButton.setChecked(isCheck);
            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckChange(this, tagRatioButton, isCheck);
            }
            mChildCheckTagRatioButton = tagRatioButton;

        }
    }


    public void setOnCheckedChangeListener(OnCheckChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }


    /**
     * 返回以选中的子button
     *
     * @return
     */
    public TagRatioButton getCheckButton() {
        return mChildCheckTagRatioButton;
    }

    private class ChildCheckedStateTracker implements TagRatioButton.OnCheckedChangeWidgetListener {

        @Override
        public void onCheckedChangeWidget(TagRatioButton button, boolean isCheck) {
            setCheck(button, isCheck);
        }
    }

    public interface OnCheckChangeListener {
        void onCheckChange(TagRadioGroup group, TagRatioButton childButton, boolean isCheck);
    }


    /**
     * View树改变的监听
     */
    private class PassThroughHierarchyChangeListener implements ViewGroup.OnHierarchyChangeListener {
        private ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener;

        @Override
        public void onChildViewAdded(View parent, View child) {
            if (parent == TagRadioGroup.this && child instanceof TagRatioButton) {
                mButtonList.add((TagRatioButton) child);
                int id = child.getId();
                // generates an id if it's missing
                if (id == View.NO_ID) {
                    id = TAG_RADIO_BUTTON_ID + IdPosition;
                    IdPosition++;
                    child.setId(id);
                }
                ((TagRatioButton) child).setOnCheckedChangeWidgetListener(
                        mChildOnCheckedChangeListener);

                if (((TagRatioButton) child).isChecked()) {

                    if (mOnCheckedChangeListener != null) {
                        mOnCheckedChangeListener.onCheckChange(TagRadioGroup.this,
                                (TagRatioButton) child, true);
                    }

                    if (mChildCheckTagRatioButton == null) {
                        mChildCheckTagRatioButton = ((TagRatioButton) child);
                    } else {
                        mChildCheckTagRatioButton.setChecked(false);

                        if (mOnCheckedChangeListener != null) {
                            mOnCheckedChangeListener.onCheckChange(TagRadioGroup.this,
                                    mChildCheckTagRatioButton, true);
                        }
                        mChildCheckTagRatioButton = ((TagRatioButton) child);
                    }
                }

            }

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }

        @Override
        public void onChildViewRemoved(View parent, View child) {
            if (parent == TagRadioGroup.this && child instanceof TagRadioGroup) {
                ((TagRatioButton) child).setOnCheckedChangeWidgetListener(null);
                mButtonList.remove(child);
            }

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }
    }
}
