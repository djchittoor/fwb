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
import android.util.Log;
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
            Log.i(NTFingerprintDimLayer.TAG, "FingerprintDimLayerController mDisplayWidth=" + this.mDisplayWidth + ", mDisplayHeight=" + this.mDisplayHeight);
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
            Log.d(NTFingerprintDimLayer.TAG, "draw alpha=" + f);
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
                    Log.d(NTFingerprintDimLayer.TAG, "===createSurface===");
                    try {
                        SurfaceControl.Builder builder = new SurfaceControl.Builder(this.mSurfaceSession);
                        builder.setName("NTFingerprintDimLayer");
                        builder.setFormat(-1);
                        builder.setFlags(131076);
                        builder.setColorLayer();
                        this.mSurfaceControl = builder.build();
                    } catch (Surface.OutOfResourcesException e) {
                        Log.e(NTFingerprintDimLayer.TAG, "Unable to create surface." + ((Object) e));
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
                    Log.d(NTFingerprintDimLayer.TAG, "===destroySurface===");
                    new SurfaceControl.Transaction().remove(this.mSurfaceControl).apply();
                    Surface surface = this.mSurface;
                    if (surface != null) {
                        surface.release();
                    }
                    this.mSurfaceControl = null;
                    this.mSurfaceAlpha = 0.0f;
                }
            }
//            if (mUdfpsController != null) {
  //              mUdfpsController.checkToReattachView();
    //        }
        }


        public void showSurface(float alpha, boolean force) {
            Log.d(TAG, "showSurface alpha=" + alpha);
            synchronized (mLock) {
                if (!force && TextUtils.equals(String.valueOf(mSurfaceAlpha), String.valueOf(alpha))) {
                    return;
                }
                mSurfaceAlpha = alpha;
                SurfaceControl.openTransaction();
                Trace.traceBegin(8L, "set_alpha");
                Log.i(TAG, "mSurfaceControl show dimlayer alpha=" + alpha);
                SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
                transaction.setAlpha(mSurfaceControl, alpha);
                transaction.show(mSurfaceControl);
                transaction.setSkipScreenshot(mSurfaceControl, true);
                transaction.apply();
                Trace.traceEnd(8L);
                SurfaceControl.closeTransaction();
            }
        }


        public void updateAlpha(float f) {
            Log.d(NTFingerprintDimLayer.TAG, "updateAlpha alpha=" + f);
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
