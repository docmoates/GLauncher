package com.android.systemui.unfold;

import com.android.systemui.unfold.util.UnfoldKeyguardVisibilityManagerImpl;
import com.android.systemui.unfold.util.UnfoldKeyguardVisibilityProvider;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class UnfoldSharedModule_UnfoldKeyguardVisibilityProviderFactory implements Factory<UnfoldKeyguardVisibilityProvider> {
  private final UnfoldSharedModule module;

  private final Provider<UnfoldKeyguardVisibilityManagerImpl> implProvider;

  private UnfoldSharedModule_UnfoldKeyguardVisibilityProviderFactory(UnfoldSharedModule module,
      Provider<UnfoldKeyguardVisibilityManagerImpl> implProvider) {
    this.module = module;
    this.implProvider = implProvider;
  }

  @Override
  public UnfoldKeyguardVisibilityProvider get() {
    return unfoldKeyguardVisibilityProvider(module, implProvider.get());
  }

  public static UnfoldSharedModule_UnfoldKeyguardVisibilityProviderFactory create(
      UnfoldSharedModule module, Provider<UnfoldKeyguardVisibilityManagerImpl> implProvider) {
    return new UnfoldSharedModule_UnfoldKeyguardVisibilityProviderFactory(module, implProvider);
  }

  public static UnfoldKeyguardVisibilityProvider unfoldKeyguardVisibilityProvider(
      UnfoldSharedModule instance, UnfoldKeyguardVisibilityManagerImpl impl) {
    return Preconditions.checkNotNullFromProvides(instance.unfoldKeyguardVisibilityProvider(impl));
  }
}
