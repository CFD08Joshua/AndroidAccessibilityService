package com.example.androidaccessibilityservice.ui.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.androidaccessibilityservice.R;
import com.example.androidaccessibilityservice.util.PermissionUtil;

public class SideBarFragment extends Fragment implements View.OnClickListener {

    private AppCompatButton mFloatWindowButton;
    private AppCompatButton mAccessibilityButton;
    //  TODO
    private static final int FLOAT_REQUEST_CODE = 213;
    private static final int ACCESSIBILITY_REQUEST_CODE = 438;

    public static SideBarFragment newInstance(){
        SideBarFragment sideBarFragment = new SideBarFragment();
        return sideBarFragment;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_side_bar,container,false);

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mFloatWindowButton = view.findViewById(R.id.btn_float_window);
        mAccessibilityButton = view.findViewById(R.id.btn_accessibility);
        mFloatWindowButton.setOnClickListener(this);
        mAccessibilityButton.setOnClickListener(this);
        floatWindowVisible();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FLOAT_REQUEST_CODE) {
            floatWindowVisible();
        } else {
            accessibilityVisible();
        }
    }

    private void floatWindowVisible() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionUtil.isCanDrawOverlays(getContext())) {
                mFloatWindowButton.setVisibility(View.GONE);
                accessibilityVisible();
            } else {
                mFloatWindowButton.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), getString(R.string.warn_user_open_float_window), Toast.LENGTH_SHORT).show();
            }
        } else {
            mFloatWindowButton.setVisibility(View.GONE);
            accessibilityVisible();
        }
    }

    /**
     * 顯示輔助功能
     */
    private void accessibilityVisible() {
        if (PermissionUtil.isAccessibilityServiceEnable(getContext())) {
            Toast.makeText(getContext(), getString(R.string.permission_notice), Toast.LENGTH_SHORT).show();
        } else {
            mAccessibilityButton.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), getString(R.string.permission_accessibility_), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_float_window:
                intentFloatWindow();
                break;
            case R.id.btn_accessibility:
                intentAccessibility();
                break;
            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void intentFloatWindow() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        startActivityForResult(intent, FLOAT_REQUEST_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void intentAccessibility() {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivityForResult(intent, ACCESSIBILITY_REQUEST_CODE);
    }
}
