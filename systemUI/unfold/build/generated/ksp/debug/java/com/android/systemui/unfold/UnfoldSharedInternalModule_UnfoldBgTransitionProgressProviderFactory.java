package com.android.systemui.unfold;

import android.os.Handler;
import com.android.systemui.unfold.config.UnfoldTransitionConfig;
import com.android.systemui.unfold.progress.FixedTimingTransitionProgressProvider;
import com.android.systemui.unfold.progress.PhysicsBasedUnfoldTransitionProgressProvider;
import com.android.systemui.unfold.updates.FoldStateProvider;
import com.android.systemui.unfold.util.ATraceLoggerTransitionProgressListener;
import com.android.systemui.unfold.util.ScaleAwareTransitionProgressProvider;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import java.util.Optional;
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
public final class UnfoldSharedInternalModule_UnfoldBgTransitionProgressProviderFactory implements Factory<Optional<UnfoldTransitionProgressProvider>> {
  private final UnfoldSharedInternalModule module;

  private final Provider<UnfoldTransitionConfig> configProvider;

  private final Provider<ScaleAwareTransitionProgressProvider.Factory> scaleAwareProviderFactoryProvider;

  private final Provider<ATraceLoggerTransitionProgressListener.Factory> tracingListenerProvider;

  private final Provider<PhysicsBasedUnfoldTransitionProgressProvider.Factory> physicsBasedUnfoldTransitionProgressProvider;

  private final Provider<FixedTimingTransitionProgressProvider> fixedTimingTransitionProgressProvider;

  private final Provider<FoldStateProvider> bgFoldStateProvider;

  private final Provider<Handler> bgHandlerProvider;

  private UnfoldSharedInternalModule_UnfoldBgTransitionProgressProviderFactory(
      UnfoldSharedInternalModule module, Provider<UnfoldTransitionConfig> configProvider,
      Provider<ScaleAwareTransitionProgressProvider.Factory> scaleAwareProviderFactoryProvider,
      Provider<ATraceLoggerTransitionProgressListener.Factory> tracingListenerProvider,
      Provider<PhysicsBasedUnfoldTransitionProgressProvider.Factory> physicsBasedUnfoldTransitionProgressProvider,
      Provider<FixedTimingTransitionProgressProvider> fixedTimingTransitionProgressProvider,
      Provider<FoldStateProvider> bgFoldStateProvider, Provider<Handler> bgHandlerProvider) {
    this.module = module;
    this.configProvider = configProvider;
    this.scaleAwareProviderFactoryProvider = scaleAwareProviderFactoryProvider;
    this.tracingListenerProvider = tracingListenerProvider;
    this.physicsBasedUnfoldTransitionProgressProvider = physicsBasedUnfoldTransitionProgressProvider;
    this.fixedTimingTransitionProgressProvider = fixedTimingTransitionProgressProvider;
    this.bgFoldStateProvider = bgFoldStateProvider;
    this.bgHandlerProvider = bgHandlerProvider;
  }

  @Override
  public Optional<UnfoldTransitionProgressProvider> get() {
    return unfoldBgTransitionProgressProvider(module, configProvider.get(), scaleAwareProviderFactoryProvider.get(), tracingListenerProvider.get(), physicsBasedUnfoldTransitionProgressProvider.get(), fixedTimingTransitionProgressProvider, bgFoldStateProvider.get(), bgHandlerProvider.get());
  }

  public static UnfoldSharedInternalModule_UnfoldBgTransitionProgressProviderFactory create(
      UnfoldSharedInternalModule module, Provider<UnfoldTransitionConfig> configProvider,
      Provider<ScaleAwareTransitionProgressProvider.Factory> scaleAwareProviderFactoryProvider,
      Provider<ATraceLoggerTransitionProgressListener.Factory> tracingListenerProvider,
      Provider<PhysicsBasedUnfoldTransitionProgressProvider.Factory> physicsBasedUnfoldTransitionProgressProvider,
      Provider<FixedTimingTransitionProgressProvider> fixedTimingTransitionProgressProvider,
      Provider<FoldStateProvider> bgFoldStateProvider, Provider<Handler> bgHandlerProvider) {
    return new UnfoldSharedInternalModule_UnfoldBgTransitionProgressProviderFactory(module, configProvider, scaleAwareProviderFactoryProvider, tracingListenerProvider, physicsBasedUnfoldTransitionProgressProvider, fixedTimingTransitionProgressProvider, bgFoldStateProvider, bgHandlerProvider);
  }

  public static Optional<UnfoldTransitionProgressProvider> unfoldBgTransitionProgressProvider(
      UnfoldSharedInternalModule instance, UnfoldTransitionConfig config,
      ScaleAwareTransitionProgressProvider.Factory scaleAwareProviderFactory,
      ATraceLoggerTransitionProgressListener.Factory tracingListener,
      PhysicsBasedUnfoldTransitionProgressProvider.Factory physicsBasedUnfoldTransitionProgressProvider,
      javax.inject.Provider<FixedTimingTransitionProgressProvider> fixedTimingTransitionProgressProvider,
      FoldStateProvider bgFoldStateProvider, Handler bgHandler) {
    return Preconditions.checkNotNullFromProvides(instance.unfoldBgTransitionProgressProvider(config, scaleAwareProviderFactory, tracingListener, physicsBasedUnfoldTransitionProgressProvider, fixedTimingTransitionProgressProvider, bgFoldStateProvider, bgHandler));
  }
}
