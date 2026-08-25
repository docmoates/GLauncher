package com.android.systemui.unfold.updates.hinge;

import android.os.Handler;
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
public final class HingeSensorAngleProvider_Factory_Impl implements HingeSensorAngleProvider.Factory {
  private final HingeSensorAngleProvider_Factory delegateFactory;

  HingeSensorAngleProvider_Factory_Impl(HingeSensorAngleProvider_Factory delegateFactory) {
    this.delegateFactory = delegateFactory;
  }

  @Override
  public HingeSensorAngleProvider create(Handler handler) {
    return delegateFactory.get(handler);
  }

  public static Provider<HingeSensorAngleProvider.Factory> create(
      HingeSensorAngleProvider_Factory delegateFactory) {
    return InstanceFactory.create(new HingeSensorAngleProvider_Factory_Impl(delegateFactory));
  }

  public static dagger.internal.Provider<HingeSensorAngleProvider.Factory> createFactoryProvider(
      HingeSensorAngleProvider_Factory delegateFactory) {
    return InstanceFactory.create(new HingeSensorAngleProvider_Factory_Impl(delegateFactory));
  }
}
