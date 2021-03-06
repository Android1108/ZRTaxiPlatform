package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.CommentRecyclerAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.PostDetailBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IPost;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItem;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseRecyclerActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 司机互动详情
 *
 * @author k-lm on 2017/11/29.
 */

public class DriverInteractionDetailActivity1 extends BaseRecyclerActivity<PostDetailBean.PostCommentsBean, CommentRecyclerAdapter> {

    @BindView(R.id.aty_driver_interaction_detail_edit_comment)
    EditText mEditComment;
    @BindView(R.id.aty_driver_interaction_detail_text_like_number)
    TextView mTextLikeNumber;
    @BindView(R.id.aty_driver_interaction_detail_layout_comment)
    LinearLayout mlayoutComment;

    private IPost mIPost;
    /**
     * 司机互动id
     */
    private long mPostId = -1;
    /**
     * 点赞数
     */
    private int mLikeNumber = 0;

    private boolean isLoadData = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_driver_interaction_detail_1;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }

    @Override
    public CommentRecyclerAdapter getAdapter(List<PostDetailBean.PostCommentsBean> list) {
        return new CommentRecyclerAdapter(this, list);
    }


    @OnClick(R.id.aty_driver_interaction_detail_text_like_number)
    void onLike() {
        mIPost.postGreate(RetrofitHelper.getBody(
                new JsonItem("id", mPostId)))
                .enqueue(new MsgCallBack<BaseBean<String>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                        mLikeNumber++;
                        mTextLikeNumber.setSelected(true);
                        mTextLikeNumber.setEnabled(false);
                        mTextLikeNumber.setText(mLikeNumber + "");
                    }
                });
    }


    @Override
    protected void init() {
        super.init();
        getRefreshLayout().setEnabled(false);
        setCenterTitle("详情");
        setIsLoad(false);
        setIsRefresh(false);
        mEditComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendComment(v.getText().toString());
                }

                return false;
            }
        });


    }

    @Override
    protected void initData() {
        super.initData();

        Intent intent = getIntent();

        if (intent == null) {
            showToast("获取数据失败");
            return;
        }

        mPostId = intent.getLongExtra("id", mPostId);

        mIPost = RetrofitHelper.create(IPost.class);
        getData();
    }

    /**
     * 返回数据
     */
    private void getData() {
        mIPost.getPostDetail(mPostId)
                .enqueue(new MsgCallBack<BaseBean<PostDetailBean>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<PostDetailBean>> call, Response<BaseBean<PostDetailBean>> response) {
                        PostDetailBean detailBean = response.body().getResult();
                        int lastPosition = detailBean.getCommentNumber() + 1;
                        getAdapter().setDetailBean(detailBean);

                        if (!isLoadData) {
                            initData(detailBean);
                        } else {
                            // 滑到最底部
                            getRecyclerView().setVerticalScrollbarPosition(lastPosition);
                        }
                        isLoadData = true;
                    }
                });
    }

    /**
     * 初始化数据
     *
     * @param detailBean
     */
    private void initData(PostDetailBean detailBean) {
        if (detailBean == null) {
            mlayoutComment.setVisibility(View.GONE);
            return;
        }
        mlayoutComment.setVisibility(View.VISIBLE);

        if (detailBean.isIsEnableComments()) {
            mEditComment.setEnabled(true);
        } else {
            mEditComment.setEnabled(false);
            mEditComment.setHint("当前互动无法发布评论");
        }

        if (detailBean.isGreated()) {
            mTextLikeNumber.setEnabled(false);
            mTextLikeNumber.setSelected(true);
        } else {
            mTextLikeNumber.setEnabled(true);
            mTextLikeNumber.setSelected(false);
        }
        mLikeNumber = detailBean.getGreateNumber();
        mTextLikeNumber.setText(mLikeNumber + "");


    }

    /**
     * 提交评论
     *
     * @param content
     */
    private void sendComment(String content) {
        if (TextUtils.isEmpty(content)) {
            showToast("评论不能为空");
            return;
        }
        mIPost.createPostComment(RetrofitHelper
                .getBody(new JsonItem("postId", mPostId),
                        new JsonItem("content", content)))
                .enqueue(new MsgCallBack<BaseBean<String>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                        mEditComment.setText("");
                        getData();
                        showToast("提交评论成功");
                    }
                });
    }


    @Override
    public void onLoad() {
        super.onLoad();
        showToast("正在加载中");
    }

    /**
     * @param context
     * @param id      司机互动id
     */
    public static void start(Context context, long id) {
        Intent starter = new Intent(context, DriverInteractionDetailActivity1.class);
        starter.putExtra("id", id);
        context.startActivity(starter);
    }
}
