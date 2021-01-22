package com.example.androidaccessibilityservice.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;

public class Utils {

    public static int getColorWrapper(Context context, int id){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    public Drawable getDrawable(Context context, int id){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getDrawable(id) ;
        } else {
            return context.getResources().getDrawable(id);
        }
    }

}
