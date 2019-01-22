package org.wzeiri.zr.zrtaxiplatform.network;

import org.wzeiri.zr.zrtaxiplatform.util.ThreadSwitch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

/**
 * @author k-lm on 2017/12/9.
 */

public class DownloadUtil {
    private DownloadUtil() {

    }

    /**
     * 保存文件
     *
     * @param body
     * @param path     文件路径
     * @param listener 回调
     */
    public static void saveFile(final ResponseBody body, final String path, final OnSaveFileListener listener) {
        ThreadSwitch.createTask(new ThreadSwitch.OnCreateListener<Boolean>() {
            @Override
            public Boolean onCreate(ThreadSwitch threadSwitch) {
                return writeResponseBodyToDisk(body, path);
            }
        })
                .switchLooper(ThreadSwitch.MAIN_THREAD)
                .submit(new ThreadSwitch.OnSubmitListener<Boolean>() {
                    @Override
                    public void onSubmit(ThreadSwitch threadSwitch, Boolean value) {
                        if (listener != null) {
                            listener.saveFile(value, path);
                        }
                    }
                });

    }


    private static boolean writeResponseBodyToDisk(ResponseBody body, String path) {
        try {
            File futureStudioIconFile = new File(path);
            if (!futureStudioIconFile.getParentFile().exists()) {
                futureStudioIconFile.getParentFile().mkdirs();
            }


            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }


    public interface OnSaveFileListener {
        void saveFile(boolean isSuccess, String file);
    }

}
