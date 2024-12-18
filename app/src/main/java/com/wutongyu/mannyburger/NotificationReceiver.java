package com.wutongyu.mannyburger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("com.wutongyu.mannyburger.PLAY_PAUSE".equals(intent.getAction())) {
            Intent serviceIntent = new Intent(context, BGMService.class);
            serviceIntent.setAction("com.wutongyu.mannyburger.PLAY_PAUSE");
            context.startService(serviceIntent);
        }
    }
}
