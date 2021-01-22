package com.example.androidaccessibilityservice.util;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.example.androidaccessibilityservice.service.MainService;
import com.example.androidaccessibilityservice.ui.App;

public class SettingAccessibilityService {

    Context context;

    public SettingAccessibilityService(Context context) {
        this.context = context;
    }

    public boolean isAccessibilityServiceSettingEnabled() {
        if (context == null) {
            return false;
        }
        final String service = context.getPackageName() + "/" + MainService.class.getCanonicalName();
        int accessibilityEnabled = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED, 0);
        if (accessibilityEnabled != 1) {
            return false;
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');
        String settingValue = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);

        mStringColonSplitter.setString(settingValue);
        while (mStringColonSplitter.hasNext()) {
            String accessibilityService = mStringColonSplitter.next();
            if (accessibilityService.equalsIgnoreCase(service)) {
                return true;
            }
        }

        return false;
    }

    public boolean isAccessibilityServiceWork() {
        return (System.currentTimeMillis() - App.timeCheckAccessibilityServiceIsWorking) < 5000;
    }

    public void jumpToAccessSetting(){
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
