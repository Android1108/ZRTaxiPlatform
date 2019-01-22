package org.wzeiri.zr.zrtaxiplatform.widget.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;

import com.aigestudio.wheelpicker.widgets.WheelDatePicker;

import org.wzeiri.zr.zrtaxiplatform.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author k-lm on 2017/12/12.
 */

public class SelectDateDialog extends BaseDialog {
    private WheelDatePicker mDatePicker;

    public SelectDateDialog(@NonNull Context context) {
        super(context);
    }

    public SelectDateDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_select_date;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        mDatePicker = view.findViewById(R.id.dialog_select_date_wdp_date_picker);
        mDatePicker.setVisibleItemCount(4);
    }


    public void setSelectDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        mDatePicker.setYear(calendar.get(Calendar.YEAR));
        mDatePicker.setMonth(calendar.get(Calendar.MONTH));
        mDatePicker.setSelectedDay(calendar.get(Calendar.DAY_OF_YEAR));


    }

    /**
     * 返回当前选择的日期
     *
     * @return
     */
    public Date getSelectDate() {
        return mDatePicker.getCurrentDate();
    }


}
