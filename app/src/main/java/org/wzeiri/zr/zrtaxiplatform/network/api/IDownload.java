package org.wzeiri.zr.zrtaxiplatform.network.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @author k-lm on 2017/12/9.
 */

public interface IDownload {
    /**
     * 下载文件
     *
     * @param fileUrl 文件url
     * @return
     */
    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);

    /**
     * 下载大文件 </n>
     * 文件过大请使用该接口
     *
     * @param fileUrl 文件url
     * @return
     */
    @Streaming
    @GET
    Call<ResponseBody> downloadBigFile(@Url String fileUrl);


}
