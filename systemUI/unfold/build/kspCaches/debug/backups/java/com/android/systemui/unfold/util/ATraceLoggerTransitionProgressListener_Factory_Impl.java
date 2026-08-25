package com.android.systemui.unfold.util;

import dagger.internal.DaggerGenerated;
import dagger.internal.InstanceFactory;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class ATraceLoggerTransitionProgressListener_Factory_Impl implements ATraceLoggerTransitionProgressListener.Factory {
  private final ATraceLoggerTransitionProgressListener_Factory delegateFactory;

  ATraceLoggerTransitionProgressListener_Factory_Impl(
      ATraceLoggerTransitionProgressListener_Factory delegateFactory) {
    this.delegateFactory = delegateFactory;
  }

  @Override
  public ATraceLoggerTransitionProgressListener create(String details) {
    return delegateFactory.get(details);
  }

  public static Provider<ATraceLoggerTransitionProgressListener.Factory> create(
      ATraceLoggerTransitionProgressListener_Factory delegateFactory) {
    return InstanceFactory.create(new ATraceLoggerTransitionProgressListener_Factory_Impl(delegateFactory));
  }

  public static dagger.internal.Provider<ATraceLoggerTransitionProgressListener.Factory> createFactoryProvider(
      ATraceLoggerTransitionProgressListener_Factory delegateFactory) {
    return InstanceFactory.create(new ATraceLoggerTransitionProgressListener_Factory_Impl(delegateFactory));
  }
}
