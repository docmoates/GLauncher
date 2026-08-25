package com.android.systemui.unfold.system;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import java.util.concurrent.Executor;
import javax.annotation.processing.Generated;
import kotlinx.coroutines.CoroutineScope;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata({
    "com.android.systemui.unfold.dagger.UnfoldTracking",
    "com.android.systemui.unfold.dagger.UnfoldSingleThreadBg",
    "com.android.systemui.dagger.qualifiers.Application"
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
public final class SystemUnfoldSharedModule_Companion_UnfoldTrackingContextFactory implements Factory<CoroutineScope> {
  private final Provider<Executor> singleThreadBgExecutorProvider;

  private final Provider<CoroutineScope> applicationScopeProvider;

  private SystemUnfoldSharedModule_Companion_UnfoldTrackingContextFactory(
      Provider<Executor> singleThreadBgExecutorProvider,
      Provider<CoroutineScope> applicationScopeProvider) {
    this.singleThreadBgExecutorProvider = singleThreadBgExecutorProvider;
    this.applicationScopeProvider = applicationScopeProvider;
  }

  @Override
  public CoroutineScope get() {
    return unfoldTrackingContext(singleThreadBgExecutorProvider.get(), applicationScopeProvider.get());
  }

  public static SystemUnfoldSharedModule_Companion_UnfoldTrackingContextFactory create(
      Provider<Executor> singleThreadBgExecutorProvider,
      Provider<CoroutineScope> applicationScopeProvider) {
    return new SystemUnfoldSharedModule_Companion_UnfoldTrackingContextFactory(singleThreadBgExecutorProvider, applicationScopeProvider);
  }

  public static CoroutineScope unfoldTrackingContext(Executor singleThreadBgExecutor,
      CoroutineScope applicationScope) {
    return Preconditions.checkNotNullFromProvides(SystemUnfoldSharedModule.Companion.unfoldTrackingContext(singleThreadBgExecutor, applicationScope));
  }
}
