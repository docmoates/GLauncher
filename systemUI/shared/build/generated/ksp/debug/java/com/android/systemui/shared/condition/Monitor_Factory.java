package com.android.systemui.shared.condition;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import java.util.concurrent.Executor;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata("com.android.systemui.dagger.qualifiers.Main")
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
public final class Monitor_Factory implements Factory<Monitor> {
  private final Provider<Executor> executorProvider;

  private Monitor_Factory(Provider<Executor> executorProvider) {
    this.executorProvider = executorProvider;
  }

  @Override
  public Monitor get() {
    return newInstance(executorProvider.get());
  }

  public static Monitor_Factory create(Provider<Executor> executorProvider) {
    return new Monitor_Factory(executorProvider);
  }

  public static Monitor newInstance(Executor executor) {
    return new Monitor(executor);
  }
}
