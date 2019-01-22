package org.wzeiri.zr.zrtaxiplatform.util;

import android.os.Parcel;
import android.os.Parcelable;

import com.baidu.mapapi.search.core.PoiInfo;

import org.wzeiri.zr.zrtaxiplatform.bean.PracticeQuestionBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author k-lm on 2017/12/7.
 */

public class AnswerHelper {
    private static AnswerHelper ourInstance;

    /**
     * 记录每道题的正确答案
     */
    private List<Integer> mCorrectIndexList = new ArrayList<>();

    /**
     * 题目
     */
    private List<PracticeQuestionBean> mPracticeQuestionList = new ArrayList<>();
    /**
     * 总体数
     */
    private int mSubjectSun = 0;
    /**
     * 删除的题目数量
     */
    private int mRemoveCount = 0;
    /**
     * 忽略的题目数量，用于答题练习时，当前回答的第一道题目，不是编号为1的题目
     */
    private int mIgnoreCount = 0;

    /**
     * 记录每道题选择的答案
     */
    private List<Integer> mSelectAnswerIndexArray = new ArrayList<>();


    public static AnswerHelper getInstance() {
        if (ourInstance == null) {
            ourInstance = new AnswerHelper();
        }
        return ourInstance;
    }

    private AnswerHelper() {
    }


    /**
     * 初始化题目
     *
     * @param list
     */
    public void init(List<PracticeQuestionBean> list) {
        init(list, 0, 1);
    }

    /**
     * 设置答题总数
     *
     * @param count
     */
    public void setSubjectCount(int count) {
        mSubjectSun = count;
    }

    /**
     * 初始化题目
     *
     * @param list
     * @param currentPagerIndex 当前加载的页数
     * @param currentPagerSize  当前加载的数量
     */
    public void init(List<PracticeQuestionBean> list, int currentPagerSize, int currentPagerIndex) {
        if (list == null || list.size() == 0) {
            return;
        }
        mPracticeQuestionList.clear();
        mCorrectIndexList.clear();
        int count = currentPagerSize * (currentPagerIndex - 1);
       /*  if (mSubjectSun < sun) {
            mSubjectSun = sun;
        }*/
        if (count > 0) {
            mIgnoreCount = count;
            mRemoveCount = count;
        }

        addQuestionList(list);


    }

    /**
     * 添加题目
     *
     * @param list
     */
    public void addQuestionList(List<PracticeQuestionBean> list) {
        if (list == null || list.size() == 0) {
            return;
        }
//        mSubjectSun += list.size();
        mPracticeQuestionList.addAll(list);
        // 记录每题的正确答案
        for (PracticeQuestionBean practiceQuestionBean : list) {
            List<PracticeQuestionBean.QuestionAnswersBean> beans = practiceQuestionBean.getQuestionAnswers();
            if (beans == null || beans.size() == 0) {
                continue;
            }

            int count = beans.size();
            int rightIndex = -1;
            // 判断是否为正确答案
            for (int i = 0; i < count; i++) {
                PracticeQuestionBean.QuestionAnswersBean bean = beans.get(i);
                if (bean.isIsRight()) {
                    rightIndex = bean.getId();
                    break;
                }
            }
            // 记录正确答案
            mCorrectIndexList.add(rightIndex);

        }


    }


    /**
     * 销毁
     */
    public void destroy() {
        mPracticeQuestionList = null;
        mCorrectIndexList = null;
        mSelectAnswerIndexArray = null;
        ourInstance = null;
        mSubjectSun = 0;
    }

    /**
     * 返回标题
     *
     * @param index
     * @return
     */
    public String getSubjectTitle(int index) {
        index -= mRemoveCount;
        if (mPracticeQuestionList == null || mPracticeQuestionList.size() <= index || index < 0) {
            return "";
        }
        PracticeQuestionBean questionBean = mPracticeQuestionList.get(index);
        if (questionBean == null) {
            return "";
        }
        return questionBean.getTitle();
    }

    /**
     * 返回内容
     *
     * @param index
     * @return
     */
    public String getSubjectContent(int index) {
        index -= mRemoveCount;
        if (mPracticeQuestionList == null || mPracticeQuestionList.size() <= index || index < 0) {
            return "";
        }
        PracticeQuestionBean questionBean = mPracticeQuestionList.get(index);
        if (questionBean == null) {
            return "";
        }
        return questionBean.getContent();
    }

    /**
     * 返回题目
     *
     * @param index 题目号
     * @return 题目
     */
    public List<PracticeQuestionBean.QuestionAnswersBean> getSubject(int index) {
        index -= mRemoveCount;
        if (mPracticeQuestionList == null || mPracticeQuestionList.size() <= index || index < 0) {
            return null;
        }
        PracticeQuestionBean questionBean = mPracticeQuestionList.get(index);
        if (questionBean == null) {
            return null;
        }
        List<PracticeQuestionBean.QuestionAnswersBean> list = questionBean.getQuestionAnswers();
        Collections.sort(list);
        return list;

    }

    /**
     * 返回正确答案位置
     *
     * @param index 题目号
     * @return 返回-1 表示题目号错误
     */
    public int getCorrectAnswerIndex(int index) {
        if (mCorrectIndexList == null || index < 0) {
            return -1;
        }
        index -= mIgnoreCount;
        if (index < 0) {
            index = 0;
        }
        return mCorrectIndexList.get(index);
    }

    /**
     * 设置当前题目选择的答案位置
     *
     * @param index       题目号
     * @param answerIndex 答案位置
     */
    public void setSelectAnswerIndex(int index, int answerIndex) {
        if (mSelectAnswerIndexArray == null || index < 0) {
            return;
        }
        int newIndex = index - mIgnoreCount;
        if (newIndex < 0) {
            newIndex = 0;
        }
        if (newIndex == mSelectAnswerIndexArray.size()) {
            mSelectAnswerIndexArray.add(newIndex, answerIndex);
        } else if (newIndex < mSelectAnswerIndexArray.size()) {
            mSelectAnswerIndexArray.set(newIndex, answerIndex);
        }
        index -= mRemoveCount;
        if (index < 0) {
            index = 0;
        }
        mPracticeQuestionList.get(index).setSelectIndex(answerIndex);
    }

    /**
     * 清空题目
     *
     * @param startIndex 开始题目数
     * @param endIndex   结束题目数
     */
    public void clearSubject(int startIndex, int endIndex) {
        int size = mPracticeQuestionList.size();
        if (size <= startIndex) {
            return;
        }
        endIndex = Math.min(size, endIndex);
        int newStartIndex = startIndex - mRemoveCount;
        int newEndIndex = endIndex - mRemoveCount;

        if (newStartIndex < 0) {
            newStartIndex = 0;
        }
        if (newEndIndex < 0) {
            newEndIndex = 0;
        }
        for (int i = newStartIndex; i < newEndIndex; i++) {
            mPracticeQuestionList.remove(newStartIndex);
        }
        mRemoveCount = endIndex;
    }


    /**
     * 返回当前题目选择的答案位置
     *
     * @param index 题目号
     * @return 返回-1表示 未答题或者题目错误
     */
    public int getSelectAnswerIndex(int index) {
        if (mSelectAnswerIndexArray == null || index < 0) {
            return -1;
        }

        if (index >= mSelectAnswerIndexArray.size()) {
            index -= mRemoveCount;
            if (index < 0) {
                index = 0;
            }
        }

        if (index >= mSelectAnswerIndexArray.size()) {
            return -1;
        }

        return mSelectAnswerIndexArray.get(index);
    }

    /**
     * 返回题目数
     *
     * @return 题目数
     */
    public int getSubjectSize() {
        return mSubjectSun;
    }


    /**
     * 返回答题信息
     *
     * @return
     */
    public SubjectInfoBean getSubjectInfo() {
        SubjectInfoBean subjectInfoBean = new SubjectInfoBean();
        if (mSelectAnswerIndexArray == null) {
            return subjectInfoBean;
        }
        subjectInfoBean.subjectNumber = mPracticeQuestionList.size();
        int selectCount = mSelectAnswerIndexArray.size();
        int correctCount = mCorrectIndexList.size();
        int questioncount = mPracticeQuestionList.size();

        for (int i = 0; i < mSubjectSun; i++) {
            int selectIndex = -1;
            int answerIndex = -1;
            if (i < selectCount) {
                selectIndex = mSelectAnswerIndexArray.get(i);
            }

            if (i < correctCount) {
                answerIndex = mCorrectIndexList.get(i);
            }

            PracticeQuestionBean bean = null;
            if (i < questioncount) {
                bean = mPracticeQuestionList.get(i);
            }


            if (bean == null) {
                continue;
            }

            if (selectIndex == answerIndex) {
                subjectInfoBean.correctNumber++;
                subjectInfoBean.correctSubjectNo.add(i + 1);
            } else {
                subjectInfoBean.errorNumber++;
                subjectInfoBean.errorSubjectNo.add(i + 1);
            }

            subjectInfoBean.subjectNoList.add(i + 1);
            subjectInfoBean.subjectIdList.add(bean.getId());
            subjectInfoBean.selectAnswerNoList.add(bean.getSelectIndex());
        }

        subjectInfoBean.correctRate = subjectInfoBean.correctNumber * 1.0F / mSubjectSun;

        return subjectInfoBean;
    }


    public class SubjectInfoBean implements Parcelable {
        // 答题数
        private int subjectNumber;
        // 正确数
        private int correctNumber;
        // 错误数
        private int errorNumber;
        // 正确率
        private float correctRate;
        /**
         * 正确题目id
         */
        private List<Integer> correctSubjectNo = new ArrayList<>();
        /**
         * 错误题目id
         */
        private List<Integer> errorSubjectNo = new ArrayList<>();
        /**
         * 题目号
         */
        private List<Integer> subjectNoList = new ArrayList<>();
        private List<Integer> subjectIdList = new ArrayList<>();

        /**
         * 选择的答案id
         */
        private List<Integer> selectAnswerNoList = new ArrayList<>();

        private SubjectInfoBean() {

        }


        public int getSubjectNumber() {
            return subjectNumber;
        }

        public int getCorrectNumber() {
            return correctNumber;
        }

        public int getErrorNumber() {
            return errorNumber;
        }

        public float getCorrectRate() {
            return correctRate;
        }

        public List<Integer> getCorrectSubjectNo() {
            return correctSubjectNo;
        }

        public List<Integer> getErrorSubjectNo() {
            return errorSubjectNo;
        }

        public List<Integer> getSubjectNoList() {
            return subjectNoList;
        }

        public List<Integer> getSelectAnswerNoList() {
            return selectAnswerNoList;
        }

        public List<Integer> getSubjectIdList() {
            return subjectIdList;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.subjectNumber);
            dest.writeInt(this.correctNumber);
            dest.writeInt(this.errorNumber);
            dest.writeFloat(this.correctRate);
            dest.writeList(this.correctSubjectNo);
            dest.writeList(this.errorSubjectNo);
            dest.writeList(this.subjectNoList);
            dest.writeList(this.selectAnswerNoList);
        }

        protected SubjectInfoBean(Parcel in) {
            this.subjectNumber = in.readInt();
            this.correctNumber = in.readInt();
            this.errorNumber = in.readInt();
            this.correctRate = in.readFloat();
            this.correctSubjectNo = new ArrayList<Integer>();
            in.readList(this.correctSubjectNo, Integer.class.getClassLoader());
            this.errorSubjectNo = new ArrayList<Integer>();
            in.readList(this.errorSubjectNo, Integer.class.getClassLoader());
            this.subjectNoList = new ArrayList<Integer>();
            in.readList(this.subjectNoList, Integer.class.getClassLoader());
            this.selectAnswerNoList = new ArrayList<Integer>();
            in.readList(this.selectAnswerNoList, Integer.class.getClassLoader());
        }

        public final Creator<SubjectInfoBean> CREATOR = new Creator<SubjectInfoBean>() {
            @Override
            public SubjectInfoBean createFromParcel(Parcel source) {
                return new SubjectInfoBean(source);
            }

            @Override
            public SubjectInfoBean[] newArray(int size) {
                return new SubjectInfoBean[size];
            }
        };
    }

}
