package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.ISundry;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItem;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 意见反馈
 *
 * @author k-lm on 2017/11/20.
 */

public class OpinionFeedbackActivity extends ActionbarActivity {

    private ISundry mISundry;

    @BindView(R.id.aty_opinion_feedback_edit_feedback)
    EditText mEditFeedback;

    @BindView(R.id.aty_opinion_feedback_text_submit)
    TextView mTextSubmit;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_opinion_feedback;
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("意见反馈");
    }

    @OnClick(R.id.aty_opinion_feedback_text_submit)
    void onSubmit() {
        String content = mEditFeedback.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            showToast("请填写反馈信息");
            return;
        }

        if (mISundry == null) {
            mISundry = RetrofitHelper.create(ISundry.class);
        }

        mISundry.createOpinionReport(RetrofitHelper
                .getBody(new JsonItem("content", content)))
                .enqueue(new MsgCallBack<BaseBean<String>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                        showToast("反馈成功");
                        finish();
                    }
                });
    }


    @Override
    protected void initData() {
        super.initData();


    }
}
