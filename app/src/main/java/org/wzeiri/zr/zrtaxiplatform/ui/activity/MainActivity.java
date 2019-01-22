package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.amin.MainVisibilityOutAnimation;
import org.wzeiri.zr.zrtaxiplatform.bean.DriverStatusBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IUser;
import org.wzeiri.zr.zrtaxiplatform.network.callback.BaseCallBack;
import org.wzeiri.zr.zrtaxiplatform.service.BaiduLocationService;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.WidgetActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.FindFragment;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.IndexFragment;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.MyFragment;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.TaxiFragment;
import org.wzeiri.zr.zrtaxiplatform.util.UserInfoHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends WidgetActivity {
    /**
     * fragmentTags
     */
    private String[] mFragmentTags = new String[]{"indexFragment", "findFragment", "taxiFragment,",
            "moreFragment"};


    @BindView(R.id.aty_index_rg_bottom_menu)
    RadioGroup mBottomMenu;

    @BindView(R.id.aty_index_img_bottom_menu_more)
    ImageView mBottomMore;

    @BindView(R.id.aty_index_layout_content_layout)
    FrameLayout mContentLayout;



    private Fragment mCurrentFragment = null;

    private FragmentManager mFragmentManager;
    /**
     * 当前刷新时间
     */
    private long mCurrentRefreshTime;

    /**
     * 当前选择的buttonId
     */
    private int mSelectId = -1;
    /**
     * 点击退出时间
     */
    private long mBackTime = 0;

    private IUser mIUser;

    private static final String[] permissionsArray = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };
    //申请的权限列表
    private List<String> permissionsList = new ArrayList<String>();
    //申请权限后的返回码
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_index;
    }


    @Override
    protected void init() {
        // changeFragment(0);
        mBottomMenu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int index = -1;
                refreshUserState();
                switch (checkedId) {
                    case R.id.aty_index_rb_bottom_menu_index:
                        index = 0;
                        break;
                    case R.id.aty_index_rb_bottom_menu_find:
                        if (!UserInfoHelper.getInstance().isAuthentication(getThis())) {
                            mBottomMenu.check(mSelectId);
                            return;
                        }

                         /*if (UserInfoHelper.getInstance().isSwitchRegion(getThis())) {
                            mBottomMenu.check(mSelectId);
                            return;
                        }*/

                        index = 1;
                        break;
                    case R.id.aty_index_rb_bottom_menu_taxi:
                        /*if (!UserInfoHelper.getInstance().isAuthentication(getThis())) {
                            mBottomMenu.check(mSelectId);
                            return;
                        }*/
                        index = 2;
                        break;
                    case R.id.aty_index_rb_bottom_menu_my:
                        index = 3;
                        break;
                }
                mSelectId = checkedId;
                changeFragment(index);


            }
        });
        SharedPreferences sharedPreferences = this.getSharedPreferences("share", MODE_PRIVATE);
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (isFirstRun){
            Log.e("debug", "第一次运行");
            editor.putBoolean("isFirstRun", false);
            editor.commit();
            checkRequiredPermission(this);
        } else {
            Log.e("debug", "不是第一次运行");
        }

        checkBottomButton(0);
    }

    @OnClick(R.id.aty_index_img_bottom_menu_more)
    void onClickMore() {
        // 未认证获取审核中提示认证

        /*if (!UserInfoHelper.getInstance().isAuthentication(getThis())) {
            return;
        }*/


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 设置回到当前activity时的过渡动画
            MainVisibilityOutAnimation animation = new MainVisibilityOutAnimation(mBottomMore);
            animation.setDuration(300);
            getWindow().setReenterTransition(animation);


            Intent intent = new Intent(this, ReleaseActivity.class);
            ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this);
            ActivityCompat.startActivityForResult(getThis(),
                    intent,
                    ReleaseActivity.REQURES_CODE,
                    transitionActivityOptions.toBundle());
        } else {
            startActivityForResult(ReleaseActivity.class, ReleaseActivity.REQURES_CODE);
        }
    }

    /**
     * 选择底部按钮
     *
     * @param index 位置
     */
    public void checkBottomButton(int index) {
        int id = 0;

        switch (index) {
            case 0:
                id = R.id.aty_index_rb_bottom_menu_index;
                break;
            case 1:
                id = R.id.aty_index_rb_bottom_menu_find;
                break;
            case 2:
                id = R.id.aty_index_rb_bottom_menu_taxi;

                break;
            case 3:
                id = R.id.aty_index_rb_bottom_menu_my;
                break;

        }

        if (id == 0) {
            return;
        }
        ((RadioButton) mBottomMenu.findViewById(id)).setChecked(true);
    }

    /**
     * 切换fragment
     *
     * @param index fragment位置
     */
    private void changeFragment(int index) {
        if (index < 0 || index > mFragmentTags.length) {
            return;
        }
        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }
        String fragmentTag = mFragmentTags[index];
        Fragment fragment = mFragmentManager.findFragmentByTag(fragmentTag);

        if (mCurrentFragment != null && mCurrentFragment.equals(fragment)) {
            return;
        }
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (fragment == null) {
            switch (index) {
                case 0:
                    fragment = new IndexFragment();
                    break;
                case 1:
                    fragment = new FindFragment();
                    break;
                case 2:
                    fragment = new TaxiFragment();
                    break;
                case 3:
                    fragment = new MyFragment();
                    break;
            }
            if (fragment == null) {
                return;
            }
            transaction.add(R.id.aty_index_layout_content_layout, fragment, fragmentTag);
        }


        if (mCurrentFragment != null) {
            transaction.hide(mCurrentFragment);
            mCurrentFragment.setUserVisibleHint(false);
        }

        mCurrentFragment = fragment;

        transaction.show(mCurrentFragment);
        mCurrentFragment.setUserVisibleHint(true);
        transaction.commit();
    }

    /**
     * 刷新用户状态
     */
    private void refreshUserState() {
        // 如果已经认证通过，可以不需要获取认证状态
        if (!UserInfoHelper.getInstance().isLogin() &&
                UserInfoHelper.getInstance().isAuthentication()) {
            return;
        }

        long time = System.currentTimeMillis();
        int difference = 1000;
        // 每次刷新间隔不能少于1秒
        if (time - mCurrentRefreshTime < difference) {
            return;
        }
        mCurrentRefreshTime = time;
        if (mIUser == null) {
            mIUser = RetrofitHelper.create(IUser.class);
        }
        mIUser.getDriverStatus()
                .enqueue(new BaseCallBack<BaseBean<DriverStatusBean>>() {
                    @Override
                    public void onSuccess(Call<BaseBean<DriverStatusBean>> call, Response<BaseBean<DriverStatusBean>> response) {
                        DriverStatusBean bean = response.body().getResult();
                        if (bean == null) {
                            return;
                        }
                        if (bean.getDriverStatus() == UserInfoHelper.AUTHENTICATION_CERTIFIED) {
                            UserInfoHelper.getInstance().setDriverStatus(bean.getDriverStatus());
                            UserInfoHelper.getInstance().setTenantId(bean.getTenantId());
                        }


                    }

                    @Override
                    public void onError(Call<BaseBean<DriverStatusBean>> call, Throwable t) {

                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BaiduLocationService.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == ReleaseActivity.REQURES_CODE) {
            releaseSuccess(data);
        }
    }

    /**
     * 发布成功后跳转
     *
     * @param intent intent
     */
    private void releaseSuccess(Intent intent) {
        if (intent == null) {
            return;
        }
        int requestCode = ReleaseActivity.getRequestCode(intent);
        checkBottomButton(3);

        switch (requestCode) {
            case ReleaseCandidateInfoActivity.REQUEST_CODE:
                startActivity(MyReleaseJobInfoActivity.class);
                break;
            case ReleaseDriverInteractionActivity.REQUEST_CODE:
                startActivity(MyReleaseDriverInteractionActivity.class);
                break;
            case ReleaseLostFoundActivity.REQUEST_CODE:
                startActivity(MyReleaseLostFoundActivity.class);
                break;
            case ReleaseChangeSeatCoverInfoActivity.REQUEST_CODE:
                int carId1 = intent.getIntExtra(ReleaseChangeSeatCoverInfoActivity.KEY_RELEASE_CAR_ID,
                        -1);
                MyReleaseSeatCoverActivity.start(getThis(), carId1);
                break;
            case ReleaseAdvertisementPhotoInfoActivity.REQUEST_CODE:
                int carId2 = intent.getIntExtra(ReleaseChangeSeatCoverInfoActivity.KEY_RELEASE_CAR_ID,
                        -1);
                MyReleasePostingAdvertisementActivity.start(getThis(), carId2);
                break;
            case ReleaseGpsFaultActivity.REQUEST_CODE:
                int carId3 = intent.getIntExtra(ReleaseGpsFaultActivity.KEY_RELEASE_CAR_ID,
                        -1);
                MyReleaseCarFaultInfoActivity.start(getThis(), carId3);
                break;
        }

    }

     private void checkRequiredPermission(final Activity activity){
        for (String permission : permissionsArray) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
            }
        }
        ActivityCompat.requestPermissions(activity, permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_PERMISSIONS);
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        // 设置时间间隔3s
        if (currentTime - mBackTime >= 3000) {
            mBackTime = currentTime;
            showToast("再按一次退出" + getString(R.string.app_name));
        } else {
            super.onBackPressed();
        }
    }
}
