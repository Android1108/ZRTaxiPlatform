package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.BaseRecyclerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 图片item
 * @author k-lm on 2017/11/15.
 */

public class ReleaseImageAdapter extends BaseRecyclerAdapter<Bitmap, ReleaseImageAdapter.ViewHolder> {
    /**
     * 封面位置
     */
    private int mCoverIndex = 1;

    private OnItemImageClickListener mItemImageClickListener;


    public ReleaseImageAdapter(List<Bitmap> list) {
        super(list);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        // 屏蔽item点击事件
        holder.itemView.setOnClickListener(null);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final Bitmap data, final int position) {
        if (data == null) {
            holder.mImageClose.setVisibility(View.GONE);
            holder.mImagePhoto.setImageResource(R.mipmap.ic_launcher);
            return;
        }
        holder.mImageClose.setVisibility(View.VISIBLE);
        holder.mImagePhoto.setImageBitmap(data);


        if (mCoverIndex == position) {
            holder.mTextCover.setVisibility(View.VISIBLE);
        } else {
            holder.mTextCover.setVisibility(View.GONE);
        }

        holder.mImageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemImageClickListener != null) {
                    mItemImageClickListener.onClose(position);
                }
            }
        });

        holder.mImagePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemImageClickListener != null) {
                    mItemImageClickListener.onImageClick(data, position);
                }
            }
        });

    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_release_info_image;
    }

    @Override
    public ViewHolder getViewHolder(View view, int viewType) {
        return new ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_release_info_image_photo)
        ImageView mImagePhoto;
        @BindView(R.id.item_release_info_image_close)
        ImageView mImageClose;
        @BindView(R.id.item_release_info_text_cover)
        TextView mTextCover;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public void setCoverIndex(int index) {
        if (index < 0) {
            return;
        }

        mCoverIndex = index;
    }

    public void setOnItemImageClickListener(OnItemImageClickListener listener) {
        mItemImageClickListener = listener;
    }

    public interface OnItemImageClickListener {
        void onClose(int position);

        void onImageClick(Bitmap bitmap, int position);

    }


    @Override
    @Deprecated
    public void setOnItemClickListener(OnItemClickListener listener) {
    }
}
