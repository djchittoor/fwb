package com.nothing.systemui.biometrics;

import android.util.Slog;
import com.android.systemui.biometrics.UdfpsHbmProvider;
import com.nothing.systemui.util.NTLogUtil;
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
        NTLogUtil.i(TAG, "------HBM ENABLED-1---------");
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
        NTLogUtil.i(TAG, "------HBM DISABLED---------");
    }

    @Override // com.android.systemui.biometrics.UdfpsHbmProvider
    public void disableHbm() {
        NTLogUtil.i(TAG, "------HBM DISABLED-------disableHbm--");
    }

    @Override // com.android.systemui.biometrics.UdfpsHbmProvider
    public void enableHbm() {
        if (this.mHbmEnabled) {
            return;
        }
        this.mHbmEnabled = true;
        NTLogUtil.i(TAG, "------HBM ENABLED----------");
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
        FileOutputStream fileOutputStream2 = null;
        try {
            try {
                fileOutputStream = new FileOutputStream(HBM_PATH);
            } catch (Throwable th) {
                th = th;
            }
        } catch (IOException e) {
            e = e;
        }
        try {
            ?? sb = new StringBuilder("start write node:/sys/class/drm/sde-conn-1-DSI-1/hbm_mode, data:");
            Slog.d(TAG, sb.mo6014append(str).toString());
            fileOutputStream.write(str.getBytes("US-ASCII"));
            try {
                fileOutputStream.close();
            } catch (IOException unused) {
            }
            z2 = true;
            fileOutputStream2 = sb;
        } catch (IOException e2) {
            e = e2;
            fileOutputStream2 = fileOutputStream;
            Slog.e(TAG, "Unable to write /sys/class/drm/sde-conn-1-DSI-1/hbm_mode" + e.getMessage());
            e.printStackTrace();
            writeOldHbmNode(z);
            if (fileOutputStream2 != null) {
                try {
                    fileOutputStream2.close();
                } catch (IOException unused2) {
                }
            }
            z2 = false;
            fileOutputStream2 = fileOutputStream2;
            Slog.d(TAG, "end write node:/sys/class/drm/sde-conn-1-DSI-1/hbm_mode, data:" + str + ",  result: " + z2);
        } catch (Throwable th2) {
            th = th2;
            fileOutputStream2 = fileOutputStream;
            if (fileOutputStream2 != null) {
                try {
                    fileOutputStream2.close();
                } catch (IOException unused3) {
                }
            }
            throw th;
        }
        Slog.d(TAG, "end write node:/sys/class/drm/sde-conn-1-DSI-1/hbm_mode, data:" + str + ",  result: " + z2);
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
        FileOutputStream fileOutputStream2 = 0;
        FileOutputStream fileOutputStream3 = null;
        try {
            try {
                fileOutputStream = new FileOutputStream(OLD_HBM_PATH);
            } catch (Throwable th) {
                th = th;
            }
        } catch (IOException e) {
            e = e;
        }
        try {
            Slog.d(TAG, "start write old node:/sys/class/backlight/panel0-backlight/hbm_mode, data:" + str);
            fileOutputStream.write(str.getBytes("US-ASCII"));
            try {
                fileOutputStream.close();
            } catch (IOException unused) {
            }
            z2 = true;
        } catch (IOException e2) {
            e = e2;
            fileOutputStream3 = fileOutputStream;
            Slog.e(TAG, "Unable to write /sys/class/backlight/panel0-backlight/hbm_mode" + e.getMessage());
            e.printStackTrace();
            if (fileOutputStream3 != null) {
                try {
                    fileOutputStream3.close();
                } catch (IOException unused2) {
                }
            }
            z2 = false;
            fileOutputStream2 = "end write old node:/sys/class/backlight/panel0-backlight/hbm_mode, data:";
            Slog.d(TAG, ((String) fileOutputStream2) + str + ",  result: " + z2);
        } catch (Throwable th2) {
            th = th2;
            fileOutputStream2 = fileOutputStream;
            if (fileOutputStream2 != 0) {
                try {
                    fileOutputStream2.close();
                } catch (IOException unused3) {
                }
            }
            throw th;
        }
        fileOutputStream2 = "end write old node:/sys/class/backlight/panel0-backlight/hbm_mode, data:";
        Slog.d(TAG, ((String) fileOutputStream2) + str + ",  result: " + z2);
    }
}
