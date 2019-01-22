package org.wzeiri.zr.zrtaxiplatform.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author k-lm on 2017/12/26.
 */

public class TenantBean implements Parcelable {


    /**
     * tenancyName : string
     * name : string
     * provinceCode : string
     * provinceName : string
     * cityCode : string
     * cityName : string
     * id : 0
     */

    private String tenancyName;
    private String name;
    private String provinceCode;
    private String provinceName;
    private String cityCode;
    private String cityName;
    private String cityLetter;
    private String plateNumberPrefix;
    private int id;

    public String getTenancyName() {
        return tenancyName;
    }

    public void setTenancyName(String tenancyName) {
        this.tenancyName = tenancyName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityLetter() {
        return cityLetter;
    }

    public void setCityLetter(String cityLetter) {
        this.cityLetter = cityLetter;
    }

    public String getPlateNumberPrefix() {
        return plateNumberPrefix;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tenancyName);
        dest.writeString(this.name);
        dest.writeString(this.provinceCode);
        dest.writeString(this.provinceName);
        dest.writeString(this.cityCode);
        dest.writeString(this.cityName);
        dest.writeString(this.cityLetter);
        dest.writeString(this.plateNumberPrefix);
        dest.writeInt(this.id);
    }

    public TenantBean() {
    }

    protected TenantBean(Parcel in) {
        this.tenancyName = in.readString();
        this.name = in.readString();
        this.provinceCode = in.readString();
        this.provinceName = in.readString();
        this.cityCode = in.readString();
        this.cityName = in.readString();
        this.cityLetter = in.readString();
        this.plateNumberPrefix = in.readString();
        this.id = in.readInt();
    }

    public static final Creator<TenantBean> CREATOR = new Creator<TenantBean>() {
        @Override
        public TenantBean createFromParcel(Parcel source) {
            return new TenantBean(source);
        }

        @Override
        public TenantBean[] newArray(int size) {
            return new TenantBean[size];
        }
    };
}
