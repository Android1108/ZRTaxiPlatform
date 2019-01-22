package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.AnnouncementDetailBean;
import org.wzeiri.zr.zrtaxiplatform.bean.TrainingBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IArticle;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseWebActivity;
import org.wzeiri.zr.zrtaxiplatform.util.TimeUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 查看培训公告详情
 *
 * @author k-lm on 2017/12/29.
 */

public class TrainDetailActivity extends BaseWebActivity {

    @BindView(R.id.aty_train_detail_text_title)
    TextView mTextTitle;
    @BindView(R.id.aty_train_detail_text_train_date)
    TextView mTextTrainDate;
    @BindView(R.id.aty_train_detail_text_train_address)
    TextView mTextTrainAddress;


    private String mTitle = "";
    private int mId = -1;
    private String mSource;
    private String mTime;

    private IArticle mIArticle;

    @Override
    protected void create() {
        super.create();
        Intent intent = getIntent();
        if (intent == null) {
            showToast("未找到相关公告");
            return;
        }
        mId = intent.getIntExtra("id", mId);
        mTitle = intent.getStringExtra("title");
        mTime = intent.getStringExtra("time");
        mSource = intent.getStringExtra("source");

    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_train_detail;
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("详情");

        if (!TextUtils.isEmpty(mTime)) {
            mTextTitle.setText(mTitle);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        mIArticle = RetrofitHelper.create(IArticle.class);
        mIArticle.getAnnouncementDetail(mId)
                .enqueue(new MsgCallBack<BaseBean<AnnouncementDetailBean>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<AnnouncementDetailBean>> call, Response<BaseBean<AnnouncementDetailBean>> response) {
                        AnnouncementDetailBean bean = response.body().getResult();

                        if (bean == null) {
                            showToast("未找到相关公告");
                            return;
                        }
                        mTextTitle.setText(bean.getTitle());


                        loadData(bean.getContent());

                        if (!bean.isIsTrainingTime()) {
                            mTextTrainDate.setVisibility(View.GONE);
                            mTextTrainAddress.setVisibility(View.GONE);
                            return;
                        }

                        String info = TimeUtil.getServerDateAndTime(bean.getTrainingTime());
                        if (!TextUtils.isEmpty(info)) {
                            info = "培训时间 " + info + "  ";
                        }

                        if (!TextUtils.isEmpty(mSource)) {
                            info += "来源 " + mSource;
                        }

                        String address = bean.getTrainingAddress();

                        if (TextUtils.isEmpty(info)) {
                            mTextTrainDate.setVisibility(View.GONE);
                        } else {

                            mTextTrainDate.setText(info);
                        }

                        if (TextUtils.isEmpty(address)) {
                            mTextTrainAddress.setVisibility(View.GONE);
                        } else {
                            address = "培训地点 " + address;
                            mTextTrainAddress.setText(address);
                        }


                    }
                });

    }


    public static void start(Context context, TrainingBean bean) {
        if (bean == null) {
            return;
        }
        Intent starter = new Intent(context, TrainDetailActivity.class);
        starter.putExtra("title", bean.getTitle());
        starter.putExtra("id", bean.getId());
        starter.putExtra("time", TimeUtil.getServerDate(bean.getCreationTime()));
        starter.putExtra("source", bean.getSource());
        context.startActivity(starter);
    }


    public static void start(Context context, int id) {
        Intent starter = new Intent(context, TrainDetailActivity.class);
        starter.putExtra("id", id);
        context.startActivity(starter);
    }

}
