package com.android.systemui.biometrics

import android.content.Context
import android.util.Slog
import android.util.Log
import java.io.FileOutputStream
import java.io.IOException
import com.android.systemui.biometrics.UdfpsDisplayModeProvider

class NothingUdfpsDisplayModeProvider constructor (private val context: Context) : UdfpsDisplayModeProvider {
    private val HBM_PATH = "/sys/class/drm/sde-conn-1-DSI-1/hbm_mode"
    private val OLD_HBM_PATH = "/sys/class/backlight/panel0-backlight/hbm_mode"
    private val TAG = "NtHBMProviderImpl"
    private var mHbmEnabled = false

    override fun enable(onEnabled: Runnable?) {
        if (mHbmEnabled) {
            return
        }
        val fileOutputStream: FileOutputStream = FileOutputStream(HBM_PATH)
        try {
            fileOutputStream.write("1".toByteArray())
            fileOutputStream.close()
            mHbmEnabled = true
        } catch (e: IOException) {
            try {
                val oldFileOutputStream = FileOutputStream(OLD_HBM_PATH)
                oldFileOutputStream.write("0".toByteArray())
                oldFileOutputStream.close()
                mHbmEnabled = true
            } catch (e2: IOException) {
                Slog.e(TAG, "Could not write to hbm_mode file", e2)
            }
        }
        onEnabled?.run()
    }

    override fun disable(onDisabled: Runnable?) {
        if (!mHbmEnabled) {
            return
        }
        val fileOutputStream: FileOutputStream = FileOutputStream(HBM_PATH)
        try {
            fileOutputStream.write("0".toByteArray())
            fileOutputStream.close()
            mHbmEnabled = false
        } catch (e: IOException) {
            try {
                val oldFileOutputStream = FileOutputStream(OLD_HBM_PATH)
                oldFileOutputStream.write("0".toByteArray())
                oldFileOutputStream.close()
                mHbmEnabled = false
            } catch (e2: IOException) {
                Slog.e(TAG, "Could not write to hbm_mode file", e2)
            }
        }
        onDisabled?.run()
    }
}
