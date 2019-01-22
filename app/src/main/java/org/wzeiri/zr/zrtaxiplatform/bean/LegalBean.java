package org.wzeiri.zr.zrtaxiplatform.bean;

import java.util.Date;
import java.util.List;

/**
 * @author k-lm on 2017/12/14.
 */

public class LegalBean {


    /**
     * replies : [{"replyContent":"string","creationTime":"2018-01-03T00:49:17.312Z","id":0}]
     * legalType : 0
     * legalTypeName : string
     * content : string
     * isReplied : true
     * creationTime : 2018-01-03T00:49:17.312Z
     * creationUserName : string
     * id : 0
     */

    private int legalType;
    private String legalTypeName;
    private String content;
    private boolean isReplied;
    private Date creationTime;
    private String creationUserName;
    private int id;
    private List<RepliesBean> replies;

    public int getLegalType() {
        return legalType;
    }

    public void setLegalType(int legalType) {
        this.legalType = legalType;
    }

    public String getLegalTypeName() {
        return legalTypeName;
    }

    public void setLegalTypeName(String legalTypeName) {
        this.legalTypeName = legalTypeName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isIsReplied() {
        return isReplied;
    }

    public void setIsReplied(boolean isReplied) {
        this.isReplied = isReplied;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getCreationUserName() {
        return creationUserName;
    }

    public void setCreationUserName(String creationUserName) {
        this.creationUserName = creationUserName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<RepliesBean> getReplies() {
        return replies;
    }

    public void setReplies(List<RepliesBean> replies) {
        this.replies = replies;
    }

    public static class RepliesBean {
        /**
         * replyContent : string
         * creationTime : 2018-01-03T00:49:17.312Z
         * id : 0
         */

        private String replyContent;
        private Date creationTime;
        private int id;

        public String getReplyContent() {
            return replyContent;
        }

        public void setReplyContent(String replyContent) {
            this.replyContent = replyContent;
        }

        public Date getCreationTime() {
            return creationTime;
        }

        public void setCreationTime(Date creationTime) {
            this.creationTime = creationTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
