package org.wzeiri.zr.zrtaxiplatform.util;

import android.text.TextUtils;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.bean.BingCarInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author k-lm on 2017/12/14.
 */

public class CarHelper {
    private static final CarHelper ourInstance = new CarHelper();
    /**
     * 当前已绑定的车辆
     */
    private List<BingCarInfoBean.BindingCarBean> mBindingCarBeanList;
    /**
     * 当前正在申请的车辆
     */
    private List<BingCarInfoBean.BindingCarRequestBean> mBindingCarRequestBeanList;

    /**
     * 当前车辆id
     */
    private int mCurrentCarId = -1;
    /**
     * 当前已绑定的车辆信息
     */
    private List<CarInfo> mBindCarInfoList = new ArrayList<>();

    /**
     * 当前正在申请的车辆信息
     */
    private List<CarInfo> mAuditCarInfoList = new ArrayList<>();
    /**
     * 当前使用的车辆信息
     */
    public CarInfo mCurrentCarInfo;

    /**
     * 是否加载过车辆信息
     */
    private boolean mIsLoadCarInfo = false;

    public static CarHelper getInstance() {
        return ourInstance;
    }


    private CarHelper() {
    }

    /**
     * 保存车辆信息
     *
     * @param bingCarInfoBean
     */
    public void save(BingCarInfoBean bingCarInfoBean) {
        clear();
        if (bingCarInfoBean == null) {
            return;
        }
        mIsLoadCarInfo = true;

        mBindingCarBeanList = bingCarInfoBean.getBindingCarList();
        mBindingCarRequestBeanList = bingCarInfoBean.getBindingCarRequestList();

        setCurrentCarId(bingCarInfoBean.getCurrentCarId());
    }

    /**
     * 设置当前使用的车辆Id
     *
     * @param id
     */
    public void setCurrentCarId(int id) {
        if (mBindingCarBeanList == null || mBindingCarBeanList.size() == 0) {
            return;
        }

        for (BingCarInfoBean.BindingCarBean bean : mBindingCarBeanList) {
            if (bean.getId() != id) {
                continue;
            }
            mCurrentCarId = id;

            if (mCurrentCarInfo == null) {
                mCurrentCarInfo = new CarInfo();
            }

            mCurrentCarInfo.bindingCarRequestStatus = -1;
            mCurrentCarInfo.brandLogoPicture = bean.getBrandLogoPicture();
            mCurrentCarInfo.carModel = bean.getCarModel();
            mCurrentCarInfo.compnay = bean.getCompnay();
            mCurrentCarInfo.plateNumber = bean.getPlateNumber();
            mCurrentCarInfo.id = bean.getId();

            return;
        }


    }

    /**
     * 是否是当前车辆
     *
     * @param bindingCarBean
     * @return
     */
    public boolean isCurrentCar(BingCarInfoBean.BindingCarBean bindingCarBean) {
        if (bindingCarBean == null) {
            return false;
        }
        return bindingCarBean.getId() == mCurrentCarId;

    }

    /**
     * 是否是当前车辆
     *
     * @param carInfo
     * @return
     */
    public boolean isCurrentCar(CarInfo carInfo) {
        if (carInfo == null) {
            return false;
        }
        return carInfo.getId() == mCurrentCarId;
    }

    /**
     * 返回当前车辆id
     *
     * @return
     */
    public int getCurrentCarId() {
        return mCurrentCarId;
    }

    public CarInfo getCurrentCarInfo() {
        return mCurrentCarInfo;
    }

    /**
     * 根据车辆id 返回相应的车辆信息
     *
     * @param carId 车辆id
     * @return
     */
    public BingCarInfoBean.BindingCarBean getCarInfo(int carId) {
        if (mBindingCarBeanList == null || mBindingCarBeanList.size() == 0) {

            return null;
        }

        for (BingCarInfoBean.BindingCarBean carBean : mBindingCarBeanList) {
            if (carBean == null) {
                continue;
            }
            if (carId == carBean.getId()) {
                return carBean;
            }
        }
        return null;
    }

    /**
     * 根据车辆id 返回认证通过的车辆信息
     *
     * @param carId 车辆id
     * @return
     */
    public CarInfo getBindCarInfo(int carId) {
        if (mBindCarInfoList == null || mBindCarInfoList.size() == 0) {
            getBindCarInfoList();
        }

        if (mBindCarInfoList == null || mBindCarInfoList.size() == 0) {
            return null;
        }

        for (CarInfo carInfo : mBindCarInfoList) {
            if (carInfo == null) {
                continue;
            }
            if (carId == carInfo.getId()) {
                return carInfo;
            }
        }
        return null;
    }


    /**
     * 返回当前绑定的车辆
     *
     * @return
     */
    public List<CarInfo> getBindCarInfoList() {
        mBindCarInfoList.clear();
        for (BingCarInfoBean.BindingCarBean bean : mBindingCarBeanList) {
            if (bean == null) {
                continue;
            }

            CarInfo carInfo = new CarInfo();
            carInfo.bindingCarRequestStatus = -1;
            carInfo.brandLogoPicture = bean.getBrandLogoPicture();
            carInfo.carModel = bean.getCarModel();
            carInfo.compnay = bean.getCompnay();
            carInfo.plateNumber = bean.getPlateNumber();
            carInfo.id = bean.getId();
            mBindCarInfoList.add(carInfo);
        }
        return mBindCarInfoList;
    }

    /**
     * 返回正在审核的车辆数据
     *
     * @return
     */
    public List<CarInfo> getAuditCartInfoList() {
        mAuditCarInfoList.clear();
        for (BingCarInfoBean.BindingCarRequestBean bean : mBindingCarRequestBeanList) {
            if (bean == null) {
                continue;
            }

            CarInfo carInfo = new CarInfo();
            carInfo.bindingCarRequestStatus = bean.getBindingCarRequestStatus();
            carInfo.brandLogoPicture = bean.getBrandLogoPicture();
            carInfo.carModel = bean.getCarModel();
            carInfo.compnay = bean.getCompnay();
            carInfo.plateNumber = bean.getPlateNumber();
            carInfo.id = bean.getId();
            mAuditCarInfoList.add(carInfo);
        }
        return mAuditCarInfoList;
    }

    /**
     * 返回所有车辆信息
     *
     * @return
     */
    public List<CarInfo> getAllCarInfoList() {
        List<CarInfo> list = new ArrayList<>();
        list.addAll(getBindCarInfoList());
        list.addAll(getAuditCartInfoList());
        return list;

    }

    /**
     * 清空数据
     */
    public void clear() {
        mCurrentCarId = -1;
        if (mBindingCarBeanList != null) {
            mBindingCarBeanList.clear();
        }
        if (mBindingCarRequestBeanList != null) {
            mBindingCarRequestBeanList.clear();
        }
        if (mBindCarInfoList != null) {
            mBindCarInfoList.clear();
        }

        mIsLoadCarInfo = false;
    }

    /**
     * 是否加载过车辆信息
     *
     * @return
     */
    public boolean isLoadCarInfo() {
        return mIsLoadCarInfo;
    }


    public static class CarInfo {

        private String brandLogoPicture;
        private String plateNumber;
        private String carModel;
        private String compnay;
        /**
         * -1为已认证 , 0 为审核中
         */
        private int bindingCarRequestStatus = -1;
        private int id;


        private CarInfo() {

        }

        public String getBrandLogoPicture() {
            return brandLogoPicture;
        }

        public String getPlateNumber() {
            return plateNumber;
        }

        public String getCarModel() {
            return carModel;
        }

        public String getCompnay() {
            return compnay;
        }

        public int getBindingCarRequestStatus() {
            return bindingCarRequestStatus;
        }

        public int getId() {
            return id;
        }

        public String getStateStr() {
            return "";
        }


    }


}
