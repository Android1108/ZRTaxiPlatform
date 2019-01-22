package org.wzeiri.zr.zrtaxiplatform.bean;

import java.util.List;

/**
 * @author k-lm on 2017/12/29.
 */

public class HomeIndexBean {


    /**
     * tenant : {"tenancyName":"string","name":"string","provinceCode":"string","provinceName":"string","cityCode":"string","cityName":"string","cityLetter":"string","id":0}
     * announcements : [{"title":"string","id":0}]
     * car : {"plateNumber":"string","id":0}
     * adver : {"adverType":0,"coverPicture":"string","linkUrl":"string","content":"string","id":0}
     */

    private TenantBean tenant;
    private CarBean car;
    private AdverBean adver;
    private List<AnnouncementsBean> announcements;
    private int driverStatus;

    public TenantBean getTenant() {
        return tenant;
    }

    public void setTenant(TenantBean tenant) {
        this.tenant = tenant;
    }

    public CarBean getCar() {
        return car;
    }

    public void setCar(CarBean car) {
        this.car = car;
    }

    public AdverBean getAdver() {
        return adver;
    }

    public void setAdver(AdverBean adver) {
        this.adver = adver;
    }

    public List<AnnouncementsBean> getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(List<AnnouncementsBean> announcements) {
        this.announcements = announcements;
    }

    public int getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(int driverStatus) {
        this.driverStatus = driverStatus;
    }

    public static class CarBean {
        /**
         * plateNumber : string
         * id : 0
         */

        private String plateNumber;
        private int id;
        private String compnay;
        private String carModel;
        private String qrCodeUrl;
        private String brandLogoPicture;
        private Boolean online;

        public String getCompnay() {
            return compnay;
        }

        public void setCompnay(String compnay) {
            this.compnay = compnay;
        }

        public String getCarModel() {
            return carModel;
        }

        public void setCarModel(String carModel) {
            this.carModel = carModel;
        }

        public String getQrCodeUrl() {
            return qrCodeUrl;
        }

        public void setQrCodeUrl(String qrCodeUrl) {
            this.qrCodeUrl = qrCodeUrl;
        }

        public String getBrandLogoPicture() {
            return brandLogoPicture;
        }

        public void setBrandLogoPicture(String brandLogoPicture) {
            this.brandLogoPicture = brandLogoPicture;
        }

        public Boolean getOnline() {
            return online;
        }

        public void setOnline(Boolean online) {
            this.online = online;
        }

        public String getPlateNumber() {
            return plateNumber;
        }

        public void setPlateNumber(String plateNumber) {
            this.plateNumber = plateNumber;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class AdverBean {
        /**
         * adverType : 0
         * coverPicture : string
         * linkUrl : string
         * content : string
         * id : 0
         */

        private int adverType;
        private String coverPicture;
        private String linkUrl;
        private String content;
        private int id;

        public int getAdverType() {
            return adverType;
        }

        public void setAdverType(int adverType) {
            this.adverType = adverType;
        }

        public String getCoverPicture() {
            return coverPicture;
        }

        public void setCoverPicture(String coverPicture) {
            this.coverPicture = coverPicture;
        }

        public String getLinkUrl() {
            return linkUrl;
        }

        public void setLinkUrl(String linkUrl) {
            this.linkUrl = linkUrl;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class AnnouncementsBean {
        /**
         * title : string
         * id : 0
         */

        private String title;
        private int id;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
