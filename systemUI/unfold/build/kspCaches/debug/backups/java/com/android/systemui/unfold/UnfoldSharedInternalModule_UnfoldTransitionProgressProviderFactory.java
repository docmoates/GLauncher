package com.android.systemui.unfold;

import android.os.Handler;
import com.android.systemui.unfold.config.UnfoldTransitionConfig;
import com.android.systemui.unfold.progress.FixedTimingTransitionProgressProvider;
import com.android.systemui.unfold.progress.MainThreadUnfoldTransitionProgressProvider;
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
@QualifierMetadata({
    "com.android.systemui.unfold.dagger.UnfoldMain",
    "com.android.systemui.unfold.dagger.UnfoldBg",
    "com.android.systemui.unfold.dagger.UnfoldBgProgressFlag"
})
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
public final class UnfoldSharedInternalModule_UnfoldTransitionProgressProviderFactory implements Factory<Optional<UnfoldTransitionProgressProvider>> {
  private final UnfoldSharedInternalModule module;

  private final Provider<UnfoldTransitionConfig> configProvider;

  private final Provider<ScaleAwareTransitionProgressProvider.Factory> scaleAwareProviderFactoryProvider;

  private final Provider<ATraceLoggerTransitionProgressListener.Factory> tracingListenerProvider;

  private final Provider<PhysicsBasedUnfoldTransitionProgressProvider.Factory> physicsBasedUnfoldTransitionProgressProvider;

  private final Provider<FixedTimingTransitionProgressProvider> fixedTimingTransitionProgressProvider;

  private final Provider<FoldStateProvider> foldStateProvider;

  private final Provider<Handler> mainHandlerProvider;

  private final Provider<MainThreadUnfoldTransitionProgressProvider.Factory> mainThreadUnfoldTransitionProgressProviderFactoryProvider;

  private final Provider<Optional<UnfoldTransitionProgressProvider>> bgProvider;

  private final Provider<Optional<Boolean>> unfoldBgProgressFlagProvider;

  private UnfoldSharedInternalModule_UnfoldTransitionProgressProviderFactory(
      UnfoldSharedInternalModule module, Provider<UnfoldTransitionConfig> configProvider,
      Provider<ScaleAwareTransitionProgressProvider.Factory> scaleAwareProviderFactoryProvider,
      Provider<ATraceLoggerTransitionProgressListener.Factory> tracingListenerProvider,
      Provider<PhysicsBasedUnfoldTransitionProgressProvider.Factory> physicsBasedUnfoldTransitionProgressProvider,
      Provider<FixedTimingTransitionProgressProvider> fixedTimingTransitionProgressProvider,
      Provider<FoldStateProvider> foldStateProvider, Provider<Handler> mainHandlerProvider,
      Provider<MainThreadUnfoldTransitionProgressProvider.Factory> mainThreadUnfoldTransitionProgressProviderFactoryProvider,
      Provider<Optional<UnfoldTransitionProgressProvider>> bgProvider,
      Provider<Optional<Boolean>> unfoldBgProgressFlagProvider) {
    this.module = module;
    this.configProvider = configProvider;
    this.scaleAwareProviderFactoryProvider = scaleAwareProviderFactoryProvider;
    this.tracingListenerProvider = tracingListenerProvider;
    this.physicsBasedUnfoldTransitionProgressProvider = physicsBasedUnfoldTransitionProgressProvider;
    this.fixedTimingTransitionProgressProvider = fixedTimingTransitionProgressProvider;
    this.foldStateProvider = foldStateProvider;
    this.mainHandlerProvider = mainHandlerProvider;
    this.mainThreadUnfoldTransitionProgressProviderFactoryProvider = mainThreadUnfoldTransitionProgressProviderFactoryProvider;
    this.bgProvider = bgProvider;
    this.unfoldBgProgressFlagProvider = unfoldBgProgressFlagProvider;
  }

  @Override
  public Optional<UnfoldTransitionProgressProvider> get() {
    return unfoldTransitionProgressProvider(module, configProvider.get(), scaleAwareProviderFactoryProvider.get(), tracingListenerProvider.get(), physicsBasedUnfoldTransitionProgressProvider.get(), fixedTimingTransitionProgressProvider, foldStateProvider.get(), mainHandlerProvider.get(), mainThreadUnfoldTransitionProgressProviderFactoryProvider.get(), bgProvider, unfoldBgProgressFlagProvider.get());
  }

  public static UnfoldSharedInternalModule_UnfoldTransitionProgressProviderFactory create(
      UnfoldSharedInternalModule module, Provider<UnfoldTransitionConfig> configProvider,
      Provider<ScaleAwareTransitionProgressProvider.Factory> scaleAwareProviderFactoryProvider,
      Provider<ATraceLoggerTransitionProgressListener.Factory> tracingListenerProvider,
      Provider<PhysicsBasedUnfoldTransitionProgressProvider.Factory> physicsBasedUnfoldTransitionProgressProvider,
      Provider<FixedTimingTransitionProgressProvider> fixedTimingTransitionProgressProvider,
      Provider<FoldStateProvider> foldStateProvider, Provider<Handler> mainHandlerProvider,
      Provider<MainThreadUnfoldTransitionProgressProvider.Factory> mainThreadUnfoldTransitionProgressProviderFactoryProvider,
      Provider<Optional<UnfoldTransitionProgressProvider>> bgProvider,
      Provider<Optional<Boolean>> unfoldBgProgressFlagProvider) {
    return new UnfoldSharedInternalModule_UnfoldTransitionProgressProviderFactory(module, configProvider, scaleAwareProviderFactoryProvider, tracingListenerProvider, physicsBasedUnfoldTransitionProgressProvider, fixedTimingTransitionProgressProvider, foldStateProvider, mainHandlerProvider, mainThreadUnfoldTransitionProgressProviderFactoryProvider, bgProvider, unfoldBgProgressFlagProvider);
  }

  public static Optional<UnfoldTransitionProgressProvider> unfoldTransitionProgressProvider(
      UnfoldSharedInternalModule instance, UnfoldTransitionConfig config,
      ScaleAwareTransitionProgressProvider.Factory scaleAwareProviderFactory,
      ATraceLoggerTransitionProgressListener.Factory tracingListener,
      PhysicsBasedUnfoldTransitionProgressProvider.Factory physicsBasedUnfoldTransitionProgressProvider,
      javax.inject.Provider<FixedTimingTransitionProgressProvider> fixedTimingTransitionProgressProvider,
      FoldStateProvider foldStateProvider, Handler mainHandler,
      MainThreadUnfoldTransitionProgressProvider.Factory mainThreadUnfoldTransitionProgressProviderFactory,
      javax.inject.Provider<Optional<UnfoldTransitionProgressProvider>> bgProvider,
      Optional<Boolean> unfoldBgProgressFlag) {
    return Preconditions.checkNotNullFromProvides(instance.unfoldTransitionProgressProvider(config, scaleAwareProviderFactory, tracingListener, physicsBasedUnfoldTransitionProgressProvider, fixedTimingTransitionProgressProvider, foldStateProvider, mainHandler, mainThreadUnfoldTransitionProgressProviderFactory, bgProvider, unfoldBgProgressFlag));
  }
}
