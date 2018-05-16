package com.example.audiovideolearning;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.audiovideolearning.utils.audio.play.AudioPlayActivity;
import com.example.audiovideolearning.utils.audio.record.activity.AudioRecordActivity;
import com.example.audiovideolearning.utils.recordVideo.MediaMuxerActivity;
import com.example.audiovideolearning.utils.recordvideo2.audiovideosample.RecordActivity;
import com.example.audiovideolearning.utils.recordvideo3.Record3Activity;
import com.example.audiovideolearning.utils.video.play.VideoPlayActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        findViewById(R.id.btn_audio_record).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this , AudioRecordActivity.class));
            }
        });

        findViewById(R.id.btn_audio_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this , AudioPlayActivity.class));
            }
        });

        findViewById(R.id.btn_video_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this , VideoPlayActivity.class));
            }
        });


        findViewById(R.id.btn_video_record).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this , MediaMuxerActivity.class));
            }
        });

        findViewById(R.id.btn_video_record2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this , RecordActivity.class));
            }
        });

        findViewById(R.id.btn_video_record3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this , Record3Activity.class));
            }
        });

    }
}
