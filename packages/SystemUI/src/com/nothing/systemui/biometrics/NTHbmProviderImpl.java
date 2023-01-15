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
        if (!this.mHbmEnabled) {
            return;
        }
        this.mHbmEnabled = false;
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


    private void writeHbmNode(boolean z) {
        String str = z ? "1" : "0";
        boolean result = false;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(HBM_PATH);
            Slog.d(TAG, "start write node:" + HBM_PATH + ", data:" + str);
            fileOutputStream.write(str.getBytes("US-ASCII"));
            result = true;
        } catch (IOException e) {
            Slog.e(TAG, "Unable to write " + HBM_PATH + e.getMessage());
	    writeOldHbmNode(z);
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                }
            }
        }
        Slog.d(TAG, "end write node:" + OLD_HBM_PATH + ", data:" + str + ",  result: " + result);
    }

    private void writeOldHbmNode(boolean z) {
        String str = z ? "1" : "0";
        boolean result = false;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(OLD_HBM_PATH);
            Slog.d(TAG, "start write node:" + OLD_HBM_PATH + ", data:" + str);
            fileOutputStream.write(str.getBytes("US-ASCII"));
            result = true;
        } catch (IOException e) {
            Slog.e(TAG, "Unable to write " + OLD_HBM_PATH + e.getMessage());
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                }
            }
        }
        Slog.d(TAG, "end write node:" + OLD_HBM_PATH + ", data:" + str + ",  result: " + result);
    }

}
