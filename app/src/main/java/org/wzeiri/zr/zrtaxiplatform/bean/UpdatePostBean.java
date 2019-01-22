package org.wzeiri.zr.zrtaxiplatform.bean;

import java.util.List;

/**
 * @author k-lm on 2018/1/8.
 */

public class UpdatePostBean {


    /**
     * title : string
     * sectionId : 0
     * content : string
     * pictures : ["string"]
     * deletePictures : [0]
     * id : 0
     */

    private String title;
    private int sectionId;
    private String content;
    private int id;
    private List<String> pictures;
    private List<Integer> deletePictures;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public List<Integer> getDeletePictures() {
        return deletePictures;
    }

    public void setDeletePictures(List<Integer> deletePictures) {
        this.deletePictures = deletePictures;
    }
}
