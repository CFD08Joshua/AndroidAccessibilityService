package com.example.androidaccessibilityservice.view.circularprogressbutton;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

public class BackgroundAndMorphingDrawables {
    Drawable backGroundDrawable;
    GradientDrawable morphingDrawable;

    void setBothDrawables(GradientDrawable drawable) {
        this.backGroundDrawable = drawable;
        this.morphingDrawable = drawable;
    }
}
