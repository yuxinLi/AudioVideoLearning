package com.example.audiovideolearning.utils.audio.utils;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

/**
 * 描    述：
 * 作    者：liyx@13322.com
 * 时    间：2018/5/11
 */
public class AudioPlayer {

    private static final String TAG = "AudioPlayer";

    private static final int DEFAULT_STREAM_TYPE = AudioManager.STREAM_MUSIC;
    private static final int DEFAULT_STREAM_RATE = 44100;
    private static final int DEFAULT_CHANNEL_CONFIG = AudioFormat.CHANNEL_OUT_MONO;
    private static final int DEFAULT_AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    private static final int DEFAULT_PLAY_MODE = AudioTrack.MODE_STREAM;

    private volatile boolean mIsPlayStarted = false;
    private AudioTrack mAudioTrack;


    public boolean startPlayer(){
        return startPlayer(DEFAULT_STREAM_TYPE , DEFAULT_STREAM_RATE , DEFAULT_CHANNEL_CONFIG , DEFAULT_AUDIO_FORMAT);
    }


    public boolean startPlayer(int streamType , int sampleRate , int channelConfig , int audioFormat){

        if (mIsPlayStarted){
            Log.e(TAG, "Player already started !");
            return false;
        }

        int bufferSizeInBytes = AudioTrack.getMinBufferSize(sampleRate , channelConfig , audioFormat);
        if (bufferSizeInBytes == AudioTrack.ERROR_BAD_VALUE){
            Log.e(TAG, "getMinBufferSize Invalid parameter !");
            return false;
        }
        Log.i(TAG, "getMinBufferSize = " + bufferSizeInBytes + " bytes !");

        mAudioTrack = new AudioTrack(streamType , sampleRate , channelConfig , audioFormat , bufferSizeInBytes , DEFAULT_PLAY_MODE);
        if (mAudioTrack.getState() == AudioTrack.STATE_UNINITIALIZED){
            Log.e(TAG, "AudioTrack initialize fail !");
            return false;
        }
        mIsPlayStarted = true;
        Log.w(TAG, "Start audio player success !");

        return true;
    }


    public void stopPlayer(){

        if (!mIsPlayStarted){
            return;
        }

        if (mAudioTrack.getState() == AudioTrack.PLAYSTATE_PLAYING){
            mAudioTrack.stop();
        }
        mAudioTrack.release();
        mIsPlayStarted = false;
        Log.w(TAG, "Stop audio player success !");

    }


    public boolean play(byte[] audioData , int offsetInBytes , int sizeInBytes){
        if (!mIsPlayStarted) {
            Log.e(TAG, "Player not started !");
            return false;
        }

        if (mAudioTrack.write(audioData , offsetInBytes , sizeInBytes) != sizeInBytes){
            Log.e(TAG, "Could not write all the samples to the audio device !");
        }

        mAudioTrack.play();
        Log.d(TAG, "OK, Played " + sizeInBytes + " bytes !");
        return true;
    }

}
