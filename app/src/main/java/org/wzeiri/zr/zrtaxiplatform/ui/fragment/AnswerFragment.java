package org.wzeiri.zr.zrtaxiplatform.ui.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.PracticeQuestionBean;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.base.BaseFragment;
import org.wzeiri.zr.zrtaxiplatform.util.AnswerHelper;
import org.wzeiri.zr.zrtaxiplatform.widget.SubjectRadioButton;

import java.util.List;

import butterknife.BindView;

/**
 * @author k-lm on 2017/11/29.
 */

public class AnswerFragment extends BaseFragment {
    /**
     * 题目号
     */
    private static final String KEY_SUBJECT_NUMBER = "subject";

    private static final String KEY_TYPE = "type";
    /**
     * 查看
     */
    private static final int CODE_CHECK = 1;
    /**
     * 答题
     */
    private static final int CODE_ANSWER = 2;

    int mSubjectIndex = -1;
    private int mType = CODE_ANSWER;
    /**
     * 是否选择答案
     */
    private boolean mIsSelectAnswer = false;

    @BindView(R.id.fragment_answer_text_title)
    TextView mTextTitle;
    @BindView(R.id.fragment_answer_rg_subject_layout)
    RadioGroup mSubjectLayout;


    private OnSelectAnswerListener mSelectAnswerListener;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_answer;
    }

    @Override
    public void create() {
        super.create();
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        mType = bundle.getInt(KEY_TYPE, mType);
    }

    @Override
    public void init() {
        super.init();
        if (mType == CODE_ANSWER) {
            initAnswer();
        } else {
            initCheck();
        }

    }

    /**
     * 初始化答题模式
     */
    private void initAnswer() {
        mSubjectLayout.setTag(-1);
        mSubjectLayout.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectIsSelectAnswer(checkedId);
                //屏蔽子view的点击
                int count = mSubjectLayout.getChildCount();
                for (int i = 0; i < count; i++) {
                    View view = mSubjectLayout.getChildAt(i);
                    view.setEnabled(false);
                }


            }
        });
    }

    /**
     * 初始化查看模式
     */
    private void initCheck() {
        mSubjectLayout.setEnabled(false);

    }


    @Override
    public void initData() {
        super.initData();
        Bundle bundle = getArguments();

        if (bundle == null) {
            return;
        }
        AnswerHelper helper = AnswerHelper.getInstance();

        mSubjectIndex = bundle.getInt(KEY_SUBJECT_NUMBER, -1);

        mTextTitle.setText(helper.getSubjectContent(mSubjectIndex));
        List<PracticeQuestionBean.QuestionAnswersBean> subjects = helper.getSubject(mSubjectIndex);
        int correctIndex = helper.getCorrectAnswerIndex(mSubjectIndex);

        if (mType == CODE_CHECK) {
            mSubjectLayout.setTag(helper.getCorrectAnswerIndex(mSubjectIndex));
            showSubjectsView(subjects, correctIndex, helper.getSelectAnswerIndex(mSubjectIndex));
        } else {
            showSubjectsView(subjects, correctIndex, -1);
        }


    }

    /**
     * 显示题目视图
     *
     * @param subjects          题目内容
     * @param correctIndex      正确答案位置
     * @param selectAnswerIndex 选择的答案位置
     */
    private void showSubjectsView(List<PracticeQuestionBean.QuestionAnswersBean> subjects, int correctIndex, int selectAnswerIndex) {
        if (subjects == null || subjects.size() == 0 || correctIndex < 0) {
            return;
        }
        int content = subjects.size();
        // 设置正确答案位置
        mSubjectLayout.setTag(correctIndex);
        for (int i = 0; i < content; i++) {
            PracticeQuestionBean.QuestionAnswersBean bean = subjects.get(i);
            SubjectRadioButton radioButton = new SubjectRadioButton(getActivity());
            radioButton.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.layout_margin));
            radioButton.setId(bean.getId());

            radioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_medium));
            radioButton.setBackgroundResource(R.drawable.bg_onclick);
            radioButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.black70));
            radioButton.setText(bean.getContent());

            radioButton.setLeftDrawText(bean.getAnswerName());
            int padding = getResources().getDimensionPixelOffset(R.dimen.layout_margin_mini);
            radioButton.setPadding(0, padding, 0, padding);

            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            // 设置底部边距
            layoutParams.bottomMargin = getResources().getDimensionPixelOffset(R.dimen.layout_margin);

            mSubjectLayout.addView(radioButton, layoutParams);
        }

        if (mType == CODE_CHECK) {
            selectIsSelectAnswer(selectAnswerIndex);
        }

    }

    /**
     * 选择答案
     *
     * @param index 位置
     */
    private void selectIsSelectAnswer(int index) {
        // 正确id
        int correctIndex = (int) mSubjectLayout.getTag();
        RadioButton selectSubject = null;

        if (index >= 0) {
            selectSubject = mSubjectLayout.findViewById(index);
        }

        mIsSelectAnswer = true;
        // 选择正确
        if (correctIndex == index && selectSubject != null) {
            selectSubject.setTextColor(ContextCompat.getColor(getActivity(), R.color.green1));
            selectSubject.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_select_correct, 0, 0, 0);
            if (mSelectAnswerListener != null) {
                mSelectAnswerListener.onSelectRightAnswer(index);
            }
        } else {
            //选择错误
            if (selectSubject != null) {
                selectSubject.setTextColor(ContextCompat.getColor(getActivity(), R.color.orange1));
                selectSubject.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_select_error, 0, 0, 0);
            }

            RadioButton correctButton = mSubjectLayout.findViewById(correctIndex);
            correctButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.green1));
            correctButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_select_correct, 0, 0, 0);
            if (mSelectAnswerListener != null) {
                mSelectAnswerListener.onSelectWrongAnswer(index);
            }
        }
    }


    /**
     * 移除所有题目
     */
    private void removeAllSubjects() {
        mSubjectLayout.removeAllViews();
        mSubjectLayout.setTag(-1);
    }

    /**
     * 是否已选择答案
     *
     * @return 是否已选择答案
     */
    public boolean isSelectedAnswer() {
        return mIsSelectAnswer;
    }


    public void setOnSelectAnswerListener(OnSelectAnswerListener listener) {
        mSelectAnswerListener = listener;
    }


    public interface OnSelectAnswerListener {
        /**
         * 选择错误答案
         */
        void onSelectWrongAnswer(int selectIndex);

        /**
         * 选择正确答案
         */
        void onSelectRightAnswer(int selectIndex);
    }

    /**
     * 答题
     *
     * @param index 题目号
     * @return
     */
    public static AnswerFragment newInstanceAnswer(int index) {
        Bundle args = new Bundle();
        AnswerFragment fragment = new AnswerFragment();
        args.putInt(KEY_SUBJECT_NUMBER, index);
        args.putInt(KEY_TYPE, CODE_ANSWER);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 查看题目
     *
     * @param index 题目号
     * @return
     */
    public static AnswerFragment newInstanceCheck(int index) {
        Bundle args = new Bundle();
        AnswerFragment fragment = new AnswerFragment();
        args.putInt(KEY_SUBJECT_NUMBER, index);
        args.putInt(KEY_TYPE, CODE_CHECK);
        fragment.setArguments(args);
        return fragment;
    }


}
