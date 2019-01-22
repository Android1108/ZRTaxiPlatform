package org.wzeiri.zr.zrtaxiplatform.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.base.recycler.LoadMoreAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.PostDetailBean;
import org.wzeiri.zr.zrtaxiplatform.common.Config;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.ImageZoomActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.UserInfoDetailActivity;
import org.wzeiri.zr.zrtaxiplatform.util.DetailInfoUtil;
import org.wzeiri.zr.zrtaxiplatform.util.DriverInteractionUtil;
import org.wzeiri.zr.zrtaxiplatform.util.GlideUtil;
import org.wzeiri.zr.zrtaxiplatform.util.TimeUtil;
import org.wzeiri.zr.zrtaxiplatform.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author k-lm on 2017/11/29.
 */

public class CommentRecyclerAdapter extends LoadMoreAdapter<PostDetailBean.PostCommentsBean> {

    private int mHeadType = 1;

    private int mCommentType = 2;


    private PostDetailBean mDetailBean;

    public CommentRecyclerAdapter(Context context, List<PostDetailBean.PostCommentsBean> list) {
        super(context, list);
        init();
    }

    @Override
    public int getItemViewType(PostDetailBean.PostCommentsBean data, int position) {
        if (position == 0 && data == null) {
            return mHeadType;
        }
        return mCommentType;
    }


    private void init() {
        RecyclerItem<PostDetailBean.PostCommentsBean, HeadViewHolder> headItem = new RecyclerItem<PostDetailBean.PostCommentsBean, HeadViewHolder>() {
            @Override
            public int getLayoutId() {
                return R.layout.item_drivier_interaction_detail_head;
            }

            @Override
            public HeadViewHolder getViewHolder(View view) {
                return new HeadViewHolder(view);
            }

            @Override
            public void onBindViewHolder(Context context, View itemView, HeadViewHolder viewHolder, int viewType, PostDetailBean.PostCommentsBean data, int adapterPosition, int readPosition) {
                if (mDetailBean == null) {
                    return;
                }

                CharSequence title = mDetailBean.getTitle();

                if (mDetailBean.isIsTop()) {
                    title = DriverInteractionUtil.getTopTitle(getContext(), title);
                }

                if (mDetailBean.isIsHot()) {
                    DriverInteractionUtil.setHostTitle(context, viewHolder.mTextTitle, title);
                } else {
                    viewHolder.mTextTitle.setText(title);
                }


                viewHolder.mTextName.setText(mDetailBean.getCreatorUserName());
                viewHolder.mTextDate.setText(TimeUtil.getServerDateAndTime(mDetailBean.getCreationTime()));

                viewHolder.mTextContent.setText(mDetailBean.getContent());

                viewHolder.mLayoutUserInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer driverId = mDetailBean.getDriverId();
                        if (driverId == null) {
                            return;
                        }
                        UserInfoDetailActivity.start(getContext(), driverId, mDetailBean.getCreatorUserName());
                    }
                });
                viewHolder.mLayoutImage.removeAllViews();


                List<PostDetailBean.PostPicturesBean> beanList = mDetailBean.getPostPictures();
                if (beanList != null) {
                    List<String> imageUrlList = new ArrayList<>();
                    for (PostDetailBean.PostPicturesBean bean : beanList) {
                        imageUrlList.add(bean.getPicture());
                    }
                    DetailInfoUtil.loadImages(viewHolder.mLayoutImage, imageUrlList);
                }

                String picture = mDetailBean.getCreatorUserProfile();
                if (TextUtils.isEmpty(picture)
                        || TextUtils.equals(picture, Config.NO_IMAGE_URL)) {
                    viewHolder.mImageAvatar.setImageResource(R.drawable.ic_default_avatar);
                } else {
                    GlideUtil.loadPath(getContext(), viewHolder.mImageAvatar, picture);
                }
            }


        };


        RecyclerItem<PostDetailBean.PostCommentsBean, CommentViewHolder> commentItem = new RecyclerItem<PostDetailBean.PostCommentsBean, CommentViewHolder>() {
            @Override
            public int getLayoutId() {
                return R.layout.item_driver_discount_comment;
            }

            @Override
            public CommentViewHolder getViewHolder(View view) {
                return new CommentViewHolder(view);
            }

            @Override
            public void onBindViewHolder(Context context, View itemView, CommentViewHolder viewHolder, int viewType, PostDetailBean.PostCommentsBean data, int adapterPosition, int readPosition) {
                if (data == null) {
                    return;
                }
                viewHolder.mTextStoreName.setText(data.getCommentUserName());
                viewHolder.mTextDate.setText(TimeUtil.getServerDate(data.getCreationTime()));
                viewHolder.mTextContent.setText(data.getContent());
                String picture = mDetailBean.getCreatorUserProfile();
                if (TextUtils.isEmpty(picture)
                        || TextUtils.equals(picture, Config.NO_IMAGE_URL)) {
                    viewHolder.mImage.setImageResource(R.drawable.ic_default_avatar);
                } else {
                    GlideUtil.loadPath(getContext(), viewHolder.mImage, picture);
                }
            }
        };

        putItemType(mCommentType, commentItem);
        putItemType(mHeadType, headItem);

    }


    static class CommentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_driver_discount_comment_image)
        CircleImageView mImage;
        @BindView(R.id.item_driver_discount_comment_text_store_name)
        TextView mTextStoreName;
        @BindView(R.id.item_driver_discount_comment_text_date)
        TextView mTextDate;
        @BindView(R.id.item_driver_discount_comment_text_content)
        TextView mTextContent;

        CommentViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class HeadViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_driver_interaction_detail_head_text_title)
        TextView mTextTitle;
        @BindView(R.id.item_driver_interaction_detail_head_image_avatar)
        CircleImageView mImageAvatar;
        @BindView(R.id.item_driver_interaction_detail_head_text_name)
        TextView mTextName;
        @BindView(R.id.item_driver_interaction_detail_head_text_date)
        TextView mTextDate;
        @BindView(R.id.item_driver_interaction_detail_head_text_content)
        TextView mTextContent;

        @BindView(R.id.item_driver_interaction_detail_head_layout_user_info)
        View mLayoutUserInfo;

        @BindView(R.id.item_driver_interaction_detail_head_layout_image)
        LinearLayout mLayoutImage;

        HeadViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 设置详情信息
     *
     * @param bean
     */
    public void setDetailBean(PostDetailBean bean) {
        mDetailBean = bean;
        if (bean == null) {
            return;
        }

        List<PostDetailBean.PostCommentsBean> list = bean.getPostComments();

        if (list == null) {
            return;
        }

        if (mList == null) {
            mList = new ArrayList<>();
        } else {
            mList.clear();
        }
        mList.add(null);
        mList.addAll(list);
        notifyDataSetChanged();

    }

}
