/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /Users/docmoates/Library/Android/sdk/build-tools/37.0.0/aidl -p/Users/docmoates/Library/Android/sdk/platforms/android-37.1/framework.aidl -o/Users/docmoates/Lawnchair/systemUI/unfold/build/generated/aidl_source_output_dir/debug/out -I/Users/docmoates/Lawnchair/systemUI/unfold/src -I/Users/docmoates/Lawnchair/systemUI/unfold/src/debug/aidl -I/Users/docmoates/Lawnchair/compatLib/build/intermediates/aidl_parcelable/debug/compileDebugAidl/out -I/Users/docmoates/.gradle/caches/9.7.1/transforms/5678b756e7305cedafb7d368432c7498/transformed/core-1.19.0/aidl -I/Users/docmoates/.gradle/caches/9.7.1/transforms/f92a0b6208819fb7599553bffe7e9174/transformed/versionedparcelable-1.1.1/aidl -d/var/folders/lf/7k355n1d2g5_v9yg4x1hy15r0000gn/T/aidl12744455210806651146.d /Users/docmoates/Lawnchair/systemUI/unfold/src/com/android/systemui/unfold/progress/IUnfoldTransitionListener.aidl
 *
 * DO NOT CHECK THIS FILE INTO A CODE TREE (e.g. git, etc..).
 * ALWAYS GENERATE THIS FILE FROM UPDATED AIDL COMPILER
 * AS A BUILD INTERMEDIATE ONLY. THIS IS NOT SOURCE CODE.
 */
package com.android.systemui.unfold.progress;
/** Implemented by remote processes to receive unfold animation events from System UI. */
public interface IUnfoldTransitionListener extends android.os.IInterface
{
  /** Default implementation for IUnfoldTransitionListener. */
  public static class Default implements com.android.systemui.unfold.progress.IUnfoldTransitionListener
  {
    /** Sent when unfold animation started. */
    @Override public void onTransitionStarted() throws android.os.RemoteException
    {
    }
    /** Sent when unfold animation progress changes. */
    @Override public void onTransitionProgress(float progress) throws android.os.RemoteException
    {
    }
    /** Sent when unfold animation finished. */
    @Override public void onTransitionFinished() throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements com.android.systemui.unfold.progress.IUnfoldTransitionListener
  {
    /** Construct the stub and attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an com.android.systemui.unfold.progress.IUnfoldTransitionListener interface,
     * generating a proxy if needed.
     */
    public static com.android.systemui.unfold.progress.IUnfoldTransitionListener asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof com.android.systemui.unfold.progress.IUnfoldTransitionListener))) {
        return ((com.android.systemui.unfold.progress.IUnfoldTransitionListener)iin);
      }
      return new com.android.systemui.unfold.progress.IUnfoldTransitionListener.Stub.Proxy(obj);
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
        case TRANSACTION_onTransitionStarted:
        {
          this.onTransitionStarted();
          break;
        }
        case TRANSACTION_onTransitionProgress:
        {
          float _arg0;
          _arg0 = data.readFloat();
          this.onTransitionProgress(_arg0);
          break;
        }
        case TRANSACTION_onTransitionFinished:
        {
          this.onTransitionFinished();
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
    }
    private static final class Proxy implements com.android.systemui.unfold.progress.IUnfoldTransitionListener
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
      /** Sent when unfold animation started. */
      @Override public void onTransitionStarted() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onTransitionStarted, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /** Sent when unfold animation progress changes. */
      @Override public void onTransitionProgress(float progress) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeFloat(progress);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onTransitionProgress, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      /** Sent when unfold animation finished. */
      @Override public void onTransitionFinished() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onTransitionFinished, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
    }
    static final int TRANSACTION_onTransitionStarted = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_onTransitionProgress = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_onTransitionFinished = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
  }
  /** @hide */
  public static final java.lang.String DESCRIPTOR = "com.android.systemui.unfold.progress.IUnfoldTransitionListener";
  /** Sent when unfold animation started. */
  public void onTransitionStarted() throws android.os.RemoteException;
  /** Sent when unfold animation progress changes. */
  public void onTransitionProgress(float progress) throws android.os.RemoteException;
  /** Sent when unfold animation finished. */
  public void onTransitionFinished() throws android.os.RemoteException;
}
