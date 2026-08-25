package com.android.systemui.unfold.system;

import android.app.ActivityManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class ActivityManagerActivityTypeProvider_Factory implements Factory<ActivityManagerActivityTypeProvider> {
  private final Provider<ActivityManager> activityManagerProvider;

  private ActivityManagerActivityTypeProvider_Factory(
      Provider<ActivityManager> activityManagerProvider) {
    this.activityManagerProvider = activityManagerProvider;
  }

  @Override
  public ActivityManagerActivityTypeProvider get() {
    return newInstance(activityManagerProvider.get());
  }

  public static ActivityManagerActivityTypeProvider_Factory create(
      Provider<ActivityManager> activityManagerProvider) {
    return new ActivityManagerActivityTypeProvider_Factory(activityManagerProvider);
  }

  public static ActivityManagerActivityTypeProvider newInstance(ActivityManager activityManager) {
    return new ActivityManagerActivityTypeProvider(activityManager);
  }
}
