package com.android.systemui.unfold.progress;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class UnfoldTransitionProgressForwarder_Factory implements Factory<UnfoldTransitionProgressForwarder> {
  @Override
  public UnfoldTransitionProgressForwarder get() {
    return newInstance();
  }

  public static UnfoldTransitionProgressForwarder_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static UnfoldTransitionProgressForwarder newInstance() {
    return new UnfoldTransitionProgressForwarder();
  }

  private static final class InstanceHolder {
    static final UnfoldTransitionProgressForwarder_Factory INSTANCE = new UnfoldTransitionProgressForwarder_Factory();
  }
}
