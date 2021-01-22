package com.example.androidaccessibilityservice.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.androidaccessibilityservice.service.StartService;
import com.example.androidaccessibilityservice.ui.fragment.SideBarFragment;
import com.example.androidaccessibilityservice.util.PermissionUtil;

/**
 * device boot receiver
 */
public class DeviceBootReceiver extends BroadcastReceiver {

    private static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_BOOT)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(PermissionUtil.isCanDrawOverlays(context) && PermissionUtil.isAccessibilityServiceEnable(context)) {
                    StartService.launchAccessibility(context);
                }else {
                    MainPageGo(context);
                }
            }else {
                if(PermissionUtil.isAccessibilityServiceEnable(context)) {
                    StartService.launchAccessibility(context);
                }else {
                    MainPageGo(context);
                }
            }
        }
    }

    private void MainPageGo(Context context) {
        Intent launch = new Intent(context, SideBarFragment.class);
        launch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(launch);
    }

}
