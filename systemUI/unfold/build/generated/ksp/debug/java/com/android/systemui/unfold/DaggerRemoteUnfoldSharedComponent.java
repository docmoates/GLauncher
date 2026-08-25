package com.android.systemui.unfold;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Handler;
import com.android.systemui.unfold.config.UnfoldTransitionConfig;
import com.android.systemui.unfold.progress.RemoteUnfoldTransitionReceiver;
import com.android.systemui.unfold.progress.RemoteUnfoldTransitionReceiver_Factory;
import com.android.systemui.unfold.updates.RotationChangeProvider;
import com.android.systemui.unfold.updates.RotationChangeProvider_Factory;
import com.android.systemui.unfold.updates.RotationChangeProvider_Factory_Impl;
import com.android.systemui.unfold.util.ATraceLoggerTransitionProgressListener_Factory;
import com.android.systemui.unfold.util.ATraceLoggerTransitionProgressListener_Factory_Impl;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.InstanceFactory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.annotation.processing.Generated;

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
public final class DaggerRemoteUnfoldSharedComponent {
  private DaggerRemoteUnfoldSharedComponent() {
  }

  public static RemoteUnfoldSharedComponent.Factory factory() {
    return new Factory();
  }

  private static final class Factory implements RemoteUnfoldSharedComponent.Factory {
    @Override
    public RemoteUnfoldSharedComponent create(Context context, UnfoldTransitionConfig config,
        Executor executor, Handler handler, Executor singleThreadBgExecutor, Handler bgHandler,
        DisplayManager displayManager, String tracingTagPrefix) {
      Preconditions.checkNotNull(context);
      Preconditions.checkNotNull(config);
      Preconditions.checkNotNull(executor);
      Preconditions.checkNotNull(handler);
      Preconditions.checkNotNull(singleThreadBgExecutor);
      Preconditions.checkNotNull(bgHandler);
      Preconditions.checkNotNull(displayManager);
      Preconditions.checkNotNull(tracingTagPrefix);
      return new RemoteUnfoldSharedComponentImpl(new UnfoldRemoteModule(), context, config, executor, handler, singleThreadBgExecutor, bgHandler, displayManager, tracingTagPrefix);
    }
  }

  private static final class RemoteUnfoldSharedComponentImpl implements RemoteUnfoldSharedComponent {
    private final UnfoldRemoteModule unfoldRemoteModule;

    private final Handler handler;

    private final RemoteUnfoldSharedComponentImpl remoteUnfoldSharedComponentImpl = this;

    Provider<UnfoldTransitionConfig> configProvider;

    Provider<String> tracingTagPrefixProvider;

    ATraceLoggerTransitionProgressListener_Factory aTraceLoggerTransitionProgressListenerProvider;

    Provider<com.android.systemui.unfold.util.ATraceLoggerTransitionProgressListener.Factory> factoryProvider;

    Provider<Boolean> useReceivingFilterProvider;

    Provider<Executor> executorProvider;

    Provider<RemoteUnfoldTransitionReceiver> remoteUnfoldTransitionReceiverProvider;

    Provider<Optional<RemoteUnfoldTransitionReceiver>> provideTransitionProvider;

    Provider<DisplayManager> displayManagerProvider;

    Provider<Context> contextProvider;

    Provider<Handler> bgHandlerProvider;

    RotationChangeProvider_Factory rotationChangeProvider;

    Provider<RotationChangeProvider.Factory> factoryProvider2;

    RemoteUnfoldSharedComponentImpl(UnfoldRemoteModule unfoldRemoteModuleParam,
        Context contextParam, UnfoldTransitionConfig configParam, Executor executorParam,
        Handler handlerParam, Executor singleThreadBgExecutorParam, Handler bgHandlerParam,
        DisplayManager displayManagerParam, String tracingTagPrefixParam) {
      this.unfoldRemoteModule = unfoldRemoteModuleParam;
      this.handler = handlerParam;
      initialize(unfoldRemoteModuleParam, contextParam, configParam, executorParam, handlerParam, singleThreadBgExecutorParam, bgHandlerParam, displayManagerParam, tracingTagPrefixParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final UnfoldRemoteModule unfoldRemoteModuleParam,
        final Context contextParam, final UnfoldTransitionConfig configParam,
        final Executor executorParam, final Handler handlerParam,
        final Executor singleThreadBgExecutorParam, final Handler bgHandlerParam,
        final DisplayManager displayManagerParam, final String tracingTagPrefixParam) {
      this.configProvider = InstanceFactory.create(configParam);
      this.tracingTagPrefixProvider = InstanceFactory.create(tracingTagPrefixParam);
      this.aTraceLoggerTransitionProgressListenerProvider = ATraceLoggerTransitionProgressListener_Factory.create(tracingTagPrefixProvider);
      this.factoryProvider = ATraceLoggerTransitionProgressListener_Factory_Impl.createFactoryProvider(aTraceLoggerTransitionProgressListenerProvider);
      this.useReceivingFilterProvider = UnfoldRemoteModule_UseReceivingFilterFactory.create(unfoldRemoteModuleParam);
      this.executorProvider = InstanceFactory.create(executorParam);
      this.remoteUnfoldTransitionReceiverProvider = RemoteUnfoldTransitionReceiver_Factory.create(useReceivingFilterProvider, executorProvider);
      this.provideTransitionProvider = DoubleCheck.provider(UnfoldRemoteModule_ProvideTransitionProviderFactory.create(unfoldRemoteModuleParam, configProvider, factoryProvider, remoteUnfoldTransitionReceiverProvider));
      this.displayManagerProvider = InstanceFactory.create(displayManagerParam);
      this.contextProvider = InstanceFactory.create(contextParam);
      this.bgHandlerProvider = InstanceFactory.create(bgHandlerParam);
      this.rotationChangeProvider = RotationChangeProvider_Factory.create(displayManagerProvider, contextProvider, bgHandlerProvider);
      this.factoryProvider2 = RotationChangeProvider_Factory_Impl.createFactoryProvider(rotationChangeProvider);
    }

    @Override
    public Optional<RemoteUnfoldTransitionReceiver> getRemoteTransitionProgress() {
      return provideTransitionProvider.get();
    }

    @Override
    public RotationChangeProvider getRotationChangeProvider() {
      return UnfoldRemoteModule_ProvideMainRotationChangeProviderFactory.provideMainRotationChangeProvider(unfoldRemoteModule, factoryProvider2.get(), handler);
    }
  }
}
