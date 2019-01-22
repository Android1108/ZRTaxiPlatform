package org.wzeiri.zr.zrtaxiplatform.bean;

/**
 * @author k-lm on 2017/12/22.
 */

public class VideoDetailBean {

    /**
     * title : string
     * coverPicture : string
     * vedioUrl : string
     * id : 0
     */

    private String title;
    private String coverPicture;
    private String vedioUrl;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
