package com.example.androidaccessibilityservice.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.androidaccessibilityservice.R;
import com.example.androidaccessibilityservice.ui.App;

public class AccessNotification {

    Context context;

    public AccessNotification(Context context) {
        this.context = context;
    }

    public boolean isNotificationListenerSettingEnabled() {
        if (context == null) {
            return false;
        }
        String notificationEnable = Settings.Secure.getString(context.getContentResolver(), "enabled_notification_listeners");
        if (TextUtils.isEmpty(notificationEnable)) {
            return false;
        }
        for (String name : notificationEnable.split(":")) {
            ComponentName componentName = ComponentName.unflattenFromString(name);
            if (componentName != null) {
                if (TextUtils.equals(context.getPackageName(), componentName.getPackageName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isNotificationListenerWork() {
        FragmentActivity fragmentActivity = (FragmentActivity) context;
        if (fragmentActivity == null) {
            return false;
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            return false;
        }
        notificationManager.cancel(12);

        boolean isWork = (System.currentTimeMillis() - App.timeCheckNotificationListenerServiceIsWorking) < 5000;

        return isWork;
    }

    public void jumpToNotificationListenerSetting() {
        Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void sendNotification() {
        FragmentActivity fragmentActivity = (FragmentActivity) context;
        if (fragmentActivity == null) {
            return;
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("1", "1", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        Notification notification = new NotificationCompat.Builder(fragmentActivity, "1")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("title")
                .setContentText("test")
                .setDefaults(Notification.FLAG_ONLY_ALERT_ONCE)
                .build();

        notificationManager.notify(12, notification);

    }

}
