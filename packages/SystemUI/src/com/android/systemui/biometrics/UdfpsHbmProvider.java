package com.android.systemui.biometrics;
public interface UdfpsHbmProvider {
    void disableHbm();

    void disableHbm(Runnable runnable);

    void enableHbm();

    void enableHbm(boolean z, Runnable runnable);

    boolean isHbmEnabled();
}
