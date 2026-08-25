package com.android.systemui.unfold;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata("com.android.systemui.unfold.dagger.UseReceivingFilter")
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
public final class UnfoldRemoteModule_UseReceivingFilterFactory implements Factory<Boolean> {
  private final UnfoldRemoteModule module;

  private UnfoldRemoteModule_UseReceivingFilterFactory(UnfoldRemoteModule module) {
    this.module = module;
  }

  @Override
  public Boolean get() {
    return useReceivingFilter(module);
  }

  public static UnfoldRemoteModule_UseReceivingFilterFactory create(UnfoldRemoteModule module) {
    return new UnfoldRemoteModule_UseReceivingFilterFactory(module);
  }

  public static boolean useReceivingFilter(UnfoldRemoteModule instance) {
    return instance.useReceivingFilter();
  }
}
