package com.nothing.systemui.biometrics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.nothing.systemui.util.NTLogUtil;
/* loaded from: classes3.dex */
public class NTUdfpsSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "NTUdfpsSurfaceView";
    boolean mHasValidSurface;
    private final SurfaceHolder mHolder;
    private final Paint mSensorPaint;

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    public NTUdfpsSurfaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setZOrderOnTop(true);
        SurfaceHolder holder = getHolder();
        this.mHolder = holder;
        holder.addCallback(this);
        holder.setFormat(1);
        Paint paint = new Paint(0);
        this.mSensorPaint = paint;
        paint.setAntiAlias(true);
        paint.setARGB(255, 255, 255, 255);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.mHasValidSurface = true;
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        this.mHasValidSurface = false;
    }

    public void drawIlluminationDot(RectF rectF) {
        Canvas canvas = null;
        try {
            try {
                NTLogUtil.d(TAG, "drawIlluminationDot");
                canvas = this.mHolder.lockCanvas();
                canvas.drawOval(rectF, this.mSensorPaint);
                if (canvas == null) {
                    return;
                }
            } catch (Exception e) {
                NTLogUtil.d(TAG, "drawIlluminationDot Exception=" + e.toString());
                if (canvas == null) {
                    return;
                }
            }
            this.mHolder.unlockCanvasAndPost(canvas);
        } catch (Throwable th) {
            if (canvas != null) {
                this.mHolder.unlockCanvasAndPost(canvas);
            }
            throw th;
        }
    }
}
