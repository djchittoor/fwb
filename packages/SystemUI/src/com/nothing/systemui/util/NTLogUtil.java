package com.nothing.systemui.util;

import android.util.Log;
/* loaded from: classes3.dex */
public class NTLogUtil {
    public static final String TAG = "SystemUI";
    private static String sSeparator = "-->";

    public static void v(String str, String str2) {
        Log.v(TAG, str + sSeparator + str2);
    }

    public static void d(String str, String str2) {
        Log.d(TAG, str + sSeparator + str2);
    }

    public static void i(String str, String str2) {
        Log.i(TAG, str + sSeparator + str2);
    }

    public static void w(String str, String str2) {
        Log.w(TAG, str + sSeparator + str2);
    }

    public static void e(String str, String str2) {
        Log.e(TAG, str + sSeparator + str2);
    }

    public static String getCallStack(int i) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StringBuilder sb = new StringBuilder();
        sb.mo6014append("----getCallStack total depth = " + i + ", print depth = " + stackTrace.length + "----\n");
        int i2 = 0;
        for (StackTraceElement stackTraceElement : stackTrace) {
            if (i2 >= i) {
                break;
            }
            sb.mo6014append("    " + i2 + ": " + stackTraceElement.toString() + "\n");
            i2++;
        }
        return sb.toString();
    }
}
