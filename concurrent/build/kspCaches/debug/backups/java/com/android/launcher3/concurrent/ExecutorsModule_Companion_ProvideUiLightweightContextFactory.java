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
    "com.android.launcher3.concurrent.annotations.LightweightBackgroundContext",
    "com.android.launcher3.concurrent.annotations.LightweightBackground"
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
public final class ExecutorsModule_Companion_ProvideUiLightweightContextFactory implements Factory<CoroutineContext> {
  private final Provider<Executor> executorProvider;

  private ExecutorsModule_Companion_ProvideUiLightweightContextFactory(
      Provider<Executor> executorProvider) {
    this.executorProvider = executorProvider;
  }

  @Override
  public CoroutineContext get() {
    return provideUiLightweightContext(executorProvider.get());
  }

  public static ExecutorsModule_Companion_ProvideUiLightweightContextFactory create(
      Provider<Executor> executorProvider) {
    return new ExecutorsModule_Companion_ProvideUiLightweightContextFactory(executorProvider);
  }

  public static CoroutineContext provideUiLightweightContext(Executor executor) {
    return Preconditions.checkNotNullFromProvides(ExecutorsModule.Companion.provideUiLightweightContext(executor));
  }
}
