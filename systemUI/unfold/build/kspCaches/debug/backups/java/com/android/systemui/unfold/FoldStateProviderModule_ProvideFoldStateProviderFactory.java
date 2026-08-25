package com.android.systemui.unfold;

import android.os.Handler;
import com.android.systemui.unfold.updates.DeviceFoldStateProvider;
import com.android.systemui.unfold.updates.FoldStateProvider;
import com.android.systemui.unfold.updates.RotationChangeProvider;
import com.android.systemui.unfold.updates.hinge.HingeAngleProvider;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("com.android.systemui.unfold.dagger.UnfoldMain")
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class FoldStateProviderModule_ProvideFoldStateProviderFactory implements Factory<FoldStateProvider> {
  private final FoldStateProviderModule module;

  private final Provider<DeviceFoldStateProvider.Factory> factoryProvider;

  private final Provider<HingeAngleProvider> hingeAngleProvider;

  private final Provider<RotationChangeProvider> rotationChangeProvider;

  private final Provider<Handler> mainHandlerProvider;

  private FoldStateProviderModule_ProvideFoldStateProviderFactory(FoldStateProviderModule module,
      Provider<DeviceFoldStateProvider.Factory> factoryProvider,
      Provider<HingeAngleProvider> hingeAngleProvider,
      Provider<RotationChangeProvider> rotationChangeProvider,
      Provider<Handler> mainHandlerProvider) {
    this.module = module;
    this.factoryProvider = factoryProvider;
    this.hingeAngleProvider = hingeAngleProvider;
    this.rotationChangeProvider = rotationChangeProvider;
    this.mainHandlerProvider = mainHandlerProvider;
  }

  @Override
  public FoldStateProvider get() {
    return provideFoldStateProvider(module, factoryProvider.get(), hingeAngleProvider.get(), rotationChangeProvider.get(), mainHandlerProvider.get());
  }

  public static FoldStateProviderModule_ProvideFoldStateProviderFactory create(
      FoldStateProviderModule module, Provider<DeviceFoldStateProvider.Factory> factoryProvider,
      Provider<HingeAngleProvider> hingeAngleProvider,
      Provider<RotationChangeProvider> rotationChangeProvider,
      Provider<Handler> mainHandlerProvider) {
    return new FoldStateProviderModule_ProvideFoldStateProviderFactory(module, factoryProvider, hingeAngleProvider, rotationChangeProvider, mainHandlerProvider);
  }

  public static FoldStateProvider provideFoldStateProvider(FoldStateProviderModule instance,
      DeviceFoldStateProvider.Factory factory, HingeAngleProvider hingeAngleProvider,
      RotationChangeProvider rotationChangeProvider, Handler mainHandler) {
    return Preconditions.checkNotNullFromProvides(instance.provideFoldStateProvider(factory, hingeAngleProvider, rotationChangeProvider, mainHandler));
  }
}
