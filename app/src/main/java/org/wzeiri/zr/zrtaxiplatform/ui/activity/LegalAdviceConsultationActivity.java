package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.bean.LegalAdviceTypeBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.ILegal;
import org.wzeiri.zr.zrtaxiplatform.network.bean.JsonItem;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.ActionbarActivity;
import org.wzeiri.zr.zrtaxiplatform.widget.ValueTextView;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 法律咨询-咨询
 *
 * @author k-lm on 2017/12/6.
 */

public class LegalAdviceConsultationActivity extends ActionbarActivity {
    @BindView(R.id.aty_legal_advice_edit_content)
    EditText mEditContent;
    @BindView(R.id.aty_legal_advice_vtv_type)
    ValueTextView mVtvType;

    private LegalAdviceTypeBean mBean = new LegalAdviceTypeBean();

    private ILegal mILegal;

    private int mLegalType = -1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_legal_advice;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        TextView textView = new TextView(getThis());
        getMenuInflater().inflate(R.menu.menu_blank, menu);
        menu.findItem(R.id.menu_blank).setActionView(textView);
        textView.setText("咨询回复");
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_small));
        textView.setTextColor(ContextCompat.getColor(this, R.color.black70));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MyLegalAdviceReplyActivity.class);
            }
        });


        return true;
    }


    @Override
    protected void init() {
        super.init();
        setCenterTitle("我要咨询");
    }


    @OnClick(R.id.aty_legal_advice_text_submit)
    void submitClick() {

        String content = mEditContent.getText().toString();

        if (TextUtils.isEmpty(content.trim())) {
            showToast("请输入要咨询的内容");
            return;
        }

        if (mBean == null) {
            showToast("请选择要咨询的类型");
            return;
        }

        if (mILegal == null) {
            mILegal = RetrofitHelper.create(ILegal.class);
        }


        mILegal.createLegal(RetrofitHelper
                .getBody(new JsonItem("content", content),
                        new JsonItem("legalType", mLegalType)))
                .enqueue(new MsgCallBack<BaseBean<String>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                        showToast("咨询成功");
                        setResult(RESULT_OK, getIntent());
                        finish();
                    }

                    @Override
                    public void onError(Call<BaseBean<String>> call, Throwable t) {
                        //super.onError(call, t);
                        showToast("咨询失败，请稍候重试");
                    }
                });

    }

    @OnClick(R.id.aty_legal_advice_vtv_type)
    void typeClick() {
        SelectLegalAdviceTypeActivity.start(getThis(), mLegalType);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null || resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == SelectLegalAdviceTypeActivity.REQUSET_CODE) {
            LegalAdviceTypeBean bean = SelectLegalAdviceTypeActivity.getSelectData(data);
            mLegalType = bean.getLegalType();
            mVtvType.setText(bean.getDisplayValue());
        }
    }
}
