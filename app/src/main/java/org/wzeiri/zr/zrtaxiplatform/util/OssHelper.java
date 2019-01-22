package org.wzeiri.zr.zrtaxiplatform.util;

import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;

/**
 * @author k-lm on 2018/1/8.
 */

public class OssHelper {

   private static OSSCredentialProvider mCredentialProvider;

    public static void  init(){
        if(mCredentialProvider != null){
            return;
        }
    }
}
