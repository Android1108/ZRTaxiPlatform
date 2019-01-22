package org.wzeiri.zr.zrtaxiplatform.widget.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;


import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.widget.wheelpicker.WheelTimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author k-lm on 2017/12/12.
 */

public class SelectTimeDialog extends BaseDialog {
    private WheelTimePicker mTimePicker;

    private Calendar mCalendar;

    private SimpleDateFormat mDateFormat;

    public SelectTimeDialog(@NonNull Context context) {
        this(context, R.style.NoTitleDialog);
    }

    public SelectTimeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mCalendar = Calendar.getInstance();
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_select_time;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        mTimePicker = view.findViewById(R.id.dialog_select_date_wdp_time_picker);
        mTimePicker.setVisibleItemCount(4);
    }


    /**
     * 设置时间
     *
     * @param date 日期格式为 yyyy-MM-DD
     */
    public void setDate(String date) {


        if (mDateFormat == null) {
            mDateFormat = new SimpleDateFormat("yyyy-MM-DD", Locale.CHINESE);
        }

        try {
            Date d = mDateFormat.parse(date);
            mCalendar.setTime(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public void setDate(Date date) {
        mCalendar.setTime(date);
    }

    public String getSelectHourToString() {
        return mTimePicker.getSelectHourStr();
    }

    public String getSelectMinuteToString() {
        return mTimePicker.getSelectMinuteStr();
    }

    /**
     * 返回当前选择的日期
     *
     * @return
     */
    public Date getSelectDate() {

        int hour = mTimePicker.getSelectHour();
        int minute = mTimePicker.getSelectMinute();
      /*  Date date = mCalendar.getTime();
        date.setHours(hour);
        date.setMinutes(minute);

        return mCalendar.getTime();*/
        mCalendar.set(Calendar.HOUR_OF_DAY, hour);
        mCalendar.set(Calendar.MINUTE, minute);
        return mCalendar.getTime();

    }

}
