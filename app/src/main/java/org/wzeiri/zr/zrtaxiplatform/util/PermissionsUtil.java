package org.wzeiri.zr.zrtaxiplatform.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * @author k-lm on 2017/11/17.
 */

public class PermissionsUtil {

    /**
     * 判断权限集合
     *
     * @param context
     * @param permissions
     * @return true 缺少权限
     */
    public static boolean lacksPermissions(Context context, String... permissions) {
        //小于23 则直接返回 false
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }
        for (String permission : permissions) {
            if (lacksPermission(context, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 申请动态权限
     *
     * @param activity
     * @param requestCode
     * @param permissions
     */
    public static void startActivityForResult(Activity activity, int requestCode, String... permissions) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    /**
     * 判断是否有拍照权限
     *
     * @return 返回true 则有权限
     */
    public static boolean isCameraPermissions(Context context) {
        return !lacksPermission(context, Manifest.permission.CAMERA);
    }

    /**
     * 是否有定位权限
     *
     * @return 返回true 则有权限
     */
    public static boolean isLocationPermissions(Context context) {
        return !lacksPermissions(context, Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    /**
     * 是否有存储权限
     *
     * @return 返回true 则有权限
     */
    public static boolean isStoragePermissions(Context context) {
        return !lacksPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }


    /**
     * 判断是否缺少权限
     *
     * @param context
     * @param permission 单个权限
     * @return 返回 true 缺少权限
     */
    private static boolean lacksPermission(Context context, String permission) {
        //小于23 则直接返回 false
        /*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }*/
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED;
    }


}
