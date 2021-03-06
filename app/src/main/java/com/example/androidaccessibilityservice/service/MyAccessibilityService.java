package com.example.androidaccessibilityservice.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentFilter;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.LinearLayout;

import com.example.androidaccessibilityservice.receiver.SideBarHideReceiver;
import com.example.androidaccessibilityservice.view.SideBarArrow;

public class MyAccessibilityService extends AccessibilityService {

    private SideBarHideReceiver mReceiver;
    private SideBarArrow mRightArrowBar;
    private SideBarArrow mLeftArrowBar;

    private static final String ACTION_HIDE = "com.xunfeivr.maxsidebar.ACTION_HIDE";

    @Override
    public void onCreate() {
        super.onCreate();
        createToucher();
    }

    @SuppressLint({"RtlHardcoded","InflateParams"})
    private void createToucher() {
        // get window manager
        WindowManager windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        // right arrow
        mRightArrowBar = new SideBarArrow();
        LinearLayout mArrowRight = mRightArrowBar.getView(this, false, windowManager, this);
        // left arrow
        mLeftArrowBar = new SideBarArrow();
        LinearLayout mArrowLeft = mLeftArrowBar.getView(this, true, windowManager, this);
        // handler another bar
        mRightArrowBar.setAnotherArrowBar(mArrowLeft);
        mLeftArrowBar.setAnotherArrowBar(mArrowRight);
        // register
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_HIDE);
        mReceiver = new SideBarHideReceiver();
        mReceiver.setSideBar(mLeftArrowBar, mRightArrowBar);
        registerReceiver(mReceiver, filter);
    }

    @Override
    public void onDestroy() {
        mRightArrowBar.clearAll();
        mLeftArrowBar.clearAll();
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {

    }

}
