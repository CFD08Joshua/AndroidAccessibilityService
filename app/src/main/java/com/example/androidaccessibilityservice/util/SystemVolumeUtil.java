package com.example.androidaccessibilityservice.util;

import android.content.Context;
import android.media.AudioManager;

public class SystemVolumeUtil {

    private static AudioManager audioManager;

    /**
     * @param context
     * music volume range 0-15
     */
    public static int getVolume(Context context) {
        if(null == audioManager) {
            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }
        return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    public static void setVolume(Context context,int volumeValue) {
        if(null == audioManager) {
            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,volumeValue,AudioManager.FLAG_PLAY_SOUND);
    }
}
