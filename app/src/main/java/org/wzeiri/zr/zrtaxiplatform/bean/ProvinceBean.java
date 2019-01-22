package org.wzeiri.zr.zrtaxiplatform.bean;

/**
 * @author k-lm on 2017/12/22.
 */

public class ProvinceBean {

    /**
     * name : string
     * code : string
     * parentCode : string
     * regionLevel : 0
     */

    private String name;
    private String code;
    private String parentCode;
    private int regionLevel;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public int getRegionLevel() {
        return regionLevel;
    }

    public void setRegionLevel(int regionLevel) {
        this.regionLevel = regionLevel;
    }
}
