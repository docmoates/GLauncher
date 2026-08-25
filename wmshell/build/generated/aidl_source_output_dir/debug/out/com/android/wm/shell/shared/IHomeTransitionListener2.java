/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /Users/docmoates/Library/Android/sdk/build-tools/37.0.0/aidl -p/Users/docmoates/Library/Android/sdk/platforms/android-37.1/framework.aidl -o/Users/docmoates/Lawnchair/wmshell/build/generated/aidl_source_output_dir/debug/out -I/Users/docmoates/Lawnchair/wmshell/shared/src -I/Users/docmoates/Lawnchair/wmshell/src/debug/aidl -I/Users/docmoates/Lawnchair/compatLib/build/intermediates/aidl_parcelable/debug/compileDebugAidl/out -I/Users/docmoates/.gradle/caches/9.7.1/transforms/5678b756e7305cedafb7d368432c7498/transformed/core-1.19.0/aidl -I/Users/docmoates/.gradle/caches/9.7.1/transforms/f92a0b6208819fb7599553bffe7e9174/transformed/versionedparcelable-1.1.1/aidl -d/var/folders/lf/7k355n1d2g5_v9yg4x1hy15r0000gn/T/aidl18180991130135244539.d /Users/docmoates/Lawnchair/wmshell/shared/src/com/android/wm/shell/shared/IHomeTransitionListener2.aidl
 *
 * DO NOT CHECK THIS FILE INTO A CODE TREE (e.g. git, etc..).
 * ALWAYS GENERATE THIS FILE FROM UPDATED AIDL COMPILER
 * AS A BUILD INTERMEDIATE ONLY. THIS IS NOT SOURCE CODE.
 */
package com.android.wm.shell.shared;
/**
 * Listener interface that Launcher attaches to SystemUI to get home activity transition callbacks
 * on the default display.
 */
public interface IHomeTransitionListener2 extends android.os.IInterface
{
  /** Default implementation for IHomeTransitionListener2. */
  public static class Default implements com.android.wm.shell.shared.IHomeTransitionListener2
  {
    /** Called when a transition changes the visibility of the home activity on the default display. */
    @Override public void onHomeVisibilityChanged(boolean isVisible) throws android.os.RemoteException
    {
    }
    /** Called when the insets at display-level change. */
    @Override public void onDisplayInsetsChanged(android.view.InsetsState insets) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements com.android.wm.shell.shared.IHomeTransitionListener2
  {
    /** Construct the stub and attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an com.android.wm.shell.shared.IHomeTransitionListener2 interface,
     * generating a proxy if needed.
     */
    public static com.android.wm.shell.shared.IHomeTransitionListener2 asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof com.android.wm.shell.shared.IHomeTransitionListener2))) {
        return ((com.android.wm.shell.shared.IHomeTransitionListener2)iin);
      }
      return new com.android.wm.shell.shared.IHomeTransitionListener2.Stub.Proxy(obj);
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
        case TRANSACTION_onHomeVisibilityChanged:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.onHomeVisibilityChanged(_arg0);
          break;
        }
        case TRANSACTION_onDisplayInsetsChanged:
        {
          android.view.InsetsState _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.view.InsetsState.CREATOR);
          this.onDisplayInsetsChanged(_arg0);
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
    }
    private static final class Proxy implements com.android.wm.shell.shared.IHomeTransitionListener2
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
      /** Called when a transition changes the visibility of the home activity on the default display. */
      @Override public void onHomeVisibilityChanged(boolean isVisible) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((isVisible)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_onHomeVisibilityChanged, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /** Called when the insets at display-level change. */
      @Override public void onDisplayInsetsChanged(android.view.InsetsState insets) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, insets, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onDisplayInsetsChanged, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
    }
    static final int TRANSACTION_onHomeVisibilityChanged = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_onDisplayInsetsChanged = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
  }
  /** @hide */
  public static final java.lang.String DESCRIPTOR = "com.android.wm.shell.shared.IHomeTransitionListener2";
  /** Called when a transition changes the visibility of the home activity on the default display. */
  public void onHomeVisibilityChanged(boolean isVisible) throws android.os.RemoteException;
  /** Called when the insets at display-level change. */
  public void onDisplayInsetsChanged(android.view.InsetsState insets) throws android.os.RemoteException;
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
