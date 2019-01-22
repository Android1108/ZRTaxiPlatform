package org.wzeiri.zr.zrtaxiplatform.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.wzeiri.zr.zrtaxiplatform.MyApplication;
import org.wzeiri.zr.zrtaxiplatform.R;
import org.wzeiri.zr.zrtaxiplatform.util.LogUtil;

import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

/**
 * @author k-lm on 2018/2/11.
 */

public class BaseJPushBroadcastReceiver extends BroadcastReceiver {
    protected static final String TAG = "JPushBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                //  LogUtil.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
//                LogUtil.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
                onReceivedMessage(context, intent, bundle);
            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
//                LogUtil.d(TAG, "[MyReceiver] 接收到推送下来的通知");
//                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
//                LogUtil.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
                onReceivedNotification(context, intent, bundle);
            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
//                LogUtil.d(TAG, "[MyReceiver] 用户点击打开了通知");
                onOpenedNotification(context, intent, bundle);

                //打开自定义的Activity
              /*  Intent i = new Intent(context, TestActivity.class);
                i.putExtras(bundle);
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);*/

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
//                LogUtil.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
//                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
//                LogUtil.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
            } else {
//                LogUtil.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {

        }

    }

    /**
     * 收到自定义信息
     */
    protected void onReceivedMessage(Context context, Intent intent, Bundle bundle) {

    }

    /**
     * 收到通知信息
     */
    protected void onReceivedNotification(Context context, Intent intent, Bundle bundle) {

    }

    /**
     * 点击通知
     */
    protected void onOpenedNotification(Context context, Intent intent, Bundle bundle) {

    }

}
