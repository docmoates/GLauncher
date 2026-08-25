package com.android.systemui.unfold.progress;

import android.os.Handler;
import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import dagger.internal.DaggerGenerated;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
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
public final class MainThreadUnfoldTransitionProgressProvider_Factory {
  private final Provider<Handler> mainHandlerProvider;

  private MainThreadUnfoldTransitionProgressProvider_Factory(
      Provider<Handler> mainHandlerProvider) {
    this.mainHandlerProvider = mainHandlerProvider;
  }

  public MainThreadUnfoldTransitionProgressProvider get(
      UnfoldTransitionProgressProvider rootProvider) {
    return newInstance(mainHandlerProvider.get(), rootProvider);
  }

  public static MainThreadUnfoldTransitionProgressProvider_Factory create(
      Provider<Handler> mainHandlerProvider) {
    return new MainThreadUnfoldTransitionProgressProvider_Factory(mainHandlerProvider);
  }

  public static MainThreadUnfoldTransitionProgressProvider newInstance(Handler mainHandler,
      UnfoldTransitionProgressProvider rootProvider) {
    return new MainThreadUnfoldTransitionProgressProvider(mainHandler, rootProvider);
  }
}
