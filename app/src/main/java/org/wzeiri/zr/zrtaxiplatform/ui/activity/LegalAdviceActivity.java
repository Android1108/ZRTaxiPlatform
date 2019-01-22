package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.LegalAdvicePagerAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.LegalAdviceTypeBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.ILegal;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseTabLayoutActivity;
import org.wzeiri.zr.zrtaxiplatform.ui.fragment.LegalAdviceReplyFragment;
import org.wzeiri.zr.zrtaxiplatform.util.UserInfoHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 法律咨询
 *
 * @author k-lm on 2017/12/14.
 */

public class LegalAdviceActivity extends BaseTabLayoutActivity<LegalAdvicePagerAdapter> {

    public static final int REQUEST_CODE = 1024;


    private List<LegalAdviceTypeBean> mList = new ArrayList<>();

    private List<Fragment> mFragmentList = new ArrayList<>();

    @Override
    protected LegalAdvicePagerAdapter getPagerAdapter() {
        return new LegalAdvicePagerAdapter(getSupportFragmentManager(),
                mList, mFragmentList);
    }


    @Override
    protected void init() {
        super.init();
        setCenterTitle("法律咨询");
    }

    @Override
    protected void initData() {
        super.initData();

        ILegal legal = RetrofitHelper.create(ILegal.class);

        legal.getLegalTypies()
                .enqueue(new MsgCallBack<BaseBean<List<LegalAdviceTypeBean>>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<List<LegalAdviceTypeBean>>> call, Response<BaseBean<List<LegalAdviceTypeBean>>> response) {
                        List<LegalAdviceTypeBean> list = response.body().getResult();
                        if (list == null || list.size() == 0) {
                            return;
                        }

                        mList.addAll(list);
                        for (LegalAdviceTypeBean bean : mList) {
                            Fragment fragment = LegalAdviceReplyFragment.newInstance(bean.getLegalType());
                            mFragmentList.add(fragment);
                        }

                        getAdapter().notifyDataSetChanged();


                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        TextView textView = new TextView(getThis());
        getMenuInflater().inflate(R.menu.menu_blank, menu);
        menu.findItem(R.id.menu_blank).setActionView(textView);
        textView.setText("我要咨询");
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_small));
        textView.setTextColor(ContextCompat.getColor(this, R.color.black70));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UserInfoHelper.getInstance().isAuthentication(getThis())) {
                    return;
                }

                if (UserInfoHelper.getInstance().isSwitchRegion(getThis())) {
                    return;
                }


                startActivityForResult(LegalAdviceConsultationActivity.class, REQUEST_CODE);
            }
        });


        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE) {
            setResult(RESULT_OK, getIntent());
            finish();
        }
    }
}
