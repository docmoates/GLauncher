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
@QualifierMetadata("com.android.systemui.unfold.dagger.UnfoldBg")
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
public final class FoldStateProviderModule_ProvideBgFoldStateProviderFactory implements Factory<FoldStateProvider> {
  private final FoldStateProviderModule module;

  private final Provider<DeviceFoldStateProvider.Factory> factoryProvider;

  private final Provider<HingeAngleProvider> hingeAngleProvider;

  private final Provider<RotationChangeProvider> rotationChangeProvider;

  private final Provider<Handler> bgHandlerProvider;

  private FoldStateProviderModule_ProvideBgFoldStateProviderFactory(FoldStateProviderModule module,
      Provider<DeviceFoldStateProvider.Factory> factoryProvider,
      Provider<HingeAngleProvider> hingeAngleProvider,
      Provider<RotationChangeProvider> rotationChangeProvider,
      Provider<Handler> bgHandlerProvider) {
    this.module = module;
    this.factoryProvider = factoryProvider;
    this.hingeAngleProvider = hingeAngleProvider;
    this.rotationChangeProvider = rotationChangeProvider;
    this.bgHandlerProvider = bgHandlerProvider;
  }

  @Override
  public FoldStateProvider get() {
    return provideBgFoldStateProvider(module, factoryProvider.get(), hingeAngleProvider.get(), rotationChangeProvider.get(), bgHandlerProvider.get());
  }

  public static FoldStateProviderModule_ProvideBgFoldStateProviderFactory create(
      FoldStateProviderModule module, Provider<DeviceFoldStateProvider.Factory> factoryProvider,
      Provider<HingeAngleProvider> hingeAngleProvider,
      Provider<RotationChangeProvider> rotationChangeProvider,
      Provider<Handler> bgHandlerProvider) {
    return new FoldStateProviderModule_ProvideBgFoldStateProviderFactory(module, factoryProvider, hingeAngleProvider, rotationChangeProvider, bgHandlerProvider);
  }

  public static FoldStateProvider provideBgFoldStateProvider(FoldStateProviderModule instance,
      DeviceFoldStateProvider.Factory factory, HingeAngleProvider hingeAngleProvider,
      RotationChangeProvider rotationChangeProvider, Handler bgHandler) {
    return Preconditions.checkNotNullFromProvides(instance.provideBgFoldStateProvider(factory, hingeAngleProvider, rotationChangeProvider, bgHandler));
  }
}
