package com.android.systemui.unfold;

import com.android.systemui.unfold.config.UnfoldTransitionConfig;
import com.android.systemui.unfold.progress.RemoteUnfoldTransitionReceiver;
import com.android.systemui.unfold.util.ATraceLoggerTransitionProgressListener;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import java.util.Optional;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
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
public final class UnfoldRemoteModule_ProvideTransitionProviderFactory implements Factory<Optional<RemoteUnfoldTransitionReceiver>> {
  private final UnfoldRemoteModule module;

  private final Provider<UnfoldTransitionConfig> configProvider;

  private final Provider<ATraceLoggerTransitionProgressListener.Factory> traceListenerProvider;

  private final Provider<RemoteUnfoldTransitionReceiver> remoteReceiverProvider;

  private UnfoldRemoteModule_ProvideTransitionProviderFactory(UnfoldRemoteModule module,
      Provider<UnfoldTransitionConfig> configProvider,
      Provider<ATraceLoggerTransitionProgressListener.Factory> traceListenerProvider,
      Provider<RemoteUnfoldTransitionReceiver> remoteReceiverProvider) {
    this.module = module;
    this.configProvider = configProvider;
    this.traceListenerProvider = traceListenerProvider;
    this.remoteReceiverProvider = remoteReceiverProvider;
  }

  @Override
  public Optional<RemoteUnfoldTransitionReceiver> get() {
    return provideTransitionProvider(module, configProvider.get(), traceListenerProvider.get(), remoteReceiverProvider);
  }

  public static UnfoldRemoteModule_ProvideTransitionProviderFactory create(
      UnfoldRemoteModule module, Provider<UnfoldTransitionConfig> configProvider,
      Provider<ATraceLoggerTransitionProgressListener.Factory> traceListenerProvider,
      Provider<RemoteUnfoldTransitionReceiver> remoteReceiverProvider) {
    return new UnfoldRemoteModule_ProvideTransitionProviderFactory(module, configProvider, traceListenerProvider, remoteReceiverProvider);
  }

  public static Optional<RemoteUnfoldTransitionReceiver> provideTransitionProvider(
      UnfoldRemoteModule instance, UnfoldTransitionConfig config,
      ATraceLoggerTransitionProgressListener.Factory traceListener,
      javax.inject.Provider<RemoteUnfoldTransitionReceiver> remoteReceiverProvider) {
    return Preconditions.checkNotNullFromProvides(instance.provideTransitionProvider(config, traceListener, remoteReceiverProvider));
  }
}
