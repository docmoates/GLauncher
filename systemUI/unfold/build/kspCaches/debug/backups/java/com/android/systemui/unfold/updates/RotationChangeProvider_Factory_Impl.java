package com.android.systemui.unfold.updates;

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
public final class RotationChangeProvider_Factory_Impl implements RotationChangeProvider.Factory {
  private final RotationChangeProvider_Factory delegateFactory;

  RotationChangeProvider_Factory_Impl(RotationChangeProvider_Factory delegateFactory) {
    this.delegateFactory = delegateFactory;
  }

  @Override
  public RotationChangeProvider create(Handler callbackHandler) {
    return delegateFactory.get(callbackHandler);
  }

  public static Provider<RotationChangeProvider.Factory> create(
      RotationChangeProvider_Factory delegateFactory) {
    return InstanceFactory.create(new RotationChangeProvider_Factory_Impl(delegateFactory));
  }

  public static dagger.internal.Provider<RotationChangeProvider.Factory> createFactoryProvider(
      RotationChangeProvider_Factory delegateFactory) {
    return InstanceFactory.create(new RotationChangeProvider_Factory_Impl(delegateFactory));
  }
}
