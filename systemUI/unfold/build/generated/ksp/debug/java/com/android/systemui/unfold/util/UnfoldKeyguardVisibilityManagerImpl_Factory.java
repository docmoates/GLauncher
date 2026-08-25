package com.android.systemui.unfold.util;

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
public final class UnfoldKeyguardVisibilityManagerImpl_Factory implements Factory<UnfoldKeyguardVisibilityManagerImpl> {
  @Override
  public UnfoldKeyguardVisibilityManagerImpl get() {
    return newInstance();
  }

  public static UnfoldKeyguardVisibilityManagerImpl_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static UnfoldKeyguardVisibilityManagerImpl newInstance() {
    return new UnfoldKeyguardVisibilityManagerImpl();
  }

  private static final class InstanceHolder {
    static final UnfoldKeyguardVisibilityManagerImpl_Factory INSTANCE = new UnfoldKeyguardVisibilityManagerImpl_Factory();
  }
}
