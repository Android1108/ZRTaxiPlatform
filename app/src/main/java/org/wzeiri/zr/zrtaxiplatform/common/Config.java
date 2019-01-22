package org.wzeiri.zr.zrtaxiplatform.common;

import android.os.Environment;

import org.wzeiri.zr.zrtaxiplatform.BuildConfig;

import java.io.File;

/**
 * @author k-lm on 2017/11/14.
 */

public class Config {
    public final static boolean DEBUG = true && BuildConfig.DEBUG;
    /**
     * 服务器地址
     */
    public static final String MOBILE_SERVER_RELEASE =
            // "http://114.55.101.33:12088"
//            "http://47.100.22.121:12088"
            "http://admin.daxiapp.cn"
                    + "";

    public static final String IMAGE_URL = MOBILE_SERVER_RELEASE + "/";

    /**
     * 空图片链接地址
     */
    public static final String NO_IMAGE_URL = IMAGE_URL + "Common/Images/no.png";
    // 微信 appId
    public static final String WECHAT_APPID = "wx0540b1d00fab34db";
    // 讯飞appId
    public static final String XUNFEI_APPID = "5a6fd364";

    /**
     * 文件保存目录
     */
    public static final String STORAGE_PATH = Environment.getExternalStorageDirectory().getPath() +
            File.separator + "ZRTaxi" + File.separator;
    /**
     * 上传图片的文件名
     */
    public static final String UPLOAD_IMAGE_NAME = "uploadImage.jpg";
    /**
     * 首页广告高度比例 gao
     */
    public static final double INDEX_ADVERTISEMENT_COVER_HEIGHT = 4.31;


    /**
     * 记录列表中封面图片 宽度比例
     */
    public static final double RECORD_COVER_WIDTH = 3 * 1.0 / 14;

    /**
     * 记录列表中封面图片 高度比例
     */
    public static final double RECORD_COVER_HEIGHT = 2 * 1.0 / 25;

    /**
     * 司机互动详情 图片宽度比例
     */
    public static final double DRIVER_INTERACTION_DETAILS_WIDTH = 515 * 1.0 / 565;


    /**
     * 司机互动详情 图片高度比例
     */
    public static final double DRIVER_INTERACTION_DETAILS_HEIGHT = DRIVER_INTERACTION_DETAILS_WIDTH * 270 / 515;

    /**
     * 商家特惠 头部高度：宽度比例
     */
    public static final double DRIVER_DISCOUNT_DETAIL_HEIGHT_WIDTH = 300 * 1.0 / 565;

}
