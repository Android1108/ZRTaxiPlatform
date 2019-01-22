package org.wzeiri.zr.zrtaxiplatform.util;

import android.text.TextUtils;

/**
 * @author k-lm on 2018/1/6.
 */

public class HtmlUtil {
    /**
     * 给html数据添加图片样式
     *
     * @param html
     * @return
     */
    public static String getImageStyle(String html) {
        if (TextUtils.isEmpty(html)) {
            return "";
        }
        return "<style type='text/css'>"
                + "img {max-width: 100%;  height:auto; float: left;}"
                + "</style>"
                + html;
    }

    /**
     * 设置body内容不会超出屏幕
     *
     * @param html
     * @return
     */
    public static String getBodyAdaptableScreenStyle(String html) {
        if (TextUtils.isEmpty(html)) {
            return "";
        }
        return "<style type='text/css'>"
                + "body{ "
                + "word-wrap:break-word;"
                + "font-family:Arial;"
                + "}"
                + "</style>"
                + html;
    }

    /**
     * 设置body内容不会超出屏幕
     *
     * @param html
     * @return
     */
    public static String getBodyPaddingStyle(String html, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        if (TextUtils.isEmpty(html)) {
            return "";
        }
        return "<style type='text/css'>"
                + "body{ "
                + "padding:" + paddingTop + "px " + paddingRight + "px " + paddingBottom + "px " + paddingLeft + "px"
                + "}"
                + "</style>"
                + html;
    }

}
