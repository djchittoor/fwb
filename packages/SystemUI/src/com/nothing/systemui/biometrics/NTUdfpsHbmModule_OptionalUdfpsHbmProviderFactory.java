package com.nothing.systemui.biometrics;

import com.android.systemui.biometrics.UdfpsHbmProvider;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes3.dex */
public final class NTUdfpsHbmModule_OptionalUdfpsHbmProviderFactory implements Factory<UdfpsHbmProvider> {
    @Override // javax.inject.Provider
    /* renamed from: get */
    public UdfpsHbmProvider mo5843get() {
        return optionalUdfpsHbmProvider();
    }

    public static NTUdfpsHbmModule_OptionalUdfpsHbmProviderFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static UdfpsHbmProvider optionalUdfpsHbmProvider() {
        return (UdfpsHbmProvider) Preconditions.checkNotNullFromProvides(NTUdfpsHbmModule.optionalUdfpsHbmProvider());
    }

    /* loaded from: classes3.dex */
    private static final class InstanceHolder {
        private static final NTUdfpsHbmModule_OptionalUdfpsHbmProviderFactory INSTANCE = new NTUdfpsHbmModule_OptionalUdfpsHbmProviderFactory();

        private InstanceHolder() {
        }
    }
}
