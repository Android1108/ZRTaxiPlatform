package org.wzeiri.zr.zrtaxiplatform.util;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by adamzfc on 2/15/17.
 */

public class TimeUtil {
    public static final SimpleDateFormat mFormatDateAndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
    public static final SimpleDateFormat mFormatTime = new SimpleDateFormat("HH:mm:ss", Locale.US);
    public static final SimpleDateFormat mFormatDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    public static final SimpleDateFormat mFormatServer = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

    /**
     * 获取服务器日期
     *
     * @param date
     * @return
     */
    public static String getServerDate(Date date) {
        if (date == null) {
            return "";
        }
        return mFormatDate.format(date);
    }

    /**
     * 获取服务器时间
     *
     * @param date
     * @return
     */
    public static String getServerTime(Date date) {
        if (date == null) {
            return "";
        }
        return mFormatTime.format(date);
    }

    /**
     * 获取服务器日期和时间
     *
     * @param date
     * @return
     */
    public static String getServerDateAndTime(Date date) {
        if (date == null) {
            return "";
        }
        return mFormatDateAndTime.format(date);
    }

    /**
     * 获取服务器日期
     *
     * @param date
     * @return
     */
    public static String getServerDate(String date) {
        if (date == null) {
            return "";
        }
        Date date2 = null;
        try {
            date2 = mFormatServer.parse(date);
        } catch (Exception e) {
            return "";
        }
        return getServerDate(date2);
    }

    /**
     * 获取服务器时间
     *
     * @param date
     * @return
     */
    public static String getServerTime(String date) {
        if (date == null) {
            return "";
        }
        Date date2 = null;
        try {
            date2 = mFormatServer.parse(date);
        } catch (Exception e) {
            return "";
        }
        return getServerTime(date2);
    }

    /**
     * 获取服务器日期和时间
     *
     * @param date
     * @return
     */
    public static String getServerDateAndTime(String date) {
        if (date == null) {
            return "";
        }
        Date date2 = null;
        try {
            date2 = mFormatServer.parse(date);
        } catch (Exception e) {
            return "";
        }
        return getServerDateAndTime(date2);
    }

    /**
     * 将date转换成服务器时间
     *
     * @param date
     * @return
     */
    public static String getServiceTDate(Date date) {
        if (date == null) {
            return "";
        }
        return mFormatServer.format(date);
    }

    /**
     * 将时间戳转换成服务器时间
     *
     * @param time
     * @return
     */

    public static String timet(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;

    }

}
