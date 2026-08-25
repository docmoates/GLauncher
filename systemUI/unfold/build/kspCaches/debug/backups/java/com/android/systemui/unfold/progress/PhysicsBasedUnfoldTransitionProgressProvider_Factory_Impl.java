package com.android.systemui.unfold.progress;

import android.os.Handler;
import com.android.systemui.unfold.updates.FoldStateProvider;
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
public final class PhysicsBasedUnfoldTransitionProgressProvider_Factory_Impl implements PhysicsBasedUnfoldTransitionProgressProvider.Factory {
  private final PhysicsBasedUnfoldTransitionProgressProvider_Factory delegateFactory;

  PhysicsBasedUnfoldTransitionProgressProvider_Factory_Impl(
      PhysicsBasedUnfoldTransitionProgressProvider_Factory delegateFactory) {
    this.delegateFactory = delegateFactory;
  }

  @Override
  public PhysicsBasedUnfoldTransitionProgressProvider create(FoldStateProvider foldStateProvider,
      Handler handler) {
    return delegateFactory.get(foldStateProvider, handler);
  }

  public static Provider<PhysicsBasedUnfoldTransitionProgressProvider.Factory> create(
      PhysicsBasedUnfoldTransitionProgressProvider_Factory delegateFactory) {
    return InstanceFactory.create(new PhysicsBasedUnfoldTransitionProgressProvider_Factory_Impl(delegateFactory));
  }

  public static dagger.internal.Provider<PhysicsBasedUnfoldTransitionProgressProvider.Factory> createFactoryProvider(
      PhysicsBasedUnfoldTransitionProgressProvider_Factory delegateFactory) {
    return InstanceFactory.create(new PhysicsBasedUnfoldTransitionProgressProvider_Factory_Impl(delegateFactory));
  }
}
