package org.wzeiri.zr.zrtaxiplatform.util;

import android.app.Activity;
import android.support.annotation.NonNull;

import org.wzeiri.zr.zrtaxiplatform.bean.UploadResultBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IFileUploader;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.base.BaseFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author k-lm on 2017/12/22.
 */

public class UploadPictureHelper {
    private BaseActivity mActivity;

    private BaseFragment mFragment;

    private IFileUploader mFileUploader;


    private List<File> mUploadFileList;

    private OnUploadListener mUploadListener;

    public UploadPictureHelper(BaseActivity activity) {
        mActivity = activity;
    }

    public UploadPictureHelper(BaseFragment fragment) {
        mFragment = fragment;
    }

    /**
     * 设置上传文件
     *
     * @param file
     */
    public void addUploadFile(File file) {
        if (mUploadFileList == null) {
            mUploadFileList = new ArrayList<>();
        }
        mUploadFileList.add(file);
    }

    /**
     * 设置上传文件
     *
     * @param files
     */
    public void addUploadFiles(List<File> files) {
        if (files == null) {
            return;
        }
        if (mUploadFileList == null) {
            mUploadFileList = new ArrayList<>();
        }
        mUploadFileList.addAll(files);
    }


    /**
     * 删除需要上传的文件
     *
     * @param file 文件
     */
    public void removeUploadFile(File file) {
        if (mUploadFileList == null) {
            return;
        }
        mUploadFileList.remove(file);

    }

    /**
     * 删除需要上传的文件
     *
     * @param index 上传顺序
     */
    public void removeUploadFile(int index) {
        if (mUploadFileList == null || mUploadFileList.size() <= index) {
            return;
        }
        mUploadFileList.remove(index);
    }

    /**
     * 删除所有需要上传的文件
     */
    public void removeAllUploadFile() {
        if (mUploadFileList == null) {
            return;
        }
        mUploadFileList.clear();
    }

    /**
     * 根据文件数量上传
     * <p> 文件数只有一个使用{@link #uploadSingleFile(File)}</p>
     * <p> 文件数有多个使用{@link #uploadFiles(List)}</p>
     */
    public void upload() {
        if (mUploadFileList == null) {
            return;
        }

        if (mUploadFileList.size() == 1) {
            uploadSingleFile(mUploadFileList.get(0));
        } else {
            uploadFiles(mUploadFileList);
        }

    }

    /**
     * 上传单个文件
     *
     * @param file 文件
     */
    public void uploadSingleFile(final File file) {
        if (mFileUploader == null) {
            mFileUploader = RetrofitHelper.create(IFileUploader.class);
        }

        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part bodyPart = MultipartBody.Part.createFormData("aFile",
                file.getName(),
                body);

        if (mActivity != null) {
            mFileUploader.uploadPicture(bodyPart)
                    .enqueue(getSingUploadCallBack(mActivity));
        } else if (mFragment != null) {

            mFileUploader.uploadPicture(bodyPart)
                    .enqueue(getSingUploadCallBack(mFragment));
        }
    }

    /**
     * 上传多文件
     *
     * @param files
     */
    public void uploadFiles(List<File> files) {
        if (files == null || files.size() == 0) {
            return;
        }

        if (mFileUploader == null) {
            mFileUploader = RetrofitHelper.create(IFileUploader.class);
        }
        int count = files.size();
        MultipartBody.Part[] parts = new MultipartBody.Part[count];

        for (int i = 0; i < count; i++) {
            File file = files.get(i);
            RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part bodyPart = MultipartBody.Part.createFormData(file.getPath(),
                    file.getName(),
                    body);
            parts[i] = bodyPart;

        }


        if (mActivity != null) {
            mFileUploader.uploadPictures(parts)
                    .enqueue(getUploadFilesCallBack(mActivity));
        } else if (mFragment != null) {
            mFileUploader.uploadPictures(parts)
                    .enqueue(getUploadFilesCallBack(mFragment));
        }
    }


    private MsgCallBack<BaseBean<UploadResultBean>> getSingUploadCallBack(BaseFragment fragment) {
        return new MsgCallBack<BaseBean<UploadResultBean>>(fragment, false) {
            @Override
            public void onSuccess(Call<BaseBean<UploadResultBean>> call, Response<BaseBean<UploadResultBean>> response) {
                uploadSuccess(response.body().getResult());
            }

            @Override
            public void onError(Call<BaseBean<UploadResultBean>> call, Throwable t) {
                uploadError(t.getMessage());

            }
        };
    }

    private MsgCallBack<BaseBean<UploadResultBean>> getSingUploadCallBack(BaseActivity activity) {
        return new MsgCallBack<BaseBean<UploadResultBean>>(activity, false) {
            @Override
            public void onSuccess(Call<BaseBean<UploadResultBean>> call, Response<BaseBean<UploadResultBean>> response) {
                uploadSuccess(response.body().getResult());
            }

            @Override
            public void onError(Call<BaseBean<UploadResultBean>> call, Throwable t) {
                uploadError(t.getMessage());

            }
        };
    }


    private MsgCallBack<BaseBean<List<UploadResultBean>>> getUploadFilesCallBack(BaseActivity activity) {
        return new MsgCallBack<BaseBean<List<UploadResultBean>>>(mFragment, false) {
            @Override
            public void onSuccess(Call<BaseBean<List<UploadResultBean>>> call, Response<BaseBean<List<UploadResultBean>>> response) {
                List<UploadResultBean> beans = response.body().getResult();
                uploadSuccess(beans);
            }

            @Override
            public void onError(Call<BaseBean<List<UploadResultBean>>> call, Throwable t) {
                uploadError(t.getMessage());
            }
        };
    }

    private MsgCallBack<BaseBean<List<UploadResultBean>>> getUploadFilesCallBack(BaseFragment fragment) {
        return new MsgCallBack<BaseBean<List<UploadResultBean>>>(fragment, false) {
            @Override
            public void onSuccess(Call<BaseBean<List<UploadResultBean>>> call, Response<BaseBean<List<UploadResultBean>>> response) {
                List<UploadResultBean> beans = response.body().getResult();
                uploadSuccess(beans);
            }

            @Override
            public void onError(Call<BaseBean<List<UploadResultBean>>> call, Throwable t) {
                uploadError(t.getMessage());
            }
        };
    }


    /**
     * 上传成功
     *
     * @param bean
     */
    private void uploadSuccess(UploadResultBean bean) {

        if (mUploadListener != null) {
            List<UploadResultBean> list = new ArrayList<>(1);
            list.add(bean);
            mUploadListener.onUploadSuccess(list);
        }
    }

    private void uploadSuccess(List<UploadResultBean> beanList) {
        if (mUploadListener != null) {
            if (beanList == null) {
                beanList = new ArrayList<>(0);
            }
            mUploadListener.onUploadSuccess(beanList);
        }
    }


    private void uploadError(String msg) {
        if (mUploadListener != null) {
            mUploadListener.onUploadError(msg);
        }
    }

    public void setOnUploadListener(OnUploadListener listener) {
        mUploadListener = listener;
    }

    public interface OnUploadListener {

        void onUploadSuccess(@NonNull List<UploadResultBean> beans);


        void onUploadError(String msg);

    }


}
