package com.android.systemui.unfold.system;

import android.os.Handler;
import android.os.Looper;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class SystemUnfoldSharedModule_Companion_UnfoldBgProgressHandlerFactory implements Factory<Handler> {
  private final Provider<Looper> looperProvider;

  private SystemUnfoldSharedModule_Companion_UnfoldBgProgressHandlerFactory(
      Provider<Looper> looperProvider) {
    this.looperProvider = looperProvider;
  }

  @Override
  public Handler get() {
    return unfoldBgProgressHandler(looperProvider.get());
  }

  public static SystemUnfoldSharedModule_Companion_UnfoldBgProgressHandlerFactory create(
      Provider<Looper> looperProvider) {
    return new SystemUnfoldSharedModule_Companion_UnfoldBgProgressHandlerFactory(looperProvider);
  }

  public static Handler unfoldBgProgressHandler(Looper looper) {
    return Preconditions.checkNotNullFromProvides(SystemUnfoldSharedModule.Companion.unfoldBgProgressHandler(looper));
  }
}
