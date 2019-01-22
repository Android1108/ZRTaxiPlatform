package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.JobRecruitmentDetailBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IJobRecruitment;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseWebActivity;
import org.wzeiri.zr.zrtaxiplatform.util.GlideUtil;
import org.wzeiri.zr.zrtaxiplatform.util.HtmlUtil;
import org.wzeiri.zr.zrtaxiplatform.util.StringUtil;
import org.wzeiri.zr.zrtaxiplatform.util.TimeUtil;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 求职信息详情
 *
 * @author k-lm on 2017/11/23.
 */

public class JobInfoDetailActivity extends ActionbarActivity {
    @BindView(R.id.aty_job_wanted_detail_info_text_title)
    TextView mTextTitle;
    @BindView(R.id.aty_job_wanted_detail_info_vtv_state)
    ValueTextView mVtvState;
    @BindView(R.id.aty_job_wanted_detail_info_text_name)
    TextView mTextName;
    @BindView(R.id.aty_job_wanted_detail_info_location)
    TextView mLocation;
    @BindView(R.id.aty_job_wanted_detail_info_image_call_phone)
    ImageView mImagePhone;
    @BindView(R.id.aty_job_wanted_detail_info_web_detail)
    WebView mWebDetail;

    @BindView(R.id.aty_job_wanted_detail_info_layout_image_layout)
    LinearLayout mImageLayout;

    private long mUserId = -1;

    private String mName;
    private String mPhone;

    private boolean mIsShowPhone = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_job_wanted_detail_info;
    }


    @OnClick(R.id.aty_job_wanted_detail_info_text_name)
    public void onViewClicked() {
//        UserInfoDetailActivity.start(getThis(), mUserId, mName);
    }

    @OnClick(R.id.aty_job_wanted_detail_info_image_call_phone)
    void callPhone() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mPhone));
        startActivity(intent);
    }

    @Override
    protected void create() {
        super.create();
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        mIsShowPhone = intent.getBooleanExtra("isShowPhone", mIsShowPhone);
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("详情");

        WebSettings settings = mWebDetail.getSettings();
        settings.setBlockNetworkImage(false);
        settings.setDomStorageEnabled(true);
        settings.setDefaultTextEncodingName("utf-8");

        if (mIsShowPhone) {
            mImagePhone.setVisibility(View.VISIBLE);
        } else {
            mImagePhone.setVisibility(View.GONE);
        }

    }


    @Override
    protected void initData() {
        super.initData();
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            showToast("获取数据失败，请重试");
            return;
        }
        int id = intent.getIntExtra("id", -1);

        IJobRecruitment jobRecruitment = RetrofitHelper.create(IJobRecruitment.class);

        jobRecruitment.getJobRecruitmentDetail(id)
                .enqueue(new MsgCallBack<BaseBean<JobRecruitmentDetailBean>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<JobRecruitmentDetailBean>> call, Response<BaseBean<JobRecruitmentDetailBean>> response) {
                        JobRecruitmentDetailBean bean = response.body().getResult();
                        if (bean == null) {
                            return;
                        }
                        mUserId = bean.getCreatorUserId();
                        mName = bean.getContactPerson();

                        mTextTitle.setText(bean.getTitle());
                        mVtvState.setTextLeft("发布时间 ");
                        mVtvState.setText(TimeUtil.getServerDate(bean.getCreationTime()));
                        mTextName.setText(mName);

                        String address = StringUtil.getString(bean.getProvinceName())
                                + StringUtil.getString(bean.getCityName())
                                + StringUtil.getString(bean.getAreaName());
                        mLocation.setText(address);


                        String htmlStr = bean.getContent();
                        htmlStr = HtmlUtil.getImageStyle(htmlStr);
                        htmlStr = HtmlUtil.getBodyAdaptableScreenStyle(htmlStr);
                        mWebDetail.loadDataWithBaseURL(null,
                                htmlStr,
                                "text/html",
                                "utf-8",
                                null);

                        String phone = bean.getContact();
                        if (!TextUtils.isEmpty(phone) && mIsShowPhone) {
                            mPhone = phone;
                            mImagePhone.setVisibility(View.VISIBLE);
                        }

                        addImages(bean.getPictures());
                    }
                });


    }

    /**
     * 添加图片
     *
     * @param imageUrlList 图片url
     */
    private void addImages(List<String> imageUrlList) {
        if (imageUrlList == null || imageUrlList.size() == 0) {
            return;
        }

        int topMargin = getResources().getDimensionPixelSize(R.dimen.layout_margin);
        int leftMargin = getResources().getDimensionPixelOffset(R.dimen.layout_margin_tiny);
        for (String url : imageUrlList) {
            if (TextUtils.isEmpty(url)) {
                continue;
            }

            ImageView imageView = new ImageView(getThis());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin = topMargin;
            layoutParams.leftMargin = leftMargin;
            layoutParams.rightMargin = leftMargin;
            mImageLayout.addView(imageView, layoutParams);
            GlideUtil.loadPath(getThis(), imageView, url);

            imageView.setOnClickListener(new ImageClickListener(url));
        }

    }

    /**
     * 图片的点击
     */
    private class ImageClickListener implements View.OnClickListener {

        private String mUrl;

        public ImageClickListener(String url) {
            mUrl = url;
        }

        @Override
        public void onClick(View v) {
            ImageZoomActivity.startUrl(getThis(), v, mUrl);
        }
    }


    public static void start(Context context, int id) {
        start(context, id, true);
    }

    public static void start(Context context, int id, boolean isShowPhone) {
        Intent starter = new Intent(context, JobInfoDetailActivity.class);
        starter.putExtra("id", id);
        starter.putExtra("isShowPhone", isShowPhone);
        context.startActivity(starter);
    }
}
