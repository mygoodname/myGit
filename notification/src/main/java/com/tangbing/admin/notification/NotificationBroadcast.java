package com.tangbing.admin.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class NotificationBroadcast extends BroadcastReceiver {
    private String broadcastAction = "com.haha.playServiceAction";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();//动作
        if (action != null && action.equals(broadcastAction)) {
            switch (intent.getStringExtra("playStatus")) {
                case "-1":
                    Log.e("NotificationBroadcast", "-1");
                    break;
                case "0":
                    Log.e("NotificationBroadcast", "0");
                    break;
                case "1":
                    Log.e("NotificationBroadcast", "1");
                    break;
                case "2":
//                        AppContext.appContext.stopAudioPlayService();
                    Log.e("NotificationBroadcast", "2");
                    break;
            }
        }
    }
}