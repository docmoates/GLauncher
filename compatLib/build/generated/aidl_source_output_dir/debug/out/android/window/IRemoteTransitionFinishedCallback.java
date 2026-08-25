/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /Users/docmoates/Library/Android/sdk/build-tools/37.0.0/aidl -p/Users/docmoates/Library/Android/sdk/platforms/android-37.1/framework.aidl -o/Users/docmoates/Lawnchair/compatLib/build/generated/aidl_source_output_dir/debug/out -I/Users/docmoates/Lawnchair/compatLib/src/main/java -I/Users/docmoates/Lawnchair/compatLib/src/debug/aidl -I/Users/docmoates/.gradle/caches/9.7.1/transforms/5678b756e7305cedafb7d368432c7498/transformed/core-1.19.0/aidl -I/Users/docmoates/.gradle/caches/9.7.1/transforms/f92a0b6208819fb7599553bffe7e9174/transformed/versionedparcelable-1.1.1/aidl -d/var/folders/lf/7k355n1d2g5_v9yg4x1hy15r0000gn/T/aidl13888766521282085084.d /Users/docmoates/Lawnchair/compatLib/src/main/java/android/window/IRemoteTransitionFinishedCallback.aidl
 *
 * DO NOT CHECK THIS FILE INTO A CODE TREE (e.g. git, etc..).
 * ALWAYS GENERATE THIS FILE FROM UPDATED AIDL COMPILER
 * AS A BUILD INTERMEDIATE ONLY. THIS IS NOT SOURCE CODE.
 */
package android.window;
/**
 * Interface to be invoked by the controlling process when a remote transition has finished.
 * 
 * @see IRemoteTransition
 * @param wct An optional WindowContainerTransaction to apply before the transition finished.
 * @param sct An optional Surface Transaction that is added to the end of the finish/cleanup
 *            transaction. This is applied by shell.Transitions (before submitting the wct).
 * {@hide}
 */
public interface IRemoteTransitionFinishedCallback extends android.os.IInterface
{
  /** Default implementation for IRemoteTransitionFinishedCallback. */
  public static class Default implements android.window.IRemoteTransitionFinishedCallback
  {
    @Override public void onTransitionFinished(android.window.WindowContainerTransaction wct, android.view.SurfaceControl.Transaction sct) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements android.window.IRemoteTransitionFinishedCallback
  {
    /** Construct the stub and attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an android.window.IRemoteTransitionFinishedCallback interface,
     * generating a proxy if needed.
     */
    public static android.window.IRemoteTransitionFinishedCallback asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof android.window.IRemoteTransitionFinishedCallback))) {
        return ((android.window.IRemoteTransitionFinishedCallback)iin);
      }
      return new android.window.IRemoteTransitionFinishedCallback.Stub.Proxy(obj);
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
        case TRANSACTION_onTransitionFinished:
        {
          android.window.WindowContainerTransaction _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.window.WindowContainerTransaction.CREATOR);
          android.view.SurfaceControl.Transaction _arg1;
          _arg1 = _Parcel.readTypedObject(data, android.view.SurfaceControl.Transaction.CREATOR);
          this.onTransitionFinished(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
    }
    private static final class Proxy implements android.window.IRemoteTransitionFinishedCallback
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
      @Override public void onTransitionFinished(android.window.WindowContainerTransaction wct, android.view.SurfaceControl.Transaction sct) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, wct, 0);
          _Parcel.writeTypedObject(_data, sct, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onTransitionFinished, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
    }
    static final int TRANSACTION_onTransitionFinished = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
  }
  /** @hide */
  public static final java.lang.String DESCRIPTOR = "android.window.IRemoteTransitionFinishedCallback";
  public void onTransitionFinished(android.window.WindowContainerTransaction wct, android.view.SurfaceControl.Transaction sct) throws android.os.RemoteException;
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
