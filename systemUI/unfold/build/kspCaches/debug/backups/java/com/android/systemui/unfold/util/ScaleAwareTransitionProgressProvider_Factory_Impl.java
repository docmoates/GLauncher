package com.android.systemui.unfold.util;

import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
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
public final class ScaleAwareTransitionProgressProvider_Factory_Impl implements ScaleAwareTransitionProgressProvider.Factory {
  private final ScaleAwareTransitionProgressProvider_Factory delegateFactory;

  ScaleAwareTransitionProgressProvider_Factory_Impl(
      ScaleAwareTransitionProgressProvider_Factory delegateFactory) {
    this.delegateFactory = delegateFactory;
  }

  @Override
  public ScaleAwareTransitionProgressProvider wrap(
      UnfoldTransitionProgressProvider progressProvider) {
    return delegateFactory.get(progressProvider);
  }

  public static Provider<ScaleAwareTransitionProgressProvider.Factory> create(
      ScaleAwareTransitionProgressProvider_Factory delegateFactory) {
    return InstanceFactory.create(new ScaleAwareTransitionProgressProvider_Factory_Impl(delegateFactory));
  }

  public static dagger.internal.Provider<ScaleAwareTransitionProgressProvider.Factory> createFactoryProvider(
      ScaleAwareTransitionProgressProvider_Factory delegateFactory) {
    return InstanceFactory.create(new ScaleAwareTransitionProgressProvider_Factory_Impl(delegateFactory));
  }
}
