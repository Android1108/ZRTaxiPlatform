package org.wzeiri.zr.zrtaxiplatform.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.MyApplication;
import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.PracticeQuestionBean;
import org.wzeiri.zr.zrtaxiplatform.bean.PracticeQuestionListBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseListBean;
import org.wzeiri.zr.zrtaxiplatform.common.Config;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IQuestion;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItem;
import org.wzeiri.zr.zrtaxiplatform.network.callback.BaseCallBack;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.service.BaiduLocationService;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.AnswerFragment;
import org.wzeiri.zr.zrtaxiplatform.util.AnswerHelper;
import org.wzeiri.zr.zrtaxiplatform.util.CountDownHelper;
import org.wzeiri.zr.zrtaxiplatform.util.SharedPreferencesUtil;
import org.wzeiri.zr.zrtaxiplatform.util.ThreadSwitch;
import org.wzeiri.zr.zrtaxiplatform.widget.dialog.IOSAlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;


/**
 * 答题
 *
 * @author k-lm on 2017/11/28.
 */

public class AnswerActivity extends ActionbarActivity {
    /**
     * 答题类型
     */
    public final static String KEY_ANSWER_TYPE = "answerType";
    /**
     * 答题数
     */
    public static final String KEY_ANSWER_NUMBER = "answerNumber";

    /**
     * 答题页数
     */
    public static final String KEY_ANSWER_PAGER = "answerPager";
    /**
     * 模拟
     */
    public final static int SIMULATION = 100;
    /**
     * 练习
     */
    public final static int EXERCISES = 101;

    /**
     * 当前答题类型
     */
    private int mAnswerType = EXERCISES;

    /**
     * 题目fragment
     */
    private AnswerFragment mCurrentFragment;

    /**
     * 题目当前页数
     */
    private int mSubjectPagerIndex = 1;
    /**
     * 题目当前页数
     */
    private int mSubjectPagerSize = 20;
    /**
     * 在当前题目与总体数差距 {@link #mCheckCount}数量时，会获取服务器数据，检测是否有题目
     */
    private int mCheckCount = 10;

    /**
     * 是否全部加载完成
     */
    private boolean isLoadFull = false;

    /**
     * 当前题目数量
     */
    private int mCurrentSubjectIndex = 0;

    private MySelectAnswerListener mSelectAnswerListener;

    private IQuestion mQuestion;

    @BindView(R.id.aty_answer_layout_submit)
    FrameLayout mSubmitLayout;

    private TextView mCurrentSubjectNum;

    private IOSAlertDialog mIosAlertDialog;
    /**
     * 考试时间
     */
    private int mTestTime;

    private CountDownHelper mCountDownHelper;

    @OnClick(R.id.aty_answer_text_submit)
    void onSubmit() {
        // 已完成最后一道
        if (isLastSubject() && mCurrentFragment.isSelectedAnswer()) {
            startActivity(TestResultsActivity.class);
            finish();
            return;
        }
        // 未完成显示对话框
        showSubmitDialog();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_answer;
    }


    @Override
    protected void init() {
        super.init();
        mSelectAnswerListener = new MySelectAnswerListener();

        Intent intent = getIntent();
        if (intent != null) {
            mAnswerType = intent.getIntExtra(KEY_ANSWER_TYPE, mAnswerType);
        }

        if (mAnswerType == SIMULATION) {
            setCenterTitle("模拟考试");
            mSubmitLayout.setVisibility(View.VISIBLE);


        } else {
            setCenterTitle("答题练习");
            mCurrentSubjectIndex = SharedPreferencesUtil.getInt(this, KEY_ANSWER_NUMBER, mCurrentSubjectIndex);
            mSubjectPagerIndex = SharedPreferencesUtil.getInt(this, KEY_ANSWER_PAGER, mSubjectPagerIndex);
            mSubmitLayout.setVisibility(View.GONE);
            isLoadFull = false;
        }

    }

    /**
     * 显示时间超时的对话框
     */
    private void showTimeOutDialog() {
        IOSAlertDialog dialog = new IOSAlertDialog(getThis());
        dialog.builder()
                .setTitle("时间已到")
                .setMsg("考试时间已到，本次考试无效")
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .setPositiveClickListener("", new IOSAlertDialog.OnDialogClickListener() {
                    @Override
                    public void onDialogClickListener(IOSAlertDialog dialog) {
                        dialog.dismiss();
                        AnswerHelper.getInstance().destroy();
                        finish();
                    }
                })
                .show();
    }

    /**
     * 设置考试时间
     *
     * @param time
     */
    private void settingExaminationTime(CountDownHelper.TimeUnit unit, int time) {
        mTestTime = time;
        // 设置考试倒计时
        mCountDownHelper = new CountDownHelper(unit, mTestTime);
        mCountDownHelper.setOnCountDownListener(new CountDownHelper.OnCountDownListener() {
            @Override
            public void onCountDownFinish() {
                setCenterTitle("模拟考试");
                showTimeOutDialog();
            }

            @Override
            public void onCountDownTick(long millisUntilFinished) {
                String title = getTime((int) millisUntilFinished);
                setCenterTitle(title);
            }
        });
    }

    /**
     * 返回时间字符串
     *
     * @param time
     * @return
     */
    private String getTime(int time) {
        time /= 1000;
        int minute = time / 60;
        int seconds;
        if (minute != 0) {
            seconds = time % 60;
        } else {
            seconds = time;
        }

        String minuteStr;
        String secondsStr;

        if (seconds < 10) {
            secondsStr = "0" + seconds;
        } else {
            secondsStr = "" + seconds;
        }

        if (minute < 10) {
            minuteStr = "0" + minute;
        } else {
            minuteStr = "" + minute;
        }

        return minuteStr + ":" + secondsStr;
    }


    @Override
    protected void initData() {
        super.initData();
        if (mAnswerType == SIMULATION) {
            getSimulationData();
        } else {
            showProgressDialog();
            getExercisesData();
        }
    }

    /**
     * 获取模拟考试数据
     */
    private void getSimulationData() {
        if (mQuestion == null) {
            mQuestion = RetrofitHelper.create(IQuestion.class);
        }
        mQuestion.getExamQuestions()
                .enqueue(new MsgCallBack<BaseBean<PracticeQuestionListBean>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<PracticeQuestionListBean>> call, Response<BaseBean<PracticeQuestionListBean>> response) {
                        PracticeQuestionListBean questionListBean = response.body().getResult();
                        if (questionListBean == null) {
                            showToast("当前题库中没有题目");
                            finish();
                            return;
                        }

                        List<PracticeQuestionBean> baseListBean = questionListBean.getQuestions();

                        if (baseListBean == null) {
                            showToast("当前题库中没有题目");
                            finish();
                            return;
                        }
                        settingExaminationTime(CountDownHelper.TimeUnit.MINUTE, questionListBean.getExamTimes());
                        AnswerHelper.getInstance().setSubjectCount(baseListBean.size());
                        AnswerHelper.getInstance().init(baseListBean);
                        selectSubject(mCurrentSubjectIndex);
                        if (mCountDownHelper != null) {
                            mCountDownHelper.startTime();
                        }

                    }
                });


    }

    /**
     * 获取练习数据
     */
    private void getExercisesData() {
        if (mQuestion == null) {
            mQuestion = RetrofitHelper.create(IQuestion.class);
        }

        mQuestion.getPracticeQuestions(mSubjectPagerIndex, mSubjectPagerSize)
                .enqueue(new MsgCallBack<BaseBean<BaseListBean<PracticeQuestionBean>>>(getThis()) {
                    @Override
                    public void onSuccess(Call<BaseBean<BaseListBean<PracticeQuestionBean>>> call, Response<BaseBean<BaseListBean<PracticeQuestionBean>>> response) {
                        BaseListBean<PracticeQuestionBean> baseListBean = response.body().getResult();
                        if ((baseListBean == null ||
                                baseListBean.getItems() == null ||
                                baseListBean.getItems().size() == 0) &&
                                mSubjectPagerIndex == 1) {
                            showToast("当前题库中没有题目");
                            finish();
                            return;
                        }
                        //题目数小于加载数，说明已加载所有题目
                        if (baseListBean == null ||
                                baseListBean.getItems() == null ||
                                baseListBean.getItems().size() < mSubjectPagerSize) {
                            isLoadFull = true;
                        }


                        List<PracticeQuestionBean> list = baseListBean == null ? null : baseListBean.getItems();
                        AnswerHelper answerHelper = AnswerHelper.getInstance();
                        if (answerHelper.getSubjectSize() == 0) {
                            // 第一次加载初始化题库
                            answerHelper.setSubjectCount(baseListBean.getTotalCount());
                            answerHelper.init(list, mSubjectPagerSize, mSubjectPagerIndex);
                            selectSubject(mCurrentSubjectIndex);
                        } else {
                            int startIndex = (mSubjectPagerIndex - 2) * mSubjectPagerSize;
                            int endIndex = 0;
                            if (startIndex == 0) {
                                endIndex = mSubjectPagerSize - 10;
                            } else {
                                startIndex -= 10;
                                endIndex = startIndex + 20;
                            }
                            // 添加题目
                            answerHelper.addQuestionList(list);
                            // 清空已近打完的题目信息
                            //answerHelper.clearSubject(startIndex, endIndex);
                        }

                        mSubjectPagerIndex++;
                        closeProgressDialog();

                    }
                });
    }


    /**
     * 进入下一道题
     *
     * @param isCorrect   是否答对题目
     * @param selectIndex 选择答案位置
     */
    private void nextSubject(final boolean isCorrect, int selectIndex) {
        AnswerHelper.getInstance().setSelectAnswerIndex(mCurrentSubjectIndex, selectIndex);

        // 当练习模式下，答题数与当前题目数相差10道时，且题目未加载完所有题目，则加载剩下的所有题目
        if (mCurrentSubjectIndex + 11 >= mSubjectPagerSize * (mSubjectPagerIndex - 1) &&
                !isLoadFull &&
                mAnswerType == EXERCISES) {
            getExercisesData();
        }

        ThreadSwitch.createTask(new ThreadSwitch.OnCreateListener<Object>() {
            @Override
            public Object onCreate(ThreadSwitch threadSwitch) {
                int time;
                // 回答正确停留 1500毫秒 ，回答错误停留1500毫秒
                if (isCorrect) {
                    time = 1500;
                } else {
                    time = 1500;
                }
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        })
                .switchLooper(ThreadSwitch.MAIN_THREAD)
                .submit(new ThreadSwitch.OnSubmitListener<Object>() {
                    @Override
                    public void onSubmit(ThreadSwitch threadSwitch, Object value) {
                        if (isDestroyed()) {
                            return;
                        }
                        nextSubject();
                    }
                });
        showToast("正在进入下一道题目");

    }

    /**
     * 下一道题目
     */
    private void nextSubject() {
        // 是否最后一道题
        if (isLastSubject()) {
            if (mAnswerType == SIMULATION) {
                startActivity(TestResultsActivity.class);
                finish();
                return;
            } else {
                // 提交成绩
                submitAchievement();
                // 重置题目 及答题记录
                mSubjectPagerIndex = 1;
                mCurrentSubjectIndex = 0;
                // 显示是否继续答题
                showContinueDialog();
                return;
            }
        }
        mCurrentSubjectIndex++;
        selectSubject(mCurrentSubjectIndex);
    }

    /**
     * 是否是最后一道题目
     *
     * @return true 为当前为最后一道题目
     */
    private boolean isLastSubject() {
        return mCurrentSubjectIndex + 1 >= AnswerHelper.getInstance().getSubjectSize();
    }

    /**
     * 选择题目
     *
     * @param index 题目位置
     */
    private void selectSubject(int index) {
        AnswerHelper helper = AnswerHelper.getInstance();
        List<PracticeQuestionBean.QuestionAnswersBean> subjectsList = helper.getSubject(index);
        int correctIndex = helper.getCorrectAnswerIndex(index);


        if (subjectsList == null || subjectsList.size() == 0 || correctIndex < 0) {
            return;
        }


        FragmentManager fragmentManager = getSupportFragmentManager();
        AnswerFragment fragment = AnswerFragment.newInstanceAnswer(index);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 第一次加载题目不需要执行动画
        if (mCurrentFragment != null) {
            transaction.setCustomAnimations(R.anim.answer_fragment_in, R.anim.answer_fragment_out);
            transaction.replace(R.id.aty_answer_layout_content, fragment);
            mCurrentFragment.setUserVisibleHint(false);
        } else {
            transaction.add(R.id.aty_answer_layout_content, fragment);
            transaction.show(fragment);
        }
        mCurrentFragment = fragment;
        mCurrentFragment.setUserVisibleHint(true);
        mCurrentFragment.setOnSelectAnswerListener(mSelectAnswerListener);
        transaction.commit();


        if (mCurrentSubjectNum != null) {
            if (mAnswerType == SIMULATION) {
                mCurrentSubjectNum.setText((index + 1) + "/" + AnswerHelper.getInstance().getSubjectSize());
            } else {
                int count = AnswerHelper.getInstance().getSubjectSize();
                mCurrentSubjectNum.setText((index + 1) + "/" + count);
            }

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_blank, menu);
        mCurrentSubjectNum = new TextView(this);
        mCurrentSubjectNum.setTextColor(ContextCompat.getColor(this, R.color.black70));
        mCurrentSubjectNum.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_small));
        menu.findItem(R.id.menu_blank).setActionView(mCurrentSubjectNum);

        mCurrentSubjectNum.setText((mCurrentSubjectIndex + 1) + "/" + AnswerHelper.getInstance().getSubjectSize());
        return true;
    }

    private class MySelectAnswerListener implements AnswerFragment.OnSelectAnswerListener {

        @Override
        public void onSelectWrongAnswer(int selectIndex) {
            nextSubject(false, selectIndex);

        }

        @Override
        public void onSelectRightAnswer(int selectIndex) {
            nextSubject(true, selectIndex);

        }
    }

    /**
     * 显示答题练习时继续答题对话框
     */
    private void showContinueDialog() {
        if (mIosAlertDialog == null) {
            mIosAlertDialog = new IOSAlertDialog(this)
                    .builder()
                    .setTitle("是否继续答题")
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(false)
                    .setNegativeClickListener("", new IOSAlertDialog.OnDialogClickListener() {
                        @Override
                        public void onDialogClickListener(IOSAlertDialog dialog) {
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .setPositiveClickListener("", new IOSAlertDialog.OnDialogClickListener() {
                        @Override
                        public void onDialogClickListener(IOSAlertDialog dialog) {
                            isLoadFull = false;
                            // 清空题目数据
                            AnswerHelper.getInstance().destroy();
                            // 重新获取题目
                            getExercisesData();
                            dialog.dismiss();

                        }
                    });

        }
        mIosAlertDialog.show();
    }

    /**
     * 显示提交成绩对话框
     */

    private void showSubmitDialog() {
        if (mIosAlertDialog == null) {
            mIosAlertDialog = new IOSAlertDialog(this)
                    .builder()
                    .setTitle("提交试卷")
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(false)
                    .setNegativeClickListener("", new IOSAlertDialog.OnDialogClickListener() {
                        @Override
                        public void onDialogClickListener(IOSAlertDialog dialog) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveClickListener("", new IOSAlertDialog.OnDialogClickListener() {
                        @Override
                        public void onDialogClickListener(IOSAlertDialog dialog) {
                            dialog.dismiss();
                            startActivity(TestResultsActivity.class);
                            finish();
                        }
                    });

        }

        int count = AnswerHelper.getInstance().getSubjectSize() - mCurrentSubjectIndex;

        if (mCurrentFragment.isSelectedAnswer()) {
            count--;
        }


        mIosAlertDialog.setMsg("还有" + count + "道题没做，确定交卷？");


        if (!mIosAlertDialog.isShowing()) {
            mIosAlertDialog.show();
        }

    }

    /**
     * 提交成绩
     */
    private void submitAchievement() {
        AnswerHelper.SubjectInfoBean subjectInfoBean = AnswerHelper.getInstance().getSubjectInfo();
        if (subjectInfoBean == null || subjectInfoBean.getSubjectNumber() == 0) {
            return;
        }

        if (mQuestion == null) {
            mQuestion = RetrofitHelper.create(IQuestion.class);
        }

        int subjectNumber = subjectInfoBean.getSubjectNumber();
        int correctNumber = subjectInfoBean.getCorrectNumber();

        mQuestion.submitPracticeQuiz(RetrofitHelper
                .getBody(new JsonItem("totalCount", subjectNumber),
                        new JsonItem("rightCount", correctNumber)))
                .enqueue(new BaseCallBack<BaseBean<String>>() {
                    @Override
                    public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {

                    }

                    @Override
                    public void onError(Call<BaseBean<String>> call, Throwable t) {

                    }
                });


    }

    @Override
    protected void onDestroy() {
        // 在练习模式下保存记录保存当前
        if (mAnswerType == EXERCISES) {
            if (mCurrentSubjectIndex >= 0 &&
                    mCurrentSubjectIndex < AnswerHelper.getInstance().getSubjectSize()) {

                // 在完成最后一题后马上退出
                if (isLastSubject() && mCurrentFragment.isSelectedAnswer()) {
                    clearAnswerRecord();
                } else {
                    mSubjectPagerIndex = mCurrentSubjectIndex / mSubjectPagerSize + 1;

                    SharedPreferencesUtil.put(this, KEY_ANSWER_NUMBER, mCurrentSubjectIndex);
                    SharedPreferencesUtil.put(this, KEY_ANSWER_PAGER, mSubjectPagerIndex);
                }
                submitAchievement();
            }
            AnswerHelper.getInstance().destroy();
        }

        if (mCountDownHelper != null) {
            mCountDownHelper.cancel();
        }
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        if (mAnswerType == SIMULATION) {
            AnswerHelper.getInstance().destroy();
        }
        super.onBackPressed();
    }


    @Override
    protected void onToolBarBackPressed() {
        if (mAnswerType == SIMULATION) {
            AnswerHelper.getInstance().destroy();
        }
        super.onToolBarBackPressed();
    }


    /**
     * 练习
     *
     * @param context
     */
    public static void startExercises(Context context) {
        Intent starter = new Intent(context, AnswerActivity.class);
        starter.putExtra(KEY_ANSWER_TYPE, EXERCISES);
        context.startActivity(starter);
    }

    /**
     * 模拟
     *
     * @param context
     */
    public static void startSimulation(Context context) {
        Intent starter = new Intent(context, AnswerActivity.class);
        starter.putExtra(KEY_ANSWER_TYPE, SIMULATION);
        context.startActivity(starter);
    }

    /**
     * 清空答题记录
     */
    public static void clearAnswerRecord() {
        SharedPreferencesUtil.remove(MyApplication.getApplication(), AnswerActivity.KEY_ANSWER_NUMBER);
        SharedPreferencesUtil.remove(MyApplication.getApplication(), AnswerActivity.KEY_ANSWER_PAGER);
    }


}
