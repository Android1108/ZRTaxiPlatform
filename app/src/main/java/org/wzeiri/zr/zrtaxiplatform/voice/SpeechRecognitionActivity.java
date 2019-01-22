package org.wzeiri.zr.zrtaxiplatform.voice;

import android.Manifest;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
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
import org.wzeiri.zr.zrtaxiplatform.ui.activity.base.WidgetActivity;
import org.wzeiri.zr.zrtaxiplatform.util.LogUtil;
import org.wzeiri.zr.zrtaxiplatform.util.PermissionsUtil;
import org.wzeiri.zr.zrtaxiplatform.voice.bean.VoiceResultBean;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 语音识别
 *
 * @author k-lm on 2018/1/25.
 */

public class SpeechRecognitionActivity extends WidgetActivity implements RecognizerListener {
    private static final String TAG = "SpeechRecognitionActivi";

    @BindView(R.id.aty_speech_recognition_button_button)
    Button mButtonButton;
    @BindView(R.id.aty_speech_recognition_text_content)
    TextView mTextContent;

    // 语音识别对象
    private SpeechRecognizer mAsr;
    // 缓存
    private SharedPreferences mSharedPreferences;
    /**
     * 识别类型 云端识别
     */
    private String mEngineType = SpeechConstant.TYPE_CLOUD;


    private boolean isStartSoundRecording = false;

    /**
     * 开始/结束 录音
     */
    @OnClick(R.id.aty_speech_recognition_button_button)
    void onSoundRecording() {
        if (mAsr == null) {
            showToast("无法开始语音识别");
            return;
        }

        if (!isStartSoundRecording) {
            mAsr.startListening(this);
            mButtonButton.setText("结束录音");
        } else {
            mAsr.stopListening();
            mButtonButton.setText("开始录音");
        }
        isStartSoundRecording = !isStartSoundRecording;
    }

    @Override
    protected void init() {
        super.init();
        boolean isPermission = PermissionsUtil.lacksPermissions(getThis(),
                Manifest.permission.RECORD_AUDIO);

        if (!isPermission) {
            initXF();
            return;
        }

        PermissionsUtil.startActivityForResult(getThis(), 1, Manifest.permission.RECORD_AUDIO);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_speech_recognition;
    }


    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            LogUtil.d("SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showToast("初始化失败,错误码：" + code);
            }
        }
    };


    @Override
    public void onVolumeChanged(int i, byte[] bytes) {
        //    volume音量值0~30，data音频数据
    }

    @Override
    public void onBeginOfSpeech() {
        //开始录音
        mTextContent.setText("");
        showToast("开始录音");
        mButtonButton.setText("结束录音");
        isStartSoundRecording = true;
    }

    @Override
    public void onEndOfSpeech() {
        //结束录音
        showToast("结束录音");
        mButtonButton.setText("开始录音");
        isStartSoundRecording = false;
    }

    @Override
    public void onResult(RecognizerResult recognizerResult, boolean isLast) {
        //听写结果回调接口(返回Json格式结果，用户可参见附录13.1)；
        // 一般情况下会通过onResults接口多次返回结果，完整的识别内容是多次结果的累加；
        // 关于解析Json的代码可参见Demo中JsonParser类；
        // isLast等于true时会话结束。

        if (recognizerResult == null) {
            showToast("recognizerResult == null");
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
        resultStr = filterSymbol(text);
        mTextContent.append(resultStr);
    }

    /**
     * 过滤标点符号
     */
    private String filterSymbol(CharSequence text) {
        Pattern pattern = Pattern.compile("。|，|？|！"); //去掉空格符合换行符
        Matcher matcher = pattern.matcher(text);
        String result = matcher.replaceAll("");
        LogUtil.d(result);
        return result;
    }


    @Override
    public void onError(SpeechError speechError) {
        // 录音出错
        int code = speechError.getErrorCode();
        isStartSoundRecording = false;
        mButtonButton.setText("开始录音");
        String errorStr = speechError.getErrorDescription();
        LogUtil.d("error", code + ": " + errorStr);
        //会话发生错误回调接口
        if (code == 10118 || code == 10112) {
            return;
        }


        showToast(errorStr);


    }

    @Override
    public void onEvent(int i, int i1, int i2, Bundle bundle) {
        //扩展用接口
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (!PermissionsUtil.lacksPermissions(getThis(), permissions)) {
                initXF();
            } else {
                showToast("获取语音权限失败");
            }
        }
    }

    /**
     * 初始化讯飞
     */
    private void initXF() {
        mAsr = SpeechRecognizer.createRecognizer(getThis(), mInitListener);
        // 设置中文
        mAsr.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置普通话
        mAsr.setParameter(SpeechConstant.ACCENT, "mandarin");
        mSharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
    }


}
