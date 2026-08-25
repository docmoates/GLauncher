package com.android.systemui.unfold.system;

import com.android.systemui.unfold.updates.FoldProvider;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import java.util.concurrent.Executor;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("com.android.systemui.unfold.dagger.UnfoldMain")
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
public final class DeviceStateRepositoryImpl_Factory implements Factory<DeviceStateRepositoryImpl> {
  private final Provider<FoldProvider> foldProvider;

  private final Provider<Executor> executorProvider;

  private DeviceStateRepositoryImpl_Factory(Provider<FoldProvider> foldProvider,
      Provider<Executor> executorProvider) {
    this.foldProvider = foldProvider;
    this.executorProvider = executorProvider;
  }

  @Override
  public DeviceStateRepositoryImpl get() {
    return newInstance(foldProvider.get(), executorProvider.get());
  }

  public static DeviceStateRepositoryImpl_Factory create(Provider<FoldProvider> foldProvider,
      Provider<Executor> executorProvider) {
    return new DeviceStateRepositoryImpl_Factory(foldProvider, executorProvider);
  }

  public static DeviceStateRepositoryImpl newInstance(FoldProvider foldProvider,
      Executor executor) {
    return new DeviceStateRepositoryImpl(foldProvider, executor);
  }
}
