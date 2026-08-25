package com.android.systemui.unfold.updates;

import android.os.Handler;
import com.android.systemui.unfold.updates.hinge.HingeAngleProvider;
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
public final class DeviceFoldStateProvider_Factory_Impl implements DeviceFoldStateProvider.Factory {
  private final DeviceFoldStateProvider_Factory delegateFactory;

  DeviceFoldStateProvider_Factory_Impl(DeviceFoldStateProvider_Factory delegateFactory) {
    this.delegateFactory = delegateFactory;
  }

  @Override
  public DeviceFoldStateProvider create(HingeAngleProvider hingeAngleProvider,
      RotationChangeProvider rotationChangeProvider, Handler progressHandler) {
    return delegateFactory.get(hingeAngleProvider, rotationChangeProvider, progressHandler);
  }

  public static Provider<DeviceFoldStateProvider.Factory> create(
      DeviceFoldStateProvider_Factory delegateFactory) {
    return InstanceFactory.create(new DeviceFoldStateProvider_Factory_Impl(delegateFactory));
  }

  public static dagger.internal.Provider<DeviceFoldStateProvider.Factory> createFactoryProvider(
      DeviceFoldStateProvider_Factory delegateFactory) {
    return InstanceFactory.create(new DeviceFoldStateProvider_Factory_Impl(delegateFactory));
  }
}
