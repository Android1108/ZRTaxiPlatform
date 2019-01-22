package org.wzeiri.zr.zrtaxiplatform.bean;

import java.util.Date;

/**
 * @author k-lm on 2017/12/28.
 */

public class ArticleDetailBean {

    /**
     * title : string
     * coverPicture : string
     * content : string
     * source : string
     * creationTime : 2017-12-28T11:26:40.721Z
     * id : 0
     */

    private String title;
    private String coverPicture;
    private String content;
    private String source;
    private Date creationTime;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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
