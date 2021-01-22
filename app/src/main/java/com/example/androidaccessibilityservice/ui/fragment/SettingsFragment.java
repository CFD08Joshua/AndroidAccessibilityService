/*
 * Copyright © 2016 - 2018 by GitHub.com/JasonQS
 * anti-recall.qsboy.com
 * All Rights Reserved
 */

package com.example.androidaccessibilityservice.ui.fragment;

import android.Manifest;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.example.androidaccessibilityservice.R;
import com.example.androidaccessibilityservice.service.MainService;
import com.example.androidaccessibilityservice.ui.App;
import com.example.androidaccessibilityservice.util.AccessNotification;
import com.example.androidaccessibilityservice.util.SettingAccessibilityService;
import com.example.androidaccessibilityservice.view.SettingsCompat;
import com.example.androidaccessibilityservice.view.circularprogressbutton.CircularProgressButton;

import java.io.File;
import java.util.Date;

public class SettingsFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {

    String TAG = "SettingsFragment";
    Handler handler = new Handler();
    SettingAccessibilityService mSettingAccessibilityService ;
    AccessNotification mAccessNotification;

    public static SettingsFragment newInstance(){
        SettingsFragment settingsFragment = new SettingsFragment();
        return settingsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        ScrollView view = (ScrollView) inflater.inflate(R.layout.fragment_settings, container, false);
        initSetting();
        initPermissionCheck(view);
        return view;
    }

    private void initSetting(){
        mSettingAccessibilityService = new SettingAccessibilityService(getContext());
        mAccessNotification = new AccessNotification(getContext());
    }


    private void initPermissionCheck(ScrollView view) {
        // 跳转
        View btnAccessibilityService = view.findViewById(R.id.btn_navigate_accessibility_service);
        View btnNotificationListener = view.findViewById(R.id.btn_navigate_notification_listener);
        View btnOverlays = view.findViewById(R.id.btn_navigate_overlays);

        btnAccessibilityService.setOnClickListener(v -> mSettingAccessibilityService.jumpToAccessSetting());

        btnNotificationListener.setOnClickListener(v -> mAccessNotification.isNotificationListenerSettingEnabled());

        btnOverlays.setOnClickListener(v -> SettingsCompat.manageDrawOverlays(getActivity()));

        // 检查权限
        CircularProgressButton btnCheckPermission = view.findViewById(R.id.btn_check_permission);
        btnCheckPermission.setOnClickListener(v -> {

            btnCheckPermission.performAccessibilityAction(AccessibilityEvent.TYPE_VIEW_CLICKED, null);
            btnCheckPermission.startAnimation();
            ViewGroup llPermission = view.findViewById(R.id.ll_permission);
            llPermission.removeAllViews();
            llPermission.addView((View) btnCheckPermission.getParent());

            //  無障礙設定
            boolean accessibilityServiceSettingEnabled = mSettingAccessibilityService.isAccessibilityServiceSettingEnabled();
            boolean accessibilityServiceIsWork = mSettingAccessibilityService.isAccessibilityServiceWork();
            handler.postDelayed(() -> addView(
                    llPermission,
                    "辅助功能权限授予",
                    accessibilityServiceSettingEnabled && !accessibilityServiceIsWork ? " --请尝试重新打开开关" : null,
                    accessibilityServiceIsWork,
                    v1 -> mSettingAccessibilityService.jumpToAccessSetting()), 500);

            boolean notificationListenerSettingEnabled = mAccessNotification.isNotificationListenerSettingEnabled();
            boolean notificationListenerIsWork = mAccessNotification.isNotificationListenerWork();

            handler.postDelayed(() -> addView(llPermission, "通知监听服务正常工作",
                    notificationListenerSettingEnabled && !notificationListenerIsWork ? " --请尝试重新打开开关" : null,
                    notificationListenerIsWork,
                    v1 -> mAccessNotification.jumpToNotificationListenerSetting()), 1000);

            handler.postDelayed(() -> addView(llPermission, "悬浮窗权限", null,
                    checkFloatingPermission(),
                    v1 -> SettingsCompat.manageDrawOverlays(getActivity())), 1500);

            handler.postDelayed(() -> addView(llPermission, "外部文件访问权限", null,
                    checkWriteExternalStoragePermission(),
                    v1 -> requestWriteExternalStorage()), 2000);

            handler.postDelayed(() -> {
                if (checkFloatingPermission() && checkWriteExternalStoragePermission() && accessibilityServiceSettingEnabled &&
                        accessibilityServiceIsWork && notificationListenerSettingEnabled && notificationListenerIsWork) {
                    btnCheckPermission.doneLoadingAnimation(
                            getResources().getColor(R.color.colorCorrect),
                            getBitmap(R.drawable.ic_accept));
                } else {
                    btnCheckPermission.doneLoadingAnimation(
                            getResources().getColor(R.color.colorError),
                            getBitmap(R.drawable.ic_cancel));
                }
            }, 2500);

            handler.postDelayed(btnCheckPermission::revertAnimation, 5000);
        });
    }


    private void requestWriteExternalStorage() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
    }

    private boolean checkFloatingPermission() {
//        XToast.build(getContext(),"hello").show();
//        return Settings.canDrawOverlays(getContext());
        return SettingsCompat.canDrawOverlays(getContext());
//        return new CheckAuthority(getContext()).checkAlertWindowPermission();
    }

    private boolean checkWriteExternalStoragePermission() {
        return (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(getContext(), "该权限能查看撤回的图片", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void addView(ViewGroup mainView, String content, String hint, boolean isChecked, View.OnClickListener onClickListener) {

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.item_check_permission, mainView, false);
        LinearLayout llPermission = view.findViewById(R.id.ll_permission);
        TextView tvPermission = view.findViewById(R.id.tv_permission_content);
        ImageView ivChecked = view.findViewById(R.id.iv_checked);
        ImageView ivFix = view.findViewById(R.id.iv_fix);

        tvPermission.setText(content);
        if (hint != null) {
            TextView tvPermissionHint = new TextView(getContext());
            tvPermissionHint.setText(hint);
            llPermission.addView(tvPermissionHint);
        }
        if (isChecked) {
            ivChecked.setImageResource(R.drawable.ic_accept);
            ivChecked.setColorFilter(0xCC22DD22);
            ivFix.setVisibility(View.GONE);
        } else {
            ivChecked.setImageResource(R.drawable.ic_cancel);
            ivChecked.setColorFilter(0xAADD2222);
        }

        if (!isChecked) {
            view.setOnClickListener(onClickListener);
        }
        mainView.addView(view);
    }

    private Bitmap getBitmap(int drawableRes) {
        Drawable drawable = VectorDrawableCompat.create(getResources(), drawableRes, null);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return bitmap;
    }

}
