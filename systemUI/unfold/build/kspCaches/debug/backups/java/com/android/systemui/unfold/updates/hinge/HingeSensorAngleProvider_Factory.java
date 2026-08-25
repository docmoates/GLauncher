package com.android.systemui.unfold.updates.hinge;

import android.hardware.SensorManager;
import android.os.Handler;
import dagger.internal.DaggerGenerated;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import java.util.concurrent.Executor;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata("com.android.systemui.unfold.dagger.UnfoldSingleThreadBg")
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
public final class HingeSensorAngleProvider_Factory {
  private final Provider<SensorManager> sensorManagerProvider;

  private final Provider<Executor> singleThreadBgExecutorProvider;

  private HingeSensorAngleProvider_Factory(Provider<SensorManager> sensorManagerProvider,
      Provider<Executor> singleThreadBgExecutorProvider) {
    this.sensorManagerProvider = sensorManagerProvider;
    this.singleThreadBgExecutorProvider = singleThreadBgExecutorProvider;
  }

  public HingeSensorAngleProvider get(Handler listenerHandler) {
    return newInstance(sensorManagerProvider.get(), singleThreadBgExecutorProvider.get(), listenerHandler);
  }

  public static HingeSensorAngleProvider_Factory create(
      Provider<SensorManager> sensorManagerProvider,
      Provider<Executor> singleThreadBgExecutorProvider) {
    return new HingeSensorAngleProvider_Factory(sensorManagerProvider, singleThreadBgExecutorProvider);
  }

  public static HingeSensorAngleProvider newInstance(SensorManager sensorManager,
      Executor singleThreadBgExecutor, Handler listenerHandler) {
    return new HingeSensorAngleProvider(sensorManager, singleThreadBgExecutor, listenerHandler);
  }
}
