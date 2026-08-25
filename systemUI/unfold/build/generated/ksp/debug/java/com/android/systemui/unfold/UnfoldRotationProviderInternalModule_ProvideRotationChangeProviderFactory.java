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

@ScopeMetadata("javax.inject.Singleton")
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
public final class UnfoldRotationProviderInternalModule_ProvideRotationChangeProviderFactory implements Factory<RotationChangeProvider> {
  private final UnfoldRotationProviderInternalModule module;

  private final Provider<RotationChangeProvider.Factory> rotationChangeProviderFactoryProvider;

  private final Provider<Handler> callbackHandlerProvider;

  private UnfoldRotationProviderInternalModule_ProvideRotationChangeProviderFactory(
      UnfoldRotationProviderInternalModule module,
      Provider<RotationChangeProvider.Factory> rotationChangeProviderFactoryProvider,
      Provider<Handler> callbackHandlerProvider) {
    this.module = module;
    this.rotationChangeProviderFactoryProvider = rotationChangeProviderFactoryProvider;
    this.callbackHandlerProvider = callbackHandlerProvider;
  }

  @Override
  public RotationChangeProvider get() {
    return provideRotationChangeProvider(module, rotationChangeProviderFactoryProvider.get(), callbackHandlerProvider.get());
  }

  public static UnfoldRotationProviderInternalModule_ProvideRotationChangeProviderFactory create(
      UnfoldRotationProviderInternalModule module,
      Provider<RotationChangeProvider.Factory> rotationChangeProviderFactoryProvider,
      Provider<Handler> callbackHandlerProvider) {
    return new UnfoldRotationProviderInternalModule_ProvideRotationChangeProviderFactory(module, rotationChangeProviderFactoryProvider, callbackHandlerProvider);
  }

  public static RotationChangeProvider provideRotationChangeProvider(
      UnfoldRotationProviderInternalModule instance,
      RotationChangeProvider.Factory rotationChangeProviderFactory, Handler callbackHandler) {
    return Preconditions.checkNotNullFromProvides(instance.provideRotationChangeProvider(rotationChangeProviderFactory, callbackHandler));
  }
}
