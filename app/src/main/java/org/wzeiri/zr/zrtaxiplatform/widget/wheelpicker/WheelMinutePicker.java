package org.wzeiri.zr.zrtaxiplatform.widget.wheelpicker;

import android.content.Context;
import android.util.AttributeSet;

import com.aigestudio.wheelpicker.WheelPicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author k-lm on 2017/12/27.
 */

public class WheelMinutePicker extends WheelPicker {

    public WheelMinutePicker(Context context) {
        this(context, null);
    }

    public WheelMinutePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData();
        initSelectMinute();
    }

    private void initData() {
        List<String> data = new ArrayList<>();
        for (int i = 0; i <= 60; i++) {
            String hour;
            if (i < 10) {
                hour = "0" + i;
            } else {
                hour = i + "";
            }
            data.add(hour);
        }
        super.setData(data);
    }


    private void initSelectMinute() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("mm", Locale.CHINESE);
        String hour = format.format(date);
        int h = Integer.valueOf(hour);
        setSelectedItemPosition(h);
    }


    /**
     * 返回当前选择的分钟
     *
     * @return
     */
    public String getSelectMinuteStr() {
        return getData().get(getSelectedItemPosition()).toString();
    }

    @Override
    public void setSelectedItemPosition(int position) {
        super.setSelectedItemPosition(position);
    }


    @Override
    public void setData(List data) {
    }

}
