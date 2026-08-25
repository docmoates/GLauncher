package com.android.systemui.unfold.updates;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Handler;
import dagger.internal.DaggerGenerated;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
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
public final class RotationChangeProvider_Factory {
  private final Provider<DisplayManager> displayManagerProvider;

  private final Provider<Context> contextProvider;

  private final Provider<Handler> bgHandlerProvider;

  private RotationChangeProvider_Factory(Provider<DisplayManager> displayManagerProvider,
      Provider<Context> contextProvider, Provider<Handler> bgHandlerProvider) {
    this.displayManagerProvider = displayManagerProvider;
    this.contextProvider = contextProvider;
    this.bgHandlerProvider = bgHandlerProvider;
  }

  public RotationChangeProvider get(Handler callbackHandler) {
    return newInstance(displayManagerProvider.get(), contextProvider.get(), bgHandlerProvider.get(), callbackHandler);
  }

  public static RotationChangeProvider_Factory create(
      Provider<DisplayManager> displayManagerProvider, Provider<Context> contextProvider,
      Provider<Handler> bgHandlerProvider) {
    return new RotationChangeProvider_Factory(displayManagerProvider, contextProvider, bgHandlerProvider);
  }

  public static RotationChangeProvider newInstance(DisplayManager displayManager, Context context,
      Handler bgHandler, Handler callbackHandler) {
    return new RotationChangeProvider(displayManager, context, bgHandler, callbackHandler);
  }
}
