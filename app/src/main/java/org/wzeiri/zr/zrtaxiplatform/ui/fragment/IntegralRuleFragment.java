package org.wzeiri.zr.zrtaxiplatform.ui.fragment;

import org.wzeiri.zr.zrtaxiplatform.bean.IntegralRuleBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.IIntegralRecord;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.base.BaseWebFragment;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 积分规则
 *
 * @author k-lm on 2017/12/20.
 */

public class IntegralRuleFragment extends BaseWebFragment {


    @Override
    public void initData() {
        super.initData();
        IIntegralRecord integralRecord = RetrofitHelper.create(IIntegralRecord.class);

        integralRecord.getIntegralAgreement()
                .enqueue(new MsgCallBack<BaseBean<IntegralRuleBean>>(this, true) {
                    @Override
                    public void onSuccess(Call<BaseBean<IntegralRuleBean>> call, Response<BaseBean<IntegralRuleBean>> response) {
                        IntegralRuleBean bean = response.body().getResult();
                        if (bean == null) {
                            return;
                        }

                        loadData(bean.getContent());
                    }
                });

    }
}
