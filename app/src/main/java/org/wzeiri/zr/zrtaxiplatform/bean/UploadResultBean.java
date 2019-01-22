package org.wzeiri.zr.zrtaxiplatform.bean;

/**
 * @author k-lm on 2017/12/22.
 */

public class UploadResultBean {


    /**
     * sourceName : 下载.png
     * fileName : 5a3c950ed4a0c61bd4d7ceaf.png
     * url : http://localhost:62114//Temp/TempFile/5a3c950ed4a0c61bd4d7ceaf.png
     */

    private String sourceName;
    private String fileName;
    private String url;

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
