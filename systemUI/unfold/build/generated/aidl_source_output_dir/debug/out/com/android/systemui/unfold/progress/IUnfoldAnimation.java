/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /Users/docmoates/Library/Android/sdk/build-tools/37.0.0/aidl -p/Users/docmoates/Library/Android/sdk/platforms/android-37.1/framework.aidl -o/Users/docmoates/Lawnchair/systemUI/unfold/build/generated/aidl_source_output_dir/debug/out -I/Users/docmoates/Lawnchair/systemUI/unfold/src -I/Users/docmoates/Lawnchair/systemUI/unfold/src/debug/aidl -I/Users/docmoates/Lawnchair/compatLib/build/intermediates/aidl_parcelable/debug/compileDebugAidl/out -I/Users/docmoates/.gradle/caches/9.7.1/transforms/5678b756e7305cedafb7d368432c7498/transformed/core-1.19.0/aidl -I/Users/docmoates/.gradle/caches/9.7.1/transforms/f92a0b6208819fb7599553bffe7e9174/transformed/versionedparcelable-1.1.1/aidl -d/var/folders/lf/7k355n1d2g5_v9yg4x1hy15r0000gn/T/aidl17867957347325538560.d /Users/docmoates/Lawnchair/systemUI/unfold/src/com/android/systemui/unfold/progress/IUnfoldAnimation.aidl
 *
 * DO NOT CHECK THIS FILE INTO A CODE TREE (e.g. git, etc..).
 * ALWAYS GENERATE THIS FILE FROM UPDATED AIDL COMPILER
 * AS A BUILD INTERMEDIATE ONLY. THIS IS NOT SOURCE CODE.
 */
package com.android.systemui.unfold.progress;
/** Interface exposed by System UI to allow remote process to register for unfold animation events. */
public interface IUnfoldAnimation extends android.os.IInterface
{
  /** Default implementation for IUnfoldAnimation. */
  public static class Default implements com.android.systemui.unfold.progress.IUnfoldAnimation
  {
    /**
     * Sets a listener for the animation.
     * 
     * Only one listener is supported. If there are multiple, the earlier one will be overridden.
     */
    @Override public void setListener(com.android.systemui.unfold.progress.IUnfoldTransitionListener listener) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements com.android.systemui.unfold.progress.IUnfoldAnimation
  {
    /** Construct the stub and attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an com.android.systemui.unfold.progress.IUnfoldAnimation interface,
     * generating a proxy if needed.
     */
    public static com.android.systemui.unfold.progress.IUnfoldAnimation asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof com.android.systemui.unfold.progress.IUnfoldAnimation))) {
        return ((com.android.systemui.unfold.progress.IUnfoldAnimation)iin);
      }
      return new com.android.systemui.unfold.progress.IUnfoldAnimation.Stub.Proxy(obj);
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
        case TRANSACTION_setListener:
        {
          com.android.systemui.unfold.progress.IUnfoldTransitionListener _arg0;
          _arg0 = com.android.systemui.unfold.progress.IUnfoldTransitionListener.Stub.asInterface(data.readStrongBinder());
          this.setListener(_arg0);
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
    }
    private static final class Proxy implements com.android.systemui.unfold.progress.IUnfoldAnimation
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
       * Sets a listener for the animation.
       * 
       * Only one listener is supported. If there are multiple, the earlier one will be overridden.
       */
      @Override public void setListener(com.android.systemui.unfold.progress.IUnfoldTransitionListener listener) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(listener);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setListener, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
    }
    static final int TRANSACTION_setListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
  }
  /** @hide */
  public static final java.lang.String DESCRIPTOR = "com.android.systemui.unfold.progress.IUnfoldAnimation";
  /**
   * Sets a listener for the animation.
   * 
   * Only one listener is supported. If there are multiple, the earlier one will be overridden.
   */
  public void setListener(com.android.systemui.unfold.progress.IUnfoldTransitionListener listener) throws android.os.RemoteException;
}
