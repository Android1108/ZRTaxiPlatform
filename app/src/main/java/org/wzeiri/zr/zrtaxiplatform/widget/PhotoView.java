package org.wzeiri.zr.zrtaxiplatform.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.util.GlideUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author k-lm on 2017/11/28.
 */

public class PhotoView extends FrameLayout {
    @BindView(R.id.view_photo_image_photo)
    ImageView mImagePhoto;
    @BindView(R.id.view_photo_image_close)
    ImageView mImageClose;
    @BindView(R.id.view_photo_text_cover)
    TextView mTextCover;
    /**
     * 是否显示封面
     */
    private boolean isShowCover = false;

    private Bitmap mBitmap;


    private OnPhotoClickListener mPhotoClickListener;

    public PhotoView(@NonNull Context context) {
        super(context);
        init();
    }

    public PhotoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PhotoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @OnClick(R.id.view_photo_image_photo)
    void onClickPhoto() {
        if (mPhotoClickListener != null) {
            mPhotoClickListener.onClickPhoto(this, mBitmap);
        }
    }


    @OnClick(R.id.view_photo_image_close)
    void onClickClose() {
        if (mPhotoClickListener != null) {
            mPhotoClickListener.onClickClose(this);
        }
    }

    private void init() {
        inflate(getContext(), R.layout.view_photo, this);
        ButterKnife.bind(this);
    }


    /**
     * 设置封面
     *
     * @param bitmap
     */
    public void setPhoto(Bitmap bitmap) {
        mBitmap = bitmap;

        // 设置bitmap显示关闭按钮
        if (bitmap == null) {
            int margin = getResources().getDimensionPixelOffset(R.dimen.layout_margin_small);
            FrameLayout.LayoutParams layoutParams = (LayoutParams) mImagePhoto.getLayoutParams();
            layoutParams.leftMargin = margin;
            layoutParams.topMargin = margin;
            layoutParams.rightMargin = margin;
            layoutParams.bottomMargin = margin;


            mImageClose.setVisibility(View.GONE);
            mImagePhoto.setImageResource(R.drawable.ic_add_photo);
            setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray10));
        } else {
            mImageClose.setVisibility(View.VISIBLE);
            mImagePhoto.setImageBitmap(bitmap);
        }
    }

    /**
     * 回收当前的bitmap
     */
    public void recycle() {
        if (mBitmap == null) {
            return;
        }
        mBitmap.recycle();
        mBitmap = null;
    }

    public Bitmap getPhoto() {
        return mBitmap;
    }

    /**
     * 是否显示删除图标
     *
     * @param isShow
     */
    public void setShowClose(boolean isShow) {
        if (isShow) {
            mImageClose.setVisibility(View.VISIBLE);
        } else {
            mImageClose.setVisibility(View.GONE);
        }
    }

    /**
     * 是否显示封面
     *
     * @param isShow 是否显示封面
     */
    public void showShowCover(boolean isShow) {
        isShowCover = isShow;
        mTextCover.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置图片的url
     *
     * @param url
     */
    public void setPhotoUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            mImagePhoto.setImageBitmap(null);
            mImageClose.setVisibility(View.GONE);
            mImagePhoto.setImageResource(R.drawable.ic_add_photo);
            setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray10));
            return;
        }
        mImageClose.setVisibility(View.VISIBLE);
        GlideUtil.loadPath(getContext(), mImagePhoto, url);
    }

    /**
     * 是否显示封面
     *
     * @return 是否显示封面
     */
    public boolean isShowCover() {
        return isShowCover;
    }


    @Override
    protected void onDetachedFromWindow() {
        if (mBitmap != null) {
            mBitmap.recycle();
        }
        super.onDetachedFromWindow();

    }

    public void setOnPhotoClickListener(OnPhotoClickListener listener) {
        mPhotoClickListener = listener;
    }


    public interface OnPhotoClickListener {
        /**
         * 点击图片
         */
        void onClickPhoto(PhotoView photoView, Bitmap bitmap);

        /**
         * 点击关闭
         */
        void onClickClose(PhotoView photoView);

    }


}
