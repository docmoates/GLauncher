package com.android.systemui.unfold.progress;

import android.content.Context;
import android.os.Handler;
import com.android.systemui.unfold.updates.FoldStateProvider;
import dagger.internal.DaggerGenerated;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
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
public final class PhysicsBasedUnfoldTransitionProgressProvider_Factory {
  private final Provider<Context> contextProvider;

  private final Provider<UnfoldFrameCallbackScheduler.Factory> schedulerFactoryProvider;

  private PhysicsBasedUnfoldTransitionProgressProvider_Factory(Provider<Context> contextProvider,
      Provider<UnfoldFrameCallbackScheduler.Factory> schedulerFactoryProvider) {
    this.contextProvider = contextProvider;
    this.schedulerFactoryProvider = schedulerFactoryProvider;
  }

  public PhysicsBasedUnfoldTransitionProgressProvider get(FoldStateProvider foldStateProvider,
      Handler progressHandler) {
    return newInstance(contextProvider.get(), schedulerFactoryProvider.get(), foldStateProvider, progressHandler);
  }

  public static PhysicsBasedUnfoldTransitionProgressProvider_Factory create(
      Provider<Context> contextProvider,
      Provider<UnfoldFrameCallbackScheduler.Factory> schedulerFactoryProvider) {
    return new PhysicsBasedUnfoldTransitionProgressProvider_Factory(contextProvider, schedulerFactoryProvider);
  }

  public static PhysicsBasedUnfoldTransitionProgressProvider newInstance(Context context,
      UnfoldFrameCallbackScheduler.Factory schedulerFactory, FoldStateProvider foldStateProvider,
      Handler progressHandler) {
    return new PhysicsBasedUnfoldTransitionProgressProvider(context, schedulerFactory, foldStateProvider, progressHandler);
  }
}
