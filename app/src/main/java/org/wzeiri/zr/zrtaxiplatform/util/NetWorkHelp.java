package org.wzeiri.zr.zrtaxiplatform.util;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import java.util.HashMap;
import java.util.Map;

/**
 * @author k-lm on 2017/12/25.
 */

public class NetWorkHelp {
    private static NetWorkHelp mInstance;

    private int mCurrentType;

    /**
     * 没有连接网络
     */
    public static final int NETWORK_NONE = -1;
    /**
     * 移动网络
     */
    public static final int NETWORK_MOBILE = 0;
    /**
     * 无线网络
     */
    public static final int NETWORK_WIFI = 1;


    private WIFiStartBroadcastReceiver mWiFiReceiver;

    private Map<Object, OnNetworkChangeListener> mNetworkChangeListenerMa = new HashMap<>();

    private NetWorkHelp() {

    }

    public static NetWorkHelp getInstance() {

        if (mInstance == null) {
            mInstance = new NetWorkHelp();
        }
        return mInstance;
    }

    /**
     * 判断是否有网络连接
     *
     * @param context
     * @return
     */
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断当前是否wifi
     *
     * @return
     */

    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * 判断移动网络是否可用
     *
     * @param context
     * @return
     */
    public boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static int getNetWorkState(Context context) {
        // 得到连接管理器对象
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {

            if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                return NETWORK_WIFI;
            } else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
                return NETWORK_MOBILE;
            }
        } else {
            return NETWORK_NONE;
        }
        return NETWORK_NONE;
    }


    /**
     * 监听wifi变化，广播
     *
     * @param context
     */
    public void registerReceiver(Context context) {
        if (mWiFiReceiver == null) {
            mWiFiReceiver = new WIFiStartBroadcastReceiver();
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        context.registerReceiver(mWiFiReceiver, filter);
    }

    /**
     * 解除监听wifi变化广播
     *
     * @param context
     */
    public void unregisterReceiver(Context context) {
        if (mWiFiReceiver == null) {
            return;
        }
        context.unregisterReceiver(mWiFiReceiver);
    }

    /**
     * 添加网络状态监听
     *
     * @param activity
     * @param listener
     */
    public void addOnNetworkChangeListener(Activity activity, OnNetworkChangeListener listener) {
        addOnNetworkChangeListener((Object) activity, listener);
    }

    /**
     * 添加网络状态监听
     *
     * @param fragment
     * @param listener
     */
    public void addOnNetworkChangeListener(Fragment fragment, OnNetworkChangeListener listener) {
        addOnNetworkChangeListener((Object) fragment, listener);
    }

    /**
     * 添加网络状态监听
     *
     * @param object
     * @param listener
     */
    private void addOnNetworkChangeListener(Object object, OnNetworkChangeListener listener) {
        if (object == null) {
            return;
        }

        if (listener == null) {
            mNetworkChangeListenerMa.remove(object);
            return;
        }

        mNetworkChangeListenerMa.put(object, listener);
    }

    /**
     * 移除网络状态监听
     *
     * @param activity
     */
    public void removeOnNetworkChangeListener(Activity activity) {
        removeOnNetworkChangeListener((Object) activity);
    }

    /**
     * 移除网络状态监听
     *
     * @param fragment
     */
    public void removeOnNetworkChangeListener(Fragment fragment) {
        removeOnNetworkChangeListener((Object) fragment);
    }

    /**
     * 移除网络状态监听
     *
     * @param object
     */
    private void removeOnNetworkChangeListener(Object object) {
        if (object == null) {
            return;
        }
        mNetworkChangeListenerMa.remove(object);
    }


    public class WIFiStartBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 如果相等的话就说明网络状态发生了变化

            // 监听wifi的开关
            /*if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            }*/

            // 监听wifi是否连接上一个有效的网络
            if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                Parcelable parcelableExtra = intent
                        .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (null != parcelableExtra) {
                    NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                    NetworkInfo.State state = networkInfo.getState();
                    boolean isConnected = state == NetworkInfo.State.CONNECTED;// 当然，这边可以更精确的确定状态
                    if (isConnected) {
                        onConnectWIFi();
                        return;
                    }
                }

            }
            //这个监听网络连接的设置，包括wifi和移动数据的打开和关闭。.
            //这个广播的最大弊端是比上边两个广播的反应要慢
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                int netWorkState = getNetWorkState(context);
                // 接口回调传过去状态的类型
                if (netWorkState == NETWORK_NONE) {
                    onDisconnectNetwork();
                } else if (netWorkState == NETWORK_MOBILE) {
                    onConnectMobileNetwork();
                } else if (netWorkState == NETWORK_WIFI) {
                    onConnectWIFi();
                }
            }
        }
    }

    /**
     * 连接wifi
     */
    private void onConnectWIFi() {
        // 如果当前已经是wifi的情况下不再执行回调
        // 不判断则有可能执行两次回调
        if (mCurrentType == NETWORK_WIFI) {
            return;
        }
        mCurrentType = NETWORK_WIFI;

        if (mNetworkChangeListenerMa.size() == 0) {
            return;
        }

        for (OnNetworkChangeListener listener : mNetworkChangeListenerMa.values()) {
            if (listener == null) {
                continue;
            }
            listener.onConnectWIFi();
        }
    }

    /**
     * 连接移动网络
     */
    private void onConnectMobileNetwork() {
        if (mCurrentType == NETWORK_MOBILE) {
            return;
        }
        mCurrentType = NETWORK_MOBILE;
        if (mNetworkChangeListenerMa.size() == 0) {
            return;
        }

        for (OnNetworkChangeListener listener : mNetworkChangeListenerMa.values()) {
            if (listener == null) {
                continue;
            }
            listener.onConnectMobileNetwork();
        }
    }

    /**
     * 断开网络
     */
    private void onDisconnectNetwork() {
        if (mCurrentType == NETWORK_NONE) {
            return;
        }
        mCurrentType = NETWORK_NONE;
        if (mNetworkChangeListenerMa.size() == 0) {
            return;
        }

        for (OnNetworkChangeListener listener : mNetworkChangeListenerMa.values()) {
            if (listener == null) {
                continue;
            }
            listener.onDisconnectNetwork();
        }
    }

    public interface OnNetworkChangeListener {
        /**
         * 连接上wifi
         */
        void onConnectWIFi();

        /**
         * 连接移动网络
         */
        void onConnectMobileNetwork();

        /**
         * 断开网络
         */
        void onDisconnectNetwork();


    }


}
