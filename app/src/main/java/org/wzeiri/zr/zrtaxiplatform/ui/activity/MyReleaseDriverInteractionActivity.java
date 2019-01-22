package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import org.wzeiri.zr.zrtaxiplatform.adapter.MyReleaseDriverInteractionAdapter;
import org.wzeiri.zr.zrtaxiplatform.adapter.inter.OnItemClickDeleteListener;
import org.wzeiri.zr.zrtaxiplatform.adapter.inter.OnItemClickEditListener;
import org.wzeiri.zr.zrtaxiplatform.bean.DriverInteractionPostBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IPost;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItem;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseListActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 我发布的司机互动
 *
 * @author k-lm on 2017/12/13.
 */

public class MyReleaseDriverInteractionActivity extends BaseListActivity<DriverInteractionPostBean, MyReleaseDriverInteractionAdapter> {

    private IPost mIPost;

    @Override
    public MyReleaseDriverInteractionAdapter getAdapter(List<DriverInteractionPostBean> list) {
        return new MyReleaseDriverInteractionAdapter(list);
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("司机之家");

        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int i = getAdapter().getData(position).getId();
                DriverInteractionDetailActivity.start(getThis(), i);
            }
        });

        getAdapter().setOnItemClickDeleteListener(new OnItemClickDeleteListener() {
            @Override
            public void onItemClickDelete(int position) {
                deleteItem(position);
            }
        });

        getAdapter().setOnItemClickEditListener(new OnItemClickEditListener() {
            @Override
            public void onItemClickEdit(int position) {
                DriverInteractionPostBean bean = getData(position);
                EditDriverInteractionActivity.start(getThis(), bean.getId(), position);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        onRefresh();
    }

    private void deleteItem(int position) {
        DriverInteractionPostBean bean = getData(position);
        mIPost.deletePost(
                RetrofitHelper.getBody(
                        new JsonItem("id", bean.getId())
                )
        ).enqueue(new MsgCallBack<BaseBean<String>>(getThis(), true) {
            @Override
            public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                onRefresh();
                showToast("删除成功");
            }
        });
    }

    @Override
    protected Call<BaseBean<BaseListBean<DriverInteractionPostBean>>> getNetCall(int currentIndex, int pagerSize) {
        if (mIPost == null) {
            mIPost = RetrofitHelper.create(IPost.class);
        }

        return mIPost.getMyPosts(pagerSize, currentIndex);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK && data == null) {
            return;
        }

        if (requestCode == EditDriverInteractionActivity.REQUEST_CODE) {
            String title = data.getStringExtra(EditDriverInteractionActivity.KEY_SELECT_TITLE);
            int position = data.getIntExtra(EditDriverInteractionActivity.KEY_SELECT_POSITION, -1);
            String content = data.getStringExtra(EditDriverInteractionActivity.KEY_SELECT_CONTENT);
            if (position < 0) {
                return;
            }

            getData(position).setTitle(title);
            getData(position).setContent(content);
            getAdapter().notifyDataSetChanged();
        }


    }
}
