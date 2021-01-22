package com.example.androidaccessibilityservice.util;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;

/**
 * A util to get and set sys brightness
 */
public class SystemBrightnessUtil {

    /**
     * 獲得亮度
     * @param context
     * @return
     */
    public static int getBrightness(Context context){
        int brightValue = 0;
        ContentResolver contentResolver = context.getContentResolver();
        try {
            brightValue = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e){
            e.printStackTrace();
        }
        return brightValue;
    }

    /**
     * 設置亮度
     * @param context
     * @param brightValue
     */
    public static void setBrightness(Context context , int brightValue){
        try {
            // change brightness mode
            Settings.System.putInt(context.getContentResolver(),Settings.System.SCREEN_BRIGHTNESS_MODE,0);
            Settings.System.putInt(context.getContentResolver(),Settings.System.SCREEN_BRIGHTNESS_MODE,brightValue);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
