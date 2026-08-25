package com.android.systemui.unfold.progress;

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
public final class MainThreadUnfoldTransitionProgressProvider_Factory_Impl implements MainThreadUnfoldTransitionProgressProvider.Factory {
  private final MainThreadUnfoldTransitionProgressProvider_Factory delegateFactory;

  MainThreadUnfoldTransitionProgressProvider_Factory_Impl(
      MainThreadUnfoldTransitionProgressProvider_Factory delegateFactory) {
    this.delegateFactory = delegateFactory;
  }

  @Override
  public MainThreadUnfoldTransitionProgressProvider create(
      UnfoldTransitionProgressProvider rootProvider) {
    return delegateFactory.get(rootProvider);
  }

  public static Provider<MainThreadUnfoldTransitionProgressProvider.Factory> create(
      MainThreadUnfoldTransitionProgressProvider_Factory delegateFactory) {
    return InstanceFactory.create(new MainThreadUnfoldTransitionProgressProvider_Factory_Impl(delegateFactory));
  }

  public static dagger.internal.Provider<MainThreadUnfoldTransitionProgressProvider.Factory> createFactoryProvider(
      MainThreadUnfoldTransitionProgressProvider_Factory delegateFactory) {
    return InstanceFactory.create(new MainThreadUnfoldTransitionProgressProvider_Factory_Impl(delegateFactory));
  }
}
