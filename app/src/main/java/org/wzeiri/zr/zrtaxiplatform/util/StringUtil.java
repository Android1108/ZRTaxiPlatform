package org.wzeiri.zr.zrtaxiplatform.util;

import android.graphics.Paint;
import android.text.TextUtils;

/**
 * @author k-lm on 2017/12/13.
 */

public class StringUtil {

    /**
     * 返回文字宽度
     *
     * @param paint
     * @param str
     * @return
     */
    public static int getTextWidth(Paint paint, CharSequence str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, 0, str.length(), widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    /**
     * 如果str为空则返回""，如果不是则返回自身
     *
     * @param str
     * @return
     */
    public static String getString(String str) {
        return getString(str, "");
    }

    /**
     * 如果str为空则返回replaceStr，如果不是则返回自身
     *
     * @param str
     * @param replaceStr
     * @return
     */
    public static String getString(String str, String replaceStr) {
        if (str == null) {
            return replaceStr;
        }
        return str;
    }

    /**
     * 判断是否全是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        char[] chars = str.toCharArray();

        for (char c : chars) {
            if (c < 48 || c > 57) {
                return false;
            }
        }
        return true;
    }

}
