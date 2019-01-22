package org.wzeiri.zr.zrtaxiplatform.network.api;

import org.wzeiri.zr.zrtaxiplatform.bean.UploadResultBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

/**
 * @author k-lm on 2017/12/22.
 */

public interface IFileUploader {
    /**
     * POST /Api/App/FileUploader/UploadPicture
     * 上传图片(保存到临时目录)
     *
     * @param file
     * @return
     */
    @Multipart
    @POST("/Api/App/FileUploader/UploadPicture")
    Call<BaseBean<UploadResultBean>> uploadPicture(@Part() MultipartBody.Part file);

    /**
     * POST /Api/App/FileUploader/BatchUploadPicture
     * 批量上传图片
     *
     * @param parts
     * @return
     */
    @Multipart
    @POST("/Api/App/FileUploader/BatchUploadPicture")
    Call<BaseBean<List<UploadResultBean>>> uploadPictures(@Part MultipartBody.Part[] parts);

}
