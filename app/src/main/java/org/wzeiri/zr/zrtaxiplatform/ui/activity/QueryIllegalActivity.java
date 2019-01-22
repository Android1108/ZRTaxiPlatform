package org.wzeiri.zr.zrtaxiplatform.ui.activity;

import android.content.DialogInterface;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.adapter.QueryIllegalAdapter;
import org.wzeiri.zr.zrtaxiplatform.bean.QueryIllegalInfoBean;
import org.wzeiri.zr.zrtaxiplatform.bean.base.BaseBean;
import org.wzeiri.zr.zrtaxiplatform.network.RetrofitHelper;
import org.wzeiri.zr.zrtaxiplatform.network.api.ITransport;
import org.wzeiri.zr.zrtaxiplatform.network.callback.MsgCallBack;
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.BaseListActivity;
import org.wzeiri.zr.zrtaxiplatform.util.UserInfoHelper;
import org.wzeiri.zr.zrtaxiplatform.widget.dialog.SendMessageCodeDialog1;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 查询违章信息
 *
 * @author k-lm on 2018/3/8.
 */

public class QueryIllegalActivity extends BaseListActivity<QueryIllegalInfoBean, QueryIllegalAdapter> {

    private ITransport mTransport;

    /**
     * 发送验证码对话框
     */
    private SendMessageCodeDialog1 mSendMessageCodeDialog;

    private boolean isSendCode;

    @Override
    protected void init() {
        super.init();
        setIsItemField(false);
        setCenterTitle("违章查询");
        mRefreshLayout.setEnabled(false);
        mListView.setDividerHeight(getResources().getDimensionPixelOffset(R.dimen.layout_margin));
//        sendMessageCode();
    }

    @Override
    protected void initData() {
        super.initData();
        loadData();
    }

    @Override
    public QueryIllegalAdapter getAdapter(List<QueryIllegalInfoBean> list) {
        return new QueryIllegalAdapter(list);
    }


    @Override
    protected Call<BaseBean<List<QueryIllegalInfoBean>>> getNotItemNetCall(int currentIndex, int pagerSize) {
        if (mTransport == null) {
            mTransport = RetrofitHelper.create(ITransport.class);
        }

//        return mTransport.queryIllegalInfo(mSendMessageCodeDialog.getMessageCode());
        return mTransport.queryIllegalInfo();
    }

    /**
     * 显示发送验证码对话框
     */
    private void showSendMessageDialog() {
        if (mSendMessageCodeDialog == null) {
            mSendMessageCodeDialog = new SendMessageCodeDialog1(getThis(), R.style.NoTitleDialog);

            mSendMessageCodeDialog.setPositiveButton_(null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    loadData();
                    dialog.dismiss();
                }
            });

            mSendMessageCodeDialog.setOnClickSendMessageCodeListener(new SendMessageCodeDialog1.OnClickSendMessageCodeListener() {
                @Override
                public void onSendMessage() {
//                    sendMessageCode();
                }
            });

            mSendMessageCodeDialog.setCanceledOnTouchOutside(false);
        }

        mSendMessageCodeDialog.show();

    }


    /**
     * 发送短信验证码
     */
    public void sendMessageCode() {
        if (mTransport == null) {
            mTransport = RetrofitHelper.create(ITransport.class);
        }
        mTransport.sendIllegalInfoSendSms()
                .enqueue(new MsgCallBack<BaseBean<String>>(getThis(), true) {
                    @Override
                    public void onSuccess(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                        if (isSendCode) {
                            showToast("消息发送成功");
                        }
                        showSendMessageDialog();
                        mSendMessageCodeDialog.startTime();
                    }

                    @Override
                    public void onError(Call<BaseBean<String>> call, Throwable t) {
                        closeProgressDialog();
                    }
                });
    }


}
