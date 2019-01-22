package org.wzeiri.zr.zrtaxiplatform.bean;


import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * @author k-lm on 2017/12/13.
 */

public class WalletDetailBean implements Parcelable {

    private BigDecimal walletBalance;
    private String zhifubaoAccountOwner;
    private String zhifubaoAccount;
    private String bankCardOwner;
    private String bankCardNo;
    private String bankName;
    private String bankCardBindPhoneNumber;

    public WalletDetailBean() {
    }


    public BigDecimal getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(BigDecimal walletBalance) {
        this.walletBalance = walletBalance;
    }

    public String getZhifubaoAccountOwner() {
        return zhifubaoAccountOwner;
    }

    public void setZhifubaoAccountOwner(String zhifubaoAccountOwner) {
        this.zhifubaoAccountOwner = zhifubaoAccountOwner;
    }

    public String getZhifubaoAccount() {
        return zhifubaoAccount;
    }

    public void setZhifubaoAccount(String zhifubaoAccount) {
        this.zhifubaoAccount = zhifubaoAccount;
    }

    public String getBankCardOwner() {
        return bankCardOwner;
    }

    public void setBankCardOwner(String bankCardOwner) {
        this.bankCardOwner = bankCardOwner;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCardBindPhoneNumber() {
        return bankCardBindPhoneNumber;
    }

    public void setBankCardBindPhoneNumber(String bankCardBindPhoneNumber) {
        this.bankCardBindPhoneNumber = bankCardBindPhoneNumber;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.walletBalance);
        dest.writeString(this.zhifubaoAccountOwner);
        dest.writeString(this.zhifubaoAccount);
        dest.writeString(this.bankCardOwner);
        dest.writeString(this.bankCardNo);
        dest.writeString(this.bankName);
        dest.writeString(this.bankCardBindPhoneNumber);
    }

    protected WalletDetailBean(Parcel in) {
        this.walletBalance = (BigDecimal) in.readSerializable();
        this.zhifubaoAccountOwner = in.readString();
        this.zhifubaoAccount = in.readString();
        this.bankCardOwner = in.readString();
        this.bankCardNo = in.readString();
        this.bankName = in.readString();
        this.bankCardBindPhoneNumber = in.readString();
    }

    public static final Creator<WalletDetailBean> CREATOR = new Creator<WalletDetailBean>() {
        @Override
        public WalletDetailBean createFromParcel(Parcel source) {
            return new WalletDetailBean(source);
        }

        @Override
        public WalletDetailBean[] newArray(int size) {
            return new WalletDetailBean[size];
        }
    };
}
