package org.wzeiri.zr.zrtaxiplatform.bean;

/**
 * @author k-lm on 2017/12/22.
 */

public  class LearnVideoBean {

    /**
     * title : string
     * coverPicture : string
     * vedioUrl : string
     * isLearned : true
     * learnedTime : 2017-12-29T00:34:52.274Z
     * id : 0
     */

    private String title;
    private String coverPicture;
    private String vedioUrl;
    private boolean isLearned;
    private String learnedTime;
    private int id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverPicture() {
        return coverPicture;
    }

    public void setCoverPicture(String coverPicture) {
        this.coverPicture = coverPicture;
    }

    public String getVedioUrl() {
        return vedioUrl;
    }

    public void setVedioUrl(String vedioUrl) {
        this.vedioUrl = vedioUrl;
    }

    public boolean isIsLearned() {
        return isLearned;
    }

    public void setIsLearned(boolean isLearned) {
        this.isLearned = isLearned;
    }

    public String getLearnedTime() {
        return learnedTime;
    }

    public void setLearnedTime(String learnedTime) {
        this.learnedTime = learnedTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

