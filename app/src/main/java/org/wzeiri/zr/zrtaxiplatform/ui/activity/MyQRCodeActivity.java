package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.MyUserInfoBean;
import org.wzeiri.zr.zrtaxiplatform.bean.RegisterBean;
import org.wzeiri.zr.zrtaxiplatform.bean.UserCodeBean;
import org.wzeiri.zr.zrtaxiplatform.bean.UserInfoBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.common.Config;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IMy;
import org.wzeiri.zr.zrtaxiplatform.network.api.IUser;
import org.wzeiri.zr.zrtaxiplatform.network.api.IUserQrCode;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseActivity;
import org.wzeiri.zr.zrtaxiplatform.util.BitmapUtil;
import org.wzeiri.zr.zrtaxiplatform.util.CarHelper;
import org.wzeiri.zr.zrtaxiplatform.util.GlideUtil;
import org.wzeiri.zr.zrtaxiplatform.util.UserInfoHelper;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 我的二维码
 *
 * @author k-lm on 2017/12/1.
 */

public class MyQRCodeActivity extends ActionbarActivity {

    @BindView(R.id.aty_my_qr_code_image_code)
    ImageView mCode;
    @BindView(R.id.layout_tag_user_info_text_name)
    TextView mTextName;
    @BindView(R.id.layout_tag_user_info_text_date)
    TextView mLisenceNumber;
    @BindView(R.id.layout_tag_user_info_image_avatar)
    ImageView mImageAvatar;
    @BindView(R.id.layout_tag_user_info_text_content)
    TextView mContent;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_qr_code;
    }

    @Override
    protected void init() {
        super.init();
        setCenterTitle("我的二维码");
    }

    @Override
    protected void initData() {
        super.initData();
        loadQRCode();

    }

    /**
     * 获取用户信息
     */
    private void loadQRCode() {
        IUser iUser = RetrofitHelper.create(IUser.class);

        iUser.getUserQrCode()
                .enqueue(new MsgCallBack<BaseBean<UserInfoBean>>(this, false) {
                    @Override
                    public void onSuccess(Call<BaseBean<UserInfoBean>> call, Response<BaseBean<UserInfoBean>> response) {
                        UserInfoBean bean = response.body().getResult();
                        if (bean == null) {
                            return;
                        }
                        mTextName.setText(bean.getUserName());
                        mLisenceNumber.setText(bean.getLisenceNumber());
                        String code = bean.getBase64QrCode();
                        Bitmap bitmap = BitmapUtil.base64ToBitmap(code);
                        mCode.setImageBitmap(bitmap);


                        String profile = bean.getProfile();
                        if(TextUtils.isEmpty(profile) ||
                                TextUtils.equals(profile, Config.NO_IMAGE_URL)  ){
                            mImageAvatar.setImageResource(R.drawable.ic_default_avatar);
                        }else {
                            GlideUtil.loadPath(getThis(),mImageAvatar,bean.getProfile());
                        }


                    }
                });
    }
}
