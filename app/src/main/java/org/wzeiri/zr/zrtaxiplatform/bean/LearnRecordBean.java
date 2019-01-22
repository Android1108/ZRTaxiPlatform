package org.wzeiri.zr.zrtaxiplatform.bean;

import java.util.Date;

/**
 * @author k-lm on 2017/12/29.
 */

public class LearnRecordBean {


    /**
     * learnVedioId : 0
     * coverPicture : string
     * creationTime : 2017-12-29T08:52:45.310Z
     * id : 0
     */

    private int learnVedioId;
    private String coverPicture;
    private Date creationTime;
    private int id;
    private String title;

    public int getLearnVedioId() {
        return learnVedioId;
    }

    public void setLearnVedioId(int learnVedioId) {
        this.learnVedioId = learnVedioId;
    }

    public String getCoverPicture() {
        return coverPicture;
    }

    public void setCoverPicture(String coverPicture) {
        this.coverPicture = coverPicture;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
