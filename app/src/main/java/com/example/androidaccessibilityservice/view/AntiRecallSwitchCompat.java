package com.example.androidaccessibilityservice.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import java.lang.reflect.Field;

public class AntiRecallSwitchCompat extends SwitchCompat {
    public AntiRecallSwitchCompat(@NonNull Context context) {
        super(context);
    }

    public AntiRecallSwitchCompat(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AntiRecallSwitchCompat(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // 利用反射把屬性保存在application裡
    public AntiRecallSwitchCompat setAttr(Class app, String booleanFieldName) {
        try {
            Field field = app.getDeclaredField(booleanFieldName);
            setChecked(field.getBoolean(app));
            setOnClickListener(v -> {
                try {
                    field.set(app, isChecked());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return this;
    }
}
