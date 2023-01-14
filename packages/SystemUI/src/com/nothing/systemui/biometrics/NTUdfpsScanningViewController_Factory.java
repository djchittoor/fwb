package com.nothing.systemui.biometrics;

import android.content.Context;
import com.airbnb.lottie.LottieAnimationView;
import com.android.keyguard.KeyguardUpdateMonitor;
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
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes3.dex */
public final class NTUdfpsScanningViewController_Factory implements Factory<NTUdfpsScanningViewController> {
    private final Provider<AuthController> authControllerProvider;
    private final Provider<BiometricUnlockController> biometricUnlockControllerProvider;
    private final Provider<KeyguardBypassController> bypassControllerProvider;
    private final Provider<CommandRegistry> commandRegistryProvider;
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    private final Provider<NotificationShadeWindowController> notificationShadeWindowControllerProvider;
    private final Provider<LottieAnimationView> scanningViewProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;
    private final Provider<Context> sysuiContextProvider;
    private final Provider<UdfpsController> udfpsControllerProvider;
    private final Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;

    public NTUdfpsScanningViewController_Factory(Provider<Context> provider, Provider<AuthController> provider2, Provider<ConfigurationController> provider3, Provider<KeyguardUpdateMonitor> provider4, Provider<KeyguardStateController> provider5, Provider<WakefulnessLifecycle> provider6, Provider<CommandRegistry> provider7, Provider<NotificationShadeWindowController> provider8, Provider<KeyguardBypassController> provider9, Provider<BiometricUnlockController> provider10, Provider<UdfpsController> provider11, Provider<StatusBarStateController> provider12, Provider<LottieAnimationView> provider13) {
        this.sysuiContextProvider = provider;
        this.authControllerProvider = provider2;
        this.configurationControllerProvider = provider3;
        this.keyguardUpdateMonitorProvider = provider4;
        this.keyguardStateControllerProvider = provider5;
        this.wakefulnessLifecycleProvider = provider6;
        this.commandRegistryProvider = provider7;
        this.notificationShadeWindowControllerProvider = provider8;
        this.bypassControllerProvider = provider9;
        this.biometricUnlockControllerProvider = provider10;
        this.udfpsControllerProvider = provider11;
        this.statusBarStateControllerProvider = provider12;
        this.scanningViewProvider = provider13;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public NTUdfpsScanningViewController mo5843get() {
        return newInstance(this.sysuiContextProvider.mo5843get(), this.authControllerProvider.mo5843get(), this.configurationControllerProvider.mo5843get(), this.keyguardUpdateMonitorProvider.mo5843get(), this.keyguardStateControllerProvider.mo5843get(), this.wakefulnessLifecycleProvider.mo5843get(), this.commandRegistryProvider.mo5843get(), this.notificationShadeWindowControllerProvider.mo5843get(), this.bypassControllerProvider.mo5843get(), this.biometricUnlockControllerProvider.mo5843get(), this.udfpsControllerProvider, this.statusBarStateControllerProvider.mo5843get(), this.scanningViewProvider.mo5843get());
    }

    public static NTUdfpsScanningViewController_Factory create(Provider<Context> provider, Provider<AuthController> provider2, Provider<ConfigurationController> provider3, Provider<KeyguardUpdateMonitor> provider4, Provider<KeyguardStateController> provider5, Provider<WakefulnessLifecycle> provider6, Provider<CommandRegistry> provider7, Provider<NotificationShadeWindowController> provider8, Provider<KeyguardBypassController> provider9, Provider<BiometricUnlockController> provider10, Provider<UdfpsController> provider11, Provider<StatusBarStateController> provider12, Provider<LottieAnimationView> provider13) {
        return new NTUdfpsScanningViewController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13);
    }

    public static NTUdfpsScanningViewController newInstance(Context context, AuthController authController, ConfigurationController configurationController, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardStateController keyguardStateController, WakefulnessLifecycle wakefulnessLifecycle, CommandRegistry commandRegistry, NotificationShadeWindowController notificationShadeWindowController, KeyguardBypassController keyguardBypassController, BiometricUnlockController biometricUnlockController, Provider<UdfpsController> provider, StatusBarStateController statusBarStateController, LottieAnimationView lottieAnimationView) {
        return new NTUdfpsScanningViewController(context, authController, configurationController, keyguardUpdateMonitor, keyguardStateController, wakefulnessLifecycle, commandRegistry, notificationShadeWindowController, keyguardBypassController, biometricUnlockController, provider, statusBarStateController, lottieAnimationView);
    }
}
