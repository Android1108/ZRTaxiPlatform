package org.wzeiri.zr.zrtaxiplatform.util;

import android.content.Context;

public class DensityUtil {
    /**
     * 屏幕宽度
     */
    public static int WINDOW_WIDTH = 0;
    /**
     * 屏幕高度
     */
    public static int WINDOW_HEIGHT = 0;
    /**
     * 对话框大小
     */
    private static int mDialogWidth = 0;
    /**
     * 对话框宽度比例
     */
    private static final float DIALOG_RATIO = 0.8F;

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 sp
     *
     * @param pxValue
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 sp 的单位 转成为 px(像素)
     *
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 返回对话框宽度
     *
     * @return
     */
    public static int getDialogWidth() {
        if (mDialogWidth == 0) {
            mDialogWidth = (int) (WINDOW_WIDTH * DIALOG_RATIO);
        }
        return mDialogWidth;
    }
}
