package com.android.systemui.unfold.progress;

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
public final class UnfoldFrameCallbackScheduler_Factory_Impl implements UnfoldFrameCallbackScheduler.Factory {
  private final UnfoldFrameCallbackScheduler_Factory delegateFactory;

  UnfoldFrameCallbackScheduler_Factory_Impl(UnfoldFrameCallbackScheduler_Factory delegateFactory) {
    this.delegateFactory = delegateFactory;
  }

  @Override
  public UnfoldFrameCallbackScheduler create() {
    return delegateFactory.get();
  }

  public static Provider<UnfoldFrameCallbackScheduler.Factory> create(
      UnfoldFrameCallbackScheduler_Factory delegateFactory) {
    return InstanceFactory.create(new UnfoldFrameCallbackScheduler_Factory_Impl(delegateFactory));
  }

  public static dagger.internal.Provider<UnfoldFrameCallbackScheduler.Factory> createFactoryProvider(
      UnfoldFrameCallbackScheduler_Factory delegateFactory) {
    return InstanceFactory.create(new UnfoldFrameCallbackScheduler_Factory_Impl(delegateFactory));
  }
}
