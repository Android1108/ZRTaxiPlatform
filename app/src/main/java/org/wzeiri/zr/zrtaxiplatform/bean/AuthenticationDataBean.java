package org.wzeiri.zr.zrtaxiplatform.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author k-lm on 2017/12/21.
 */

public class AuthenticationDataBean implements Parcelable {


    /**
     * name : string
     * phoneNumber : string
     * lisenceNumber : string
     * plateNumber : string
     * brand : string
     * brandId : 0
     * carModel : string
     * color : string
     * colorId : 0
     * company : string
     * companyId : 0
     * carLisenceNumber : string
     * tenantId : 0
     */

    private String name;
    private String phoneNumber;
    private String lisenceNumber;
    private String plateNumber;
    private String brand;
    private int brandId;
    private String carModel;
    private String color;
    private int colorId;
    private String company;
    private int companyId;
    private String carLisenceNumber;
    private int tenantId;
    private String idCardNumber;
    private String tenant;
    private String lisencePicture;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLisenceNumber() {
        return lisenceNumber;
    }

    public void setLisenceNumber(String lisenceNumber) {
        this.lisenceNumber = lisenceNumber;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getCarLisenceNumber() {
        return carLisenceNumber;
    }

    public void setCarLisenceNumber(String carLisenceNumber) {
        this.carLisenceNumber = carLisenceNumber;
    }

    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getLisencePicture() {
        return lisencePicture;
    }

    public void setLisencePicture(String lisencePicture) {
        this.lisencePicture = lisencePicture;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.lisenceNumber);
        dest.writeString(this.plateNumber);
        dest.writeString(this.brand);
        dest.writeInt(this.brandId);
        dest.writeString(this.carModel);
        dest.writeString(this.color);
        dest.writeInt(this.colorId);
        dest.writeString(this.company);
        dest.writeInt(this.companyId);
        dest.writeString(this.carLisenceNumber);
        dest.writeInt(this.tenantId);
        dest.writeString(this.idCardNumber);
        dest.writeString(this.tenant);
        dest.writeString(this.lisencePicture);
    }

    public AuthenticationDataBean() {
    }

    protected AuthenticationDataBean(Parcel in) {
        this.name = in.readString();
        this.phoneNumber = in.readString();
        this.lisenceNumber = in.readString();
        this.plateNumber = in.readString();
        this.brand = in.readString();
        this.brandId = in.readInt();
        this.carModel = in.readString();
        this.color = in.readString();
        this.colorId = in.readInt();
        this.company = in.readString();
        this.companyId = in.readInt();
        this.carLisenceNumber = in.readString();
        this.tenantId = in.readInt();
        this.idCardNumber = in.readString();
        this.tenant = in.readString();
        this.lisencePicture = in.readString();
    }

    public static final Creator<AuthenticationDataBean> CREATOR = new Creator<AuthenticationDataBean>() {
        @Override
        public AuthenticationDataBean createFromParcel(Parcel source) {
            return new AuthenticationDataBean(source);
        }

        @Override
        public AuthenticationDataBean[] newArray(int size) {
            return new AuthenticationDataBean[size];
        }
    };
}
