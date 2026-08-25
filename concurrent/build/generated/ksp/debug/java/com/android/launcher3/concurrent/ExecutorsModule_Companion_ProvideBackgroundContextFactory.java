package com.android.launcher3.concurrent;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import java.util.concurrent.Executor;
import javax.annotation.processing.Generated;
import kotlin.coroutines.CoroutineContext;

@ScopeMetadata
@QualifierMetadata({
    "com.android.launcher3.concurrent.annotations.BackgroundContext",
    "com.android.launcher3.concurrent.annotations.Background"
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
public final class ExecutorsModule_Companion_ProvideBackgroundContextFactory implements Factory<CoroutineContext> {
  private final Provider<Executor> executorProvider;

  private ExecutorsModule_Companion_ProvideBackgroundContextFactory(
      Provider<Executor> executorProvider) {
    this.executorProvider = executorProvider;
  }

  @Override
  public CoroutineContext get() {
    return provideBackgroundContext(executorProvider.get());
  }

  public static ExecutorsModule_Companion_ProvideBackgroundContextFactory create(
      Provider<Executor> executorProvider) {
    return new ExecutorsModule_Companion_ProvideBackgroundContextFactory(executorProvider);
  }

  public static CoroutineContext provideBackgroundContext(Executor executor) {
    return Preconditions.checkNotNullFromProvides(ExecutorsModule.Companion.provideBackgroundContext(executor));
  }
}
