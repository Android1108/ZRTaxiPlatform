package org.wzeiri.zr.zrtaxiplatform.bean;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * @author k-lm on 2017/12/12.
 */

public class PracticeQuestionBean {


    /**
     * title : string
     * content : string
     * describe : string
     * questionAnswers : [{"isRight":true,"answerIndex":0,"answerName":"string","content":"string","questionId":0,"id":0}]
     * id : 0
     */

    private String title;
    private String content;
    private String describe;
    private int id;
    /**
     * 选择的答案
     */
    private int selectIndex;
    private List<QuestionAnswersBean> questionAnswers;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<QuestionAnswersBean> getQuestionAnswers() {
        return questionAnswers;
    }

    public void setQuestionAnswers(List<QuestionAnswersBean> questionAnswers) {
        this.questionAnswers = questionAnswers;
    }

    public int getSelectIndex() {
        return selectIndex;
    }

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
    }

    public static class QuestionAnswersBean implements Comparable<QuestionAnswersBean> {
        /**
         * isRight : true
         * answerIndex : 0
         * answerName : string
         * content : string
         * questionId : 0
         * id : 0
         */

        private boolean isRight;
        private int answerIndex;
        private String answerName;
        private String content;
        private int questionId;
        private int id;

        public boolean isIsRight() {
            return isRight;
        }

        public void setIsRight(boolean isRight) {
            this.isRight = isRight;
        }

        public int getAnswerIndex() {
            return answerIndex;
        }

        public void setAnswerIndex(int answerIndex) {
            this.answerIndex = answerIndex;
        }

        public String getAnswerName() {
            return answerName;
        }

        public void setAnswerName(String answerName) {
            this.answerName = answerName;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getQuestionId() {
            return questionId;
        }

        public void setQuestionId(int questionId) {
            this.questionId = questionId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Override
        public int compareTo(@NonNull QuestionAnswersBean o) {
            return answerIndex - o.answerIndex;
        }
    }
}
