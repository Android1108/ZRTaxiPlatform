package org.wzeiri.zr.zrtaxiplatform.bean;

import java.util.List;

/**
 * @author k-lm on 2017/12/14.
 */

public class BingCarInfoBean {


    /**
     * bindingCarList : [{"brandLogoPicture":"string","plateNumber":"string","carModel":"string","compnay":"string","id":0}]
     * bindingCarRequestList : [{"brandLogoPicture":"string","plateNumber":"string","carModel":"string","compnay":"string","bindingCarRequestStatus":"created","id":0}]
     * currentCarId : 0
     */

    private int currentCarId;
    private List<BindingCarBean> bindingCarList;
    private List<BindingCarRequestBean> bindingCarRequestList;

    public int getCurrentCarId() {
        return currentCarId;
    }

    public void setCurrentCarId(int currentCarId) {
        this.currentCarId = currentCarId;
    }

    public List<BindingCarBean> getBindingCarList() {
        return bindingCarList;
    }

    public void setBindingCarList(List<BindingCarBean> bindingCarList) {
        this.bindingCarList = bindingCarList;
    }

    public List<BindingCarRequestBean> getBindingCarRequestList() {
        return bindingCarRequestList;
    }

    public void setBindingCarRequestList(List<BindingCarRequestBean> bindingCarRequestList) {
        this.bindingCarRequestList = bindingCarRequestList;
    }

    public static class BindingCarBean {
        /**
         * brandLogoPicture : string
         * plateNumber : string
         * carModel : string
         * compnay : string
         * id : 0
         */

        private String brandLogoPicture;
        private String plateNumber;
        private String carModel;
        private String compnay;
        private Boolean online;
        private String qrCodeUrl;
        private int id;

        public String getQrCodeUrl() {
            return qrCodeUrl;
        }

        public void setQrCodeUrl(String qrCodeUrl) {
            this.qrCodeUrl = qrCodeUrl;
        }

        public Boolean getOnline() {
            return online;
        }

        public void setOnline(Boolean online) {
            this.online = online;
        }

        public String getBrandLogoPicture() {
            return brandLogoPicture;
        }

        public void setBrandLogoPicture(String brandLogoPicture) {
            this.brandLogoPicture = brandLogoPicture;
        }

        public String getPlateNumber() {
            return plateNumber;
        }

        public void setPlateNumber(String plateNumber) {
            this.plateNumber = plateNumber;
        }

        public String getCarModel() {
            return carModel;
        }

        public void setCarModel(String carModel) {
            this.carModel = carModel;
        }

        public String getCompnay() {
            return compnay;
        }

        public void setCompnay(String compnay) {
            this.compnay = compnay;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class BindingCarRequestBean {
        /**
         * brandLogoPicture : string
         * plateNumber : string
         * carModel : string
         * compnay : string
         * bindingCarRequestStatus : created
         * id : 0
         */

        private String brandLogoPicture;
        private String plateNumber;
        private String carModel;
        private String compnay;
        private int bindingCarRequestStatus;
        private int id;

        public String getBrandLogoPicture() {
            return brandLogoPicture;
        }

        public void setBrandLogoPicture(String brandLogoPicture) {
            this.brandLogoPicture = brandLogoPicture;
        }

        public String getPlateNumber() {
            return plateNumber;
        }

        public void setPlateNumber(String plateNumber) {
            this.plateNumber = plateNumber;
        }

        public String getCarModel() {
            return carModel;
        }

        public void setCarModel(String carModel) {
            this.carModel = carModel;
        }

        public String getCompnay() {
            return compnay;
        }

        public void setCompnay(String compnay) {
            this.compnay = compnay;
        }

        public int getBindingCarRequestStatus() {
            return bindingCarRequestStatus;
        }

        public void setBindingCarRequestStatus(int bindingCarRequestStatus) {
            this.bindingCarRequestStatus = bindingCarRequestStatus;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
