package com.example.androidaccessibilityservice.ui;

import android.app.Application;
import android.content.Context;

import java.util.Date;

public class App extends Application {


    /**
     * 检查权限按钮的点击时间
     */
    public static long timeCheckAccessibilityServiceIsWorking = 0;
    public static long timeCheckNotificationListenerServiceIsWorking = 0;

}
