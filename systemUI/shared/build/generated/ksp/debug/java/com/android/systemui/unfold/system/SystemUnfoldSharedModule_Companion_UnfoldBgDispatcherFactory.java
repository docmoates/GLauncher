package com.android.systemui.unfold.system;

import android.os.Handler;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import kotlinx.coroutines.CoroutineDispatcher;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("com.android.systemui.unfold.dagger.UnfoldBg")
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
public final class SystemUnfoldSharedModule_Companion_UnfoldBgDispatcherFactory implements Factory<CoroutineDispatcher> {
  private final Provider<Handler> handlerProvider;

  private SystemUnfoldSharedModule_Companion_UnfoldBgDispatcherFactory(
      Provider<Handler> handlerProvider) {
    this.handlerProvider = handlerProvider;
  }

  @Override
  public CoroutineDispatcher get() {
    return unfoldBgDispatcher(handlerProvider.get());
  }

  public static SystemUnfoldSharedModule_Companion_UnfoldBgDispatcherFactory create(
      Provider<Handler> handlerProvider) {
    return new SystemUnfoldSharedModule_Companion_UnfoldBgDispatcherFactory(handlerProvider);
  }

  public static CoroutineDispatcher unfoldBgDispatcher(Handler handler) {
    return Preconditions.checkNotNullFromProvides(SystemUnfoldSharedModule.Companion.unfoldBgDispatcher(handler));
  }
}
