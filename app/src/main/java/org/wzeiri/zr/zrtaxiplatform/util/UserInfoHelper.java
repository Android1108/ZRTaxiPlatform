package org.wzeiri.zr.zrtaxiplatform.util;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import org.wzeiri.zr.zrtaxiplatform.MyApplication;
import org.wzeiri.zr.zrtaxiplatform.bean.LoginBean;
import org.wzeiri.zr.zrtaxiplatform.bean.TenantBean;
import org.wzeiri.zr.zrtaxiplatform.bean.UserInfoBean;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.AnswerActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.LoginActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.base.BaseFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * @author klm on 2017/9/27.
 */

public class UserInfoHelper {
    private static final UserInfoHelper ourInstance = new UserInfoHelper();

    private final String SAVE_USER_ID_KEY = "key_user_id";
    private final String SAVE_ACCESS_TOKEN_KEY = "key_access_token";
    private final String SAVE_ENCRYPTED_ACCESSTO_KEY = "key_encrypted_accessto";
    private final String SAVE_INVALID_TIME_KEY = "key_invalid_time";
    private final String SAVE_LOGIN_TIME_KEY = "key_login_time";
    private final String SAVE_DRIVER_STATUS_KEY = "key_driver_status";

    private final String SAVE_NAME_KEY = "key_name";
    private final String SAVE_BASE_QR_CODE = "key_base_qr_code ";
    private final String SAVE_PROFILE_PICTURE_KEY = "key_profile_picture";
    private final String SAVE_PHONE_KEY = "key_phone";

    private final String SAVE_PLATE_NUMBER_PREFIX_KEY = "key_plate_number_prefix";

    private final String SAVE_BINDING_CODE_KEY = "key_binding_code";

    private final String SAVE_AUTHENTICATION_TENANT_ID_KEY = "key_authentication_tenant_id";

    private final String SAVE_STAR_MONTH_KEY = "key_star_of_the_month";

    /**
     * 用户id
     */
    private int mUserId;
    /**
     * token
     */
    private String mAccessToken = "";
    /**
     * 加密后的AccessToken(Signalr连接Token) ,
     */
    private String mEncryptedAccessToken;
    /**
     * 失效时间 ， 单位为s
     */
    private int mInvalidTime;
    /**
     * 登录时间
     */
    private long mLoginTime;
    /**
     * 用户名
     */
    private String mName = "";
    /**
     * 用户头像地址
     */
    private String mProfilePicture = "";
    /**
     * 二维码base64
     */
    private String mBase64QrCode = "";
    /**
     * 当前车牌号
     */
    private String mCarLisenceNumber = "";

    /**
     * 当前手机号
     */
    private String mPhone = "";
    /**
     * 当前认证状态
     */
    private int mDriverStatus = -1;
    /**
     * 车牌号前缀
     */
    private String mPlateNumberPrefix = "";
    /**
     * 是否绑定二维码
     */
    private boolean mIsBindCode = false;
    /**
     * 认证的地区id
     */
    private int mTenantId = -1;
    /**
     * 当前选择的地区id
     */
    private int mCurrentTenantId = -1;
    /**
     * 每月之星数量
     */
    private int mArtyStarTimes = 0;

    /**
     * 未认证
     */
    public static final int UN_AUTHENTICATION = 1;
    /**
     * 审核中
     */
    public static final int AUTHENTICATION_AUDIT = 2;
    /**
     * 认证成功
     */
    public static final int AUTHENTICATION_CERTIFIED = 4;
    /**
     * 认证失败
     */
    public static final int AUTHENTICATION_ERROR = 8;

    private String mBankCardNo ="";

    private String mUrl="";

    private String mBindCode="";

    private Map<Object, OnUserStateListener> mListenerMap = new HashMap<>();
    private Map<Object, OnChangeCityListener> mChangeCityMap = new HashMap<>();
    private Map<Object, OnAuthenticationChangeListener> mAuthenticationCityMap = new HashMap<>();

    public static UserInfoHelper getInstance() {
        return ourInstance;
    }

    private UserInfoHelper() {
        mUserId = SharedPreferencesUtil.getInt(MyApplication.getApplication(), SAVE_USER_ID_KEY, -1);
        mAccessToken = SharedPreferencesUtil.getString(MyApplication.getApplication(), SAVE_ACCESS_TOKEN_KEY, "");
        mEncryptedAccessToken = SharedPreferencesUtil.getString(MyApplication.getApplication(), SAVE_ENCRYPTED_ACCESSTO_KEY, "");
        mInvalidTime = SharedPreferencesUtil.getInt(MyApplication.getApplication(), SAVE_INVALID_TIME_KEY, 0);
        mLoginTime = SharedPreferencesUtil.getLong(MyApplication.getApplication(), SAVE_LOGIN_TIME_KEY);
        mDriverStatus = SharedPreferencesUtil.getInt(MyApplication.getApplication(), SAVE_DRIVER_STATUS_KEY, 1);
        mProfilePicture = SharedPreferencesUtil.getString(MyApplication.getApplication(), SAVE_PROFILE_PICTURE_KEY, "");
        mName = SharedPreferencesUtil.getString(MyApplication.getApplication(), SAVE_NAME_KEY, "");
        mPhone = SharedPreferencesUtil.getString(MyApplication.getApplication(), SAVE_PHONE_KEY, "");
        mPlateNumberPrefix = SharedPreferencesUtil.getString(MyApplication.getApplication(), SAVE_PLATE_NUMBER_PREFIX_KEY, "");
        mIsBindCode = SharedPreferencesUtil.getBoolean(MyApplication.getApplication(), SAVE_BINDING_CODE_KEY, mIsBindCode);
        mTenantId = SharedPreferencesUtil.getInt(MyApplication.getApplication(), SAVE_AUTHENTICATION_TENANT_ID_KEY);
        mArtyStarTimes = SharedPreferencesUtil.getInt(MyApplication.getApplication(), SAVE_STAR_MONTH_KEY, mArtyStarTimes);

    }

    /**
     * 保存用户信息
     *
     * @param bean
     */
    public void save(LoginBean bean) {
        if (bean == null) {
            return;
        }

        mUserId = bean.getUserId();
        mAccessToken = bean.getAccessToken();
        mEncryptedAccessToken = bean.getEncryptedAccessToken();
        mInvalidTime = bean.getExpireInSeconds();
        mLoginTime = System.currentTimeMillis();
        mDriverStatus = bean.getDriverStatus();
        mArtyStarTimes = bean.getArtyStarTimes();
        mTenantId = bean.getTenantId();
        if (!mIsBindCode) {
            mIsBindCode = bean.isHasBindingCode();
        }

        TenantBean tenantBean = bean.getTenant();
        if (tenantBean != null) {
            mPlateNumberPrefix = tenantBean.getPlateNumberPrefix();
        }

        saveUserInfo();
    }

    /**
     * 保存信息
     *
     * @param bean
     */
    public void save(UserInfoBean bean) {
        if (bean == null) {
            return;
        }
        mName = bean.getUserName();
        mProfilePicture = bean.getProfile();
        mBase64QrCode = bean.getBase64QrCode();
        mCarLisenceNumber = bean.getLisenceNumber();
        TenantBean tenantBean = bean.getTenant();
        if (tenantBean != null) {
            mPlateNumberPrefix = tenantBean.getPlateNumberPrefix();
        }
        saveCarInfo();
    }

    public void savePhone(String phone) {
        mPhone = phone;
        savePhone();
    }

    /**
     * 设置当前的地区id
     *
     * @param tenantId
     */
    public void setCurrentTenantId(int tenantId) {
        mCurrentTenantId = tenantId;
    }

    /**
     * 设置是否绑定二维码
     *
     * @param isBindCode 否绑定二维码
     */
    public void setIsBindCode(boolean isBindCode) {
        mIsBindCode = isBindCode;
        SharedPreferencesUtil.putBoolean(MyApplication.getApplication(), SAVE_BINDING_CODE_KEY, mIsBindCode);
    }

    public void setUrl(String url) {
        this.mUrl = url;
        SharedPreferencesUtil.putBoolean(MyApplication.getApplication(), SAVE_BINDING_CODE_KEY, mIsBindCode);
    }
    /**
     * 返回是否绑定二维码
     *
     * @return 返回是否绑定二维码
     */
    public boolean isBindCode() {
        return mIsBindCode;
    }


    public void setBindCode(String code){
        this.mBindCode=code;
    }


    public String getBindCode(){
        return mBindCode;
    }

    /**
     * 清空用户信息
     */
    public void clearUserInfo() {
        if (mUserId < 0 && TextUtils.isEmpty(mAccessToken)) {
            return;
        }

        mUserId = -1;
        mAccessToken = "";
        mEncryptedAccessToken = "";
        mInvalidTime = 0;
        mLoginTime = 0;
        mName = "";
        mProfilePicture = "";
        mBase64QrCode = "";
        mCarLisenceNumber = "";
        mPhone = "";
        mDriverStatus = -1;
        mPlateNumberPrefix = "";
        mTenantId = -1;
        mIsBindCode = false;
        mCurrentTenantId = -1;
        mArtyStarTimes = 0;
        // 清空答题记录
        AnswerActivity.clearAnswerRecord();
        saveUserInfo();
        savePhone();
    }

    /**
     * 判断用户是否登录
     *
     * @return true 表示已登录
     */
    public boolean isLogin() {
        return mUserId > 0 && !TextUtils.isEmpty(mAccessToken);
    }

    /**
     * 判断用户是否失效
     *
     * @return true 表示帐号失效
     */
    public boolean isInvalid() {
        if (!isLogin() || mLoginTime <= 0) {
            return true;
        }

        long difference = (System.currentTimeMillis() - mLoginTime) / 1000;
        // 失效时间 为 服务器的失效时间提早2小时
        return difference >= mInvalidTime - 7200;
    }

    /**
     * 保存用户当前信息
     */
    private void saveUserInfo() {
        SharedPreferencesUtil.putInt(MyApplication.getApplication(), SAVE_USER_ID_KEY, mUserId);
        SharedPreferencesUtil.putString(MyApplication.getApplication(), SAVE_ACCESS_TOKEN_KEY, mAccessToken);
        SharedPreferencesUtil.putString(MyApplication.getApplication(), SAVE_ENCRYPTED_ACCESSTO_KEY, mEncryptedAccessToken);
        SharedPreferencesUtil.putInt(MyApplication.getApplication(), SAVE_INVALID_TIME_KEY, mInvalidTime);
        SharedPreferencesUtil.putLong(MyApplication.getApplication(), SAVE_LOGIN_TIME_KEY, mLoginTime);
        SharedPreferencesUtil.putInt(MyApplication.getApplication(), SAVE_DRIVER_STATUS_KEY, mDriverStatus);
        SharedPreferencesUtil.putString(MyApplication.getApplication(), SAVE_PLATE_NUMBER_PREFIX_KEY, mPlateNumberPrefix);
        SharedPreferencesUtil.putBoolean(MyApplication.getApplication(), SAVE_BINDING_CODE_KEY, mIsBindCode);
        SharedPreferencesUtil.putInt(MyApplication.getApplication(), SAVE_AUTHENTICATION_TENANT_ID_KEY, mTenantId);
        SharedPreferencesUtil.putInt(MyApplication.getApplication(), SAVE_STAR_MONTH_KEY, mArtyStarTimes);
    }

    /**
     * 保存车辆及用户信息
     */
    private void saveCarInfo() {
        SharedPreferencesUtil.putString(MyApplication.getApplication(), SAVE_NAME_KEY, mName);
        SharedPreferencesUtil.putString(MyApplication.getApplication(), SAVE_PROFILE_PICTURE_KEY, mProfilePicture);
        SharedPreferencesUtil.putString(MyApplication.getApplication(), SAVE_BASE_QR_CODE, mBase64QrCode);
    }

    /**
     * 保存手机号码
     */
    private void savePhone() {
        SharedPreferencesUtil.putString(MyApplication.getApplication(), SAVE_PHONE_KEY, mPhone);
    }

    public String getmUrl(){
        return  mUrl;

    }

    public int getUserId() {
        return mUserId;
    }

    public String getAccessToken() {
        if (mAccessToken == null) {
            return "";
        }
        return mAccessToken;
    }

    public int getArtyStarTimes() {
        return mArtyStarTimes;
    }

    public void setArtyStarTimes(int artyStarTimes) {
        this.mArtyStarTimes = artyStarTimes;
    }

    public String getPhone() {
        return mPhone;
    }

    public String getEncryptedAccessToken() {
        return mEncryptedAccessToken;
    }

    public int getInvalidTime() {
        return mInvalidTime;
    }

    public long getmLoginTime() {
        return mLoginTime;
    }

    public String getCarLisenceNumber() {
        return mCarLisenceNumber;
    }

    public void setCarLisenceNumber(String mCarLisenceNumber) {
        this.mCarLisenceNumber = mCarLisenceNumber;
    }


    public String getBase64QrCode() {
        return mBase64QrCode;
    }

    public String getPlateNumberPrefix() {
        return mPlateNumberPrefix;
    }

    public String getBankCardNo(){
        return mBankCardNo;
    }

    public void setPlateNumberPrefix(String plateNumberPrefix) {
        this.mPlateNumberPrefix = plateNumberPrefix;
    }


    public void setBankCardNo(String BankCardNo) {
        this.mBankCardNo = BankCardNo;
    }
    /**
     * 添加登录状态提醒
     *
     * @param activity
     * @param listener
     */
    public void addOnUserStateListener(Activity activity, OnUserStateListener listener) {
        addOnUserStateListener((Object) activity, listener);
    }

    /**
     * 添加登录状态提醒
     *
     * @param fragment
     * @param listener
     */
    public void addOnUserStateListener(Fragment fragment, OnUserStateListener listener) {
        addOnUserStateListener((Object) fragment, listener);
    }


    /**
     * 添加登录状态提醒
     *
     * @param object
     * @param listener
     */
    private void addOnUserStateListener(Object object, OnUserStateListener listener) {
        if (object == null) {
            return;
        }
        mListenerMap.put(object, listener);
    }

    /**
     * 移除登录状态提醒
     *
     * @param activity
     */
    public void removeOnUserStateListener(Activity activity) {
        removeOnUserStateListener((Object) activity);
    }


    /**
     * 添加城市改变状态提醒
     *
     * @param activity
     * @param listener
     */
    public void addOnUserStateListener(Activity activity, OnChangeCityListener listener) {
        addOnChangeCityListener((Object) activity, listener);
    }

    /**
     * 添加城市改变状态提醒
     *
     * @param fragment
     * @param listener
     */
    public void addOnChangeCityListener(Fragment fragment, OnChangeCityListener listener) {
        addOnChangeCityListener((Object) fragment, listener);
    }


    /**
     * 添加城市改变状态提醒
     *
     * @param object
     * @param listener
     */
    private void addOnChangeCityListener(Object object, OnChangeCityListener listener) {
        if (object == null) {
            return;
        }
        mChangeCityMap.put(object, listener);
    }

    /**
     * 移除城市改变状态提醒
     *
     * @param activity
     */
    public void removeOnChangeCityListener(Activity activity) {
        removeOnChangeCityListener((Object) activity);
    }

    /**
     * 移除城市改变状态提醒
     *
     * @param fragment
     */
    public void removeOnChangeCityListener(Fragment fragment) {
        removeOnChangeCityListener((Object) fragment);
    }

    /**
     * 移除登录状态提醒
     *
     * @param object
     */
    private void removeOnChangeCityListener(Object object) {
        if (object == null) {
            return;
        }
        mChangeCityMap.remove(object);
    }


    /**
     * 移除登录状态提醒
     *
     * @param fragment
     */
    public void removeOnUserStateListener(Fragment fragment) {
        removeOnUserStateListener((Object) fragment);
    }

    /**
     * 移除登录状态提醒
     *
     * @param object
     */
    private void removeOnUserStateListener(Object object) {
        if (object == null) {
            return;
        }
        mListenerMap.remove(object);
    }

    /**
     * 添加认证改变状态提醒
     *
     * @param activity
     * @param listener
     */
    public void addOnAuthenticationListener(Activity activity, OnAuthenticationChangeListener listener) {
        addOnAuthenticationListener((Object) activity, listener);
    }

    /**
     * 添加认证改变状态提醒
     *
     * @param fragment
     * @param listener
     */
    public void addOnAuthenticationListener(Fragment fragment, OnAuthenticationChangeListener listener) {
        addOnAuthenticationListener((Object) fragment, listener);
    }

    /**
     * 添加认证改变状态提醒
     *
     * @param object
     * @param listener
     */
    private void addOnAuthenticationListener(Object object, OnAuthenticationChangeListener listener) {
        if (object == null) {
            return;
        }
        mAuthenticationCityMap.put(object, listener);
    }

    /**
     * 移除认证改变状态提醒
     *
     * @param activity
     */
    public void removeOnAuthenticationListener(Activity activity) {
        removeOnAuthenticationListener((Object) activity);
    }

    /**
     * 移除认证改变状态提醒
     *
     * @param fragment
     */
    public void removeOnAuthenticationListener(Fragment fragment) {
        removeOnAuthenticationListener((Object) fragment);
    }

    /**
     * 移除认证改变状态提醒
     *
     * @param object
     */
    private void removeOnAuthenticationListener(Object object) {
        if (object == null) {
            return;
        }
        mAuthenticationCityMap.remove(object);
    }


    /**
     * 登录失效
     */
    public void loginInvalid() {
        clearUserInfo();
        Message message = new Message();
        message.what = -1;
        mHandler.sendMessage(message);
    }

    /**
     * 切换地区
     *
     * @param newTenantBean
     */
    public void changeCity(TenantBean newTenantBean) {
        Message message = new Message();
        message.what = 2;
        message.obj = newTenantBean;
        mHandler.sendMessage(message);


    }


    /**
     * 登录成功
     */
    public void loginSuccess() {
        Message message = new Message();
        message.what = 1;
        mHandler.sendMessage(message);
    }


    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg == null) {
                return;
            }

            switch (msg.what) {
                // 登录
                case 1:
                    for (OnUserStateListener listener : mListenerMap.values()) {
                        if (listener == null) {
                            continue;
                        }
                        listener.onLogin(UserInfoHelper.this);
                    }
                    break;
                //注销
                case -1:
                    for (OnUserStateListener listener : mListenerMap.values()) {
                        if (listener == null) {
                            continue;
                        }
                        listener.onInvalid();
                    }
                    break;
                // 切换城市
                case 2:
                    TenantBean tenantBean = (TenantBean) msg.obj;
                    for (OnChangeCityListener listener : mChangeCityMap.values()) {
                        if (listener == null) {
                            continue;
                        }
                        listener.onChangeCity(tenantBean);
                    }
                    break;
                // 认证
                case 3:
                    for (OnAuthenticationChangeListener listener : mAuthenticationCityMap.values()) {
                        if (listener == null) {
                            continue;
                        }
                        listener.onAuthenticationChangeListener();
                    }

                    break;
            }
        }
    };

    public String getName() {
        return mName;
    }

    public String getProfilePicture() {
        return mProfilePicture;
    }


    public static class OnLoginUserState implements OnUserStateListener {

        private Activity mActivity;

        private Fragment mFragment;

        public OnLoginUserState(Activity activity) {
            mActivity = activity;
        }

        public OnLoginUserState(Fragment fragment) {
            mFragment = fragment;
        }

        @Override
        public void onLogin(UserInfoHelper help) {

        }

        @Override
        public void onInvalid() {
            if (mActivity != null) {
                LoginActivity.start(mActivity);
            } else if (mFragment != null) {
                LoginActivity.start(mFragment);
            }

        }


    }

    /**
     * 设置当前司机认证状态
     *
     * @param driverStatus
     */
    public void setDriverStatus(int driverStatus) {
        // 当认证状态发生改变，则调用接口
        if (mDriverStatus != driverStatus) {
            sendAuthenticationChange();
        }
        mDriverStatus = driverStatus;
    }

    /**
     * 发送认证 回调
     */
    public void sendAuthenticationChange() {
        Message message = new Message();
        message.what = 3;
        mHandler.sendMessage(message);
    }

    /**
     * 设置当前认证地区id
     *
     * @param tenantId 当前认证地区id
     */
    public void setTenantId(int tenantId) {
        mTenantId = tenantId;
    }

    /**
     * 返回当前认证的地区id
     *
     * @return 当前认证的地区id
     */
    public int getTenantId() {
        return mTenantId;
    }

    /**
     * 是否切换地区
     *
     * @return 返回true 表示已切换地区
     */
    public boolean isSwitchRegion() {
        return mTenantId != mCurrentTenantId;
    }

    /**
     * 是否切换地区
     *
     * @return 返回true 表示已切换地区
     */
    public boolean isSwitchRegion(BaseActivity activity) {
        if (mTenantId != mCurrentTenantId) {
            activity.showToast("请切换回认证城市再进行此操作");
        }
        return mTenantId != mCurrentTenantId;
    }

    /**
     * 是否切换地区
     *
     * @return 返回true 表示已切换地区
     */
    public boolean isSwitchRegion(BaseFragment fragment) {
        if (mTenantId != mCurrentTenantId) {
            fragment.showToast("请切换回认证城市再进行此操作");
        }

        return mTenantId != mCurrentTenantId;
    }


    /**
     * 是否认证通过
     *
     * @return 认证通过返回true，未认证、认证中或认证失败返回false
     */
    public boolean isAuthentication() {
        return mDriverStatus == AUTHENTICATION_CERTIFIED;
    }

    /**
     * 是否认证通过
     *
     * @param activity
     * @return 认证通过返回true，未认证、认证中或认证失败返回false
     */
    public boolean isAuthentication(BaseActivity activity) {
        if (mDriverStatus == UN_AUTHENTICATION || mDriverStatus == AUTHENTICATION_ERROR) {
            // IdCardAuthenticationActivity.start(activity, false);
            activity.showToast("请进行认证");
        } else if (mDriverStatus == AUTHENTICATION_AUDIT) {
            activity.showToast("审核中，请耐心等待");
        }
        return mDriverStatus == AUTHENTICATION_CERTIFIED;
    }

    public boolean isAuthentication(BaseFragment fragment) {
        if (mDriverStatus == UN_AUTHENTICATION || mDriverStatus == AUTHENTICATION_ERROR) {
            // IdCardAuthenticationActivity.start(activity, false);
            fragment.showToast("请进行认证");
        } else if (mDriverStatus == AUTHENTICATION_AUDIT) {
            fragment.showToast("审核中，请耐心等待");
        }
        return mDriverStatus == AUTHENTICATION_CERTIFIED;
    }

    /**
     * 返回认证状态
     *
     * @return
     */
    public int getAuthenticationState() {
        return mDriverStatus;
    }

    /**
     * 返回认证状态字符串
     *
     * @return
     */
    public String getDriverStatusStr() {
        String state = "";
        switch (mDriverStatus) {
            case UN_AUTHENTICATION:
                state = "未认证";
                break;
            case AUTHENTICATION_AUDIT:
                state = "审核中";
                break;
            case AUTHENTICATION_CERTIFIED:
                state = "已认证";
                break;
            case AUTHENTICATION_ERROR:
                state = "审核不通过";
                break;
        }
        return state;
    }


    public interface OnUserStateListener {
        void onLogin(UserInfoHelper help);

        void onInvalid();
    }


    public interface OnChangeCityListener {
        void onChangeCity(TenantBean bean);
    }


    public interface OnAuthenticationChangeListener {
        void onAuthenticationChangeListener();
    }


}
