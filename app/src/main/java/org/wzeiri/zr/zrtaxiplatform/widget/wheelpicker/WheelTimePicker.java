package org.wzeiri.zr.zrtaxiplatform.widget.wheelpicker;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.aigestudio.wheelpicker.WheelPicker;

import org.wzeiri.zr.zrtaxiplatform.R;

/**
 * @author k-lm on 2017/12/27.
 */

public class WheelTimePicker extends LinearLayout implements WheelPicker.OnItemSelectedListener {

    private WheelHourPicker mHourPicker;
    private WheelMinutePicker mMinutePicker;
    /**
     * 当前选择的小时
     */
    private String mSelectHourString = "";
    /**
     * 当前选择的分钟
     */
    private String mSelectMinuteString = "";
    /**
     * 当前选择的小时
     */
    private int mSelectHour = -1;

    /**
     * 当前选择的分钟
     */
    private int mSelectMinute = -1;

    public WheelTimePicker(Context context) {
        this(context, null);
    }

    public WheelTimePicker(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WheelTimePicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {
        inflate(getContext(), R.layout.view_wheel_time_picker, this);
        mHourPicker = findViewById(R.id.view_wheel_time_hour);
        mMinutePicker = findViewById(R.id.view_wheel_time_minute);

        mHourPicker.setOnItemSelectedListener(this);
        mMinutePicker.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(WheelPicker picker, Object data, int position) {
        switch (picker.getId()) {
            case R.id.view_wheel_time_hour:
                mSelectHourString = data.toString();
                mSelectHour = position;
                break;
            case R.id.view_wheel_time_minute:
                mSelectMinuteString = data.toString();
                mSelectMinute = position;
                break;
        }
    }

    /**
     * 设置显示时间
     *
     * @param count
     */
    public void setVisibleItemCount(int count) {
        mMinutePicker.setVisibleItemCount(count);
        mHourPicker.setVisibleItemCount(count);
    }

    /**
     * 返回当前选择的小时
     *
     * @return
     */
    public int getSelectHour() {
        if (mSelectHour < 0) {
            mSelectHour = mHourPicker.getSelectedItemPosition();
        }
        return mSelectHour;
    }

    /**
     * 返回当前选择的小时
     *
     * @return
     */
    public String getSelectHourStr() {
        if (TextUtils.isEmpty(mSelectHourString)) {
            mSelectHourString = mHourPicker.getSelectHourStr();
        }
        return mSelectHourString;
    }

    /**
     * 返回当前选择的分钟
     *
     * @return
     */
    public int getSelectMinute() {
        if (mSelectMinute < 0) {
            mSelectMinute = mMinutePicker.getSelectedItemPosition();
        }
        return mSelectMinute;
    }

    /**
     * 返回当前选择的分钟
     *
     * @return
     */
    public String getSelectMinuteStr() {
        if (TextUtils.isEmpty(mSelectMinuteString)) {
            mSelectMinuteString = mMinutePicker.getSelectMinuteStr();
        }
        return mSelectMinuteString;
    }
}
