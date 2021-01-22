/*
 * Copyright © 2016 - 2018 by GitHub.com/JasonQS
 * anti-recall.qsboy.com
 * All Rights Reserved
 */

package com.example.androidaccessibilityservice.service;

import android.accessibilityservice.AccessibilityService;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.Date;
import java.util.List;

public class MainService extends AccessibilityService {


    private String TAG = "Main Service";
    private String packageName;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        try {
            if (event.getPackageName() == null) {
                Log.d(TAG, "onAccessibilityEvent: package name is null, return");
                return;
            }
            packageName = event.getPackageName() + "";


            int eventType = event.getEventType();
            if (eventType != AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
                Log.w(TAG, AccessibilityEvent.eventTypeToString(eventType));
            } else {
                Log.v(TAG, AccessibilityEvent.eventTypeToString(eventType));
            }

            switch (eventType) {
                case AccessibilityEvent.TYPE_VIEW_CLICKED:
                    onClick(event);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void onClick(AccessibilityEvent event) {
        Log.i(TAG, "onClick " + event.getText());
        if (event.getSource() == null) {
            Log.i(TAG, "onClick: event.getSource() is null, return");
            return;
        }
        AccessibilityNodeInfo root = getRootInActiveWindow();
    }



    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.e(TAG, "onServiceConnected");
    }

    @Override
    public void onInterrupt() {

    }


}
