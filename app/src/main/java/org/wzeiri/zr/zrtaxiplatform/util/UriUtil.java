package org.wzeiri.zr.zrtaxiplatform.util;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * @author k-lm on 2018/1/31.
 */

public class UriUtil {

    public static Uri getUri(Context context, String authority, File file) {
        Uri uri = null;

        // 7.0 权限问题
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context, authority, file);
        } else {
            uri = Uri.fromFile(file);
        }

        return uri;
    }

}
