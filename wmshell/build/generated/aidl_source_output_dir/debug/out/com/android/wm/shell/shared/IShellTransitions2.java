/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /Users/docmoates/Library/Android/sdk/build-tools/37.0.0/aidl -p/Users/docmoates/Library/Android/sdk/platforms/android-37.1/framework.aidl -o/Users/docmoates/Lawnchair/wmshell/build/generated/aidl_source_output_dir/debug/out -I/Users/docmoates/Lawnchair/wmshell/shared/src -I/Users/docmoates/Lawnchair/wmshell/src/debug/aidl -I/Users/docmoates/Lawnchair/compatLib/build/intermediates/aidl_parcelable/debug/compileDebugAidl/out -I/Users/docmoates/.gradle/caches/9.7.1/transforms/5678b756e7305cedafb7d368432c7498/transformed/core-1.19.0/aidl -I/Users/docmoates/.gradle/caches/9.7.1/transforms/f92a0b6208819fb7599553bffe7e9174/transformed/versionedparcelable-1.1.1/aidl -d/var/folders/lf/7k355n1d2g5_v9yg4x1hy15r0000gn/T/aidl3506321621718719808.d /Users/docmoates/Lawnchair/wmshell/shared/src/com/android/wm/shell/shared/IShellTransitions2.aidl
 *
 * DO NOT CHECK THIS FILE INTO A CODE TREE (e.g. git, etc..).
 * ALWAYS GENERATE THIS FILE FROM UPDATED AIDL COMPILER
 * AS A BUILD INTERMEDIATE ONLY. THIS IS NOT SOURCE CODE.
 */
package com.android.wm.shell.shared;
/** Interface that is exposed to remote callers to manipulate the transitions feature. */
public interface IShellTransitions2 extends android.os.IInterface
{
  /** Default implementation for IShellTransitions2. */
  public static class Default implements com.android.wm.shell.shared.IShellTransitions2
  {
    /**
     * Registers a remote transition handler for all operations excluding takeovers (see
     * registerRemoteForTakeover()).
     */
    @Override public void registerRemote(android.window.TransitionFilter filter, android.window.RemoteTransition remoteTransition) throws android.os.RemoteException
    {
    }
    /** Unregisters a remote transition handler for all operations. */
    @Override public void unregisterRemote(android.window.RemoteTransition remoteTransition) throws android.os.RemoteException
    {
    }
    /** Retrieves the apply-token used by transactions in Shell */
    @Override public android.os.IBinder getShellApplyToken() throws android.os.RemoteException
    {
      return null;
    }
    /** Set listener that will receive callbacks about transitions involving home activity. */
    @Override public void setHomeTransitionListener(com.android.wm.shell.shared.IHomeTransitionListener2 listener) throws android.os.RemoteException
    {
    }
    /** Returns a container surface for the home root task. */
    @Override public android.view.SurfaceControl getHomeTaskOverlayContainer() throws android.os.RemoteException
    {
      return null;
    }
    /** Registers a remote transition for takeover operations only. */
    @Override public void registerRemoteForTakeover(android.window.TransitionFilter filter, android.window.RemoteTransition remoteTransition) throws android.os.RemoteException
    {
    }
    /** Set listener that will receive callbacks about transitions involving focus switch. */
    @Override public void setFocusTransitionListener(com.android.wm.shell.shared.IFocusTransitionListener2 listener) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements com.android.wm.shell.shared.IShellTransitions2
  {
    /** Construct the stub and attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an com.android.wm.shell.shared.IShellTransitions2 interface,
     * generating a proxy if needed.
     */
    public static com.android.wm.shell.shared.IShellTransitions2 asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof com.android.wm.shell.shared.IShellTransitions2))) {
        return ((com.android.wm.shell.shared.IShellTransitions2)iin);
      }
      return new com.android.wm.shell.shared.IShellTransitions2.Stub.Proxy(obj);
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
        case TRANSACTION_registerRemote:
        {
          android.window.TransitionFilter _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.window.TransitionFilter.CREATOR);
          android.window.RemoteTransition _arg1;
          _arg1 = _Parcel.readTypedObject(data, android.window.RemoteTransition.CREATOR);
          this.registerRemote(_arg0, _arg1);
          break;
        }
        case TRANSACTION_unregisterRemote:
        {
          android.window.RemoteTransition _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.window.RemoteTransition.CREATOR);
          this.unregisterRemote(_arg0);
          break;
        }
        case TRANSACTION_getShellApplyToken:
        {
          android.os.IBinder _result = this.getShellApplyToken();
          reply.writeNoException();
          reply.writeStrongBinder(_result);
          break;
        }
        case TRANSACTION_setHomeTransitionListener:
        {
          com.android.wm.shell.shared.IHomeTransitionListener2 _arg0;
          _arg0 = com.android.wm.shell.shared.IHomeTransitionListener2.Stub.asInterface(data.readStrongBinder());
          this.setHomeTransitionListener(_arg0);
          break;
        }
        case TRANSACTION_getHomeTaskOverlayContainer:
        {
          android.view.SurfaceControl _result = this.getHomeTaskOverlayContainer();
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_registerRemoteForTakeover:
        {
          android.window.TransitionFilter _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.window.TransitionFilter.CREATOR);
          android.window.RemoteTransition _arg1;
          _arg1 = _Parcel.readTypedObject(data, android.window.RemoteTransition.CREATOR);
          this.registerRemoteForTakeover(_arg0, _arg1);
          break;
        }
        case TRANSACTION_setFocusTransitionListener:
        {
          com.android.wm.shell.shared.IFocusTransitionListener2 _arg0;
          _arg0 = com.android.wm.shell.shared.IFocusTransitionListener2.Stub.asInterface(data.readStrongBinder());
          this.setFocusTransitionListener(_arg0);
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
    }
    private static final class Proxy implements com.android.wm.shell.shared.IShellTransitions2
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
      /**
       * Registers a remote transition handler for all operations excluding takeovers (see
       * registerRemoteForTakeover()).
       */
      @Override public void registerRemote(android.window.TransitionFilter filter, android.window.RemoteTransition remoteTransition) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, filter, 0);
          _Parcel.writeTypedObject(_data, remoteTransition, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_registerRemote, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /** Unregisters a remote transition handler for all operations. */
      @Override public void unregisterRemote(android.window.RemoteTransition remoteTransition) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, remoteTransition, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_unregisterRemote, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /** Retrieves the apply-token used by transactions in Shell */
      @Override public android.os.IBinder getShellApplyToken() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.os.IBinder _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getShellApplyToken, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readStrongBinder();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      /** Set listener that will receive callbacks about transitions involving home activity. */
      @Override public void setHomeTransitionListener(com.android.wm.shell.shared.IHomeTransitionListener2 listener) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(listener);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setHomeTransitionListener, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /** Returns a container surface for the home root task. */
      @Override public android.view.SurfaceControl getHomeTaskOverlayContainer() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.view.SurfaceControl _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getHomeTaskOverlayContainer, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, android.view.SurfaceControl.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      /** Registers a remote transition for takeover operations only. */
      @Override public void registerRemoteForTakeover(android.window.TransitionFilter filter, android.window.RemoteTransition remoteTransition) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, filter, 0);
          _Parcel.writeTypedObject(_data, remoteTransition, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_registerRemoteForTakeover, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /** Set listener that will receive callbacks about transitions involving focus switch. */
      @Override public void setFocusTransitionListener(com.android.wm.shell.shared.IFocusTransitionListener2 listener) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(listener);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setFocusTransitionListener, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
    }
    static final int TRANSACTION_registerRemote = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_unregisterRemote = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_getShellApplyToken = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_setHomeTransitionListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_getHomeTaskOverlayContainer = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    static final int TRANSACTION_registerRemoteForTakeover = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
    static final int TRANSACTION_setFocusTransitionListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
  }
  /** @hide */
  public static final java.lang.String DESCRIPTOR = "com.android.wm.shell.shared.IShellTransitions2";
  /**
   * Registers a remote transition handler for all operations excluding takeovers (see
   * registerRemoteForTakeover()).
   */
  public void registerRemote(android.window.TransitionFilter filter, android.window.RemoteTransition remoteTransition) throws android.os.RemoteException;
  /** Unregisters a remote transition handler for all operations. */
  public void unregisterRemote(android.window.RemoteTransition remoteTransition) throws android.os.RemoteException;
  /** Retrieves the apply-token used by transactions in Shell */
  public android.os.IBinder getShellApplyToken() throws android.os.RemoteException;
  /** Set listener that will receive callbacks about transitions involving home activity. */
  public void setHomeTransitionListener(com.android.wm.shell.shared.IHomeTransitionListener2 listener) throws android.os.RemoteException;
  /** Returns a container surface for the home root task. */
  public android.view.SurfaceControl getHomeTaskOverlayContainer() throws android.os.RemoteException;
  /** Registers a remote transition for takeover operations only. */
  public void registerRemoteForTakeover(android.window.TransitionFilter filter, android.window.RemoteTransition remoteTransition) throws android.os.RemoteException;
  /** Set listener that will receive callbacks about transitions involving focus switch. */
  public void setFocusTransitionListener(com.android.wm.shell.shared.IFocusTransitionListener2 listener) throws android.os.RemoteException;
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
