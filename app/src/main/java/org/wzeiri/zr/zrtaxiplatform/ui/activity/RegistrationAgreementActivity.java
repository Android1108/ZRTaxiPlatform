package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import org.wzeiri.zr.zrtaxiplatform.bean.AgreementBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IUser;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseWebActivity;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 注册协议
 *
 * @author k-lm on 2018/1/2.
 */

public class RegistrationAgreementActivity extends BaseWebActivity {

    @Override
    protected void init() {
        super.init();
        setCenterTitle("注册协议");
    }


    @Override
    protected void initData() {
        super.initData();
        IUser iUser = RetrofitHelper.create(IUser.class);
        iUser.getRegisterAgreement()
                .enqueue(new MsgCallBack<BaseBean<AgreementBean>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<AgreementBean>> call, Response<BaseBean<AgreementBean>> response) {
                        AgreementBean bean = response.body().getResult();
                        if (bean == null) {
                            return;
                        }

                        String content = bean.getContent();
                        loadData(content);
                    }
                });
    }
}
