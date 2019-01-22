package org.wzeiri.zr.zrtaxiplatform.bean;

import java.util.Date;
import java.util.List;

/**
 * @author k-lm on 2017/12/15.
 */

public class PostDetailBean {

    /**
     * title : string
     * sectionId : 0
     * content : string
     * viewer : 0
     * greateNumber : 0
     * commentNumber : 0
     * postStatus : 0
     * isEnableComments : true
     * isTop : true
     * isHot : true
     * creationTime : 2017-12-15T05:01:44.811Z
     * creatorUserId : 0
     * creatorUserName : string
     * creatorUserProfile : string
     * postComments : [{"commentUserId":0,"commentUserName":"string","commentUserProfile":"string","content":"string","postId":0,"creationTime":"2017-12-15T05:01:44.811Z","id":0}]
     * postPictures : ["string"]
     * id : 0
     */

    private String title;
    private int sectionId;
    private String content;
    private int viewer;
    private int greateNumber;
    private int commentNumber;
    private int postStatus;
    private String sectionName;
    private boolean isEnableComments;
    private boolean isTop;
    private boolean isHot;
    private Date creationTime;
    private int creatorUserId;
    private String creatorUserName;
    private String creatorUserProfile;
    private int id;
    private List<PostCommentsBean> postComments;
    private List<PostPicturesBean> postPictures;
    private boolean isGreated;
    private Integer driverId;

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getViewer() {
        return viewer;
    }

    public void setViewer(int viewer) {
        this.viewer = viewer;
    }

    public int getGreateNumber() {
        return greateNumber;
    }

    public void setGreateNumber(int greateNumber) {
        this.greateNumber = greateNumber;
    }

    public int getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(int commentNumber) {
        this.commentNumber = commentNumber;
    }

    public int getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(int postStatus) {
        this.postStatus = postStatus;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public boolean isIsEnableComments() {
        return isEnableComments;
    }

    public void setIsEnableComments(boolean isEnableComments) {
        this.isEnableComments = isEnableComments;
    }

    public boolean isIsTop() {
        return isTop;
    }

    public void setIsTop(boolean isTop) {
        this.isTop = isTop;
    }

    public boolean isIsHot() {
        return isHot;
    }

    public void setIsHot(boolean isHot) {
        this.isHot = isHot;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public int getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(int creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public String getCreatorUserName() {
        return creatorUserName;
    }

    public void setCreatorUserName(String creatorUserName) {
        this.creatorUserName = creatorUserName;
    }

    public String getCreatorUserProfile() {
        return creatorUserProfile;
    }

    public void setCreatorUserProfile(String creatorUserProfile) {
        this.creatorUserProfile = creatorUserProfile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<PostCommentsBean> getPostComments() {
        return postComments;
    }

    public void setPostComments(List<PostCommentsBean> postComments) {
        this.postComments = postComments;
    }

    public List<PostPicturesBean> getPostPictures() {
        return postPictures;
    }

    public void setPostPictures(List<PostPicturesBean> postPictures) {
        this.postPictures = postPictures;
    }

    public boolean isGreated() {
        return isGreated;
    }

    public void setGreated(boolean greated) {
        isGreated = greated;
    }

    public static class PostCommentsBean {
        /**
         * commentUserId : 0
         * commentUserName : string
         * commentUserProfile : string
         * content : string
         * postId : 0
         * creationTime : 2017-12-15T05:01:44.811Z
         * id : 0
         */

        private int commentUserId;
        private String commentUserName;
        private String commentUserProfile;
        private String content;
        private int postId;
        private Date creationTime;
        private int id;

        public int getCommentUserId() {
            return commentUserId;
        }

        public void setCommentUserId(int commentUserId) {
            this.commentUserId = commentUserId;
        }

        public String getCommentUserName() {
            return commentUserName;
        }

        public void setCommentUserName(String commentUserName) {
            this.commentUserName = commentUserName;
        }

        public String getCommentUserProfile() {
            return commentUserProfile;
        }

        public void setCommentUserProfile(String commentUserProfile) {
            this.commentUserProfile = commentUserProfile;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getPostId() {
            return postId;
        }

        public void setPostId(int postId) {
            this.postId = postId;
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


    public static class PostPicturesBean {

        /**
         * picture : http://zunrong01.oss-cn-hangzhou.aliyuncs.com/2018/1/5a4f19a7d4a0c61740736a1c.jpg?Expires=1515203637&OSSAccessKeyId=LTAIVSRAAzGw4VvC&Signature=5nQA3Hwbpa5zJoWyteYzHhAOQ4A%3D
         * id : 1
         */

        private String picture;
        private int id;

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
