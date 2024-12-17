package com.wutongyu.mannyburger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("com.wutongyu.mannyburger.PLAY_PAUSE".equals(intent.getAction())) {
            Intent serviceIntent = new Intent(context, BGMService.class);
            if (BGMService.isMediaPlayerPlaying()) {
                serviceIntent.putExtra("action", "pause");
            } else {
                serviceIntent.putExtra("action", "play");
            }
            context.startService(serviceIntent);
        }
    }
}
