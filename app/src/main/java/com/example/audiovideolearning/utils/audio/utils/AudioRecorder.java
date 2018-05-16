package com.example.audiovideolearning.utils.audio.utils;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.text.TextUtils;
import android.util.Log;

import com.example.audiovideolearning.utils.audio.record.record.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 描    述：
 * 作    者：liyx@13322.com
 * 时    间：2018/5/14
 */
public class AudioRecorder {
    private static AudioRecorder audioRecorder;
    private final static int AUDIO_INPUT = MediaRecorder.AudioSource.MIC;
    private final static int AUDIO_SAMPLE_RATE = 16000;
    private final static int AUDIO_CHANNEL = AudioFormat.CHANNEL_IN_MONO;
    private final static int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private int buffSizeInBytes = 0;
    private AudioRecord audioRecord;

    private Status status = Status.STATUS_NO_READY;
    //文件名
    private String fileName;

    //录音文件
    private List<String> filesName = new ArrayList<>();
    private AudioRecorder() {
    }

    //单例模式
    public static AudioRecorder getInstance() {
        if (audioRecorder == null) {
            audioRecorder = new AudioRecorder();
        }
        return audioRecorder;
    }



    public void createDefaultAudio(String fileName){
        buffSizeInBytes = AudioRecord.getMinBufferSize(AUDIO_SAMPLE_RATE  ,
                AUDIO_CHANNEL , AUDIO_ENCODING);

        audioRecord = new AudioRecord(AUDIO_INPUT , AUDIO_SAMPLE_RATE ,
                AUDIO_CHANNEL , AUDIO_ENCODING , buffSizeInBytes);

        this.fileName = fileName;
        status = Status.STATUS_READY;
    }


    public void startRecord(final RecordStreamListener listener){
        if (status == Status.STATUS_NO_READY || TextUtils.isEmpty(fileName)) {
            throw new IllegalStateException("录音尚未初始化,请检查是否禁止了录音权限~");
        }
        if (status == Status.STATUS_START) {
            throw new IllegalStateException("正在录音");
        }
        audioRecord.startRecording();
        new Thread(new Runnable() {
            @Override
            public void run() {
                writeDataTOFile(listener);
            }
        }).start();

    }

    public void pauseRecord() {

        audioRecord.stop();
    }

    public void stopRecord(){
        audioRecord.stop();
        release();
    }

    private void release() {
        audioRecord.release();
    }

    private void mergePCMFilesToWAVFile(final List<String> filePaths) {
        // TODO
    }


    private void writeDataTOFile(RecordStreamListener listener) {
        byte[] audiodata = new byte[buffSizeInBytes];
        FileOutputStream fos = null;
        int readsize = 0;
        try {
            String currentFileName = fileName;
            if (status == Status.STATUS_PAUSE) {
                //假如是暂停录音 将文件名后面加个数字,防止重名文件内容被覆盖
                currentFileName += filesName.size();

            }
            filesName.add(currentFileName);
            File file = new File(FileUtils.getPcmFileAbsolutePath(currentFileName));
            if (file.exists()) {
                file.delete();
            }
            fos = new FileOutputStream(file);// 建立一个可存取字节的文件
        } catch (IllegalStateException e) {
            Log.e("AudioRecorder", e.getMessage());
            throw new IllegalStateException(e.getMessage());
        } catch (FileNotFoundException e) {
            Log.e("AudioRecorder", e.getMessage());
        }
        status = Status.STATUS_START;

        while (status == Status.STATUS_START){
            readsize = audioRecord.read(audiodata , 0 , buffSizeInBytes);
            if (AudioRecord.ERROR_INVALID_OPERATION != readsize && fos != null){
                try {
                    fos.write(audiodata);
                    if (listener != null){
                        listener.recordOfByte(audiodata , 0 , audiodata.length);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }


    public  enum Status {
        //未开始
        STATUS_NO_READY,
        //预备
        STATUS_READY,
        //录音
        STATUS_START,
        //暂停
        STATUS_PAUSE,
        //停止
        STATUS_STOP
    }

    public interface RecordStreamListener {
        void recordOfByte(byte[] data, int begin, int end);
    }
}
