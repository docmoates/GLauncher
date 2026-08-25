package com.android.systemui.unfold;

import android.content.ContentResolver;
import android.content.Context;
import android.hardware.SensorManager;
import android.hardware.display.DisplayManager;
import android.os.Handler;
import com.android.systemui.unfold.config.UnfoldTransitionConfig;
import com.android.systemui.unfold.progress.FixedTimingTransitionProgressProvider;
import com.android.systemui.unfold.progress.FixedTimingTransitionProgressProvider_Factory;
import com.android.systemui.unfold.progress.MainThreadUnfoldTransitionProgressProvider_Factory;
import com.android.systemui.unfold.progress.MainThreadUnfoldTransitionProgressProvider_Factory_Impl;
import com.android.systemui.unfold.progress.PhysicsBasedUnfoldTransitionProgressProvider_Factory;
import com.android.systemui.unfold.progress.PhysicsBasedUnfoldTransitionProgressProvider_Factory_Impl;
import com.android.systemui.unfold.progress.UnfoldFrameCallbackScheduler_Factory;
import com.android.systemui.unfold.progress.UnfoldFrameCallbackScheduler_Factory_Impl;
import com.android.systemui.unfold.updates.DeviceFoldStateProvider_Factory;
import com.android.systemui.unfold.updates.DeviceFoldStateProvider_Factory_Impl;
import com.android.systemui.unfold.updates.FoldProvider;
import com.android.systemui.unfold.updates.FoldStateProvider;
import com.android.systemui.unfold.updates.RotationChangeProvider;
import com.android.systemui.unfold.updates.RotationChangeProvider_Factory;
import com.android.systemui.unfold.updates.RotationChangeProvider_Factory_Impl;
import com.android.systemui.unfold.updates.hinge.HingeAngleProvider;
import com.android.systemui.unfold.updates.hinge.HingeSensorAngleProvider_Factory;
import com.android.systemui.unfold.updates.hinge.HingeSensorAngleProvider_Factory_Impl;
import com.android.systemui.unfold.updates.screen.ScreenStatusProvider;
import com.android.systemui.unfold.util.ATraceLoggerTransitionProgressListener_Factory;
import com.android.systemui.unfold.util.ATraceLoggerTransitionProgressListener_Factory_Impl;
import com.android.systemui.unfold.util.CurrentActivityTypeProvider;
import com.android.systemui.unfold.util.ScaleAwareTransitionProgressProvider_Factory;
import com.android.systemui.unfold.util.ScaleAwareTransitionProgressProvider_Factory_Impl;
import com.android.systemui.unfold.util.UnfoldKeyguardVisibilityManagerImpl;
import com.android.systemui.unfold.util.UnfoldKeyguardVisibilityManagerImpl_Factory;
import com.android.systemui.unfold.util.UnfoldKeyguardVisibilityProvider;
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
public final class DaggerUnfoldSharedComponent {
  /**
   * A {@link Provider} that returns {@code Optional.empty()}.
   */
  @SuppressWarnings("rawtypes")
  private static final Provider ABSENT_JDK_OPTIONAL_PROVIDER = InstanceFactory.create(Optional.empty());

  private DaggerUnfoldSharedComponent() {
  }

  public static UnfoldSharedComponent.Factory factory() {
    return new Factory();
  }

  /**
   * Returns a {@link Provider} that returns {@code Optional.empty()}.
   */
  private static <T> Provider<Optional<T>> absentJdkOptionalProvider() {
    @SuppressWarnings("unchecked") // safe covariant cast
    Provider<Optional<T>> provider = (Provider<Optional<T>>) ABSENT_JDK_OPTIONAL_PROVIDER;
    return provider;
  }

  private static final class Factory implements UnfoldSharedComponent.Factory {
    @Override
    public UnfoldSharedComponent create(Context context, UnfoldTransitionConfig config,
        ScreenStatusProvider screenStatusProvider, FoldProvider foldProvider,
        CurrentActivityTypeProvider activityTypeProvider, SensorManager sensorManager,
        Handler handler, Executor executor, Executor singleThreadBgExecutor,
        String tracingTagPrefix, DisplayManager displayManager, Handler bgHandler,
        ContentResolver contentResolver) {
      Preconditions.checkNotNull(context);
      Preconditions.checkNotNull(config);
      Preconditions.checkNotNull(screenStatusProvider);
      Preconditions.checkNotNull(foldProvider);
      Preconditions.checkNotNull(activityTypeProvider);
      Preconditions.checkNotNull(sensorManager);
      Preconditions.checkNotNull(handler);
      Preconditions.checkNotNull(executor);
      Preconditions.checkNotNull(singleThreadBgExecutor);
      Preconditions.checkNotNull(tracingTagPrefix);
      Preconditions.checkNotNull(displayManager);
      Preconditions.checkNotNull(bgHandler);
      Preconditions.checkNotNull(contentResolver);
      return new UnfoldSharedComponentImpl(new UnfoldSharedModule(), new UnfoldSharedInternalModule(), new UnfoldRotationProviderInternalModule(), new HingeAngleProviderInternalModule(), new FoldStateProviderModule(), context, config, screenStatusProvider, foldProvider, activityTypeProvider, sensorManager, handler, executor, singleThreadBgExecutor, tracingTagPrefix, displayManager, bgHandler, contentResolver);
    }
  }

  private static final class UnfoldSharedComponentImpl implements UnfoldSharedComponent {
    private final UnfoldSharedComponentImpl unfoldSharedComponentImpl = this;

    Provider<UnfoldTransitionConfig> configProvider;

    Provider<ContentResolver> contentResolverProvider;

    ScaleAwareTransitionProgressProvider_Factory scaleAwareTransitionProgressProvider;

    Provider<com.android.systemui.unfold.util.ScaleAwareTransitionProgressProvider.Factory> factoryProvider;

    Provider<String> tracingTagPrefixProvider;

    ATraceLoggerTransitionProgressListener_Factory aTraceLoggerTransitionProgressListenerProvider;

    Provider<com.android.systemui.unfold.util.ATraceLoggerTransitionProgressListener.Factory> factoryProvider2;

    Provider<Context> contextProvider;

    UnfoldFrameCallbackScheduler_Factory unfoldFrameCallbackSchedulerProvider;

    Provider<com.android.systemui.unfold.progress.UnfoldFrameCallbackScheduler.Factory> factoryProvider3;

    PhysicsBasedUnfoldTransitionProgressProvider_Factory physicsBasedUnfoldTransitionProgressProvider;

    Provider<com.android.systemui.unfold.progress.PhysicsBasedUnfoldTransitionProgressProvider.Factory> factoryProvider4;

    Provider<ScreenStatusProvider> screenStatusProvider;

    Provider<CurrentActivityTypeProvider> activityTypeProvider;

    Provider<UnfoldKeyguardVisibilityManagerImpl> unfoldKeyguardVisibilityManagerImplProvider;

    Provider<UnfoldKeyguardVisibilityProvider> unfoldKeyguardVisibilityProvider;

    Provider<FoldProvider> foldProvider;

    DeviceFoldStateProvider_Factory deviceFoldStateProvider;

    Provider<com.android.systemui.unfold.updates.DeviceFoldStateProvider.Factory> factoryProvider5;

    Provider<Handler> handlerProvider;

    Provider<SensorManager> sensorManagerProvider;

    Provider<Executor> singleThreadBgExecutorProvider;

    HingeSensorAngleProvider_Factory hingeSensorAngleProvider;

    Provider<com.android.systemui.unfold.updates.hinge.HingeSensorAngleProvider.Factory> factoryProvider6;

    Provider<HingeAngleProvider> hingeAngleProvider;

    Provider<DisplayManager> displayManagerProvider;

    Provider<Handler> bgHandlerProvider;

    RotationChangeProvider_Factory rotationChangeProvider;

    Provider<RotationChangeProvider.Factory> factoryProvider7;

    Provider<RotationChangeProvider> provideRotationChangeProvider;

    Provider<FoldStateProvider> provideFoldStateProvider;

    Provider<FixedTimingTransitionProgressProvider> fixedTimingTransitionProgressProvider;

    MainThreadUnfoldTransitionProgressProvider_Factory mainThreadUnfoldTransitionProgressProvider;

    Provider<com.android.systemui.unfold.progress.MainThreadUnfoldTransitionProgressProvider.Factory> factoryProvider8;

    Provider<HingeAngleProvider> hingeAngleProviderBgProvider;

    Provider<RotationChangeProvider> provideBgRotationChangeProvider;

    Provider<FoldStateProvider> provideBgFoldStateProvider;

    Provider<Optional<UnfoldTransitionProgressProvider>> unfoldBgTransitionProgressProvider;

    Provider<Optional<Boolean>> unfoldBgProgressFlagOptionalOfBooleanProvider;

    Provider<Optional<UnfoldTransitionProgressProvider>> unfoldTransitionProgressProvider;

    UnfoldSharedComponentImpl(UnfoldSharedModule unfoldSharedModuleParam,
        UnfoldSharedInternalModule unfoldSharedInternalModuleParam,
        UnfoldRotationProviderInternalModule unfoldRotationProviderInternalModuleParam,
        HingeAngleProviderInternalModule hingeAngleProviderInternalModuleParam,
        FoldStateProviderModule foldStateProviderModuleParam, Context contextParam,
        UnfoldTransitionConfig configParam, ScreenStatusProvider screenStatusProviderParam,
        FoldProvider foldProviderParam, CurrentActivityTypeProvider activityTypeProviderParam,
        SensorManager sensorManagerParam, Handler handlerParam, Executor executorParam,
        Executor singleThreadBgExecutorParam, String tracingTagPrefixParam,
        DisplayManager displayManagerParam, Handler bgHandlerParam,
        ContentResolver contentResolverParam) {

      initialize(unfoldSharedModuleParam, unfoldSharedInternalModuleParam, unfoldRotationProviderInternalModuleParam, hingeAngleProviderInternalModuleParam, foldStateProviderModuleParam, contextParam, configParam, screenStatusProviderParam, foldProviderParam, activityTypeProviderParam, sensorManagerParam, handlerParam, executorParam, singleThreadBgExecutorParam, tracingTagPrefixParam, displayManagerParam, bgHandlerParam, contentResolverParam);
      initialize2(unfoldSharedModuleParam, unfoldSharedInternalModuleParam, unfoldRotationProviderInternalModuleParam, hingeAngleProviderInternalModuleParam, foldStateProviderModuleParam, contextParam, configParam, screenStatusProviderParam, foldProviderParam, activityTypeProviderParam, sensorManagerParam, handlerParam, executorParam, singleThreadBgExecutorParam, tracingTagPrefixParam, displayManagerParam, bgHandlerParam, contentResolverParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final UnfoldSharedModule unfoldSharedModuleParam,
        final UnfoldSharedInternalModule unfoldSharedInternalModuleParam,
        final UnfoldRotationProviderInternalModule unfoldRotationProviderInternalModuleParam,
        final HingeAngleProviderInternalModule hingeAngleProviderInternalModuleParam,
        final FoldStateProviderModule foldStateProviderModuleParam, final Context contextParam,
        final UnfoldTransitionConfig configParam,
        final ScreenStatusProvider screenStatusProviderParam, final FoldProvider foldProviderParam,
        final CurrentActivityTypeProvider activityTypeProviderParam,
        final SensorManager sensorManagerParam, final Handler handlerParam,
        final Executor executorParam, final Executor singleThreadBgExecutorParam,
        final String tracingTagPrefixParam, final DisplayManager displayManagerParam,
        final Handler bgHandlerParam, final ContentResolver contentResolverParam) {
      this.configProvider = InstanceFactory.create(configParam);
      this.contentResolverProvider = InstanceFactory.create(contentResolverParam);
      this.scaleAwareTransitionProgressProvider = ScaleAwareTransitionProgressProvider_Factory.create(contentResolverProvider);
      this.factoryProvider = ScaleAwareTransitionProgressProvider_Factory_Impl.createFactoryProvider(scaleAwareTransitionProgressProvider);
      this.tracingTagPrefixProvider = InstanceFactory.create(tracingTagPrefixParam);
      this.aTraceLoggerTransitionProgressListenerProvider = ATraceLoggerTransitionProgressListener_Factory.create(tracingTagPrefixProvider);
      this.factoryProvider2 = ATraceLoggerTransitionProgressListener_Factory_Impl.createFactoryProvider(aTraceLoggerTransitionProgressListenerProvider);
      this.contextProvider = InstanceFactory.create(contextParam);
      this.unfoldFrameCallbackSchedulerProvider = UnfoldFrameCallbackScheduler_Factory.create();
      this.factoryProvider3 = UnfoldFrameCallbackScheduler_Factory_Impl.createFactoryProvider(unfoldFrameCallbackSchedulerProvider);
      this.physicsBasedUnfoldTransitionProgressProvider = PhysicsBasedUnfoldTransitionProgressProvider_Factory.create(contextProvider, factoryProvider3);
      this.factoryProvider4 = PhysicsBasedUnfoldTransitionProgressProvider_Factory_Impl.createFactoryProvider(physicsBasedUnfoldTransitionProgressProvider);
      this.screenStatusProvider = InstanceFactory.create(screenStatusProviderParam);
      this.activityTypeProvider = InstanceFactory.create(activityTypeProviderParam);
      this.unfoldKeyguardVisibilityManagerImplProvider = DoubleCheck.provider(UnfoldKeyguardVisibilityManagerImpl_Factory.create());
      this.unfoldKeyguardVisibilityProvider = DoubleCheck.provider(UnfoldSharedModule_UnfoldKeyguardVisibilityProviderFactory.create(unfoldSharedModuleParam, unfoldKeyguardVisibilityManagerImplProvider));
      this.foldProvider = InstanceFactory.create(foldProviderParam);
      this.deviceFoldStateProvider = DeviceFoldStateProvider_Factory.create(configProvider, contextProvider, screenStatusProvider, activityTypeProvider, unfoldKeyguardVisibilityProvider, foldProvider);
      this.factoryProvider5 = DeviceFoldStateProvider_Factory_Impl.createFactoryProvider(deviceFoldStateProvider);
      this.handlerProvider = InstanceFactory.create(handlerParam);
      this.sensorManagerProvider = InstanceFactory.create(sensorManagerParam);
      this.singleThreadBgExecutorProvider = InstanceFactory.create(singleThreadBgExecutorParam);
      this.hingeSensorAngleProvider = HingeSensorAngleProvider_Factory.create(sensorManagerProvider, singleThreadBgExecutorProvider);
      this.factoryProvider6 = HingeSensorAngleProvider_Factory_Impl.createFactoryProvider(hingeSensorAngleProvider);
      this.hingeAngleProvider = HingeAngleProviderInternalModule_HingeAngleProviderFactory.create(hingeAngleProviderInternalModuleParam, configProvider, handlerProvider, factoryProvider6);
    }

    @SuppressWarnings("unchecked")
    private void initialize2(final UnfoldSharedModule unfoldSharedModuleParam,
        final UnfoldSharedInternalModule unfoldSharedInternalModuleParam,
        final UnfoldRotationProviderInternalModule unfoldRotationProviderInternalModuleParam,
        final HingeAngleProviderInternalModule hingeAngleProviderInternalModuleParam,
        final FoldStateProviderModule foldStateProviderModuleParam, final Context contextParam,
        final UnfoldTransitionConfig configParam,
        final ScreenStatusProvider screenStatusProviderParam, final FoldProvider foldProviderParam,
        final CurrentActivityTypeProvider activityTypeProviderParam,
        final SensorManager sensorManagerParam, final Handler handlerParam,
        final Executor executorParam, final Executor singleThreadBgExecutorParam,
        final String tracingTagPrefixParam, final DisplayManager displayManagerParam,
        final Handler bgHandlerParam, final ContentResolver contentResolverParam) {
      this.displayManagerProvider = InstanceFactory.create(displayManagerParam);
      this.bgHandlerProvider = InstanceFactory.create(bgHandlerParam);
      this.rotationChangeProvider = RotationChangeProvider_Factory.create(displayManagerProvider, contextProvider, bgHandlerProvider);
      this.factoryProvider7 = RotationChangeProvider_Factory_Impl.createFactoryProvider(rotationChangeProvider);
      this.provideRotationChangeProvider = DoubleCheck.provider(UnfoldRotationProviderInternalModule_ProvideRotationChangeProviderFactory.create(unfoldRotationProviderInternalModuleParam, factoryProvider7, handlerProvider));
      this.provideFoldStateProvider = DoubleCheck.provider(FoldStateProviderModule_ProvideFoldStateProviderFactory.create(foldStateProviderModuleParam, factoryProvider5, hingeAngleProvider, provideRotationChangeProvider, handlerProvider));
      this.fixedTimingTransitionProgressProvider = FixedTimingTransitionProgressProvider_Factory.create(provideFoldStateProvider);
      this.mainThreadUnfoldTransitionProgressProvider = MainThreadUnfoldTransitionProgressProvider_Factory.create(handlerProvider);
      this.factoryProvider8 = MainThreadUnfoldTransitionProgressProvider_Factory_Impl.createFactoryProvider(mainThreadUnfoldTransitionProgressProvider);
      this.hingeAngleProviderBgProvider = HingeAngleProviderInternalModule_HingeAngleProviderBgFactory.create(hingeAngleProviderInternalModuleParam, configProvider, bgHandlerProvider, factoryProvider6);
      this.provideBgRotationChangeProvider = DoubleCheck.provider(UnfoldRotationProviderInternalModule_ProvideBgRotationChangeProviderFactory.create(unfoldRotationProviderInternalModuleParam, factoryProvider7, bgHandlerProvider));
      this.provideBgFoldStateProvider = DoubleCheck.provider(FoldStateProviderModule_ProvideBgFoldStateProviderFactory.create(foldStateProviderModuleParam, factoryProvider5, hingeAngleProviderBgProvider, provideBgRotationChangeProvider, bgHandlerProvider));
      this.unfoldBgTransitionProgressProvider = DoubleCheck.provider(UnfoldSharedInternalModule_UnfoldBgTransitionProgressProviderFactory.create(unfoldSharedInternalModuleParam, configProvider, factoryProvider, factoryProvider2, factoryProvider4, fixedTimingTransitionProgressProvider, provideBgFoldStateProvider, bgHandlerProvider));
      this.unfoldBgProgressFlagOptionalOfBooleanProvider = absentJdkOptionalProvider();
      this.unfoldTransitionProgressProvider = DoubleCheck.provider(UnfoldSharedInternalModule_UnfoldTransitionProgressProviderFactory.create(unfoldSharedInternalModuleParam, configProvider, factoryProvider, factoryProvider2, factoryProvider4, fixedTimingTransitionProgressProvider, provideFoldStateProvider, handlerProvider, factoryProvider8, unfoldBgTransitionProgressProvider, unfoldBgProgressFlagOptionalOfBooleanProvider));
    }

    @Override
    public Optional<UnfoldTransitionProgressProvider> getUnfoldTransitionProvider() {
      return unfoldTransitionProgressProvider.get();
    }
  }
}
