package com.android.systemui.unfold.util;

import android.content.ContentResolver;
import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import dagger.internal.DaggerGenerated;
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
public final class ScaleAwareTransitionProgressProvider_Factory {
  private final Provider<ContentResolver> contentResolverProvider;

  private ScaleAwareTransitionProgressProvider_Factory(
      Provider<ContentResolver> contentResolverProvider) {
    this.contentResolverProvider = contentResolverProvider;
  }

  public ScaleAwareTransitionProgressProvider get(
      UnfoldTransitionProgressProvider progressProviderToWrap) {
    return newInstance(progressProviderToWrap, contentResolverProvider.get());
  }

  public static ScaleAwareTransitionProgressProvider_Factory create(
      Provider<ContentResolver> contentResolverProvider) {
    return new ScaleAwareTransitionProgressProvider_Factory(contentResolverProvider);
  }

  public static ScaleAwareTransitionProgressProvider newInstance(
      UnfoldTransitionProgressProvider progressProviderToWrap, ContentResolver contentResolver) {
    return new ScaleAwareTransitionProgressProvider(progressProviderToWrap, contentResolver);
  }
}
