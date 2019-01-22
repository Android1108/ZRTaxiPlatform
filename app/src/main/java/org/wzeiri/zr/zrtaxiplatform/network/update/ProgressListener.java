package org.wzeiri.zr.zrtaxiplatform.network.update;

/**
 * Created by klm on 2017/4/11.
 */

public interface ProgressListener {
    /**
     * @param progress 已经下载或上传字节数
     * @param total    总字节数
     * @param done     是否完成
     */
    void onProgress(long progress, long total, boolean done);
}
