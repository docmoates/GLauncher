package com.android.systemui.unfold.config;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class ResourceUnfoldTransitionConfig_Factory implements Factory<ResourceUnfoldTransitionConfig> {
  @Override
  public ResourceUnfoldTransitionConfig get() {
    return newInstance();
  }

  public static ResourceUnfoldTransitionConfig_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ResourceUnfoldTransitionConfig newInstance() {
    return new ResourceUnfoldTransitionConfig();
  }

  private static final class InstanceHolder {
    static final ResourceUnfoldTransitionConfig_Factory INSTANCE = new ResourceUnfoldTransitionConfig_Factory();
  }
}
