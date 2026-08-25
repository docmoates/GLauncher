package com.android.systemui.unfold.updates;

import android.content.Context;
import android.os.Handler;
import com.android.systemui.unfold.config.UnfoldTransitionConfig;
import com.android.systemui.unfold.updates.hinge.HingeAngleProvider;
import com.android.systemui.unfold.updates.screen.ScreenStatusProvider;
import com.android.systemui.unfold.util.CurrentActivityTypeProvider;
import com.android.systemui.unfold.util.UnfoldKeyguardVisibilityProvider;
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
public final class DeviceFoldStateProvider_Factory {
  private final Provider<UnfoldTransitionConfig> configProvider;

  private final Provider<Context> contextProvider;

  private final Provider<ScreenStatusProvider> screenStatusProvider;

  private final Provider<CurrentActivityTypeProvider> activityTypeProvider;

  private final Provider<UnfoldKeyguardVisibilityProvider> unfoldKeyguardVisibilityProvider;

  private final Provider<FoldProvider> foldProvider;

  private DeviceFoldStateProvider_Factory(Provider<UnfoldTransitionConfig> configProvider,
      Provider<Context> contextProvider, Provider<ScreenStatusProvider> screenStatusProvider,
      Provider<CurrentActivityTypeProvider> activityTypeProvider,
      Provider<UnfoldKeyguardVisibilityProvider> unfoldKeyguardVisibilityProvider,
      Provider<FoldProvider> foldProvider) {
    this.configProvider = configProvider;
    this.contextProvider = contextProvider;
    this.screenStatusProvider = screenStatusProvider;
    this.activityTypeProvider = activityTypeProvider;
    this.unfoldKeyguardVisibilityProvider = unfoldKeyguardVisibilityProvider;
    this.foldProvider = foldProvider;
  }

  public DeviceFoldStateProvider get(HingeAngleProvider hingeAngleProvider,
      RotationChangeProvider rotationChangeProvider, Handler progressHandler) {
    return newInstance(configProvider.get(), contextProvider.get(), screenStatusProvider.get(), activityTypeProvider.get(), unfoldKeyguardVisibilityProvider.get(), foldProvider.get(), hingeAngleProvider, rotationChangeProvider, progressHandler);
  }

  public static DeviceFoldStateProvider_Factory create(
      Provider<UnfoldTransitionConfig> configProvider, Provider<Context> contextProvider,
      Provider<ScreenStatusProvider> screenStatusProvider,
      Provider<CurrentActivityTypeProvider> activityTypeProvider,
      Provider<UnfoldKeyguardVisibilityProvider> unfoldKeyguardVisibilityProvider,
      Provider<FoldProvider> foldProvider) {
    return new DeviceFoldStateProvider_Factory(configProvider, contextProvider, screenStatusProvider, activityTypeProvider, unfoldKeyguardVisibilityProvider, foldProvider);
  }

  public static DeviceFoldStateProvider newInstance(UnfoldTransitionConfig config, Context context,
      ScreenStatusProvider screenStatusProvider, CurrentActivityTypeProvider activityTypeProvider,
      UnfoldKeyguardVisibilityProvider unfoldKeyguardVisibilityProvider, FoldProvider foldProvider,
      HingeAngleProvider hingeAngleProvider, RotationChangeProvider rotationChangeProvider,
      Handler progressHandler) {
    return new DeviceFoldStateProvider(config, context, screenStatusProvider, activityTypeProvider, unfoldKeyguardVisibilityProvider, foldProvider, hingeAngleProvider, rotationChangeProvider, progressHandler);
  }
}
