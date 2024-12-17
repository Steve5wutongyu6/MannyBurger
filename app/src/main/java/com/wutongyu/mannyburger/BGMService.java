package com.wutongyu.mannyburger;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

public class BGMService extends Service {
    private final IBinder binder = new LocalBinder();
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private static final String CHANNEL_ID = "MannyBurgerMusicChannel";
    private static final int NOTIFICATION_ID = 1;
    private static final String TAG = "BGMService";

    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind called");
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate called");
        mediaPlayer = MediaPlayer.create(this, R.raw.bgm);
        mediaPlayer.setLooping(true); // 设置循环播放
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand called");
        startForeground(NOTIFICATION_ID, createNotification());
        if (!isPlaying) {
            playMusic();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        stopForeground(true);
    }

    public void playMusic() {
        Log.d(TAG, "playMusic called");
        if (!isPlaying && mediaPlayer != null) {
            mediaPlayer.start();
            isPlaying = true;
        }
    }

    public void pauseMusic() {
        Log.d(TAG, "pauseMusic called");
        if (isPlaying && mediaPlayer != null) {
            mediaPlayer.pause();
            isPlaying = false;
        }
    }

    public class LocalBinder extends Binder {
        public BGMService getService() {
            return BGMService.this;
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Manny Burger Music",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private Notification createNotification() {
        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle("Manny Burger")
                    .setContentText("Background music is playing")
                    .setSmallIcon(R.drawable.music);
        } else {
            builder = new Notification.Builder(this)
                    .setContentTitle("Manny Burger")
                    .setContentText("Background music is playing")
                    .setSmallIcon(R.drawable.music)
                    .setPriority(Notification.PRIORITY_LOW);
        }
        return builder.build();
    }
}



