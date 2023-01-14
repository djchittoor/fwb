package com.nothing.systemui.biometrics;

import android.content.Context;
import android.graphics.PointF;
import android.hardware.biometrics.BiometricSourceType;
import android.icu.lang.UCharacter;
import android.view.ViewGroup;
import com.airbnb.lottie.LottieAnimationView;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.biometrics.UdfpsController;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.ViewController;
import com.nothing.systemui.util.NTLogUtil;
import javax.inject.Inject;
import javax.inject.Provider;
/* loaded from: classes3.dex */
public class NTUdfpsScanningViewController extends ViewController<LottieAnimationView> implements KeyguardStateController.Callback, WakefulnessLifecycle.Observer {
    private static final String TAG = "ScanningViewController";
    private AuthController mAuthController;
    private AuthController.Callback mAuthControllerCallback;
    private PointF mFingerprintSensorLocation;
    private KeyguardStateController mKeyguardStateController;
    private KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private KeyguardUpdateMonitorCallback mKeyguardUpdateMonitorCallback;
    private int mSensorRadius;
    private UdfpsController mUdfpsController;
    private UdfpsController.Callback mUdfpsControllerCallback;
    private Provider<UdfpsController> mUdfpsControllerProvider;
    private int mUdfpsScanningViewSize;
    private WakefulnessLifecycle mWakefulnessLifecycle;

    @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
    public void onStartedGoingToSleep() {
    }

    @Inject
    public NTUdfpsScanningViewController(Context context, AuthController authController, ConfigurationController configurationController, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardStateController keyguardStateController, WakefulnessLifecycle wakefulnessLifecycle, CommandRegistry commandRegistry, NotificationShadeWindowController notificationShadeWindowController, KeyguardBypassController keyguardBypassController, BiometricUnlockController biometricUnlockController, Provider<UdfpsController> provider, StatusBarStateController statusBarStateController, LottieAnimationView lottieAnimationView) {
        super(lottieAnimationView);
        this.mUdfpsScanningViewSize = UCharacter.UnicodeBlock.NYIAKENG_PUACHUE_HMONG_ID;
        this.mKeyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() { // from class: com.nothing.systemui.biometrics.NTUdfpsScanningViewController.1
            @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
            public void onBiometricAuthenticated(int i, BiometricSourceType biometricSourceType, boolean z) {
            }

            @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
            public void onBiometricAuthFailed(BiometricSourceType biometricSourceType) {
                if (biometricSourceType != BiometricSourceType.FINGERPRINT || NTUdfpsScanningViewController.this.mFingerprintSensorLocation == null) {
                    return;
                }
                NTUdfpsScanningViewController.this.stopAnimation();
            }
        };
        this.mUdfpsControllerCallback = new UdfpsController.Callback() { // from class: com.nothing.systemui.biometrics.NTUdfpsScanningViewController.2
            @Override // com.android.systemui.biometrics.UdfpsController.Callback
            public void onFingerDown() {
                NTLogUtil.i(NTUdfpsScanningViewController.TAG, "onFingerDown mFingerprintSensorLocation=" + ((Object) NTUdfpsScanningViewController.this.mFingerprintSensorLocation));
                if (NTUdfpsScanningViewController.this.mFingerprintSensorLocation != null) {
                    ((LottieAnimationView) NTUdfpsScanningViewController.this.mView).setVisibility(0);
                    ((LottieAnimationView) NTUdfpsScanningViewController.this.mView).playAnimation();
                    return;
                }
                NTLogUtil.i(NTUdfpsScanningViewController.TAG, "fingerprintSensorLocation=null onFingerDown. Skip showing dwell ripple");
            }

            @Override // com.android.systemui.biometrics.UdfpsController.Callback
            public void onFingerUp() {
                NTLogUtil.d(NTUdfpsScanningViewController.TAG, "onFingerUp: ");
                NTUdfpsScanningViewController.this.stopAnimation();
            }
        };
        this.mAuthControllerCallback = new AuthController.Callback() { // from class: com.nothing.systemui.biometrics.NTUdfpsScanningViewController.3
            @Override // com.android.systemui.biometrics.AuthController.Callback
            public void onAllAuthenticatorsRegistered() {
                NTLogUtil.i(NTUdfpsScanningViewController.TAG, "onAllAuthenticatorsRegistered");
                NTUdfpsScanningViewController.this.updateSensorLocation();
                NTUdfpsScanningViewController.this.updateUdfpsDependentParams();
            }
        };
        this.mAuthController = authController;
        this.mUdfpsControllerProvider = provider;
        updateUdfpsController();
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mKeyguardStateController = keyguardStateController;
        this.mWakefulnessLifecycle = wakefulnessLifecycle;
    }

    private void updateUdfpsController() {
        if (this.mAuthController.isUdfpsEnrolled(KeyguardUpdateMonitor.getCurrentUser())) {
            this.mUdfpsController = this.mUdfpsControllerProvider.mo5843get();
        } else {
            this.mUdfpsController = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.util.ViewController
    public void onInit() {
        NTLogUtil.e(TAG, "onInit=");
    }

    @Override // com.android.systemui.util.ViewController
    public void onViewAttached() {
        this.mAuthController.addCallback(this.mAuthControllerCallback);
        updateSensorLocation();
        updateUdfpsDependentParams();
        if (this.mUdfpsController != null) {
            NTLogUtil.i(TAG, "mUdfpsController addCallback");
            this.mUdfpsController.addCallback(this.mUdfpsControllerCallback);
        }
        this.mKeyguardUpdateMonitor.registerCallback(this.mKeyguardUpdateMonitorCallback);
        this.mKeyguardStateController.addCallback(this);
        this.mWakefulnessLifecycle.addObserver(this);
        ((LottieAnimationView) this.mView).setVisibility(8);
    }

    @Override // com.android.systemui.util.ViewController
    public void onViewDetached() {
        UdfpsController udfpsController = this.mUdfpsController;
        if (udfpsController != null) {
            udfpsController.removeCallback(this.mUdfpsControllerCallback);
        }
        this.mAuthController.removeCallback(this.mAuthControllerCallback);
        this.mKeyguardUpdateMonitor.removeCallback(this.mKeyguardUpdateMonitorCallback);
        this.mKeyguardStateController.removeCallback(this);
        this.mWakefulnessLifecycle.removeObserver(this);
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
    public void onKeyguardFadingAwayChanged() {
        this.mKeyguardStateController.isKeyguardFadingAway();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopAnimation() {
        ((LottieAnimationView) this.mView).setVisibility(8);
        ((LottieAnimationView) this.mView).pauseAnimation();
        ((LottieAnimationView) this.mView).setProgress(0.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSensorLocation() {
        this.mFingerprintSensorLocation = this.mAuthController.getFingerprintSensorLocation();
        NTLogUtil.i(TAG, "updateSensorLocation fingerprintSensorLocation= " + ((Object) this.mFingerprintSensorLocation));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateUdfpsDependentParams() {
        NTLogUtil.i(TAG, "updateUdfpsDependentParams");
    }

    private void updateViewLayoutParams() {
        NTLogUtil.i(TAG, "updateViewLayoutParams fingerprintSensorLocation= " + ((Object) this.mFingerprintSensorLocation) + ", mUdfpsScanningViewRadius=" + this.mUdfpsScanningViewSize);
        ViewGroup.LayoutParams layoutParams = ((LottieAnimationView) this.mView).getLayoutParams();
        layoutParams.width = this.mUdfpsScanningViewSize;
        layoutParams.height = this.mUdfpsScanningViewSize;
        ((LottieAnimationView) this.mView).setLayoutParams(layoutParams);
        if (this.mFingerprintSensorLocation != null) {
            ((LottieAnimationView) this.mView).setTranslationX(this.mFingerprintSensorLocation.x - (this.mUdfpsScanningViewSize / 2));
            ((LottieAnimationView) this.mView).setTranslationY(this.mFingerprintSensorLocation.y - (this.mUdfpsScanningViewSize / 2));
            return;
        }
        ((LottieAnimationView) this.mView).setTranslationX(540 - (this.mUdfpsScanningViewSize / 2));
        ((LottieAnimationView) this.mView).setTranslationY(2170 - (this.mUdfpsScanningViewSize / 2));
    }
}
