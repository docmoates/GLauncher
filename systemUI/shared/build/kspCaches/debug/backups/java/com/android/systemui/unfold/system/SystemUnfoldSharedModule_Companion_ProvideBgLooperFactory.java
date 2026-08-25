package com.android.systemui.unfold.system;

import android.os.Looper;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class SystemUnfoldSharedModule_Companion_ProvideBgLooperFactory implements Factory<Looper> {
  @Override
  public Looper get() {
    return provideBgLooper();
  }

  public static SystemUnfoldSharedModule_Companion_ProvideBgLooperFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static Looper provideBgLooper() {
    return Preconditions.checkNotNullFromProvides(SystemUnfoldSharedModule.Companion.provideBgLooper());
  }

  private static final class InstanceHolder {
    static final SystemUnfoldSharedModule_Companion_ProvideBgLooperFactory INSTANCE = new SystemUnfoldSharedModule_Companion_ProvideBgLooperFactory();
  }
}
