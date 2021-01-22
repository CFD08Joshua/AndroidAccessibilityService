package com.example.androidaccessibilityservice.view.circularprogressbutton;

public interface AnimatedButton {
    void startAnimation();
    void revertAnimation();
    void revertAnimation(final OnAnimationEndListener onAnimationEndListener);
    void dispose();
    void setProgress(int progress);
    void resetProgress();
}
