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

public class WheelHourPicker extends WheelPicker {

    public WheelHourPicker(Context context) {
        this(context, null);
    }

    public WheelHourPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData();
        initSelectHour();
    }

    private void initData() {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
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


    private void initSelectHour() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH", Locale.CHINESE);
        String hour = format.format(date);
        int h = Integer.valueOf(hour);
        setSelectedItemPosition(h);
    }


    @Override
    public void setSelectedItemPosition(int position) {
        super.setSelectedItemPosition(position);
    }

    /**
     * 返回当前选择的小时
     *
     * @return
     */
    public String getSelectHourStr() {
        return getData().get(getSelectedItemPosition()).toString();
    }

    @Override
    public void setData(List data) {
    }

}
