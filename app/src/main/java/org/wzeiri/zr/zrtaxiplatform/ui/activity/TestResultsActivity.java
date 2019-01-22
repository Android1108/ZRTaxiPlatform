package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.AnswerResultsBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IQuestion;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItem;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItemArray;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.util.AnswerHelper;
import org.wzeiri.zr.zrtaxiplatform.util.TimeUtil;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 考试结果
 *
 * @author k-lm on 2017/12/7.
 */

public class TestResultsActivity extends ActionbarActivity {
    @BindView(R.id.aty_test_results_text_time)
    TextView mTextTime;
    @BindView(R.id.aty_test_results_vtv_subject_number)
    ValueTextView mVtvSubjectNumber;
    @BindView(R.id.aty_test_results_vtv_correct_number)
    ValueTextView mVtvCorrectNumber;
    @BindView(R.id.aty_test_results_vtv_error_number)
    ValueTextView mVtvErrorNumber;
    @BindView(R.id.aty_test_results_vtv_correct_rate)
    ValueTextView mVtvCorrectRate;
    @BindView(R.id.aty_test_results_layout_content)
    LinearLayout mLayoutContent;

    @BindView(R.id.aty_test_results_layout_result_layout)
    LinearLayout mResultLayout;

    /**
     * 正确率
     */
    private float mCorrectRate = 1;

    AnswerHelper.SubjectInfoBean mSubjectInfoBean;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_test_results;
    }


    @OnClick(R.id.aty_test_results_text_check_error_subject)
    public void onmTextCheckErrorSubjectClicked() {
        if (mCorrectRate == 1) {
            showToast("没有可以查看的错题");
            return;
        }


        startActivity(AnswerCheckErrorActivity.class);
    }

    @OnClick(R.id.aty_test_results_text_check_reexamination)
    public void onmTextCheckReexaminationClicked() {
        AnswerHelper.getInstance().destroy();
        AnswerActivity.startSimulation(this);
        finish();
    }


    @Override
    protected void init() {
        super.init();
        setCenterTitle("考试成绩");
    }


    @Override
    protected void initData() {
        super.initData();
        mTextTime.setText(TimeUtil.getServerDateAndTime(new Date()));
        submitAchievement();

    }


    @Override
    public void onBackPressed() {
        AnswerHelper.getInstance().destroy();
        super.onBackPressed();
    }

    @Override
    protected void onToolBarBackPressed() {
        AnswerHelper.getInstance().destroy();
        super.onToolBarBackPressed();
    }

    /**
     * 提交考试结果
     */
    private void submitAchievement() {
        mSubjectInfoBean = AnswerHelper.getInstance().getSubjectInfo();
        IQuestion mIQuestion = RetrofitHelper.create(IQuestion.class);
        int subjectNumber = mSubjectInfoBean.getSubjectNumber();

        if (subjectNumber == 0) {
            return;
        }

        // 统计每题选择的答案
        JsonItem[][] subjectArray = new JsonItem[subjectNumber][2];
        List<Integer> subjectIdList = mSubjectInfoBean.getSubjectIdList();
        List<Integer> selectAnswerNoList = mSubjectInfoBean.getSelectAnswerNoList();
        for (int i = 0; i < subjectArray.length; i++) {
            JsonItem[] jsonItemArray = subjectArray[i];
            int answerId = selectAnswerNoList.get(i);
            if (answerId < 0) {
                subjectArray[i] = null;
                continue;
            }
            JsonItem questionId = new JsonItem("questionId", subjectIdList.get(i));
            JsonItem userAnswerId = new JsonItem("userAnswerId", answerId);
            jsonItemArray[0] = questionId;
            jsonItemArray[1] = userAnswerId;
            subjectArray[i] = jsonItemArray;
        }

        JsonItemArray jsonItemArray = new JsonItemArray("quizItems", subjectArray);

        showProgressDialog("正在提交数据请稍候");
        mIQuestion.submitExamQuiz(RetrofitHelper
                .getBody(jsonItemArray,
                        new JsonItem("totalCount", mSubjectInfoBean.getSubjectNumber())))
                .enqueue(new MsgCallBack<BaseBean<AnswerResultsBean>>(getThis(), false) {
                    @Override
                    public void onSuccess(Call<BaseBean<AnswerResultsBean>> call, Response<BaseBean<AnswerResultsBean>> response) {
                        // 只有提交成功才能查看结果
                        mResultLayout.setVisibility(View.VISIBLE);
                        mVtvSubjectNumber.setText(mSubjectInfoBean.getSubjectNumber() + "题");
                        mVtvCorrectNumber.setText(mSubjectInfoBean.getCorrectNumber() + "题");
                        mVtvErrorNumber.setText(mSubjectInfoBean.getErrorNumber() + "题");
                        mCorrectRate = mSubjectInfoBean.getCorrectRate();
                        mVtvCorrectRate.setText(((int) (mCorrectRate * 100)) + "%");
                    }

                    @Override
                    public void onError(Call<BaseBean<AnswerResultsBean>> call, Throwable t) {
                        super.onError(call, t);
                        // 提交失败后，无法查看错题集
                        mCorrectRate = 1;
                    }
                });

    }


}
