package com.wutongyu.mannyburger;

import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class VideoPlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        VideoView videoView = findViewById(R.id.videoView);
        String videoPath = getIntent().getStringExtra("videoPath");

        // 检查视频路径是否为 raw 文件夹中的资源,根据于不同情况设置不同的播放路径
        if (videoPath.startsWith("android.resource://")) {
            videoView.setVideoURI(Uri.parse(videoPath));
        } else {
            videoView.setVideoURI(Uri.parse("file://" + videoPath));
        }
        videoView.start();
    }
}
