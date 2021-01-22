package com.example.androidaccessibilityservice.util;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.view.accessibility.AccessibilityManager;

import androidx.annotation.RequiresApi;

import java.util.List;

public class PermissionUtil {


    /**
     * 是否可以繪製疊加層
     *
     * @param context
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isCanDrawOverlays(Context context) {
        return Settings.canDrawOverlays(context);
    }

    /**
     * 是否設定可以設置
     * @param context
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isSettingsCanWrite(Context context) {
        return Settings.System.canWrite(context);
    }

    /**
     * 是否啟用了輔助功能
     * @param context
     * @return
     */
    public static boolean isAccessibilityServiceEnable(Context context) {
        AccessibilityManager accessibilityManager =
                (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        assert accessibilityManager != null;
        List<AccessibilityServiceInfo> accessibilityServiceInfoList =
                accessibilityManager.getEnabledAccessibilityServiceList(
                        AccessibilityServiceInfo.FEEDBACK_ALL_MASK);
        for (AccessibilityServiceInfo info : accessibilityServiceInfoList) {
            if (info.getId().contains(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }
}
