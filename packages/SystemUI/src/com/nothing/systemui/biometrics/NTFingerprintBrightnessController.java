package com.nothing.systemui.biometrics;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.provider.Settings;
import com.android.systemui.R;
import com.android.systemui.biometrics.UdfpsController;
import android.util.Log;
/* loaded from: classes3.dex */
public class NTFingerprintBrightnessController {
    private static final String TAG = "FpBrightnessController";
    private static final Uri URI_SCREEN_BRIGHTNESS = Settings.System.getUriFor("screen_brightness");
    private final float BRIGHTNESS_SCALE_RATE;
    private AlphaCallback mAlphaCallback;
    private int[] mAlphaMap;
    protected ContentResolver mContentResolver;
    private Context mContext;
    private boolean mDelayDraw;
    private final HandlerThread mFpThread;
    private final Handler mHandler;
    private final LocalObserver mLocalObserver;
    private NTFingerprintDimLayer mNTFingerprintDimLayer;
    private UdfpsController mUdfpsController;
    private final Handler mWorkerHandler;
    private DimLayerRunnable mDimLayerRunnable = new DimLayerRunnable(0.0f);
    private DimLayerUpdateAlphaRunnable mDimLayerUpdateAlphaRunnable = new DimLayerUpdateAlphaRunnable(0.0f);
    private int mCurrentBrightness = -2;

    /* loaded from: classes3.dex */
    public interface AlphaCallback {
        void onAlpha(float f);
    }

    public NTFingerprintBrightnessController(Context context, UdfpsController udfpsController) {
        Handler handler = new Handler(Looper.getMainLooper());
        this.mHandler = handler;
        this.BRIGHTNESS_SCALE_RATE = 13.784314f;
        this.mContext = context;
        this.mContentResolver = context.getContentResolver();
        this.mNTFingerprintDimLayer = new NTFingerprintDimLayer(context, udfpsController);
        HandlerThread handlerThread = new HandlerThread(TAG, -2);
        this.mFpThread = handlerThread;
        handlerThread.start();
        this.mWorkerHandler = new Handler(handlerThread.getLooper());
        this.mUdfpsController = udfpsController;
        this.mAlphaMap = context.getResources().getIntArray(R.array.nt_udfps_alpha_array);
        this.mLocalObserver = new LocalObserver(handler);
    }

    public void dismiss() {
        Log.d(TAG, "=dismiss=");
        this.mWorkerHandler.removeCallbacks(this.mDimLayerRunnable);
        this.mWorkerHandler.post(new Runnable() { // from class: com.nothing.systemui.biometrics.NTFingerprintBrightnessController.1
            @Override // java.lang.Runnable
            public void run() {
                Log.d(NTFingerprintBrightnessController.TAG, "===dismiss dimlayer===");
                NTFingerprintBrightnessController.this.mNTFingerprintDimLayer.dismiss();
            }
        });
        this.mLocalObserver.unregister();
        this.mCurrentBrightness = -2;
    }

    public boolean needUpdateAlpha() {
        boolean z = this.mCurrentBrightness != getScreenBrightnessInt();
        Log.d(TAG, "=needUpdateAlpha=" + z);
        return z;
    }

    public void updateDimLayerAlphaIfNeed() {
        if (needUpdateAlpha()) {
            int screenBrightnessInt = getScreenBrightnessInt();
            float f = 1.0f;
            if (screenBrightnessInt == -1) {
                this.mDelayDraw = true;
            } else if (screenBrightnessInt != -2) {
                f = getAlpha(screenBrightnessInt);
            }
            Log.d(TAG, "updateDimLayerAlphaIfNeed brightness=" + screenBrightnessInt + ", alpha=" + f);
            this.mCurrentBrightness = screenBrightnessInt;
            this.mDimLayerUpdateAlphaRunnable.setAlpha(f);
            this.mWorkerHandler.removeCallbacks(this.mDimLayerUpdateAlphaRunnable);
            this.mWorkerHandler.post(this.mDimLayerUpdateAlphaRunnable);
        }
    }

    public void drawDimLayer(boolean z) {
        int screenBrightnessInt = getScreenBrightnessInt();
        if (this.mCurrentBrightness == screenBrightnessInt && !z) {
            Log.d(TAG, "don't draw fingerprint dimlayer again, brightness=" + screenBrightnessInt);
            return;
        }
        this.mCurrentBrightness = screenBrightnessInt;
        try {
            if (this.mDelayDraw) {
                Log.d(TAG, "delay waking up 16ms for drawing fingerprint dimlayer.");
                Thread.sleep(16L);
                this.mDelayDraw = false;
            }
        } catch (InterruptedException unused) {
        }
        float f = 1.0f;
        if (screenBrightnessInt == -1) {
            this.mDelayDraw = true;
        } else if (screenBrightnessInt != -2) {
            f = getAlpha(screenBrightnessInt);
        }
        Log.d(TAG, "drawDimLayer brightness=" + screenBrightnessInt + ", alpha=" + f);
        this.mDimLayerRunnable.setAlpha(f);
        this.mWorkerHandler.removeCallbacks(this.mDimLayerRunnable);
        this.mWorkerHandler.post(this.mDimLayerRunnable);
        this.mLocalObserver.register();
    }

    private float getAlpha(int i) {
        int i2 = (int) (i * 13.784314f);
        Log.d(TAG, "get brightness = " + i + ", mAlphaMap.length=" + this.mAlphaMap.length + ", brightnessIndex=" + i2);
        if (i2 >= 0) {
            int[] iArr = this.mAlphaMap;
            if (i2 >= iArr.length) {
                return 0.0f;
            }
            return 1.0f - (iArr[i2] / iArr.length);
        }
        return 0.0f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getScreenBrightnessInt() {
        return Settings.System.getIntForUser(this.mContext.getContentResolver(), "screen_brightness", -1, -2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public final class LocalObserver extends ContentObserver {
        private boolean mRegistered;
        private final ContentResolver mResolver;

        public LocalObserver(Handler handler) {
            super(handler);
            this.mResolver = NTFingerprintBrightnessController.this.mContext.getContentResolver();
        }

        public void register() {
            if (this.mRegistered) {
                this.mResolver.unregisterContentObserver(this);
            }
            Log.i(NTFingerprintBrightnessController.TAG, "register");
            this.mResolver.registerContentObserver(NTFingerprintBrightnessController.URI_SCREEN_BRIGHTNESS, true, this);
            this.mRegistered = true;
        }

        public void unregister() {
            Log.i(NTFingerprintBrightnessController.TAG, "unregister");
            if (this.mRegistered) {
                this.mResolver.unregisterContentObserver(this);
            }
            this.mRegistered = false;
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z, Uri uri) {
            Log.i(NTFingerprintBrightnessController.TAG, "onChange uri=" + ((Object) uri));
            if (NTFingerprintBrightnessController.URI_SCREEN_BRIGHTNESS.equals(uri)) {
                Log.i(NTFingerprintBrightnessController.TAG, "newScreenBrightness=" + NTFingerprintBrightnessController.this.getScreenBrightnessInt());
                NTFingerprintBrightnessController.this.updateDimLayerAlphaIfNeed();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class DimLayerRunnable implements Runnable {
        float mAlpha;

        DimLayerRunnable(float f) {
            this.mAlpha = f;
        }

        @Override // java.lang.Runnable
        public void run() {
            Log.d(NTFingerprintBrightnessController.TAG, "drawDimLayer alpha = " + this.mAlpha);
            if (NTFingerprintBrightnessController.this.mAlphaCallback != null) {
                NTFingerprintBrightnessController.this.mAlphaCallback.onAlpha(this.mAlpha);
            }
            NTFingerprintBrightnessController.this.mNTFingerprintDimLayer.draw(this.mAlpha);
        }

        public void setAlpha(float f) {
            this.mAlpha = f;
        }

        public float getAlpha() {
            return this.mAlpha;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class DimLayerUpdateAlphaRunnable implements Runnable {
        float mAlpha;

        DimLayerUpdateAlphaRunnable(float f) {
            this.mAlpha = f;
        }

        @Override // java.lang.Runnable
        public void run() {
            Log.d(NTFingerprintBrightnessController.TAG, "DimLayerUpdateAlphaRunnable alpha = " + this.mAlpha);
            if (NTFingerprintBrightnessController.this.mAlphaCallback != null) {
                NTFingerprintBrightnessController.this.mAlphaCallback.onAlpha(this.mAlpha);
            }
            NTFingerprintBrightnessController.this.mNTFingerprintDimLayer.updateAlpha(this.mAlpha);
        }

        public void setAlpha(float f) {
            this.mAlpha = f;
        }

        public float getAlpha() {
            return this.mAlpha;
        }
    }

    public void setAlphaCallback(AlphaCallback alphaCallback) {
        this.mAlphaCallback = alphaCallback;
    }
}
