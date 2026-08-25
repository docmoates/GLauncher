package com.android.systemui.unfold;

import android.os.Handler;
import com.android.systemui.unfold.updates.RotationChangeProvider;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class UnfoldRemoteModule_ProvideMainRotationChangeProviderFactory implements Factory<RotationChangeProvider> {
  private final UnfoldRemoteModule module;

  private final Provider<RotationChangeProvider.Factory> rotationChangeProviderFactoryProvider;

  private final Provider<Handler> callbackHandlerProvider;

  private UnfoldRemoteModule_ProvideMainRotationChangeProviderFactory(UnfoldRemoteModule module,
      Provider<RotationChangeProvider.Factory> rotationChangeProviderFactoryProvider,
      Provider<Handler> callbackHandlerProvider) {
    this.module = module;
    this.rotationChangeProviderFactoryProvider = rotationChangeProviderFactoryProvider;
    this.callbackHandlerProvider = callbackHandlerProvider;
  }

  @Override
  public RotationChangeProvider get() {
    return provideMainRotationChangeProvider(module, rotationChangeProviderFactoryProvider.get(), callbackHandlerProvider.get());
  }

  public static UnfoldRemoteModule_ProvideMainRotationChangeProviderFactory create(
      UnfoldRemoteModule module,
      Provider<RotationChangeProvider.Factory> rotationChangeProviderFactoryProvider,
      Provider<Handler> callbackHandlerProvider) {
    return new UnfoldRemoteModule_ProvideMainRotationChangeProviderFactory(module, rotationChangeProviderFactoryProvider, callbackHandlerProvider);
  }

  public static RotationChangeProvider provideMainRotationChangeProvider(
      UnfoldRemoteModule instance, RotationChangeProvider.Factory rotationChangeProviderFactory,
      Handler callbackHandler) {
    return Preconditions.checkNotNullFromProvides(instance.provideMainRotationChangeProvider(rotationChangeProviderFactory, callbackHandler));
  }
}
