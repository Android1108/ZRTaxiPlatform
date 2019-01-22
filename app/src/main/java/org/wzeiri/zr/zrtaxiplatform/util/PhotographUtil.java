package org.wzeiri.zr.zrtaxiplatform.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author k-lm on 2017/11/17.
 */

public class PhotographUtil {
    /**
     * 相机requestCode
     */
    public static final int CAMERA_REQUEST_CODE = 1024;

    /**
     * 相册requestCode
     */
    public static final int ALBUM_REQUEST_CODE = 1025;

    /**
     * 进入相机
     */
    public static boolean takeCamera(Activity activity, Uri imageUri) {
        return takeCamera(activity, imageUri, CAMERA_REQUEST_CODE);
    }

    /**
     * 进入相机
     */
    public static boolean takeCamera(Fragment fragment, Uri imageUri) {
        return takeCamera(fragment, imageUri, CAMERA_REQUEST_CODE);
    }

    /**
     * 进入相机
     */
    public static boolean takeCamera(Activity activity, Uri imageUri, int requestCode) {
        if (!isExistenceSDCard()) {
            return false;
        }

        if (!PermissionsUtil.isCameraPermissions(activity)) {
            PermissionsUtil.startActivityForResult(activity,
                    CAMERA_REQUEST_CODE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return true;
        }


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        //intent.putExtra("android.intent.extras.CAMERA_FACING", 0);
        activity.startActivityForResult(intent, requestCode);

        return true;
    }

    /**
     * 进入相机
     */
    public static boolean takeCamera(Fragment fragment, Uri imageUri, int requestCode) {
        if (!isExistenceSDCard()) {
            return false;
        }


        if (!PermissionsUtil.isCameraPermissions(fragment.getContext())) {
            String[] permissions = new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};
            fragment.requestPermissions(permissions, requestCode);
            return true;
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        fragment.startActivityForResult(intent, requestCode);

        return true;
    }

    /**
     * 进入相册
     */
    public static void choosePicture(Activity activity) {
        choosePicture(activity, ALBUM_REQUEST_CODE);
    }

    /**
     * 进入相册
     */
    public static void choosePicture(Fragment fragment) {
        choosePicture(fragment, ALBUM_REQUEST_CODE);
    }

    /**
     * 进入相册
     */
    public static void choosePicture(Activity activity, int requestCode) {
        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        activity.startActivityForResult(pickIntent, requestCode);
    }

    /**
     * 进入相册
     */
    public static void choosePicture(Fragment fragment, int requestCode) {
        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        fragment.startActivityForResult(pickIntent, requestCode);
    }

    /**
     * 根据文件名创建一个uri
     *
     * @param fileName 文件名称
     * @return 返回uri
     */
    public static Uri createImageUri(String fileName, @NonNull Context context) {
        if (!isExistenceSDCard()) {
            return null;
        }
        if (TextUtils.isEmpty(fileName)) {
            fileName = "image";
        }
        Uri imageUri;
        // 创建File对象 用于存储拍照后的图片
        File outputImage = new File(context.getExternalCacheDir(), fileName + ".jpg");
        // 如果文件存在 先删除
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageUri = UriUtil.getUri(context, context.getPackageName() + ".fileprovider", outputImage);
        return imageUri;
    }

    /**
     * 判断是否有sd卡
     *
     * @return
     */
    private static boolean isExistenceSDCard() {
        return !Environment.MEDIA_SHARED.equals(Environment.getExternalStorageState());
    }


    /**
     * 权限回调
     *
     * @param activity
     * @param requestCode
     * @param permissions
     * @param grantResults
     * @param uri          图片uri
     */
    public static boolean onRequestPermissionsResult(Activity activity,
                                                     int requestCode,
                                                     @NonNull String[] permissions,
                                                     @NonNull int[] grantResults,
                                                     Uri uri) {
        return onRequestPermissionsResult(activity,
                requestCode,
                permissions,
                grantResults,
                uri,
                CAMERA_REQUEST_CODE);

    }

    /**
     * 权限回调
     *
     * @param fragment
     * @param requestCode
     * @param permissions
     * @param grantResults
     * @param uri          图片uri
     */
    public static boolean onRequestPermissionsResult(Fragment fragment,
                                                     int requestCode,
                                                     @NonNull String[] permissions,
                                                     @NonNull int[] grantResults,
                                                     Uri uri) {
        return onRequestPermissionsResult(fragment,
                requestCode,
                permissions,
                grantResults,
                uri,
                CAMERA_REQUEST_CODE);

    }

    /**
     * 权限回调
     *
     * @param activity
     * @param requestCode
     * @param permissions
     * @param grantResults
     * @param uri               图片uri
     * @param cameraRequestCode 相机回调
     */
    public static boolean onRequestPermissionsResult(Activity activity,
                                                     int requestCode,
                                                     @NonNull String[] permissions,
                                                     @NonNull int[] grantResults,
                                                     Uri uri,
                                                     int cameraRequestCode) {
        if (permissions.length > 0 &&
                !PermissionsUtil.lacksPermissions(activity, permissions) &&
                requestCode == CAMERA_REQUEST_CODE) {
            takeCamera(activity, uri, cameraRequestCode);
            return true;
        }
        return false;

    }

    /**
     * 权限回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     * @param uri               图片uri
     * @param cameraRequestCode 相机回调
     */
    public static boolean onRequestPermissionsResult(Fragment fragment,
                                                     int requestCode,
                                                     @NonNull String[] permissions,
                                                     @NonNull int[] grantResults,
                                                     Uri uri,
                                                     int cameraRequestCode) {
        if (permissions.length > 0 &&
                !PermissionsUtil.lacksPermissions(fragment.getContext(), permissions) &&
                requestCode == CAMERA_REQUEST_CODE) {
            takeCamera(fragment, uri, cameraRequestCode);
            return true;
        }
        return false;

    }

}
