package com.example.androidaccessibilityservice.service;

import android.content.Context;
import android.content.Intent;
/**
 * launch the sidebar service
 */
public class StartService {

    public static void launchAccessibility(Context context) {
        Intent intent = new Intent(context, MyAccessibilityService.class);
        context.startService(intent);
    }
}
