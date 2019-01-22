package org.wzeiri.zr.zrtaxiplatform.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author k-lm on 2018/3/5.
 */

public class DeviceVehicleBean implements Parcelable {


    /**
     * name : string
     * lisenceNumber : string
     * plateNumber : string
     * brand : string
     * brandId : 0
     * carModel : string
     * company : string
     * companyId : 0
     */

    private String name;
    private String lisenceNumber;
    private String plateNumber;
    private String brand;
    private int brandId;
    private String carModel;
    private String company;
    private int companyId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.lisenceNumber);
        dest.writeString(this.plateNumber);
        dest.writeString(this.brand);
        dest.writeInt(this.brandId);
        dest.writeString(this.carModel);
        dest.writeString(this.company);
        dest.writeInt(this.companyId);
    }

    public DeviceVehicleBean() {
    }

    protected DeviceVehicleBean(Parcel in) {
        this.name = in.readString();
        this.lisenceNumber = in.readString();
        this.plateNumber = in.readString();
        this.brand = in.readString();
        this.brandId = in.readInt();
        this.carModel = in.readString();
        this.company = in.readString();
        this.companyId = in.readInt();
    }

    public static final Parcelable.Creator<DeviceVehicleBean> CREATOR = new Parcelable.Creator<DeviceVehicleBean>() {
        @Override
        public DeviceVehicleBean createFromParcel(Parcel source) {
            return new DeviceVehicleBean(source);
        }

        @Override
        public DeviceVehicleBean[] newArray(int size) {
            return new DeviceVehicleBean[size];
        }
    };
}
