package org.wzeiri.zr.zrtaxiplatform.service;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import org.wzeiri.zr.zrtaxiplatform.util.PermissionsUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 百度定位服务
 *
 * @author k-lm on 2017/11/28.
 */

public class BaiduLocationService extends Service {

    public static final int REQUEST_CODE_LOCATION = 109;
    /**
     * 是否执行多次
     */
    public static final String MULTIPLE_EXECUTION = "multipleExecution";


    private static boolean isMany = false;

    private static LocationClient mLocationClient = null;
    /**
     * 详细地址
     */
    private static String mAddress;
    /**
     * 省份
     */
    private static String mProvince;
    /**
     * 城市
     */
    private static String mCity;
    /**
     * 区县
     */
    private static String mDistrict;
    /**
     * 街道
     */
    private static String mStreet;
    /**
     * 纬度
     */
    private static double mLatitude;
    /**
     * 经度
     */
    private static double mLongitude;


    private BDLocationListener mLocationListener;
    /**
     * 是否 定位失败
     */
    private static boolean mIsLocationError = false;


    private static Map<Object, OnLocationListener> mLocationListenerMap = new HashMap<>();
    private static Map<Object, OnUpLocationListener> mUpLocationListenerMap = new HashMap<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        initLocation();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return super.onStartCommand(intent, flags, startId);
        }
        isMany = intent.getBooleanExtra(MULTIPLE_EXECUTION, false);

        if (mLocationClient.isStarted()) {
            return START_STICKY;
        }

        // 允许多次执行或者 没有执行过则开启服务
        if (isMany || TextUtils.isEmpty(mAddress)) {
            mLocationClient.registerLocationListener(mLocationListener);
            mLocationClient.start();
        }

        return START_STICKY;
    }


    private void initLocation() {
        mLocationClient = new LocationClient(getApplicationContext());
        LocationClientOption option = new LocationClientOption();
        // 设置获取地址信息
        option.setIsNeedAddress(true);
        // 设置获取时间
        option.setScanSpan(4000);
        option.setCoorType("bd09ll");

        option.setEnableSimulateGps(true);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        mLocationClient.setLocOption(option);


        mLocationListener = new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                int type = location.getLocType();

                // 定位失败
                if (type == BDLocation.TypeNone ||
                        type == BDLocation.TypeNetWorkException ||
                        type == BDLocation.TypeServerError ||
                        type == BDLocation.TypeServerDecryptError ||
                        type == BDLocation.TypeServerCheckKeyError) {
                    locationError();
                    return;
                }
                mIsLocationError = false;

                String addr = location.getAddrStr();    //获取详细地址信息
                String province = location.getProvince();    //获取省份
                String city = location.getCity();    //获取城市
                String district = location.getDistrict();    //获取区县
                String street = location.getStreet();    //获取街道信息


                // 4.9e-324 是百度地图默认坐标，表示百度无法获取定位信息
                if (location.getLongitude() != 4.9e-324) {
                    mLongitude = location.getLongitude();
                }

                if (location.getLatitude() != 4.9e-324) {
                    mLatitude = location.getLatitude();
                }


                mAddress = addr;
                mProvince = province;
                mCity = city;
                mDistrict = district;
                mStreet = street;

                if (mLocationListenerMap.size() > 0) {
                    // 执行回调
                    for (OnLocationListener listener : mLocationListenerMap.values()) {
                        if (listener != null) {
                            listener.onReceiveLocation();
                        }
                    }
                    // 执行完后清空所有回调
                    mLocationListenerMap.clear();
                }

                if (mUpLocationListenerMap.size() > 0) {
                    for (OnLocationListener listener : mUpLocationListenerMap.values()) {
                        if (listener != null) {
                            listener.onReceiveLocation();
                        }
                    }
                }


                // 如果获取到信息则不再获取定位
                if (!TextUtils.isEmpty(mAddress) && !isMany) {
                    mLocationClient.unRegisterLocationListener(this);
                    mLocationClient.stop();
                }
            }
        };

        mLocationClient.registerLocationListener(mLocationListener);
    }


    /**
     * 返回详细地址
     *
     * @return 返回详细地址
     */
    public static String getAddress() {
        if (TextUtils.isEmpty(mAddress)) {
            return "";
        }
        return mAddress;
    }

    /**
     * 返回省
     *
     * @return 返回省
     */
    public static String getProvince() {
        if (TextUtils.isEmpty(mProvince)) {
            return "";
        }
        return mProvince;
    }

    /**
     * 停止定位服务
     */
    public static void stopLocation() {
        if (mLocationClient == null || !mLocationClient.isStarted()) {
            return;
        }
        mLocationClient.stop();
        isMany = false;
    }

    /**
     * 返回城市
     *
     * @return 返回城市
     */
    @NonNull
    public static String getCity() {
        if (TextUtils.isEmpty(mCity)) {
            return "";
        }
        return mCity;
    }

    /**
     * 返回区县
     *
     * @return 返回区县
     */
    @NonNull
    public static String getDistrict() {
        if (TextUtils.isEmpty(mDistrict)) {
            return "";
        }
        return mDistrict;
    }

    /**
     * 返回街道
     *
     * @return 返回街道
     */
    @NonNull
    public static String getStreet() {
        if (TextUtils.isEmpty(mStreet)) {
            return "";
        }
        return mStreet;
    }

    public static double getLatitude() {
        return mLatitude;
    }

    public static double getLongitude() {
        return mLongitude;
    }

    /**
     * 启动服务
     *
     * @param activity
     */
    public static void start(Activity activity) {
        start(activity, (OnLocationListener) null);
    }

    /**
     * 启动服务
     *
     * @param activity
     */
    public static void start(Activity activity, OnLocationListener listener) {
        BaiduLocationService.addOnLocationListener(activity, listener);
        if (!PermissionsUtil.isLocationPermissions(activity)) {
            String[] permissions = new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            };
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE_LOCATION);
            locationError();
            return;
        }
        Intent starter = new Intent(activity, BaiduLocationService.class);
        activity.startService(starter);
    }


    /**
     * 启动服务
     *
     * @param activity
     */
    public static void start(Activity activity, OnUpLocationListener listener) {
        BaiduLocationService.addOnLocationListener(activity, listener);
        if (!PermissionsUtil.isLocationPermissions(activity)) {
            String[] permissions = new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            };
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE_LOCATION);
            locationError();
            return;
        }
        Intent starter = new Intent(activity, BaiduLocationService.class);
        starter.putExtra(MULTIPLE_EXECUTION, true);
        activity.startService(starter);
    }

    /**
     * 添加服务回调
     *
     * @param activity
     * @param listener
     */
    public static void addOnLocationListener(Activity activity, OnLocationListener listener) {
        addOnLocationListener((Object) activity, listener);
    }

    /**
     * 添加定位服务回调
     *
     * @param fragment
     * @param listener
     */
    public static void addOnLocationListener(Fragment fragment, OnLocationListener listener) {
        addOnLocationListener((Object) fragment, listener);
    }

    /**
     * 添加定位服务回调
     *
     * @param object
     * @param listener
     */
    private static void addOnLocationListener(Object object, OnLocationListener listener) {
        // 如果已经获取到定位信息则不再添加,则是直接执行回调
        if (object == null || !TextUtils.isEmpty(mAddress)) {
            if (listener != null) {
                listener.onReceiveLocation();
            }
            return;
        }
        // 如果当前是定位失败直接调用error方法，在添加回调
        if (mIsLocationError) {
            if (listener != null) {
                listener.onError();
            }
        }

        if (listener == null) {
            mLocationListenerMap.remove(object);
            return;
        }
        mLocationListenerMap.put(object, listener);
    }


    /**
     * 添加服务回调
     *
     * @param activity
     * @param listener
     */
    public static void addOnLocationListener(Activity activity, OnUpLocationListener listener) {
        addOnLocationListener((Object) activity, listener);
    }

    /**
     * 添加定位服务回调
     *
     * @param fragment
     * @param listener
     */
    public static void addOnLocationListener(Fragment fragment, OnUpLocationListener listener) {
        addOnLocationListener((Object) fragment, listener);
    }

    /**
     * 添加服务回调
     *
     * @param activity
     */
    public static void removeOnLocationListener(Activity activity) {
        mLocationListenerMap.remove(activity);
    }

    /**
     * 添加定位服务回调
     *
     * @param fragment
     */
    public static void removeOnLocationListener(Fragment fragment) {
        mUpLocationListenerMap.remove(fragment);
    }

    /**
     * 添加定位服务回调
     *
     * @param object
     * @param listener
     */
    private static void addOnLocationListener(Object object, OnUpLocationListener listener) {
        // 如果已经获取到定位信息则不再添加,则是直接执行回调
        if (object == null) {
            return;
        }

        if (listener == null) {
            mUpLocationListenerMap.remove(object);
            return;
        }
        mUpLocationListenerMap.put(object, listener);
    }


    /**
     * 停止服务
     *
     * @param activity
     */
    public static void stop(Activity activity) {
        Intent intent = new Intent(activity, BaiduLocationService.class);
        activity.stopService(intent);
    }


    /**
     * 权限回调
     */
    public static void onRequestPermissionsResult(Activity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissions.length > 0 &&
                !PermissionsUtil.lacksPermissions(activity, permissions) &&
                requestCode == REQUEST_CODE_LOCATION) {
            start(activity);
        } else {
            mLocationListenerMap.clear();
            mUpLocationListenerMap.clear();
        }

    }

    /**
     * 定位失败
     */
    private static void locationError() {
        if (mIsLocationError) {
            return;
        }
        mIsLocationError = true;
        for (OnLocationListener listener : mLocationListenerMap.values()) {
            if (listener == null) {
                continue;
            }
            listener.onError();
        }

        for (OnUpLocationListener listener : mUpLocationListenerMap.values()) {
            if (listener == null) {
                continue;
            }
            listener.onError();
        }


    }

    @Override
    public void onDestroy() {
        // 关闭百度定位
        mLocationClient.unRegisterLocationListener(mLocationListener);
        mLocationClient.stop();
        super.onDestroy();
    }


    /**
     * 定位接口，回调接口只执行一次
     */
    public interface OnLocationListener {
        void onReceiveLocation();

        void onError();
    }

    /**
     * 定位接口，回调会执行多次
     */
    public interface OnUpLocationListener extends OnLocationListener {
    }


}
