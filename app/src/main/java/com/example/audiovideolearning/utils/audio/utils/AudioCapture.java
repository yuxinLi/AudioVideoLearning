package com.example.audiovideolearning.utils.audio.utils;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

/**
 * 描    述：
 * 作    者：liyx@13322.com
 * 时    间：2018/5/11
 */
public class AudioCapture {
    private static final String TAG = "AudioCapturer";

    private static final int DEFAULT_SOURCE = MediaRecorder.AudioSource.MIC;
    private static final int DEFAULT_SAMPLE_RATE = 44100;
    private static final int DEFAULT_CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    private static final int DEFAULT_DATA_FORMAT = AudioFormat.ENCODING_PCM_16BIT;

    private int mMinBufferSize = 0;

//    private static final int SAMPLE_PER_FRAME = 1024;

    private AudioRecord mAudioRecord;
    private Thread mCaptureThread;
    private boolean mIsCaptureStarted = false;
    private volatile boolean mIsLoopExit = false;

    private AudioFrameCapturedListener mAudioFrameCapturedListener;


    public boolean startCapture(){
        return startCapture(DEFAULT_SOURCE , DEFAULT_SAMPLE_RATE , DEFAULT_CHANNEL_CONFIG , DEFAULT_DATA_FORMAT);
    }


    public boolean startCapture(int audioSource , int sampleRate , int channelConfig , int audioFormat){
        if (mIsCaptureStarted){
            Log.e(TAG, "Capture already started !");
            return false;
        }

        mMinBufferSize = AudioRecord.getMinBufferSize(sampleRate , channelConfig , audioFormat);
        if (mMinBufferSize == AudioRecord.ERROR_BAD_VALUE){
            Log.e(TAG, "minBufferSize Invalid parameter !");
            return false;
        }

        mAudioRecord = new AudioRecord(audioSource , sampleRate , channelConfig , audioFormat , mMinBufferSize*4);
        if (mAudioRecord.getState() == AudioRecord.STATE_UNINITIALIZED){
            Log.e(TAG, "AudioRecord initialize fail !");
            return false;
        }
        mAudioRecord.startRecording();

        mIsLoopExit = false;
        mCaptureThread = new Thread(new AudioCaptureRunnable());
        mCaptureThread.start();
        mIsCaptureStarted = true;
        Log.w(TAG, "Start audio capture success !");

        return true;

    }


    public void stopCapture(){
        if (!mIsCaptureStarted){
            return;
        }

        mIsLoopExit = true;
        try {
            mCaptureThread.interrupt();
            mCaptureThread.join(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (mAudioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING){
            mAudioRecord.stop();
        }
        mAudioRecord.release();

        mIsCaptureStarted = false;
        mAudioFrameCapturedListener = null;

        Log.i(TAG, "Stop audio capture success !");
    }


    public boolean isCaptureStarted() {
        return mIsCaptureStarted;
    }
    public void setAudioFrameCapturedListener(AudioFrameCapturedListener listener) {
        mAudioFrameCapturedListener = listener;
    }


    public interface AudioFrameCapturedListener{
        void onAudioFrameCaptured(byte[] audioData);
    }

    private class AudioCaptureRunnable implements Runnable{

        @Override
        public void run() {
            while (!mIsLoopExit){
                byte[] buffer = new byte[mMinBufferSize];
                int ret = mAudioRecord.read(buffer , 0 , buffer.length);

                if (ret == AudioRecord.ERROR_INVALID_OPERATION){
                    Log.e(TAG, "Error ERROR_INVALID_OPERATION");

                }else if (ret == AudioRecord.ERROR_BAD_VALUE){
                    Log.e(TAG, "Error ERROR_BAD_VALUE");
                }else {
                    Log.d("TAG", "Audio captured: " + buffer.length + " , ret = "+ ret);
                    if (mAudioFrameCapturedListener != null){
                        mAudioFrameCapturedListener.onAudioFrameCaptured(buffer);
                    }
                }
            }
        }
    }
}
