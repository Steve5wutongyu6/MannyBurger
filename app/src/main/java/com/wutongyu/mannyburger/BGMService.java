package com.wutongyu.mannyburger;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

public class BGMService extends Service {
    private static final String CHANNEL_ID = "MannyBurgerMusicChannel";
    private static final int NOTIFICATION_ID = 1;
    private static final String TAG = "BGMService";
    private static MediaPlayer mediaPlayer;
    private final IBinder binder = new LocalBinder();
    private boolean isPlaying = false;
    private AudioManager audioManager;
    private AudioFocusRequest focusRequest;
    private AudioManager.OnAudioFocusChangeListener focusListener;

    public static boolean isMediaPlayerPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

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
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.bgm);
            mediaPlayer.setLooping(true); // 设置循环播放
        }
        createNotificationChannel();

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        focusListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                switch (focusChange) {
                    case AudioManager.AUDIOFOCUS_GAIN:
                        if (!isPlaying && mediaPlayer != null) {
                            playMusic();
                        }
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                        if (mediaPlayer != null) {
                            mediaPlayer.setVolume(0.1f, 0.1f);
                        }
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    case AudioManager.AUDIOFOCUS_LOSS:
                        pauseMusic();
                        break;
                }
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AudioAttributes playbackAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            focusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setAudioAttributes(playbackAttributes)
                    .setAcceptsDelayedFocusGain(true)
                    .setOnAudioFocusChangeListener(focusListener)
                    .build();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand called");
        if (audioManager.requestAudioFocus(focusListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            startForeground(NOTIFICATION_ID, createNotification());
            if (intent != null && "com.wutongyu.mannyburger.PLAY_PAUSE".equals(intent.getAction())) {
                handlePlayPauseAction();
            } else if (!isPlaying) {
                playMusic();
            }
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
        audioManager.abandonAudioFocusRequest(focusRequest);
    }

    public void playMusic() {
        Log.d(TAG, "playMusic called");
        if (!isPlaying && mediaPlayer != null) {
            mediaPlayer.start();
            isPlaying = true;
            updateNotification(true);
        }
    }

    public void pauseMusic() {
        Log.d(TAG, "pauseMusic called");
        if (isPlaying && mediaPlayer != null) {
            mediaPlayer.pause();
            isPlaying = false;
            updateNotification(false);
        }
    }

    private void handlePlayPauseAction() {
        if (isPlaying) {
            pauseMusic();
        } else {
            playMusic();
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
        return buildNotification(isPlaying);
    }

    private void updateNotification(boolean isPlaying) {
        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, buildNotification(isPlaying));
    }

    private Notification buildNotification(boolean isPlaying) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent playPauseIntent = new Intent(this, NotificationReceiver.class);
        playPauseIntent.setAction("com.wutongyu.mannyburger.PLAY_PAUSE");
        PendingIntent playPausePendingIntent = PendingIntent.getBroadcast(this,
                0, playPauseIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle("Manny Burger")
                    .setContentText(isPlaying ? "Playing..." : "Paused")
                    .setSmallIcon(R.drawable.music)
                    .addAction(new Notification.Action.Builder(
                            isPlaying ? R.drawable.pause : R.drawable.back,
                            isPlaying ? "Pause" : "Play",
                            playPausePendingIntent).build())
                    .setContentIntent(pendingIntent);
        } else {
            builder = new Notification.Builder(this)
                    .setContentTitle("Manny Burger")
                    .setContentText(isPlaying ? "Playing..." : "Paused")
                    .setSmallIcon(R.drawable.music)
                    .addAction(isPlaying ? R.drawable.pause : R.drawable.back,
                            isPlaying ? "Pause" : "Play",
                            playPausePendingIntent)
                    .setContentIntent(pendingIntent)
                    .setPriority(Notification.PRIORITY_LOW);
        }
        return builder.build();
    }

    public class LocalBinder extends Binder {
        public BGMService getService() {
            return BGMService.this;
        }
    }
}
