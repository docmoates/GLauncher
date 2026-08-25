/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /Users/docmoates/Library/Android/sdk/build-tools/37.0.0/aidl -p/Users/docmoates/Library/Android/sdk/platforms/android-37.1/framework.aidl -o/Users/docmoates/Lawnchair/wmshell/build/generated/aidl_source_output_dir/debug/out -I/Users/docmoates/Lawnchair/wmshell/shared/src -I/Users/docmoates/Lawnchair/wmshell/src/debug/aidl -I/Users/docmoates/Lawnchair/compatLib/build/intermediates/aidl_parcelable/debug/compileDebugAidl/out -I/Users/docmoates/.gradle/caches/9.7.1/transforms/5678b756e7305cedafb7d368432c7498/transformed/core-1.19.0/aidl -I/Users/docmoates/.gradle/caches/9.7.1/transforms/f92a0b6208819fb7599553bffe7e9174/transformed/versionedparcelable-1.1.1/aidl -d/var/folders/lf/7k355n1d2g5_v9yg4x1hy15r0000gn/T/aidl11136139471929051641.d /Users/docmoates/Lawnchair/wmshell/shared/src/com/android/wm/shell/shared/IFocusTransitionListener2.aidl
 *
 * DO NOT CHECK THIS FILE INTO A CODE TREE (e.g. git, etc..).
 * ALWAYS GENERATE THIS FILE FROM UPDATED AIDL COMPILER
 * AS A BUILD INTERMEDIATE ONLY. THIS IS NOT SOURCE CODE.
 */
package com.android.wm.shell.shared;
/** Listener interface that to get focus-related transition callbacks. */
public interface IFocusTransitionListener2 extends android.os.IInterface
{
  /** Default implementation for IFocusTransitionListener2. */
  public static class Default implements com.android.wm.shell.shared.IFocusTransitionListener2
  {
    /** Called when a transition changes the top, focused display. */
    @Override public void onFocusedDisplayChanged(int displayId) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements com.android.wm.shell.shared.IFocusTransitionListener2
  {
    /** Construct the stub and attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an com.android.wm.shell.shared.IFocusTransitionListener2 interface,
     * generating a proxy if needed.
     */
    public static com.android.wm.shell.shared.IFocusTransitionListener2 asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof com.android.wm.shell.shared.IFocusTransitionListener2))) {
        return ((com.android.wm.shell.shared.IFocusTransitionListener2)iin);
      }
      return new com.android.wm.shell.shared.IFocusTransitionListener2.Stub.Proxy(obj);
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
        case TRANSACTION_onFocusedDisplayChanged:
        {
          int _arg0;
          _arg0 = data.readInt();
          this.onFocusedDisplayChanged(_arg0);
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
    }
    private static final class Proxy implements com.android.wm.shell.shared.IFocusTransitionListener2
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
      /** Called when a transition changes the top, focused display. */
      @Override public void onFocusedDisplayChanged(int displayId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(displayId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onFocusedDisplayChanged, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
    }
    static final int TRANSACTION_onFocusedDisplayChanged = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
  }
  /** @hide */
  public static final java.lang.String DESCRIPTOR = "com.android.wm.shell.shared.IFocusTransitionListener2";
  /** Called when a transition changes the top, focused display. */
  public void onFocusedDisplayChanged(int displayId) throws android.os.RemoteException;
}
