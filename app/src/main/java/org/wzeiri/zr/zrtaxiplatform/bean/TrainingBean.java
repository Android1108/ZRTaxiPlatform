package org.wzeiri.zr.zrtaxiplatform.bean;

import java.util.Date;

/**
 * @author k-lm on 2017/12/28.
 */

public class TrainingBean {


    /**
     * title : string
     * coverPicture : string
     * trainingTime : 2017-12-28T11:26:40.708Z
     * trainingAddress : string
     * creationTime : 2017-12-28T11:26:40.708Z
     */
    private int id;
    private String title;
    private String coverPicture;
    private String trainingTime;
    private String trainingAddress;
    private Date creationTime;
    private String source;

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

    public String getTrainingTime() {
        return trainingTime;
    }

    public void setTrainingTime(String trainingTime) {
        this.trainingTime = trainingTime;
    }

    public String getTrainingAddress() {
        return trainingAddress;
    }

    public void setTrainingAddress(String trainingAddress) {
        this.trainingAddress = trainingAddress;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
