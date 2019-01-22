package org.wzeiri.zr.zrtaxiplatform.util;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.ImageZoomActivity;
import org.wzeiri.zr.zrtaxiplatform.widget.PhotoView;
import org.wzeiri.zr.zrtaxiplatform.widget.dialog.BottomDialog;
import org.wzeiri.zr.zrtaxiplatform.widget.flow.FlowLayout;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 添加图片帮助类
 *
 * @author k-lm on 2017/11/28.
 */

public class PhotoHelper {
    private static final String TAG = "PhotoHelper";

    /**
     * 最大数量
     */
    private int mMaxCount;

    private Activity mActivity;
    /**
     * 是否显示封面
     */
    private boolean isShowCover = false;
    /**
     * 是否显示添加照片视图
     */
    private boolean isShowAddPhotoView = false;


    private ImageView imageView;

    private FlowLayout mFlowLayout;
    /**
     * 当前数量
     */
    private int mCurrentCount = 0;

    private String mPath;

    /**
     * 相册名称
     */
    private String mBlbumName = "album";
    /**
     * 相机名称
     */
    private String mCameraName = "camera";
    /**
     * 后缀名
     */
    private String mSuffixName = ".jpg";

    private OnPhotoUpdateListener mPhotoUpdateListener;

    private Uri mCameraUri;
    /**
     * bitmap宽度
     */
    private final int mBitmapWidth = 768;
    /**
     * bitmap高度
     */
    private final int mBitmapHeight = 432;
    /**
     * 弹出底部选择图片来源对话框
     */
    private BottomDialog mDialog;
    /**
     * 图片编号
     */
    private static int mPhotoNo = 1;
    /**
     * 地址
     */
    private SparseArray<String> mPathArray = new SparseArray<>();
    /**
     * 图片操作回调接口
     */
    private OnPhotoOperationListener mPhotoOperationListener;

    /**
     * 记录photoView的点击回调
     */
    private List<PhotoClickListener> mPhotoClickListenerList = new ArrayList<>(3);

    private RequestManager mRequestManager = null;
    /**
     * 是否执行默认操作
     */
    private boolean mIsExecuteDefaultOperation = true;


    public PhotoHelper(Activity activity, FlowLayout flowLayout, int maxCount, String path) {
        if (maxCount <= 0) {
            mMaxCount = 1;
        } else {
            mMaxCount = maxCount;
        }
        mFlowLayout = flowLayout;
        mActivity = activity;
        mPath = path;
        addPhotoBitmap(null, true, true);
    }


    public PhotoHelper(Activity activity, ImageView ImageView, int maxCount, String path) {
        if (maxCount <= 0) {
            mMaxCount = 1;
        } else {
            mMaxCount = maxCount;
        }
        imageView = ImageView;
        mActivity = activity;
        mPath = path;
        addPhotoBitmap(null, true, true);
    }

    /**
     * 添加图片
     *
     * @param bitmap
     */
    public void addPhoto(Bitmap bitmap) {
        addPhotoBitmap(bitmap, true, false);
    }

    /**
     * 添加图片
     *
     * @param bitmap
     * @param isShowClose 是否显示关闭图标
     */
    public void addPhoto(Bitmap bitmap, boolean isShowClose) {
        addPhotoBitmap(bitmap, isShowClose, false);
    }

    /**
     * 添加图片
     *
     * @param url 图片url地址
     */
    public void addPhoto(String url) {
        addPhoto(url, true);
    }

    /**
     * 添加图片
     *
     * @param url         图片url地址
     * @param isShowClose 是否显示关闭图标
     */
    public void addPhoto(String url, boolean isShowClose) {
        // 超出数量不再添加 且没有显示添加照片视图
        if (mCurrentCount == mMaxCount && !isShowAddPhotoView) {
            return;
        }
        if (TextUtils.isEmpty(url)) {
            return;
        }

        final PhotoView photoView = new PhotoView(mActivity);
        int size = DensityUtil.dip2px(mActivity, 80);
        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(size, size);
        layoutParams.rightMargin = mActivity.getResources().getDimensionPixelOffset(R.dimen.layout_margin_mini);
        mFlowLayout.addView(photoView, layoutParams);
        photoView.setPhotoUrl(url);
        photoView.setShowClose(isShowClose);


        if (!isShowCover) {
            isShowCover = true;
            photoView.showShowCover(isShowCover);
        }
        PhotoClickListener photoClickListener = new PhotoClickListener(mCurrentCount);
        photoView.setTag(R.id.view_photo_url_tag, url);
        photoView.setOnPhotoClickListener(photoClickListener);
        mPhotoClickListenerList.add(photoClickListener);

        mCurrentCount++;
        if (mCurrentCount == mMaxCount && isShowAddPhotoView) {
            mFlowLayout.removeViewAt(0);
            isShowAddPhotoView = false;
            // 重置点击事件的顺序
            for (int i = 0; i < mMaxCount; i++) {
                PhotoClickListener listener = mPhotoClickListenerList.get(i);
                listener.mPosition = i;
            }
        }


    }

    /**
     * 添加图片
     *
     * @param bitmap
     * @param isNull 是否允许为空
     */
    private void addPhotoBitmap(Bitmap bitmap, boolean isShowClose, boolean isNull) {
        // 超出数量不再添加 且没有显示添加照片视图
        if (mCurrentCount == mMaxCount && !isShowAddPhotoView) {
            return;
        }

        if (!isNull && bitmap == null) {
            return;
        }
        if (isNull) {
            isShowClose = false;
        }

        PhotoView photoView = new PhotoView(mActivity);
        photoView.setPhoto(bitmap);

        PhotoClickListener photoClickListener;

        if (bitmap != null && !isShowCover) {
            photoClickListener = new PhotoClickListener(mCurrentCount);
            isShowCover = true;
            photoView.showShowCover(isShowCover);
            photoView.setTag(R.id.view_photo_uri_tag, mCameraUri);
            photoView.setOnPhotoClickListener(photoClickListener);
            mPhotoClickListenerList.add(photoClickListener);
            mCurrentCount++;
        } else if (bitmap == null) {
            isShowAddPhotoView = true;
            photoView.setOnPhotoClickListener(new AddPhotoListener());
        } else {
            photoClickListener = new PhotoClickListener(mCurrentCount);
            photoView.setOnPhotoClickListener(photoClickListener);
            photoView.setTag(R.id.view_photo_uri_tag, mCameraUri);
            mPhotoClickListenerList.add(photoClickListener);
            mCurrentCount++;
        }
        photoView.setShowClose(isShowClose);

        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.rightMargin = mActivity.getResources().getDimensionPixelOffset(R.dimen.layout_margin_mini);

        // 如果bitmap为空以及允许为空 则为添加图片视图 ，排在第一个位置
        if (bitmap == null && isNull) {
            mFlowLayout.addView(photoView, 0, layoutParams);
        } else {
            mFlowLayout.addView(photoView, layoutParams);
        }

        LogUtil.d(mCurrentCount);
        if (mCurrentCount == mMaxCount && isShowAddPhotoView) {
            mFlowLayout.removeViewAt(0);
            LogUtil.d(mCurrentCount);
            isShowAddPhotoView = false;
            int size = mPhotoClickListenerList.size();
            // 重置点击事件的顺序
            for (int i = 0; i < size; i++) {
                PhotoClickListener listener = mPhotoClickListenerList.get(i);
                listener.mPosition = i;
            }
        }

    }

    /**
     * 设置是否执行默认操作
     *
     * @param isExecuteDefaultOperation 是否执行默认操作
     */
    public void setIsExecuteDefaultOperation(boolean isExecuteDefaultOperation) {
        mIsExecuteDefaultOperation = isExecuteDefaultOperation;
    }

    /**
     * 添加图片
     */
    private class AddPhotoListener implements PhotoView.OnPhotoClickListener {

        @Override
        public void onClickPhoto(PhotoView photoView, Bitmap bitmap) {
            if (!mIsExecuteDefaultOperation) {
                return;
            }
            showDialog();
        }

        @Override
        public void onClickClose(PhotoView photoView) {
        }
    }

    /**
     * 选择图片及删除图片
     */
    private class PhotoClickListener implements PhotoView.OnPhotoClickListener {
        private int mPosition;

        public PhotoClickListener(int index) {
            mPosition = index;
        }

        @Override
        public void onClickPhoto(PhotoView photoView, Bitmap bitmap) {
            if (mPhotoOperationListener != null) {
                mPhotoOperationListener.onPhotoClick(mPosition, bitmap);
            }
            if (!mIsExecuteDefaultOperation) {
                return;
            }

            Uri uri = (Uri) photoView.getTag(R.id.view_photo_uri_tag);
            if (uri != null) {
                ImageZoomActivity.startPath(mActivity, photoView, getRealFilePath(mActivity, uri));
                return;
            }

            String url = (String) photoView.getTag(R.id.view_photo_url_tag);
            if (!TextUtils.isEmpty(url)) {
                ImageZoomActivity.startUrl(mActivity, photoView, url);
            }


        }

        @Override
        public void onClickClose(PhotoView photoView) {
            if (mPhotoOperationListener != null) {
                mPhotoOperationListener.onPhotoDelete(mPosition);
            }
            if (!mIsExecuteDefaultOperation) {
                return;
            }
            mPhotoClickListenerList.remove(mPosition);
            int size = mPhotoClickListenerList.size();
            // 重置点击事件的顺序
            for (int i = 0; i < size; i++) {
                PhotoClickListener listener = mPhotoClickListenerList.get(i);
                listener.mPosition = i;
            }
            removePhotoView(photoView);


        }

    }

    /**
     * 删除view
     *
     * @param photoView
     */
    private void removePhotoView(PhotoView photoView) {
        // 如果该photo的bitmap是 通过url添加的，则需要删除对应的Target
        // 否则在删除后在Activity销毁时Glide会报异常
        Object object = photoView.getTag();
        if (object instanceof SimpleTarget) {
            mRequestManager.clear((SimpleTarget) object);
        }
        photoView.recycle();
        mFlowLayout.removeView(photoView);
        mCurrentCount--;
        // 在显示所有图片，不显示添加照片视图的情况下
        if (!isShowAddPhotoView) {
            addPhotoBitmap(null, true, true);
            isShowAddPhotoView = true;
        }

        // 删除的是封面
        if (photoView.isShowCover()) {
            if (mCurrentCount <= 0) {
                isShowCover = false;
                return;
            }

            View view = mFlowLayout.getChildAt(1);

            if (!(view instanceof PhotoView)) {
                isShowCover = false;
                return;
            }
            // 设置为封面
            ((PhotoView) view).showShowCover(true);
        }
    }

    /**
     * 删除最后一张图片
     */
    public void removeLastPhoto() {
        if (isShowAddPhotoView && !isShowCover) {
            return;
        }
        int position = mCurrentCount;
        if (isShowAddPhotoView) {
            position++;
        }

        View view = mFlowLayout.getChildAt(position);

        if (!(view instanceof PhotoView)) {
            return;
        }

        removePhotoView((PhotoView) view);
    }

    /**
     * 删除图片
     *
     * @param position
     */
    public void removePhoto(int position) {
        if (isShowAddPhotoView) {
            position++;
        }

        if (position > mCurrentCount) {
            return;
        }

        View view = mFlowLayout.getChildAt(position);

        if (!(view instanceof PhotoView)) {
            return;
        }

        removePhotoView((PhotoView) view);

    }

    /**
     * 返回照片
     *
     * @return
     */
    public List<Bitmap> getPhotoBitMaps() {
        int count = mFlowLayout.getChildCount();
        List<Bitmap> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            View view = mFlowLayout.getChildAt(i);

            if (!(view instanceof PhotoView)) {
                continue;
            }
            Bitmap bitmap = ((PhotoView) view).getPhoto();
            if (bitmap != null) {
                list.add(bitmap);
            }
        }
        return list;
    }

    /**
     * 返回当前所有图片的base64编码
     * <p>
     * 最好需要在子线程中使用该方法
     * </p>
     *
     * @return
     */
    public List<String> getImageBase64List() {
        int count = mFlowLayout.getChildCount();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            View view = mFlowLayout.getChildAt(i);

            if (!(view instanceof PhotoView)) {
                continue;
            }
            Bitmap bitmap = ((PhotoView) view).getPhoto();
            if (bitmap != null) {
                try {
                    String base64 = BitmapUtil.getBitmapBase64(bitmap);
                    list.add(base64);
                } catch (IOException e) {
                    //  e.printStackTrace();
                    continue;
                }
            }
        }
        return list;

    }

    /**
     * 返回图片数量
     *
     * @return
     */
    public int getImageSize() {
        return mCurrentCount;
    }

    /**
     * 获取uri路径
     *
     * @param context
     * @param uri
     * @return
     */
    private String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 返回压缩后的bitmap
     *
     * @param uri
     * @param outPath
     * @param name
     * @return
     * @throws IOException
     */
    private Bitmap getBitMap(Uri uri, String outPath, String name) throws IOException {
        if (uri == null) {
            return null;
        }

        InputStream input = mActivity.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.RGB_565;//optional
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        if (input != null) {
            input.close();
        }

        // 部分情况下bitmap有可能为空
        if (bitmap == null) {
            String path = getRealFilePath(mActivity, uri);
            bitmap = BitmapUtil.getBitmap(path);
        }

        //Bitmap bitmap = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver() , uri);
        bitmap = BitmapUtil.ratio(bitmap, mBitmapWidth, mBitmapHeight);
        BitmapUtil.compressAndGenImage(bitmap, outPath, name, 80);


        return bitmap;
    }


    /**
     * 根据相册返回的uri返回真实路径
     *
     * @param context
     * @param uri
     * @param selection
     * @return
     */
    private String getImagePath(Context context, Uri uri, String selection) {
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = context.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 前往相册
     */
    private void choosePicture() {
        PhotographUtil.choosePicture(mActivity);
    }

    /**
     * 前往相机
     */
    private void takeCamera() {
        if (PermissionsUtil.isCameraPermissions(mActivity)) {
            String fileName = mCameraName + mPhotoNo;
            mPhotoNo++;
            mCameraUri = PhotographUtil.createImageUri(fileName, mActivity);
            // 记录当前文件名
            mPathArray.put(mPhotoNo, fileName);

            boolean isTake = PhotographUtil.takeCamera(mActivity, mCameraUri);

            if (!isTake) {
                //  LogUtil.d("没有存储目录");
                Toast.makeText(mActivity, "没有存储目录", Toast.LENGTH_SHORT).show();
            }
            if (mPhotoUpdateListener != null) {
                mPhotoUpdateListener.onStartCameraOrAlbum(isTake);
            }
        } else {
            PermissionsUtil.startActivityForResult(mActivity, PhotographUtil.CAMERA_REQUEST_CODE, Manifest.permission.CAMERA);
        }
    }


    /**
     * 显示对话框
     */
    private void showDialog() {
        if (mDialog == null) {
            mDialog = new BottomDialog(mActivity, R.style.NoTitleDialog);
            String[] strings = new String[]{"相册", "相机"};
            mDialog.addDataArray(strings);

            mDialog.setOnItemClickListener(new BottomDialog.OnItemClickListener() {
                @Override
                public void onItemClick(BottomDialog dialog, int position) {
                    if (position == 0) {
                        choosePicture();
                    } else {
                        takeCamera();
                    }
                }
            });

        }

        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }


    public void setOnPhotoUpdateListener(OnPhotoUpdateListener listener) {
        mPhotoUpdateListener = listener;
    }

    public void setOnPhotoOperationListener(OnPhotoOperationListener listener) {
        mPhotoOperationListener = listener;
    }


    /**
     * 获取相册和相机的回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode != PhotographUtil.ALBUM_REQUEST_CODE &&
                requestCode != PhotographUtil.CAMERA_REQUEST_CODE) {
            return;
        }

        if (mPhotoUpdateListener != null) {
            mPhotoUpdateListener.onResult(resultCode == Activity.RESULT_OK);
        }

        if (resultCode != Activity.RESULT_OK) {
            return;
        }


        ThreadSwitch threadSwitch = null;
        if (requestCode == PhotographUtil.ALBUM_REQUEST_CODE) {
            // 处理相册
            threadSwitch = ThreadSwitch.createTask(new ThreadSwitch.OnCreateListener<Bitmap>() {
                @Override
                public Bitmap onCreate(ThreadSwitch threadSwitch) {
                    mCameraUri = data.getData();
                    Bitmap bitmap = null;
                    try {
                        bitmap = getBitMap(mCameraUri, mPath, mBlbumName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return bitmap;
                }
            });

        } else if (requestCode == PhotographUtil.CAMERA_REQUEST_CODE) {
            // 处理相机
            threadSwitch = ThreadSwitch.createTask(new ThreadSwitch.OnCreateListener<Bitmap>() {
                @Override
                public Bitmap onCreate(ThreadSwitch threadSwitch) {
                    try {
                        return getBitMap(mCameraUri, mPath, mCameraName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            });
        }

        if (threadSwitch != null) {
            threadSwitch.switchLooper(ThreadSwitch.MAIN_THREAD)
                    .submit(new ThreadSwitch.OnSubmitListener<Bitmap>() {
                        @Override
                        public void onSubmit(ThreadSwitch threadSwitch, Bitmap value) {
                            if (mPhotoUpdateListener != null) {
                                mPhotoUpdateListener.onResultBitmap(value);
                            }
                            if (value == null) {
                                return;
                            }
                            //addPhoto(value);
                        }
                    });
        }
    }

    /**
     * 返回当前uri
     *
     * @return
     */
    public Uri getCurrentImageUri() {
        return mCameraUri;
    }

    /**
     * 销毁
     */
    public void onDestroy() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }

        // 回收glide
        int childSize = mFlowLayout.getChildCount();
        for (int i = 0; i < childSize; i++) {
            View child = mFlowLayout.getChildAt(i);
            if (!(child instanceof PhotoView)) {
                continue;
            }

            Object object = child.getTag();
            if (object == null || !(object instanceof SimpleTarget)) {
                continue;
            }
            mRequestManager.clear((Target<?>) object);
        }


        // 删除文件
        int cont = mPathArray.size();
        String[] paths = new String[cont];
        if (cont > 0) {
            for (int i = 0; i < cont; i++) {
                String fileName = mPathArray.valueAt(i);
                File outputImage = new File(mActivity.getExternalCacheDir(), fileName + mSuffixName);
                if (outputImage.exists()) {
                    outputImage.delete();
                } else {
                    continue;
                }
                paths[i] = outputImage.getPath();

                File cacheFile = Glide.getPhotoCacheDir(mActivity, outputImage.getPath());

                if (cacheFile != null && cacheFile.exists()) {
                    cacheFile.delete();
                }

                // 发送广播 删除缩略图
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(outputImage);
                intent.setData(uri);
                mActivity.sendBroadcast(intent);


            }
            /*mActivity.getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    MediaStore.Images.Media.DATA + "=?", paths);*/
        }

    }

    /**
     * 申请权限回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (!PermissionsUtil.lacksPermissions(mActivity, permissions)) {
            boolean isTakePicture = PhotographUtil.takeCamera(mActivity, mCameraUri);
            if (!isTakePicture) {
                Toast.makeText(mActivity, "没有存储目录", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public interface OnPhotoUpdateListener {
        /**
         * 开始进入相机或相册
         *
         * @param isSuccess 是否成功进入相机或相册
         */
        void onStartCameraOrAlbum(boolean isSuccess);

        /**
         * 返回结果
         *
         * @param isSuccess 是否选择照片或拍照
         */
        void onResult(boolean isSuccess);

        /**
         * 返回处理bitmap结果
         *
         * @param bitmap
         */
        void onResultBitmap(Bitmap bitmap);
    }


    public interface OnPhotoOperationListener {
        void onPhotoDelete(int position);

        void onPhotoClick(int position, Bitmap bitmap);
    }


}
