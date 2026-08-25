/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /Users/docmoates/Library/Android/sdk/build-tools/37.0.0/aidl -p/Users/docmoates/Library/Android/sdk/platforms/android-37.1/framework.aidl -o/Users/docmoates/Lawnchair/systemUI/shared/build/generated/aidl_source_output_dir/debug/out -I/Users/docmoates/Lawnchair/systemUI/shared/src -I/Users/docmoates/Lawnchair/systemUI/shared/src/debug/aidl -I/Users/docmoates/Lawnchair/compatLib/build/intermediates/aidl_parcelable/debug/compileDebugAidl/out -I/Users/docmoates/Lawnchair/hidden-api/build/intermediates/aidl_parcelable/debug/compileDebugAidl/out -I/Users/docmoates/Lawnchair/systemUI/unfold/build/intermediates/aidl_parcelable/debug/compileDebugAidl/out -I/Users/docmoates/Lawnchair/systemUI/plugin/build/intermediates/aidl_parcelable/debug/compileDebugAidl/out -I/Users/docmoates/Lawnchair/systemUI/plugin_core/build/intermediates/aidl_parcelable/debug/compileDebugAidl/out -I/Users/docmoates/Lawnchair/wmshell/build/intermediates/aidl_parcelable/debug/compileDebugAidl/out -I/Users/docmoates/Lawnchair/systemUI/log/build/intermediates/aidl_parcelable/debug/compileDebugAidl/out -I/Users/docmoates/.gradle/caches/9.7.1/transforms/5678b756e7305cedafb7d368432c7498/transformed/core-1.19.0/aidl -I/Users/docmoates/.gradle/caches/9.7.1/transforms/f92a0b6208819fb7599553bffe7e9174/transformed/versionedparcelable-1.1.1/aidl -d/var/folders/lf/7k355n1d2g5_v9yg4x1hy15r0000gn/T/aidl13664363432432127629.d /Users/docmoates/Lawnchair/systemUI/shared/src/com/android/systemui/shared/system/smartspace/ISysuiUnlockAnimationController.aidl
 *
 * DO NOT CHECK THIS FILE INTO A CODE TREE (e.g. git, etc..).
 * ALWAYS GENERATE THIS FILE FROM UPDATED AIDL COMPILER
 * AS A BUILD INTERMEDIATE ONLY. THIS IS NOT SOURCE CODE.
 */
package com.android.systemui.shared.system.smartspace;
// System UI unlock controller. Launcher will provide a LauncherUnlockAnimationController to this
// controller, which System UI will use to control the unlock animation within the Launcher window.
public interface ISysuiUnlockAnimationController extends android.os.IInterface
{
  /** Default implementation for ISysuiUnlockAnimationController. */
  public static class Default implements com.android.systemui.shared.system.smartspace.ISysuiUnlockAnimationController
  {
    // Provides an implementation of the LauncherUnlockAnimationController to System UI, so that
    // SysUI can use it to control the unlock animation in the launcher window.
    @Override public void setLauncherUnlockController(java.lang.String activityClass, com.android.systemui.shared.system.smartspace.ILauncherUnlockAnimationController callback) throws android.os.RemoteException
    {
    }
    // Called by Launcher whenever anything happens to change the state of its smartspace. System UI
    // proactively saves this and uses it to perform the unlock animation without needing to make a
    // blocking query to Launcher asking about the smartspace state.
    @Override public void onLauncherSmartspaceStateUpdated(com.android.systemui.shared.system.smartspace.SmartspaceState state) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements com.android.systemui.shared.system.smartspace.ISysuiUnlockAnimationController
  {
    /** Construct the stub and attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an com.android.systemui.shared.system.smartspace.ISysuiUnlockAnimationController interface,
     * generating a proxy if needed.
     */
    public static com.android.systemui.shared.system.smartspace.ISysuiUnlockAnimationController asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof com.android.systemui.shared.system.smartspace.ISysuiUnlockAnimationController))) {
        return ((com.android.systemui.shared.system.smartspace.ISysuiUnlockAnimationController)iin);
      }
      return new com.android.systemui.shared.system.smartspace.ISysuiUnlockAnimationController.Stub.Proxy(obj);
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
        case TRANSACTION_setLauncherUnlockController:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          com.android.systemui.shared.system.smartspace.ILauncherUnlockAnimationController _arg1;
          _arg1 = com.android.systemui.shared.system.smartspace.ILauncherUnlockAnimationController.Stub.asInterface(data.readStrongBinder());
          this.setLauncherUnlockController(_arg0, _arg1);
          break;
        }
        case TRANSACTION_onLauncherSmartspaceStateUpdated:
        {
          com.android.systemui.shared.system.smartspace.SmartspaceState _arg0;
          _arg0 = _Parcel.readTypedObject(data, com.android.systemui.shared.system.smartspace.SmartspaceState.CREATOR);
          this.onLauncherSmartspaceStateUpdated(_arg0);
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
    }
    private static final class Proxy implements com.android.systemui.shared.system.smartspace.ISysuiUnlockAnimationController
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
      // Provides an implementation of the LauncherUnlockAnimationController to System UI, so that
      // SysUI can use it to control the unlock animation in the launcher window.
      @Override public void setLauncherUnlockController(java.lang.String activityClass, com.android.systemui.shared.system.smartspace.ILauncherUnlockAnimationController callback) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(activityClass);
          _data.writeStrongInterface(callback);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setLauncherUnlockController, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      // Called by Launcher whenever anything happens to change the state of its smartspace. System UI
      // proactively saves this and uses it to perform the unlock animation without needing to make a
      // blocking query to Launcher asking about the smartspace state.
      @Override public void onLauncherSmartspaceStateUpdated(com.android.systemui.shared.system.smartspace.SmartspaceState state) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, state, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onLauncherSmartspaceStateUpdated, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
    }
    static final int TRANSACTION_setLauncherUnlockController = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_onLauncherSmartspaceStateUpdated = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
  }
  /** @hide */
  public static final java.lang.String DESCRIPTOR = "com.android.systemui.shared.system.smartspace.ISysuiUnlockAnimationController";
  // Provides an implementation of the LauncherUnlockAnimationController to System UI, so that
  // SysUI can use it to control the unlock animation in the launcher window.
  public void setLauncherUnlockController(java.lang.String activityClass, com.android.systemui.shared.system.smartspace.ILauncherUnlockAnimationController callback) throws android.os.RemoteException;
  // Called by Launcher whenever anything happens to change the state of its smartspace. System UI
  // proactively saves this and uses it to perform the unlock animation without needing to make a
  // blocking query to Launcher asking about the smartspace state.
  public void onLauncherSmartspaceStateUpdated(com.android.systemui.shared.system.smartspace.SmartspaceState state) throws android.os.RemoteException;
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
