package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import org.wzeiri.zr.zrtaxiplatform.bean.ForumSectionBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IPost;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.SelectListActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 选择司机互动板块
 *
 * @author k-lm on 2017/12/18.
 */

public class SelectDriverInteractionPlateActivity extends SelectListActivity {

    private IPost mPost;
    private List<ForumSectionBean> mDataList = new ArrayList<>();


    public static final int REQUEST_CODE = 10010;

    @Override
    protected void create() {
        super.create();
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        mSelectId = intent.getIntExtra(KEY_SELECT_ID, mSelectId);

    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("发布司机互动");
        getAdapter().setSelectPosition(mSelectId);
        setOnSelectItemListener(new OnSelectItemListener() {
            @Override
            public void onSelectItem(String data, int position) {
                int selectId = mDataList.get(position).getId();
                Intent intent = new Intent();
                intent.putExtra(KEY_SELECT_ID, selectId);
                intent.putExtra(KEY_SELECT_NAME, data);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        mPost = RetrofitHelper.create(IPost.class);
        onRefresh();
    }

    @Deprecated
    protected void getData() {
        if (mPost == null) {
            mPost = RetrofitHelper.create(IPost.class);
        }

        mPost.getForumSections()
                .enqueue(new MsgCallBack<BaseBean<List<ForumSectionBean>>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<List<ForumSectionBean>>> call, Response<BaseBean<List<ForumSectionBean>>> response) {
                        loadDateEnd();
                        List<ForumSectionBean> beanList = response.body().getResult();
                        if (beanList == null || beanList.size() == 0) {
                            return;
                        }

                        // 清空数据
                        mDataList.clear();
                        removeAll();
                        // 整合数据
                        List<String> dataList = new ArrayList<>();
                        for (ForumSectionBean bean : beanList) {
                            if (bean == null) {
                                continue;
                            }
                            String str = bean.getName();
                            dataList.add(str);
                            mDataList.add(bean);
                        }
                        addDatas(dataList);
                        getAdapter().notifyDataSetChanged();

                    }

                    @Override
                    public void onError(Call<BaseBean<List<ForumSectionBean>>> call, Throwable t) {
                        super.onError(call, t);
                        loadDateEnd();
                    }
                });
    }


    public static void start(Activity activity, int selectId) {
        Intent starter = new Intent(activity, SelectDriverInteractionPlateActivity.class);
        starter.putExtra(KEY_SELECT_ID, selectId);
        activity.startActivityForResult(starter, REQUEST_CODE);
    }

}
