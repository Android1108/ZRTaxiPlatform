package org.wzeiri.zr.zrtaxiplatform.widget.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.network.api.ITransport;
import org.wzeiri.zr.zrtaxiplatform.util.CountDownHelper;
import org.wzeiri.zr.zrtaxiplatform.util.UserInfoHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author k-lm on 2018/3/9.
 */

public class SendMessageCodeDialog1 extends BaseDialog {
    @BindView(R.id.dialog_send_message_text_title)
    TextView mTitle;
    @BindView(R.id.dialog_message)
    TextView mMessage;
    @BindView(R.id.dialog_send_message_text_send_code)
    TextView mTextSendCode;
    @BindView(R.id.dialog_send_message_edit_code)
    EditText mEditCode;

    private CountDownHelper mCountDownHelper;

    private ITransport mTransport;

    private OnClickSendMessageCodeListener mClickSendMessageCodeListener;

    public SendMessageCodeDialog1(@NonNull Context context) {
        super(context);
    }

    public SendMessageCodeDialog1(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_send_message1;
    }


    @OnClick(R.id.dialog_send_message_text_send_code)
    void OnSendCode() {
        if (mClickSendMessageCodeListener != null) {
            mClickSendMessageCodeListener.onSendMessage();
        }
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        ButterKnife.bind(this, view);
        mTitle.setText("短信验证");

        String message = "司机认证需要短信确认，验证码已发送至您在运管部门登记的手机";
        mMessage.setText(message);

    }


    /**
     * 开始计时
     */
    public void startTime() {
        if (mCountDownHelper == null) {
            mCountDownHelper = new CountDownHelper();
            mCountDownHelper.setOnCountDownListener(new CountDownHelper.OnCountDownListener() {
                @Override
                public void onCountDownFinish() {
                    mTextSendCode.setEnabled(true);
                    mTextSendCode.setTextColor(ContextCompat.getColor(getContext(), R.color.orange1));
                    mTextSendCode.setText("重新获取");
                }

                @Override
                public void onCountDownTick(long millisUntilFinished) {
                    String time = (millisUntilFinished / 1000 - 1) + "s";
                    mTextSendCode.setText(time);
                }
            });
        }
        mTextSendCode.setEnabled(false);
        mTextSendCode.setTextColor(ContextCompat.getColor(getContext(), R.color.gray40));
        mCountDownHelper.startTime();
    }

    /**
     * 返回用户输入的验证码
     *
     * @return 验证码
     */
    public String getMessageCode() {
        return mEditCode.getText().toString();
    }

    @Override
    public void cancel() {
        super.cancel();
        mCountDownHelper.cancel();
    }

    public void setOnClickSendMessageCodeListener(OnClickSendMessageCodeListener listener) {
        mClickSendMessageCodeListener = listener;
    }

    /**
     * 点击发送验证码回调
     */
    public interface OnClickSendMessageCodeListener {
        void onSendMessage();
    }

}
