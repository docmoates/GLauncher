package com.android.systemui.unfold;

import android.os.Handler;
import com.android.systemui.unfold.config.UnfoldTransitionConfig;
import com.android.systemui.unfold.updates.hinge.HingeAngleProvider;
import com.android.systemui.unfold.updates.hinge.HingeSensorAngleProvider;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
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
public final class HingeAngleProviderInternalModule_HingeAngleProviderBgFactory implements Factory<HingeAngleProvider> {
  private final HingeAngleProviderInternalModule module;

  private final Provider<UnfoldTransitionConfig> configProvider;

  private final Provider<Handler> handlerProvider;

  private final Provider<HingeSensorAngleProvider.Factory> hingeAngleSensorProvider;

  private HingeAngleProviderInternalModule_HingeAngleProviderBgFactory(
      HingeAngleProviderInternalModule module, Provider<UnfoldTransitionConfig> configProvider,
      Provider<Handler> handlerProvider,
      Provider<HingeSensorAngleProvider.Factory> hingeAngleSensorProvider) {
    this.module = module;
    this.configProvider = configProvider;
    this.handlerProvider = handlerProvider;
    this.hingeAngleSensorProvider = hingeAngleSensorProvider;
  }

  @Override
  public HingeAngleProvider get() {
    return hingeAngleProviderBg(module, configProvider.get(), handlerProvider.get(), hingeAngleSensorProvider.get());
  }

  public static HingeAngleProviderInternalModule_HingeAngleProviderBgFactory create(
      HingeAngleProviderInternalModule module, Provider<UnfoldTransitionConfig> configProvider,
      Provider<Handler> handlerProvider,
      Provider<HingeSensorAngleProvider.Factory> hingeAngleSensorProvider) {
    return new HingeAngleProviderInternalModule_HingeAngleProviderBgFactory(module, configProvider, handlerProvider, hingeAngleSensorProvider);
  }

  public static HingeAngleProvider hingeAngleProviderBg(HingeAngleProviderInternalModule instance,
      UnfoldTransitionConfig config, Handler handler,
      HingeSensorAngleProvider.Factory hingeAngleSensorProvider) {
    return Preconditions.checkNotNullFromProvides(instance.hingeAngleProviderBg(config, handler, hingeAngleSensorProvider));
  }
}
