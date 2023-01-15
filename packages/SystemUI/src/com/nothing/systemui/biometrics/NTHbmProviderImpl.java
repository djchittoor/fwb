package com.nothing.systemui.biometrics;

import android.util.Slog;
import com.android.systemui.biometrics.UdfpsHbmProvider;
import android.util.Log;
import java.io.FileOutputStream;
import java.io.IOException;
/* loaded from: classes3.dex */
public class NTHbmProviderImpl implements UdfpsHbmProvider {
    private static final String HBM_PATH = "/sys/class/drm/sde-conn-1-DSI-1/hbm_mode";
    private static final String OLD_HBM_PATH = "/sys/class/backlight/panel0-backlight/hbm_mode";
    private static final String TAG = "NtHBMProviderImpl";
    private boolean mHbmEnabled;

    @Override // com.android.systemui.biometrics.UdfpsHbmProvider
    public void enableHbm(boolean z, Runnable runnable) {
        if (this.mHbmEnabled) {
            return;
        }
        if (runnable != null) {
            runnable.run();
        }
        this.mHbmEnabled = true;
        Log.i(TAG, "------HBM ENABLED-1---------");
    }

    @Override // com.android.systemui.biometrics.UdfpsHbmProvider
    public void disableHbm(Runnable runnable) {
        if (!this.mHbmEnabled) {
            return;
        }
        if (runnable != null) {
            runnable.run();
        }
        this.mHbmEnabled = false;
        Log.i(TAG, "------HBM DISABLED---------");
    }

    @Override // com.android.systemui.biometrics.UdfpsHbmProvider
    public void disableHbm() {
        Log.i(TAG, "------HBM DISABLED-------disableHbm--");
    }

    @Override // com.android.systemui.biometrics.UdfpsHbmProvider
    public void enableHbm() {
        if (this.mHbmEnabled) {
            return;
        }
        this.mHbmEnabled = true;
        Log.i(TAG, "------HBM ENABLED----------");
    }

    @Override // com.android.systemui.biometrics.UdfpsHbmProvider
    public boolean isHbmEnabled() {
        return this.mHbmEnabled;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v7, types: [java.lang.StringBuilder] */
    private void writeHbmNode(boolean z) {
        boolean z2;
        FileOutputStream fileOutputStream;
        String str = z ? "1" : "0";

	try{
             fileOutputStream = new FileOutputStream(HBM_PATH);
             fileOutputStream.write(str.getBytes());
             fileOutputStream.close();
	     Slog.e(TAG, "sent hbm_path");
            }catch(Exception e){
		Slog.e(TAG, e.getMessage());
	    }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v0 */
    /* JADX WARN: Type inference failed for: r2v1 */
    /* JADX WARN: Type inference failed for: r2v2, types: [java.io.FileOutputStream] */
    /* JADX WARN: Type inference failed for: r2v4, types: [java.lang.String] */
    private void writeOldHbmNode(boolean z) {
        boolean z2;
        FileOutputStream fileOutputStream;
        String str = z ? "1" : "0";
        try{
             fileOutputStream = new FileOutputStream(OLD_HBM_PATH);
             fileOutputStream.write(str.getBytes());
             fileOutputStream.close();
             Slog.e(TAG, "sent hbm_path");
            }catch(Exception e){
                Slog.e(TAG, e.getMessage());
            }
    }

}
