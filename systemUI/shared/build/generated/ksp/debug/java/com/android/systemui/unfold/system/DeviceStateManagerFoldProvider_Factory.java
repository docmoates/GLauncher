package com.android.systemui.unfold.system;

import android.content.Context;
import android.hardware.devicestate.DeviceStateManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
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
public final class DeviceStateManagerFoldProvider_Factory implements Factory<DeviceStateManagerFoldProvider> {
  private final Provider<DeviceStateManager> deviceStateManagerProvider;

  private final Provider<Context> contextProvider;

  private DeviceStateManagerFoldProvider_Factory(
      Provider<DeviceStateManager> deviceStateManagerProvider, Provider<Context> contextProvider) {
    this.deviceStateManagerProvider = deviceStateManagerProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public DeviceStateManagerFoldProvider get() {
    return newInstance(deviceStateManagerProvider.get(), contextProvider.get());
  }

  public static DeviceStateManagerFoldProvider_Factory create(
      Provider<DeviceStateManager> deviceStateManagerProvider, Provider<Context> contextProvider) {
    return new DeviceStateManagerFoldProvider_Factory(deviceStateManagerProvider, contextProvider);
  }

  public static DeviceStateManagerFoldProvider newInstance(DeviceStateManager deviceStateManager,
      Context context) {
    return new DeviceStateManagerFoldProvider(deviceStateManager, context);
  }
}
