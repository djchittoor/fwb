package com.nothing.systemui.biometrics;

import com.android.systemui.biometrics.UdfpsHbmProvider;
import dagger.Module;
import dagger.Provides;
@Module
/* loaded from: classes3.dex */
public abstract class NTUdfpsHbmModule {
    @Provides
    public static UdfpsHbmProvider optionalUdfpsHbmProvider() {
        return new NTHbmProviderImpl();
    }
}
