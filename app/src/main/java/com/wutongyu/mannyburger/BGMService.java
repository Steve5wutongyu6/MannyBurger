package com.wutongyu.mannyburger;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

public class BGMService extends Service {
    private final IBinder binder = new LocalBinder();
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;

    public boolean isPlaying() {
        return isPlaying;
    }

    public class LocalBinder extends Binder {
        public BGMService getService() {
            return BGMService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.bgm);
        mediaPlayer.setLooping(true); // 设置循环播放
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void playMusic() {
        if (!isPlaying) {
            mediaPlayer.start();
            isPlaying = true;
        }
    }

    public void pauseMusic() {
        if (isPlaying) {
            mediaPlayer.pause();
            isPlaying = false;
        }
    }
}