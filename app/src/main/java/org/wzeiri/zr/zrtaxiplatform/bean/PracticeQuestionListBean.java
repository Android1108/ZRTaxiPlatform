package org.wzeiri.zr.zrtaxiplatform.bean;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * @author k-lm on 2017/12/12.
 */

public class PracticeQuestionListBean {

    private List<PracticeQuestionBean> questions;

    private int examTimes;

    public List<PracticeQuestionBean> getQuestions() {
        return questions;
    }

    public void setQuestions(List<PracticeQuestionBean> questions) {
        this.questions = questions;
    }

    public int getExamTimes() {
        return examTimes;
    }

    public void setExamTimes(int examTimes) {
        this.examTimes = examTimes;
    }
}
