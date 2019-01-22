package org.wzeiri.zr.zrtaxiplatform.util;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import org.wzeiri.zr.zrtaxiplatform.R;

import java.io.File;

/**
 * 第三方地图工具类
 *
 * @author k-lm on 2017/11/30.
 */

public class ThirdPartyMapUtil {
    /**
     * 判断是否有该app
     *
     * @param packageName 包名
     * @return
     */
    public static boolean isPackageInstalled(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    /**
     * 是否安装百度地图
     *
     * @return
     */
    public static boolean isInstallBaiduMap() {
        return isPackageInstalled("com.baidu.BaiduMap");
    }

    /**
     * 是否安装腾讯地图
     *
     * @return
     */
    public static boolean isInstallTencentMap() {
        return isPackageInstalled("com.tencent.map");
    }

    /**
     * 是否安装高德地图
     *
     * @return
     */
    public static boolean isInstallMinimap() {
        return isPackageInstalled("com.autonavi.minimap");
    }

    /**
     * 进入百度地图
     *
     * @param context
     * @param region               城市名或县名
     * @param name                 目标名称
     * @param originLatitude       起点纬度
     * @param originLongitude      起点经度
     * @param destinationLatitude  目标纬度
     * @param destinationLongitude 目标经度
     */
    public static void startBaiduMap(Context context, String region, String name, double originLatitude, double originLongitude, double destinationLatitude, double destinationLongitude) {
        Intent intent = new Intent();
        StringBuilder builder = new StringBuilder("baidumap://map/direction?region=");
        builder.append(region);
        if (originLatitude != 0 && originLongitude != 0) {
            builder.append("&origin=");
            builder.append(originLatitude);
            builder.append(",");
            builder.append(originLongitude);
        }

        builder.append("&destination=name:");
        builder.append(name);
        if (destinationLatitude != 0 && destinationLongitude != 0) {
            builder.append("|latlng:");
            builder.append(destinationLatitude);
            builder.append(",");
            builder.append(destinationLongitude);
        }

        builder.append("&mode=driving");

        intent.setData(Uri.parse(builder.toString()));

        context.startActivity(intent);
    }

    /**
     * 跳转高德地图
     *
     * @param context
     * @param originLatitude       起点纬度
     * @param originLongitude      起点经度
     * @param name                 目标名称
     * @param destinationLatitude  目标纬度
     * @param destinationLongitude 目标经度
     * @param dev                  是否偏移(0:lat 和 lon 是已经加密后的,不需要国测加密; 1:需要国测加密)
     */
    public static void startMinimap(Context context, double originLatitude, double originLongitude, String name, double destinationLatitude, double destinationLongitude, int dev) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setPackage("com.autonavi.minimap");
        intent.addCategory("android.intent.category.DEFAULT");


        StringBuilder builder = new StringBuilder("androidamap://route?sourceApplication=");
        builder.append(context.getString(R.string.app_name));
        if (originLongitude != 0 && originLatitude != 0) {
            builder.append("&slon=");
            builder.append(originLongitude);
            builder.append("&slat=");
            builder.append(originLatitude);
        }

        if (destinationLongitude != 0 && destinationLatitude != 0) {
            builder.append("&dlon=");
            builder.append(destinationLongitude);
            builder.append("&dlat=");
            builder.append(destinationLatitude);
        }


        builder.append("&dname=");
        builder.append(name);
        builder.append("&dev=");
        builder.append(dev);
        builder.append("&t=2");


        intent.setData(Uri.parse(builder.toString()));

        context.startActivity(intent);
    }

    /**
     * 跳转到腾讯地图
     *
     * @param context
     * @param fromName             起点名称
     * @param originLatitude       起点纬度
     * @param originLongitude      起点经度
     * @param toName               目标名称
     * @param destinationLatitude  目标纬度
     * @param destinationLongitude 目标经度
     */
    public static void startTencentMap(Context context, String fromName, double originLatitude, double originLongitude, String toName, double destinationLatitude, double destinationLongitude) {

        Intent intent = new Intent();
        StringBuilder builder = new StringBuilder("qqmap://map/routeplan?type=drive");
        builder.append("&from=");
        builder.append(fromName);

        if (originLatitude != 0 && originLongitude != 0) {
            builder.append("&fromcoord=");
            builder.append(originLatitude);
            builder.append(",");
            builder.append(originLongitude);
        }
        builder.append("&to=");
        builder.append(toName);
        if (destinationLatitude != 0 && destinationLongitude != 0) {
            builder.append("&tocoord=");
            builder.append(destinationLatitude);
            builder.append(",");
            builder.append(destinationLongitude);
        }
        builder.append("&policy=1&coord_type=1&referer=");
        builder.append(context.getString(R.string.app_name));
        intent.setData(Uri.parse(builder.toString()));
        //intent.setData(Uri.parse("qqmap://map/routeplan?type=drive&fromcoord=39.980683,116.302&to=中关村&tocoord=39.9836,116.3164&policy=1&referer=myapp"));

        context.startActivity(intent);


    }

    /**
     * 跳转到外部地图
     *
     * @param context
     * @param toName               目标名称
     * @param destinationLatitude  目标纬度
     * @param destinationLongitude 目标经度
     */
    public static void startMap(Context context, String fromName, double originLatitude, double originLongitude, String toName, double destinationLatitude, double destinationLongitude) {
        try {
            //跳转第三方应用
            Uri mUri = Uri.parse("geo:" + destinationLatitude + "," + destinationLongitude + "?q=" + toName);
            Intent mIntent = new Intent(Intent.ACTION_VIEW, mUri);
            context.startActivity(mIntent);
        } catch (ActivityNotFoundException e) {
            // 通过浏览器跳转

            StringBuilder builder = new StringBuilder("http://apis.map.qq.com/uri/v1/routeplan?type=drive");
            builder.append("&from=");
            builder.append(fromName);

            if (originLatitude != 0 && originLongitude != 0) {
                builder.append("&fromcoord=");
                builder.append(originLatitude);
                builder.append(",");
                builder.append(originLongitude);
            }
            builder.append("&to=");
            builder.append(toName);
            if (destinationLatitude != 0 && destinationLongitude != 0) {
                builder.append("&tocoord=");
                builder.append(destinationLatitude);
                builder.append(",");
                builder.append(destinationLongitude);
            }
            builder.append("&policy=1&coord_type=1&referer=");
            builder.append(context.getString(R.string.app_name));

            Uri mUri = Uri.parse(builder.toString());
            Intent mIntent = new Intent(Intent.ACTION_VIEW, mUri);
            context.startActivity(mIntent);

        }

    }

}
