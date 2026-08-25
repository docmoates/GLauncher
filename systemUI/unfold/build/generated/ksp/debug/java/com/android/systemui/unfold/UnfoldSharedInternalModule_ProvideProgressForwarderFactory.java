package com.android.systemui.unfold;

import com.android.systemui.unfold.config.UnfoldTransitionConfig;
import com.android.systemui.unfold.progress.UnfoldTransitionProgressForwarder;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import java.util.Optional;
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
public final class UnfoldSharedInternalModule_ProvideProgressForwarderFactory implements Factory<Optional<UnfoldTransitionProgressForwarder>> {
  private final UnfoldSharedInternalModule module;

  private final Provider<UnfoldTransitionConfig> configProvider;

  private final Provider<UnfoldTransitionProgressForwarder> progressForwarderProvider;

  private UnfoldSharedInternalModule_ProvideProgressForwarderFactory(
      UnfoldSharedInternalModule module, Provider<UnfoldTransitionConfig> configProvider,
      Provider<UnfoldTransitionProgressForwarder> progressForwarderProvider) {
    this.module = module;
    this.configProvider = configProvider;
    this.progressForwarderProvider = progressForwarderProvider;
  }

  @Override
  public Optional<UnfoldTransitionProgressForwarder> get() {
    return provideProgressForwarder(module, configProvider.get(), progressForwarderProvider);
  }

  public static UnfoldSharedInternalModule_ProvideProgressForwarderFactory create(
      UnfoldSharedInternalModule module, Provider<UnfoldTransitionConfig> configProvider,
      Provider<UnfoldTransitionProgressForwarder> progressForwarderProvider) {
    return new UnfoldSharedInternalModule_ProvideProgressForwarderFactory(module, configProvider, progressForwarderProvider);
  }

  public static Optional<UnfoldTransitionProgressForwarder> provideProgressForwarder(
      UnfoldSharedInternalModule instance, UnfoldTransitionConfig config,
      javax.inject.Provider<UnfoldTransitionProgressForwarder> progressForwarder) {
    return Preconditions.checkNotNullFromProvides(instance.provideProgressForwarder(config, progressForwarder));
  }
}
