package org.wzeiri.zr.zrtaxiplatform.widget.dialog;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;

import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.util.LogUtil;
import org.wzeiri.zr.zrtaxiplatform.util.PermissionsUtil;
import org.wzeiri.zr.zrtaxiplatform.voice.bean.VoiceResultBean;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author k-lm on 2018/1/29.
 */

public class SoundRecordingDialog extends BaseDialog implements RecognizerListener {
    private static final String TAG = "SoundRecordingDialog";

    private ImageView mImageButton;

    private TextView mTextHint;

    // 语音识别对象
    private SpeechRecognizer mAsr;
    /**
     * 识别类型 云端识别
     */
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    /**
     * 语音回调
     */
    private OnVoiceListener mVoiceListener;

    private boolean isStartSoundRecording = false;

    /**
     * 录音内容
     */
    private String mContent = "";

    public SoundRecordingDialog(@NonNull Context context) {
        super(context);
    }

    public SoundRecordingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }


    @Override
    public int getLayoutId() {
        return R.layout.dialog_sound_recording;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageButton = (ImageView) findViewById(R.id.dialog_sound_recording_image_button);
        mTextHint = (TextView) findViewById(R.id.dialog_sound_recording_text_hint);
        initVoice();

        mImageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mAsr.startListening(SoundRecordingDialog.this);
                        startSpeech();
                        return true;
                    case MotionEvent.ACTION_UP:
                        mTextHint.setText("正在识别");
                        mAsr.stopListening();
                        //endSpeech();
                        return true;
                }


                return false;
            }
        });
    }

    /**
     * 初始化语音
     */
    private void initVoice() {
        mAsr = SpeechRecognizer.createRecognizer(getContext(), new InitListener() {
            @Override
            public void onInit(int code) {
                LogUtil.d("SpeechRecognizer init() code = " + code);
            }
        });
        // 设置中文
        mAsr.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置普通话
        mAsr.setParameter(SpeechConstant.ACCENT, "mandarin");


    }

    @Override
    public void onVolumeChanged(int i, byte[] bytes) {
        //    volume音量值0~30，data音频数据
    }

    @Override
    public void onBeginOfSpeech() {
        //开始录音
        startSpeech();
    }

    @Override
    public void onEndOfSpeech() {
        //结束录音
        endSpeech();
    }

    @Override
    public void onResult(RecognizerResult recognizerResult, boolean isLast) {
        //听写结果回调接口(返回Json格式结果，用户可参见附录13.1)；
        // 一般情况下会通过onResults接口多次返回结果，完整的识别内容是多次结果的累加；
        // 关于解析Json的代码可参见Demo中JsonParser类；
        // isLast等于true时会话结束。

        if (recognizerResult == null) {
            return;
        }

        String resultStr = recognizerResult.getResultString();
        LogUtil.d(TAG, "recognizer result：" + resultStr);
        StringBuilder text = new StringBuilder();
        VoiceResultBean bean;
        Gson gson = new Gson();
        if ("cloud".equalsIgnoreCase(mEngineType)) {
            bean = gson.fromJson(resultStr, VoiceResultBean.class);
        } else {
            bean = gson.fromJson(resultStr, VoiceResultBean.class);
        }

        if (bean != null) {
            List<VoiceResultBean.WsBean> wsBeanList = bean.getWs();
            if (wsBeanList != null && wsBeanList.size() > 0) {
                for (VoiceResultBean.WsBean wsBean : wsBeanList) {
                    List<VoiceResultBean.WsBean.CwBean> cwBeanList = wsBean.getCw();
                    if (cwBeanList != null && cwBeanList.size() > 0) {
                        for (VoiceResultBean.WsBean.CwBean cwBean : cwBeanList) {
                            text.append(cwBean.getW());
                        }
                    }
                }
            }
        }

        mContent += filterSymbol(text);

        if (isLast) {
            endSpeech();
        }
    }

    /**
     * 过滤标点符号
     */
    private String filterSymbol(CharSequence text) {
        Pattern pattern = Pattern.compile("。|，|？|！"); //去掉空格符合换行符
        Matcher matcher = pattern.matcher(text);
        String result = matcher.replaceAll("");
        LogUtil.d(TAG, "filterSymbol:  " + result);
        return result;
    }

    @Override
    public void onError(SpeechError speechError) {
        // 录音出错
        int code = speechError.getErrorCode();
        mTextHint.setText("按住说话");
        isStartSoundRecording = false;
        mImageButton.setSelected(false);
        String errorStr = speechError.getErrorDescription();
        LogUtil.d(TAG, code + ": " + errorStr);
        //会话发生错误回调接口
        if (code == 10118 || code == 10112) {
            mVoiceListener.onEndSpeech("");
            return;
        }
        if (mVoiceListener != null) {
            mVoiceListener.onError(code, errorStr);
        }
    }

    @Override
    public void onEvent(int i, int i1, int i2, Bundle bundle) {
        //扩展用接口
    }


    public void setOnVoiceListener(OnVoiceListener listener) {
        mVoiceListener = listener;
    }

    /**
     * 开始语音
     */
    private void startSpeech() {
        mContent = "";
        mTextHint.setText("松开结束");
        mImageButton.setSelected(true);
        isStartSoundRecording = true;
        if (mVoiceListener != null) {
            mVoiceListener.onStartSpeech();
        }
    }

    /**
     * 结束语音
     */
    private void endSpeech() {
        mTextHint.setText("按住说话");
        isStartSoundRecording = false;
        mImageButton.setSelected(false);
        if (mVoiceListener != null) {
            mVoiceListener.onEndSpeech(mContent);
        }
    }


    /**
     * 语音回调
     */
    public interface OnVoiceListener {
        /**
         * 开始
         */
        void onStartSpeech();

        /**
         * 结束
         *
         * @param content
         */
        void onEndSpeech(String content);

        /**
         * 错误
         *
         * @param errorCode    错误code
         * @param errorMessage 错误信息
         */
        void onError(int errorCode, String errorMessage);
    }

}
