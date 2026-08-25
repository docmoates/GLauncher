package com.android.systemui.unfold.progress;

import com.android.systemui.unfold.updates.FoldStateProvider;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
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
public final class FixedTimingTransitionProgressProvider_Factory implements Factory<FixedTimingTransitionProgressProvider> {
  private final Provider<FoldStateProvider> foldStateProvider;

  private FixedTimingTransitionProgressProvider_Factory(
      Provider<FoldStateProvider> foldStateProvider) {
    this.foldStateProvider = foldStateProvider;
  }

  @Override
  public FixedTimingTransitionProgressProvider get() {
    return newInstance(foldStateProvider.get());
  }

  public static FixedTimingTransitionProgressProvider_Factory create(
      Provider<FoldStateProvider> foldStateProvider) {
    return new FixedTimingTransitionProgressProvider_Factory(foldStateProvider);
  }

  public static FixedTimingTransitionProgressProvider newInstance(
      FoldStateProvider foldStateProvider) {
    return new FixedTimingTransitionProgressProvider(foldStateProvider);
  }
}
