package com.example.audiovideolearning.utils.recordvideo3;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.audiovideolearning.R;

import java.io.File;

public class Record3Activity extends AppCompatActivity {

    Button button;
    String videoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record3);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,Manifest.permission.CAMERA}, 5);
        }
        button = (Button) findViewById(R.id.btn_record_video);
        File fpath = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/pauseRecordDemo/video");
        if (!fpath.exists()) {
            fpath.mkdirs();
        }
        videoPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/pauseRecordDemo/video/";
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = VedioRecordActivity.startRecordActivity(videoPath,Record3Activity.this);
                startActivity(intent);
            }
        });
    }
}
