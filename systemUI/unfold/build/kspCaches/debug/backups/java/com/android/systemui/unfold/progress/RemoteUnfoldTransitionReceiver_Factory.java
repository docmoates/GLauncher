package com.android.systemui.unfold.progress;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import java.util.concurrent.Executor;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata({
    "com.android.systemui.unfold.dagger.UseReceivingFilter",
    "com.android.systemui.unfold.dagger.UnfoldMain"
})
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
public final class RemoteUnfoldTransitionReceiver_Factory implements Factory<RemoteUnfoldTransitionReceiver> {
  private final Provider<Boolean> useReceivingFilterProvider;

  private final Provider<Executor> executorProvider;

  private RemoteUnfoldTransitionReceiver_Factory(Provider<Boolean> useReceivingFilterProvider,
      Provider<Executor> executorProvider) {
    this.useReceivingFilterProvider = useReceivingFilterProvider;
    this.executorProvider = executorProvider;
  }

  @Override
  public RemoteUnfoldTransitionReceiver get() {
    return newInstance(useReceivingFilterProvider.get(), executorProvider.get());
  }

  public static RemoteUnfoldTransitionReceiver_Factory create(
      Provider<Boolean> useReceivingFilterProvider, Provider<Executor> executorProvider) {
    return new RemoteUnfoldTransitionReceiver_Factory(useReceivingFilterProvider, executorProvider);
  }

  public static RemoteUnfoldTransitionReceiver newInstance(boolean useReceivingFilter,
      Executor executor) {
    return new RemoteUnfoldTransitionReceiver(useReceivingFilter, executor);
  }
}
