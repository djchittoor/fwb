package com.nothing.systemui.biometrics;

import android.content.Context;
import android.graphics.Point;
import android.hardware.display.DisplayManagerInternal;
import android.os.Trace;
import android.text.TextUtils;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceControl;
import android.view.SurfaceSession;
import android.view.WindowManager;
import com.android.server.LocalServices;
import com.android.systemui.biometrics.UdfpsController;
import com.nothing.systemui.util.NTLogUtil;
/* loaded from: classes3.dex */
public class NTFingerprintDimLayer {
    private static final boolean DEBUG = true;
    private static final String TAG = "FpDimLayer";
    private static FingerprintDimLayerController mFDC;
    private Context mContext;
    private final DisplayManagerInternal mDisplayManagerInternal;
    private UdfpsController mUdfpsController;

    public NTFingerprintDimLayer(Context context, UdfpsController udfpsController) {
        this.mContext = context;
        mFDC = new FingerprintDimLayerController(context);
        this.mDisplayManagerInternal = (DisplayManagerInternal) LocalServices.getService(DisplayManagerInternal.class);
        this.mUdfpsController = udfpsController;
    }

    public void draw(float f) {
        mFDC.draw(f);
    }

    public void dismiss() {
        mFDC.dismiss();
    }

    public void updateAlpha(float f) {
        mFDC.updateAlpha(f);
    }

    public SurfaceControl getSurfaceControl() {
        return mFDC.getSurfaceControl();
    }

    public void setScreenState(boolean z) {
        mFDC.setScreenOn(z);
    }

    public void setDimlayerBlowAodContent(boolean z) {
        mFDC.setDimlayerBlowAodContent(z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class FingerprintDimLayerController {
        private boolean mBelowAodContent;
        private Display mDisplay;
        private int mDisplayHeight;
        private int mDisplayWidth;
        private boolean mIsScreenOn;
        private final Object mLock = new Object();
        private Surface mSurface;
        private float mSurfaceAlpha;
        private SurfaceControl mSurfaceControl;
        private SurfaceSession mSurfaceSession;

        public FingerprintDimLayerController(Context context) {
            this.mDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
            Point point = new Point();
            this.mDisplay.getRealSize(point);
            int i = point.x;
            int i2 = point.y;
            this.mDisplayHeight = Math.max(i, i2);
            this.mDisplayWidth = Math.min(i, i2);
            NTLogUtil.i(NTFingerprintDimLayer.TAG, "FingerprintDimLayerController mDisplayWidth=" + this.mDisplayWidth + ", mDisplayHeight=" + this.mDisplayHeight);
        }

        public void dismiss() {
            if (this.mSurfaceControl != null) {
                destroySurface();
            }
        }

        public SurfaceControl getSurfaceControl() {
            return this.mSurfaceControl;
        }

        public void setScreenOn(boolean z) {
            this.mIsScreenOn = z;
        }

        public void setDimlayerBlowAodContent(boolean z) {
            this.mBelowAodContent = z;
        }

        public void draw(float f) {
            NTLogUtil.d(NTFingerprintDimLayer.TAG, "draw alpha=" + f);
            if (f == 0.0f && this.mIsScreenOn) {
                dismiss();
            } else if (this.mSurfaceControl == null) {
                createSurface(f);
            } else {
                showSurface(f, true);
            }
        }

        private void createSurface(float f) {
            synchronized (this.mLock) {
                if (this.mSurfaceSession == null) {
                    this.mSurfaceSession = new SurfaceSession();
                }
                SurfaceControl.openTransaction();
                if (this.mSurfaceControl == null) {
                    NTLogUtil.d(NTFingerprintDimLayer.TAG, "===createSurface===");
                    try {
                        SurfaceControl.Builder builder = new SurfaceControl.Builder(this.mSurfaceSession);
                        builder.setName("NTFingerprintDimLayer");
                        builder.setFormat(-1);
                        builder.setFlags(131076);
                        builder.setColorLayer();
                        this.mSurfaceControl = builder.build();
                    } catch (Surface.OutOfResourcesException e) {
                        NTLogUtil.e(NTFingerprintDimLayer.TAG, "Unable to create surface." + ((Object) e));
                    }
                    new SurfaceControl.Transaction().setBufferSize(this.mSurfaceControl, this.mDisplayWidth, this.mDisplayHeight).apply();
                    Surface surface = new Surface();
                    this.mSurface = surface;
                    surface.copyFrom(this.mSurfaceControl);
                    showSurface(f, false);
                }
                SurfaceControl.closeTransaction();
            }
        }

        private void destroySurface() {
            synchronized (this.mLock) {
                if (this.mSurfaceControl != null) {
                    NTLogUtil.d(NTFingerprintDimLayer.TAG, "===destroySurface===");
                    new SurfaceControl.Transaction().remove(this.mSurfaceControl).apply();
                    Surface surface = this.mSurface;
                    if (surface != null) {
                        surface.release();
                    }
                    this.mSurfaceControl = null;
                    this.mSurfaceAlpha = 0.0f;
                }
            }
            if (NTFingerprintDimLayer.this.mUdfpsController != null) {
                NTFingerprintDimLayer.this.mUdfpsController.checkToReattachView();
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:7:0x002a, code lost:
            if (android.text.TextUtils.equals(java.lang.String.valueOf(r6.mSurfaceAlpha), java.lang.String.valueOf(r7)) == false) goto L5;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private boolean showSurface(float r7, boolean r8) {
            /*
                r6 = this;
                java.lang.String r0 = "mSurfaceControl show dimlayer alpha="
                java.lang.String r1 = "FpDimLayer"
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                java.lang.String r3 = "showSurface alpha="
                r2.<init>(r3)
                java.lang.StringBuilder r2 = r2.mo6010append(r7)
                java.lang.String r2 = r2.toString()
                com.nothing.systemui.util.NTLogUtil.d(r1, r2)
                java.lang.Object r1 = r6.mLock
                monitor-enter(r1)
                r2 = 1
                if (r8 != 0) goto L2c
                float r8 = r6.mSurfaceAlpha     // Catch: java.lang.Throwable -> L6e
                java.lang.String r8 = java.lang.String.valueOf(r8)     // Catch: java.lang.Throwable -> L6e
                java.lang.String r3 = java.lang.String.valueOf(r7)     // Catch: java.lang.Throwable -> L6e
                boolean r8 = android.text.TextUtils.equals(r8, r3)     // Catch: java.lang.Throwable -> L6e
                if (r8 != 0) goto L67
            L2c:
                r6.mSurfaceAlpha = r7     // Catch: java.lang.Throwable -> L6e
                android.view.SurfaceControl.openTransaction()     // Catch: java.lang.Throwable -> L6e
                java.lang.String r8 = "set_alpha"
                r3 = 8
                android.os.Trace.traceBegin(r3, r8)     // Catch: java.lang.Throwable -> L69
                java.lang.String r8 = "FpDimLayer"
                java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L69
                r5.<init>(r0)     // Catch: java.lang.Throwable -> L69
                java.lang.StringBuilder r0 = r5.mo6010append(r7)     // Catch: java.lang.Throwable -> L69
                java.lang.String r0 = r0.toString()     // Catch: java.lang.Throwable -> L69
                com.nothing.systemui.util.NTLogUtil.i(r8, r0)     // Catch: java.lang.Throwable -> L69
                android.view.SurfaceControl$Transaction r8 = new android.view.SurfaceControl$Transaction     // Catch: java.lang.Throwable -> L69
                r8.<init>()     // Catch: java.lang.Throwable -> L69
                android.view.SurfaceControl r0 = r6.mSurfaceControl     // Catch: java.lang.Throwable -> L69
                r8.setAlpha(r0, r7)     // Catch: java.lang.Throwable -> L69
                android.view.SurfaceControl r7 = r6.mSurfaceControl     // Catch: java.lang.Throwable -> L69
                r8.show(r7)     // Catch: java.lang.Throwable -> L69
                android.view.SurfaceControl r6 = r6.mSurfaceControl     // Catch: java.lang.Throwable -> L69
                r8.setSkipScreenshot(r6, r2)     // Catch: java.lang.Throwable -> L69
                r8.apply()     // Catch: java.lang.Throwable -> L69
                android.os.Trace.traceEnd(r3)     // Catch: java.lang.Throwable -> L69
                android.view.SurfaceControl.closeTransaction()     // Catch: java.lang.Throwable -> L6e
            L67:
                monitor-exit(r1)     // Catch: java.lang.Throwable -> L6e
                return r2
            L69:
                r6 = move-exception
                android.view.SurfaceControl.closeTransaction()     // Catch: java.lang.Throwable -> L6e
                throw r6     // Catch: java.lang.Throwable -> L6e
            L6e:
                r6 = move-exception
                monitor-exit(r1)     // Catch: java.lang.Throwable -> L6e
                throw r6
            */
            throw new UnsupportedOperationException("Method not decompiled: com.nothing.systemui.biometrics.NTFingerprintDimLayer.FingerprintDimLayerController.showSurface(float, boolean):boolean");
        }

        public void updateAlpha(float f) {
            NTLogUtil.d(NTFingerprintDimLayer.TAG, "updateAlpha alpha=" + f);
            if (this.mSurfaceControl == null) {
                return;
            }
            synchronized (this.mLock) {
                if (!TextUtils.equals(String.valueOf(this.mSurfaceAlpha), String.valueOf(f))) {
                    this.mSurfaceAlpha = f;
                    SurfaceControl.openTransaction();
                    Trace.traceBegin(8L, "set_alpha");
                    SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
                    transaction.setAlpha(this.mSurfaceControl, f);
                    transaction.setSkipScreenshot(this.mSurfaceControl, true);
                    transaction.show(this.mSurfaceControl);
                    transaction.apply();
                    Trace.traceEnd(8L);
                    SurfaceControl.closeTransaction();
                }
            }
        }
    }
}
