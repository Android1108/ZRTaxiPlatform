package org.wzeiri.zr.zrtaxiplatform.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author k-lm on 2017/12/14.
 */

public class OilCardBean implements Parcelable {

    /**
     * oilCardType : 0
     * oilCardTypeName : string
     * oilCardNumber : string
     * id : 0
     */

    private int oilCardType;
    private String oilCardTypeName;
    private String oilCardNumber;
    private int id;

    public OilCardBean() {

    }

    protected OilCardBean(Parcel in) {
        oilCardType = in.readInt();
        oilCardTypeName = in.readString();
        oilCardNumber = in.readString();
        id = in.readInt();
    }

    public static final Creator<OilCardBean> CREATOR = new Creator<OilCardBean>() {
        @Override
        public OilCardBean createFromParcel(Parcel in) {
            return new OilCardBean(in);
        }

        @Override
        public OilCardBean[] newArray(int size) {
            return new OilCardBean[size];
        }
    };

    public int getOilCardType() {
        return oilCardType;
    }

    public void setOilCardType(int oilCardType) {
        this.oilCardType = oilCardType;
    }

    public String getOilCardTypeName() {
        return oilCardTypeName;
    }

    public void setOilCardTypeName(String oilCardTypeName) {
        this.oilCardTypeName = oilCardTypeName;
    }

    public String getOilCardNumber() {
        return oilCardNumber;
    }

    public void setOilCardNumber(String oilCardNumber) {
        this.oilCardNumber = oilCardNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(oilCardType);
        dest.writeString(oilCardTypeName);
        dest.writeString(oilCardNumber);
        dest.writeInt(id);
    }
}
