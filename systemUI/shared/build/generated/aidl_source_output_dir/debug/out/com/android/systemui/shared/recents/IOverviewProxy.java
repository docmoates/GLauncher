/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /Users/docmoates/Library/Android/sdk/build-tools/37.0.0/aidl -p/Users/docmoates/Library/Android/sdk/platforms/android-37.1/framework.aidl -o/Users/docmoates/Lawnchair/systemUI/shared/build/generated/aidl_source_output_dir/debug/out -I/Users/docmoates/Lawnchair/systemUI/shared/src -I/Users/docmoates/Lawnchair/systemUI/shared/src/debug/aidl -I/Users/docmoates/Lawnchair/compatLib/build/intermediates/aidl_parcelable/debug/compileDebugAidl/out -I/Users/docmoates/Lawnchair/hidden-api/build/intermediates/aidl_parcelable/debug/compileDebugAidl/out -I/Users/docmoates/Lawnchair/systemUI/unfold/build/intermediates/aidl_parcelable/debug/compileDebugAidl/out -I/Users/docmoates/Lawnchair/systemUI/plugin/build/intermediates/aidl_parcelable/debug/compileDebugAidl/out -I/Users/docmoates/Lawnchair/systemUI/plugin_core/build/intermediates/aidl_parcelable/debug/compileDebugAidl/out -I/Users/docmoates/Lawnchair/wmshell/build/intermediates/aidl_parcelable/debug/compileDebugAidl/out -I/Users/docmoates/Lawnchair/systemUI/log/build/intermediates/aidl_parcelable/debug/compileDebugAidl/out -I/Users/docmoates/.gradle/caches/9.7.1/transforms/5678b756e7305cedafb7d368432c7498/transformed/core-1.19.0/aidl -I/Users/docmoates/.gradle/caches/9.7.1/transforms/f92a0b6208819fb7599553bffe7e9174/transformed/versionedparcelable-1.1.1/aidl -d/var/folders/lf/7k355n1d2g5_v9yg4x1hy15r0000gn/T/aidl1172030979492185853.d /Users/docmoates/Lawnchair/systemUI/shared/src/com/android/systemui/shared/recents/IOverviewProxy.aidl
 *
 * DO NOT CHECK THIS FILE INTO A CODE TREE (e.g. git, etc..).
 * ALWAYS GENERATE THIS FILE FROM UPDATED AIDL COMPILER
 * AS A BUILD INTERMEDIATE ONLY. THIS IS NOT SOURCE CODE.
 */
package com.android.systemui.shared.recents;
// Next ID: 29
public interface IOverviewProxy extends android.os.IInterface
{
  /** Default implementation for IOverviewProxy. */
  public static class Default implements com.android.systemui.shared.recents.IOverviewProxy
  {
    @Override public void onActiveNavBarRegionChanges(android.graphics.Region activeRegion) throws android.os.RemoteException
    {
    }
    @Override public void onInitialize(android.os.Bundle params) throws android.os.RemoteException
    {
    }
    /** Sent when overview button is pressed to toggle show/hide of overview. */
    @Override public void onOverviewToggle() throws android.os.RemoteException
    {
    }
    /** Sent when overview is to be shown. */
    @Override public void onOverviewShown(boolean triggeredFromAltTab) throws android.os.RemoteException
    {
    }
    /** Sent when overview is to be hidden. */
    @Override public void onOverviewHidden(boolean triggeredFromAltTab, boolean triggeredFromHomeKey) throws android.os.RemoteException
    {
    }
    /**
     * Sent when device assistant changes its default assistant whether it is available or not.
     * @param longPressHomeEnabled if 3-button nav assistant can be invoked or not
     */
    @Override public void onAssistantAvailable(boolean available, boolean longPressHomeEnabled) throws android.os.RemoteException
    {
    }
    /** Sent when the assistant changes how visible it is to the user. */
    @Override public void onAssistantVisibilityChanged(float visibility) throws android.os.RemoteException
    {
    }
    /**
     * Sent when the assistant has been invoked with the given type (defined in AssistManager) and
     * should be shown. This method should be used if SystemUiProxy#setAssistantOverridesRequested
     * was previously called including this invocation type.
     */
    @Override public void onAssistantOverrideInvoked(int invocationType) throws android.os.RemoteException
    {
    }
    /** Sent when some system ui state changes. */
    @Override public void onSystemUiStateChanged(long stateFlags) throws android.os.RemoteException
    {
    }
    /** Sent when suggested rotation button could be shown */
    @Override public void onRotationProposal(int rotation, boolean isValid) throws android.os.RemoteException
    {
    }
    /** Sent when disable flags change */
    @Override public void disable(int displayId, int state1, int state2, boolean animate) throws android.os.RemoteException
    {
    }
    /** Sent when behavior changes. See WindowInsetsController#@Behavior */
    @Override public void onSystemBarAttributesChanged(int displayId, int behavior) throws android.os.RemoteException
    {
    }
    /** Sent when the desired dark intensity of the nav buttons has changed */
    @Override public void onNavButtonsDarkIntensityChanged(float darkIntensity) throws android.os.RemoteException
    {
    }
    /** Sent when when navigation bar luma sampling is enabled or disabled. */
    @Override public void onNavigationBarLumaSamplingEnabled(int displayId, boolean enable) throws android.os.RemoteException
    {
    }
    /** Sent when split keyboard shortcut is triggered to enter stage split. */
    @Override public void enterStageSplitFromRunningApp(boolean leftOrTop) throws android.os.RemoteException
    {
    }
    /** Sent when the task bar stash state is toggled. */
    @Override public void onTaskbarToggled() throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements com.android.systemui.shared.recents.IOverviewProxy
  {
    /** Construct the stub and attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an com.android.systemui.shared.recents.IOverviewProxy interface,
     * generating a proxy if needed.
     */
    public static com.android.systemui.shared.recents.IOverviewProxy asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof com.android.systemui.shared.recents.IOverviewProxy))) {
        return ((com.android.systemui.shared.recents.IOverviewProxy)iin);
      }
      return new com.android.systemui.shared.recents.IOverviewProxy.Stub.Proxy(obj);
    }
    @Override public android.os.IBinder asBinder()
    {
      return this;
    }
    @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
    {
      if (code >= android.os.IBinder.FIRST_CALL_TRANSACTION && code <= android.os.IBinder.LAST_CALL_TRANSACTION) {
        data.enforceInterface(DESCRIPTOR);
      }
      switch (code)
      {
        case TRANSACTION_onActiveNavBarRegionChanges:
        {
          android.graphics.Region _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.graphics.Region.CREATOR);
          this.onActiveNavBarRegionChanges(_arg0);
          break;
        }
        case TRANSACTION_onInitialize:
        {
          android.os.Bundle _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.os.Bundle.CREATOR);
          this.onInitialize(_arg0);
          break;
        }
        case TRANSACTION_onOverviewToggle:
        {
          this.onOverviewToggle();
          break;
        }
        case TRANSACTION_onOverviewShown:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.onOverviewShown(_arg0);
          break;
        }
        case TRANSACTION_onOverviewHidden:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.onOverviewHidden(_arg0, _arg1);
          break;
        }
        case TRANSACTION_onAssistantAvailable:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.onAssistantAvailable(_arg0, _arg1);
          break;
        }
        case TRANSACTION_onAssistantVisibilityChanged:
        {
          float _arg0;
          _arg0 = data.readFloat();
          this.onAssistantVisibilityChanged(_arg0);
          break;
        }
        case TRANSACTION_onAssistantOverrideInvoked:
        {
          int _arg0;
          _arg0 = data.readInt();
          this.onAssistantOverrideInvoked(_arg0);
          break;
        }
        case TRANSACTION_onSystemUiStateChanged:
        {
          long _arg0;
          _arg0 = data.readLong();
          this.onSystemUiStateChanged(_arg0);
          break;
        }
        case TRANSACTION_onRotationProposal:
        {
          int _arg0;
          _arg0 = data.readInt();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.onRotationProposal(_arg0, _arg1);
          break;
        }
        case TRANSACTION_disable:
        {
          int _arg0;
          _arg0 = data.readInt();
          int _arg1;
          _arg1 = data.readInt();
          int _arg2;
          _arg2 = data.readInt();
          boolean _arg3;
          _arg3 = (0!=data.readInt());
          this.disable(_arg0, _arg1, _arg2, _arg3);
          break;
        }
        case TRANSACTION_onSystemBarAttributesChanged:
        {
          int _arg0;
          _arg0 = data.readInt();
          int _arg1;
          _arg1 = data.readInt();
          this.onSystemBarAttributesChanged(_arg0, _arg1);
          break;
        }
        case TRANSACTION_onNavButtonsDarkIntensityChanged:
        {
          float _arg0;
          _arg0 = data.readFloat();
          this.onNavButtonsDarkIntensityChanged(_arg0);
          break;
        }
        case TRANSACTION_onNavigationBarLumaSamplingEnabled:
        {
          int _arg0;
          _arg0 = data.readInt();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.onNavigationBarLumaSamplingEnabled(_arg0, _arg1);
          break;
        }
        case TRANSACTION_enterStageSplitFromRunningApp:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.enterStageSplitFromRunningApp(_arg0);
          break;
        }
        case TRANSACTION_onTaskbarToggled:
        {
          this.onTaskbarToggled();
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
    }
    private static final class Proxy implements com.android.systemui.shared.recents.IOverviewProxy
    {
      private android.os.IBinder mRemote;
      Proxy(android.os.IBinder remote)
      {
        mRemote = remote;
      }
      @Override public android.os.IBinder asBinder()
      {
        return mRemote;
      }
      public final java.lang.String getInterfaceDescriptor()
      {
        return DESCRIPTOR;
      }
      @Override public void onActiveNavBarRegionChanges(android.graphics.Region activeRegion) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, activeRegion, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onActiveNavBarRegionChanges, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      @Override public void onInitialize(android.os.Bundle params) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, params, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onInitialize, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /** Sent when overview button is pressed to toggle show/hide of overview. */
      @Override public void onOverviewToggle() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onOverviewToggle, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /** Sent when overview is to be shown. */
      @Override public void onOverviewShown(boolean triggeredFromAltTab) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((triggeredFromAltTab)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_onOverviewShown, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /** Sent when overview is to be hidden. */
      @Override public void onOverviewHidden(boolean triggeredFromAltTab, boolean triggeredFromHomeKey) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((triggeredFromAltTab)?(1):(0)));
          _data.writeInt(((triggeredFromHomeKey)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_onOverviewHidden, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /**
       * Sent when device assistant changes its default assistant whether it is available or not.
       * @param longPressHomeEnabled if 3-button nav assistant can be invoked or not
       */
      @Override public void onAssistantAvailable(boolean available, boolean longPressHomeEnabled) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((available)?(1):(0)));
          _data.writeInt(((longPressHomeEnabled)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_onAssistantAvailable, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /** Sent when the assistant changes how visible it is to the user. */
      @Override public void onAssistantVisibilityChanged(float visibility) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeFloat(visibility);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onAssistantVisibilityChanged, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /**
       * Sent when the assistant has been invoked with the given type (defined in AssistManager) and
       * should be shown. This method should be used if SystemUiProxy#setAssistantOverridesRequested
       * was previously called including this invocation type.
       */
      @Override public void onAssistantOverrideInvoked(int invocationType) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(invocationType);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onAssistantOverrideInvoked, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /** Sent when some system ui state changes. */
      @Override public void onSystemUiStateChanged(long stateFlags) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeLong(stateFlags);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onSystemUiStateChanged, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /** Sent when suggested rotation button could be shown */
      @Override public void onRotationProposal(int rotation, boolean isValid) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(rotation);
          _data.writeInt(((isValid)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_onRotationProposal, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /** Sent when disable flags change */
      @Override public void disable(int displayId, int state1, int state2, boolean animate) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(displayId);
          _data.writeInt(state1);
          _data.writeInt(state2);
          _data.writeInt(((animate)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_disable, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /** Sent when behavior changes. See WindowInsetsController#@Behavior */
      @Override public void onSystemBarAttributesChanged(int displayId, int behavior) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(displayId);
          _data.writeInt(behavior);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onSystemBarAttributesChanged, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /** Sent when the desired dark intensity of the nav buttons has changed */
      @Override public void onNavButtonsDarkIntensityChanged(float darkIntensity) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeFloat(darkIntensity);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onNavButtonsDarkIntensityChanged, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /** Sent when when navigation bar luma sampling is enabled or disabled. */
      @Override public void onNavigationBarLumaSamplingEnabled(int displayId, boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(displayId);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_onNavigationBarLumaSamplingEnabled, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /** Sent when split keyboard shortcut is triggered to enter stage split. */
      @Override public void enterStageSplitFromRunningApp(boolean leftOrTop) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((leftOrTop)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_enterStageSplitFromRunningApp, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /** Sent when the task bar stash state is toggled. */
      @Override public void onTaskbarToggled() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onTaskbarToggled, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
    }
    static final int TRANSACTION_onActiveNavBarRegionChanges = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
    static final int TRANSACTION_onInitialize = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
    static final int TRANSACTION_onOverviewToggle = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
    static final int TRANSACTION_onOverviewShown = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
    static final int TRANSACTION_onOverviewHidden = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
    static final int TRANSACTION_onAssistantAvailable = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
    static final int TRANSACTION_onAssistantVisibilityChanged = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
    static final int TRANSACTION_onAssistantOverrideInvoked = (android.os.IBinder.FIRST_CALL_TRANSACTION + 28);
    static final int TRANSACTION_onSystemUiStateChanged = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
    static final int TRANSACTION_onRotationProposal = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
    static final int TRANSACTION_disable = (android.os.IBinder.FIRST_CALL_TRANSACTION + 19);
    static final int TRANSACTION_onSystemBarAttributesChanged = (android.os.IBinder.FIRST_CALL_TRANSACTION + 20);
    static final int TRANSACTION_onNavButtonsDarkIntensityChanged = (android.os.IBinder.FIRST_CALL_TRANSACTION + 22);
    static final int TRANSACTION_onNavigationBarLumaSamplingEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 23);
    static final int TRANSACTION_enterStageSplitFromRunningApp = (android.os.IBinder.FIRST_CALL_TRANSACTION + 25);
    static final int TRANSACTION_onTaskbarToggled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 27);
  }
  /** @hide */
  public static final java.lang.String DESCRIPTOR = "com.android.systemui.shared.recents.IOverviewProxy";
  public void onActiveNavBarRegionChanges(android.graphics.Region activeRegion) throws android.os.RemoteException;
  public void onInitialize(android.os.Bundle params) throws android.os.RemoteException;
  /** Sent when overview button is pressed to toggle show/hide of overview. */
  public void onOverviewToggle() throws android.os.RemoteException;
  /** Sent when overview is to be shown. */
  public void onOverviewShown(boolean triggeredFromAltTab) throws android.os.RemoteException;
  /** Sent when overview is to be hidden. */
  public void onOverviewHidden(boolean triggeredFromAltTab, boolean triggeredFromHomeKey) throws android.os.RemoteException;
  /**
   * Sent when device assistant changes its default assistant whether it is available or not.
   * @param longPressHomeEnabled if 3-button nav assistant can be invoked or not
   */
  public void onAssistantAvailable(boolean available, boolean longPressHomeEnabled) throws android.os.RemoteException;
  /** Sent when the assistant changes how visible it is to the user. */
  public void onAssistantVisibilityChanged(float visibility) throws android.os.RemoteException;
  /**
   * Sent when the assistant has been invoked with the given type (defined in AssistManager) and
   * should be shown. This method should be used if SystemUiProxy#setAssistantOverridesRequested
   * was previously called including this invocation type.
   */
  public void onAssistantOverrideInvoked(int invocationType) throws android.os.RemoteException;
  /** Sent when some system ui state changes. */
  public void onSystemUiStateChanged(long stateFlags) throws android.os.RemoteException;
  /** Sent when suggested rotation button could be shown */
  public void onRotationProposal(int rotation, boolean isValid) throws android.os.RemoteException;
  /** Sent when disable flags change */
  public void disable(int displayId, int state1, int state2, boolean animate) throws android.os.RemoteException;
  /** Sent when behavior changes. See WindowInsetsController#@Behavior */
  public void onSystemBarAttributesChanged(int displayId, int behavior) throws android.os.RemoteException;
  /** Sent when the desired dark intensity of the nav buttons has changed */
  public void onNavButtonsDarkIntensityChanged(float darkIntensity) throws android.os.RemoteException;
  /** Sent when when navigation bar luma sampling is enabled or disabled. */
  public void onNavigationBarLumaSamplingEnabled(int displayId, boolean enable) throws android.os.RemoteException;
  /** Sent when split keyboard shortcut is triggered to enter stage split. */
  public void enterStageSplitFromRunningApp(boolean leftOrTop) throws android.os.RemoteException;
  /** Sent when the task bar stash state is toggled. */
  public void onTaskbarToggled() throws android.os.RemoteException;
  /** @hide */
  static class _Parcel {
    static private <T> T readTypedObject(
        android.os.Parcel parcel,
        android.os.Parcelable.Creator<T> c) {
      if (parcel.readInt() != 0) {
          return c.createFromParcel(parcel);
      } else {
          return null;
      }
    }
    static private <T extends android.os.Parcelable> void writeTypedObject(
        android.os.Parcel parcel, T value, int parcelableFlags) {
      if (value != null) {
        parcel.writeInt(1);
        value.writeToParcel(parcel, parcelableFlags);
      } else {
        parcel.writeInt(0);
      }
    }
  }
}
