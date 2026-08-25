package com.android.systemui.unfold.progress;

import dagger.internal.DaggerGenerated;
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
public final class UnfoldFrameCallbackScheduler_Factory {
  public UnfoldFrameCallbackScheduler get() {
    return newInstance();
  }

  public static UnfoldFrameCallbackScheduler_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static UnfoldFrameCallbackScheduler newInstance() {
    return new UnfoldFrameCallbackScheduler();
  }

  private static final class InstanceHolder {
    static final UnfoldFrameCallbackScheduler_Factory INSTANCE = new UnfoldFrameCallbackScheduler_Factory();
  }
}
