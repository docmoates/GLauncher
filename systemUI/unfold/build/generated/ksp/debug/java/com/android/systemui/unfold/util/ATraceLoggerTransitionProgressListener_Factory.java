package com.android.systemui.unfold.util;

import dagger.internal.DaggerGenerated;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata("com.android.systemui.unfold.util.UnfoldTransitionATracePrefix")
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
public final class ATraceLoggerTransitionProgressListener_Factory {
  private final Provider<String> tracePrefixProvider;

  private ATraceLoggerTransitionProgressListener_Factory(Provider<String> tracePrefixProvider) {
    this.tracePrefixProvider = tracePrefixProvider;
  }

  public ATraceLoggerTransitionProgressListener get(String details) {
    return newInstance(tracePrefixProvider.get(), details);
  }

  public static ATraceLoggerTransitionProgressListener_Factory create(
      Provider<String> tracePrefixProvider) {
    return new ATraceLoggerTransitionProgressListener_Factory(tracePrefixProvider);
  }

  public static ATraceLoggerTransitionProgressListener newInstance(String tracePrefix,
      String details) {
    return new ATraceLoggerTransitionProgressListener(tracePrefix, details);
  }
}
