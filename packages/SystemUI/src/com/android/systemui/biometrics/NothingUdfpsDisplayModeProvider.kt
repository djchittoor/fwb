package com.android.systemui.biometrics

import android.util.Slog
import android.util.Log
import java.io.FileOutputStream
import java.io.IOException
import com.android.systemui.biometrics.UdfpsDisplayModeProvider
import com.android.systemui.dagger.SysUISingleton
import com.android.systemui.dagger.qualifiers.DisplayId
import javax.inject.Inject

@SysUISingleton
class NothingUdfpsDisplayModeProvider: UdfpsDisplayModeProvider {
    private val HBM_PATH = "/sys/class/drm/sde-conn-1-DSI-1/hbm_mode"
    private val OLD_HBM_PATH = "/sys/class/backlight/panel0-backlight/hbm_mode"
    private val TAG = "NtHBMProviderImpl"
    private var mHbmEnabled = false

    override fun enable(isEnabled: Runnable?) {
        if (mHbmEnabled) {
            return
        }
        if (runnable != null) {
            runnable.run()
        }
        val fileOutputStream: FileOutputStream
        try {
            fileOutputStream = FileOutputStream(HBM_PATH)
            fileOutputStream.write("1".toByteArray())
            fileOutputStream.close()
            mHbmEnabled = true
        } catch (e: IOException) {
            try {
                fileOutputStream = FileOutputStream(OLD_HBM_PATH)
                fileOutputStream.write("1".toByteArray())
                fileOutputStream.close()
                mHbmEnabled = true
            } catch (e2: IOException) {
                Slog.e(TAG, "Could not write to hbm_mode file", e2)
            }
        }
    }

    override fun disable() {
        if (!mHbmEnabled) {
            return
        }
        val fileOutputStream: FileOutputStream
        try {
            fileOutputStream = FileOutputStream(HBM_PATH)
            fileOutputStream.write("0".toByteArray())
            fileOutputStream.close()
            mHbmEnabled = false
        } catch (e: IOException) {
            try {
                fileOutputStream = FileOutputStream(OLD_HBM_PATH)
                fileOutputStream.write("0".toByteArray())
                fileOutputStream.close()
                mHbmEnabled = false
            } catch (e2: IOException) {
                Slog.e(TAG, "Could not write to hbm_mode file", e2)
            }
        }
    }
}
